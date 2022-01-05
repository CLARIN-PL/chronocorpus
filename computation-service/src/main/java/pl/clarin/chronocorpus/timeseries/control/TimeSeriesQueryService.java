package pl.clarin.chronocorpus.timeseries.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.timeseries.entity.TimeSeries;
import pl.clarin.chronocorpus.timeseries.entity.TimeSeriesRow;
import pl.clarin.chronocorpus.timeseries.entity.TimeUnit;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.*;
import java.util.stream.Collectors;

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

    public JsonArray findTimeSeries(List<String> keyWords, Optional<Integer> pos, Optional<TimeUnit> unit,
                                    Set<Property> metadata, boolean byBase, boolean onlyAll) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        List<TimeSeriesRow> all = new ArrayList<>();

        keyWords.forEach(kw -> {
            List<TimeSeriesRow> r = findByKeyWord(kw, pos, unit, metadata, byBase);
            if(r!= null && !r.isEmpty()) {
                all.addAll(r);
                arrayBuilder.add(new TimeSeries(kw, byBase, pos.orElse(0), r).toJson());
            }
        });
        all.sort(Comparator.comparing(TimeSeriesRow::getYear).thenComparing(TimeSeriesRow::getMonth));
        Map<String, Integer> sum = all.stream().collect(
                Collectors.groupingBy(TimeSeriesRow::getKey, Collectors.summingInt(TimeSeriesRow::getCount)));
        if(onlyAll) {
            JsonArrayBuilder arrayBuilderAll = Json.createArrayBuilder();
            arrayBuilderAll.add(new TimeSeries("all", byBase, pos.orElse(0), sum).toJsonMergedGraphs());
            return arrayBuilderAll.build();
        }else{
            arrayBuilder.add(new TimeSeries("all", byBase, pos.orElse(0), sum).toJsonMergedGraphs());
            return arrayBuilder.build();
        }
    }

    //TODO needs solution for multi word expressions like Armia Czerwona
    public List<TimeSeriesRow> findByKeyWord(String keyWord, Optional<Integer> pos, Optional<TimeUnit> unit, Set<Property> metadata, boolean byBase) {

        Map<TimeSeriesRow, TimeSeriesRow> result = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments()) {

//            if ( (d.getMetadata() == null || d.getMetadata().matches(metadata)) && (byBase ? d.isBaseIn(keyWord) : d.isOrthIn(keyWord))) {

            if ((d.getMetadata() != null ? d.getMetadata().matches(metadata) : true) && (byBase ? d.isBaseIn(keyWord) : d.isOrthIn(keyWord))) {
                unit.ifPresent(u -> {
                    if (TimeUnit.year.equals(u)) {
                        pos.ifPresent(p -> {
                            String year = d.getMetadata().getProperty("publication_year");
                            Integer count = byBase ? d.documentBaseFrequency().get(keyWord).getValue(p) :
                                    d.documentOrthFrequency().get(keyWord).getValue(p);
                            if (year != null) {
                                TimeSeriesRow row = new TimeSeriesRow(Integer.parseInt(year), 0, count);
                                if (!result.containsKey(row)) {
                                    result.put(row, row);
                                } else {
                                    result.get(row).addCount(row.getCount());
                                }
                            }
                        });
                    } else if (TimeUnit.month.equals(u)) {
                        pos.ifPresent(p -> {
                            String year = d.getMetadata().getProperty("publication_year");
                            String month = d.getMetadata().getProperty("publication_month");
                            Integer count = byBase ? d.documentBaseFrequency().get(keyWord).getValue(p) :
                                    d.documentOrthFrequency().get(keyWord).getValue(p);

                            Integer y = Integer.parseInt(year);
                            Integer m = Integer.parseInt(month);
                            TimeSeriesRow row = new TimeSeriesRow(y, m, count);

                            if (!result.containsKey(row)) {
                                result.put(row, row);
                            } else {
                                result.get(row).addCount(row.getCount());
                            }
                        });
                    } else if (TimeUnit.day.equals(u)) {
                        pos.ifPresent(p -> {
                            String year = d.getMetadata().getProperty("publication_year");
                            String month = d.getMetadata().getProperty("publication_month");
                            String day = d.getMetadata().getProperty("publication_day");
                            Integer count = byBase ? d.documentBaseFrequency().get(keyWord).getValue(p) :
                                    d.documentOrthFrequency().get(keyWord).getValue(p);
                            TimeSeriesRow row = new TimeSeriesRow(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), count);
                            if (!result.containsKey(row)) {
                                result.put(row, row);
                            } else {
                                result.get(row).addCount(row.getCount());
                            }
                        });
                    }
                });
            }
        }

        List<TimeSeriesRow> r = new ArrayList<>(result.values());
        r.sort(Comparator.comparing(TimeSeriesRow::getYear).thenComparing(TimeSeriesRow::getMonth));

        return r;

    }

}
