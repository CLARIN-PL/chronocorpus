package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.task.entity.TaskType;

public class GeoNamesQuery extends Query {

    public static class Builder extends Query.Builder<GeoNamesQuery.Builder> {

        @Override
        public GeoNamesQuery.Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.geo_proper_names;
        }

        public GeoNamesQuery build() {
            return new GeoNamesQuery(this);
        }
    }

    private GeoNamesQuery(Builder builder) {
        super(builder);
    }

}
