package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.task.entity.TaskType;

public class StatisticsQuery extends Query {

    public static class Builder extends Query.Builder<Builder> {
        @Override
        public Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.statistics;
        }

        public StatisticsQuery build() {
            return new StatisticsQuery(this);
        }
    }

    private StatisticsQuery(Builder builder) {
        super(builder);
    }

}
