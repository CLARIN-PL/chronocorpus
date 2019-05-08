package pl.clarin.chronocorpus.timeseries.entity;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.List;
import java.util.Map;

public class TimeSeries {

    private String word;
    private boolean isBaseForm;
    private Integer pos;
    private List<TimeSeriesRow> series;

    public TimeSeries(String word, boolean isBaseForm, Integer pos, List<TimeSeriesRow> series) {
        this.word = word;
        this.series = series;
        this.pos = pos;
        this.isBaseForm = isBaseForm;
    }

    public JsonObject toJson(){
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("word", word);
        b.add("is_base_form", isBaseForm);
        b.add("part_of_speech", pos);
        JsonArrayBuilder s = Json.createArrayBuilder();
        series.forEach(row -> s.add(Json.createObjectBuilder()
                .add(row.getKey(), row.getCount())));
        b.add("series", s);
        return  b.build();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
