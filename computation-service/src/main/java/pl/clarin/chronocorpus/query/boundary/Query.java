package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.UUID;
import java.util.stream.Stream;

public class Query {

    protected JsonObjectBuilder json;
    protected JsonArrayBuilder queryParameters;
    protected JsonArrayBuilder meta;
    protected JsonArrayBuilder responseParameters;

    public static abstract class Builder<T extends Builder<T>> {

        private JsonObjectBuilder json = Json.createObjectBuilder();
        private JsonArrayBuilder queryParameters = Json.createArrayBuilder();
        private JsonArrayBuilder meta = Json.createArrayBuilder();
        private JsonArrayBuilder responseParameters = Json.createArrayBuilder();

        protected abstract T getThis();

        public abstract TaskType getTaskType();

        public T withQueryProperty(Property p) {
            queryParameters.add(p.toJson());
            return getThis();
        }

        public T withMetaJurnalTitle(String title) {
            Property p = new Property("journal_title", title);
            meta.add(p.toJson());
            return getThis();
        }

        public T withMetaPublicationYear(String year) {
            Property p = new Property("publication_year", year);
            meta.add(p.toJson());
            return getThis();
        }

        public T withMetaPublicationMonth(String month) {
            Property p = new Property("publication_month", month);
            meta.add(p.toJson());
            return getThis();
        }

        public T withMetaPublicationDay(String day) {
            Property p = new Property("publication_day", day);
            meta.add(p.toJson());
            return getThis();
        }

        public T withMetaAuthor(String authors) {
            Property p = new Property("authors", authors);
            meta.add(p.toJson());
            return getThis();
        }

        public T withAdditionalResponseProperties(String... props) {
            Stream.of(props).forEach(responseParameters::add);
            return getThis();
        }

        public Query build() {
            return new Query(this);
        }
    }

    protected Query(Builder builder) {
        this.json = builder.json;
        this.meta = builder.meta;
        this.queryParameters = builder.queryParameters;
        this.responseParameters = builder.responseParameters;

        json.add("id", UUID.randomUUID().toString());
        json.add("task_type", builder.getTaskType().name());
        json.add("metadata_filter", meta);
        json.add("query_parameters", queryParameters);
        json.add("response_parameters", responseParameters);
    }

    public JsonObject getJson() {
        return json.build();
    }
}

