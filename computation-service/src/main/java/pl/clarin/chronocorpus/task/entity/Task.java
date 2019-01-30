package pl.clarin.chronocorpus.task.entity;

import pl.clarin.chronocorpus.document.entity.Property;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Task {

    protected String id;
    protected Set<Property> metadata;
    protected Set<Property> params;

    public Task(JsonObject json) {
        this.id = json.getString("id");

        JsonArray jsonParams = json.getJsonArray("params");
        params = jsonParams.getValuesAs(JsonObject.class)
                .stream()
                .map(j -> new Property(j.getString("name"), j.getString("value")))
                .collect(Collectors.toSet());


        JsonArray jsonMetadata = json.getJsonArray("metadata_filter");
        metadata = jsonMetadata.getValuesAs(JsonObject.class)
                .stream()
                .map(j -> new Property(j.getString("name"), j.getString("value")))
                .collect(Collectors.toSet());

    }

    public String getId() {
        return id;
    }

    public abstract JsonObject doTask();

}
