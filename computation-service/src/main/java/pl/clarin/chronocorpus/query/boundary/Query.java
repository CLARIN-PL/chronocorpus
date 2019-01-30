package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.UUID;

public abstract class Query {

    protected JsonObjectBuilder json = Json.createObjectBuilder();
    protected JsonArrayBuilder params = Json.createArrayBuilder();
    protected JsonArrayBuilder meta = Json.createArrayBuilder();

    public Query(TaskType tt) {
        json.add("id", UUID.randomUUID().toString());
        json.add("task_type", tt.name());
    }

    public Query withMetaPublicationYear(String year) {
        Property p = new Property("publication_year", year);
        meta.add(p.toJson());
        return this;
    }

    public Query withMetaPublicationMonth(String month) {
        Property p = new Property("publication_month", month);
        meta.add(p.toJson());
        return this;
    }

    public Query withMetaPublicationDay(String day) {
        Property p = new Property("publication_day", day);
        meta.add(p.toJson());
        return this;
    }

    public String getJsonString() {
        json.add("metadata_filter", meta);
        json.add("params", params);
        return json.build().toString();
    }
}

