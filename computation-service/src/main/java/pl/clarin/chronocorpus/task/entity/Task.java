package pl.clarin.chronocorpus.task.entity;

import pl.clarin.chronocorpus.document.entity.Property;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import java.util.Set;
import java.util.stream.Collectors;
import pl.clarin.chronocorpus.Progress;

public abstract class Task {

    
    protected String id;
    protected Set<Property> metadata;
    protected Set<Property> queryParameters;
    protected Set<String> responseParameters;
    

    public Task(JsonObject json) {
        this.id = json.getString("id");

        JsonArray jsonParams = json.getJsonArray("query_parameters");
        queryParameters = jsonParams.getValuesAs(JsonObject.class)
                .stream()
                .map(j -> new Property(j.getString("name"), j.getString("value")))
                .collect(Collectors.toSet());

        JsonArray jsonMetadata = json.getJsonArray("metadata_filter");
        metadata = jsonMetadata.getValuesAs(JsonObject.class)
                .stream()
                .map(j -> new Property(j.getString("name"), j.getString("value")))
                .collect(Collectors.toSet());

        JsonArray jsonResponseParams = json.getJsonArray("response_parameters");
        responseParameters = jsonResponseParams.getValuesAs(JsonString.class)
                .stream()
                .map(s -> s.getChars().toString())
                .collect(Collectors.toSet());
    }
    
    
    
    public abstract JsonObject doTask(Progress pr);
    public  JsonObject doTask()
    {
        return doTask(new Progress(null));
    }
    
}
