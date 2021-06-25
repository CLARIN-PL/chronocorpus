package pl.clarin.chronocorpus.administration.entity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistics implements Serializable {

    private final AtomicInteger concordanceQueryCount = new AtomicInteger(0);
    private final AtomicInteger frequencyQueryCount = new AtomicInteger(0);
    private final AtomicInteger timeSeriesQueryCount = new AtomicInteger(0);
    private final AtomicInteger wordProfileQueryCount = new AtomicInteger(0);
    private final AtomicInteger quantityAnalysisQueryCount = new AtomicInteger(0);
    private final AtomicInteger mapNamesQueryCount = new AtomicInteger(0);
    private final AtomicInteger documentsQueryCount = new AtomicInteger(0);

    public AtomicInteger getConcordanceQueryCount() {
        return concordanceQueryCount;
    }

    public AtomicInteger getFrequencyQueryCount() {
        return frequencyQueryCount;
    }

    public AtomicInteger getTimeSeriesQueryCount() {
        return timeSeriesQueryCount;
    }

    public AtomicInteger getWordProfileQueryCount() {
        return wordProfileQueryCount;
    }

    public AtomicInteger getQuantityAnalysisQueryCount() {
        return quantityAnalysisQueryCount;
    }

    public AtomicInteger getMapNamesQueryCount() {
        return mapNamesQueryCount;
    }

    public AtomicInteger getDocumentsQueryCount() {
        return documentsQueryCount;
    }

    public JsonObject toJson(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("concordance_query_count",concordanceQueryCount.get());
        builder.add("frequency_query_count",frequencyQueryCount.get());
        builder.add("time_series_query_count",timeSeriesQueryCount.get());
        builder.add("word_profile_query_count",wordProfileQueryCount.get());
        builder.add("quantity_analysis_query_count",quantityAnalysisQueryCount.get());
        builder.add("map_names_query_count",mapNamesQueryCount.get());
        builder.add("documents_query_count",documentsQueryCount.get());

        return builder.build();
    }
}
