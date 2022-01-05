package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;
import pl.clarin.chronocorpus.timeseries.entity.TimeUnit;

public class TimeSeriesQuery extends Query {

    public static class Builder extends Query.Builder<TimeSeriesQuery.Builder> {

        @Override
        public TimeSeriesQuery.Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.time_series;
        }

        public TimeSeriesQuery.Builder withBase(String base) {
            Property p = new Property("base", base.toLowerCase());
            withQueryProperty(p);
            return this;
        }

        public TimeSeriesQuery.Builder withOrth(String orth) {
            Property p = new Property("orth", orth);
            withQueryProperty(p);
            return this;
        }

        public TimeSeriesQuery.Builder withPartOfSpeech(String pos) {
            Property p = new Property("part_of_speech", pos);
            withQueryProperty(p);
            return this;
        }

        public TimeSeriesQuery.Builder withUnit(TimeUnit unit) {
            Property p = new Property("time_unit", unit.name());
            withQueryProperty(p);
            return this;
        }

        public TimeSeriesQuery.Builder withSemanticList(String lst) {
            Property p = new Property("semantic_list", lst);
            withQueryProperty(p);
            return this;
        }

        public TimeSeriesQuery build() {
            return new TimeSeriesQuery(this);
        }
    }

    private TimeSeriesQuery(Builder builder) {
        super(builder);
    }

}
