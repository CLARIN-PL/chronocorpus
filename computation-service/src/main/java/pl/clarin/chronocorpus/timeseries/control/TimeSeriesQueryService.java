package pl.clarin.chronocorpus.timeseries.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Word;

import javax.json.JsonArray;
import java.util.Set;
import java.util.function.Predicate;

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
    //TODO process years array [1945,1950,1951]
    //TODO grouping by unit month or year
    public JsonArray findTimeSeries(String keyWord, Set<Property> metadata, boolean byBase) {

        for (Document d : DocumentStore.getInstance().getDocuments()) {

            if (d.getMetadata().matches(metadata) && (byBase ? d.isBaseIn(keyWord) : d.isOrthIn(keyWord))) {

            }
        }

        return null;
    }

    public Predicate<Word> getOrthPredicate(String keyWord) {
        return word -> word.getOrth().equals(keyWord);
    }

    public Predicate<Word> getBasePredicate(String keyWord) {
        return word -> word.getBase().equals(keyWord);
    }
}
