package pl.clarin.geocoder;

import javax.json.Json;
import javax.json.JsonObject;

public class Task {

    String path;

    public Task(JsonObject json) {
        this.path = json.getString("path");
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JsonObject getJson(){
        return Json.createObjectBuilder()
                .add("path", path).build();
    }
}
