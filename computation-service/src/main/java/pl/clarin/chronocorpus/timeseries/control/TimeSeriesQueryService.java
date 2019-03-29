package pl.clarin.chronocorpus.timeseries.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.timeseries.entity.TimeSeries;
import pl.clarin.chronocorpus.timeseries.entity.TimeUnit;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TimeSeriesQueryService {

    public static volatile TimeSeriesQueryService instance;

    private TimeSeriesQueryService() {
    }

    public static TimeSeriesQueryService getInstance() {
        if (instance == null) {
            synchronized (TimeSeriesQueryService.class) {
                if (instance == null) {
                    instance = new TimeSeriesQueryService();
                }
            }
        }
        return instance;
    }

    //TODO needs solution for multi word expressions like Armia Czerwona
    public JsonObject findTimeSeries(String keyWord, Optional<Integer> pos, Optional<TimeUnit> unit, Set<Property> metadata, boolean byBase) {

        Map<String, Integer> result = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments()) {

            if (d.getMetadata().matches(metadata) && (byBase ? d.isBaseIn(keyWord) : d.isOrthIn(keyWord))) {
                unit.ifPresent(u -> {
                    if (TimeUnit.year.equals(u)) {
                        pos.ifPresent(p -> {
                            String year = d.getMetadata().getProperty("publication_year");
                            Integer count = byBase ? d.documentBaseFrequency().get(keyWord).getValue(p) :
                                    d.documentOrthFrequency().get(keyWord).getValue(p);
                            if (!result.containsKey(year)) {
                                result.put(year, count);
                            } else {
                                Integer val = result.get("year");
                                result.replace(year, val + count);
                            }
                        });
                    } else if (TimeUnit.month.equals(u)) {
                        pos.ifPresent(p -> {
                            String year = d.getMetadata().getProperty("publication_year");
                            String month = d.getMetadata().getProperty("publication_month");
                            String key = month + "-" + year;
                            Integer count = byBase ? d.documentBaseFrequency().get(keyWord).getValue(p) :
                                    d.documentOrthFrequency().get(keyWord).getValue(p);
                            if (!result.containsKey(key)) {
                                result.put(key, count);
                            } else {
                                Integer val = result.get("key");
                                result.replace("key", val + count);
                            }
                        });
                    }
                });
            }
        }

        return new TimeSeries(keyWord, byBase, pos.orElse(0), result).toJson();
    }

}
