package pl.clarin.chronocorpus.timeseries.entity;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Map;

public class TimeSeries {

    private String word;
    private Map<String, Long> series;

    public TimeSeries(String word, Map<String, Long> series) {
        this.word = word;
        this.series = series;
    }

    public JsonObject toJson(){
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("word", word);
        JsonArrayBuilder s = Json.createArrayBuilder();
        series.forEach((k,v) -> s.add(Json.createObjectBuilder().add(k,v)));
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
