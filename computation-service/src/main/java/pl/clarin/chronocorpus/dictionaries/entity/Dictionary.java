package pl.clarin.chronocorpus.dictionaries.entity;

import javax.json.Json;
import javax.json.JsonObject;

public class Dictionary {

    private long id;
    private String value;

    public Dictionary(long id, String value) {
        this.id = id;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                .add("id", id)
                .add("value",value)
                .build();
    }

}
