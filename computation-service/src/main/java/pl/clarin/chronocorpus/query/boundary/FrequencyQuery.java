package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;

public class FrequencyQuery extends Query {

    public static class Builder extends Query.Builder<FrequencyQuery.Builder> {

        @Override
        public FrequencyQuery.Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.frequency;
        }

        public FrequencyQuery.Builder countByBase(boolean base) {
            Property p = new Property("count_by_base", base);
            withQueryProperty(p);
            return this;
        }

        public FrequencyQuery.Builder stopList(String stop) {
            Property p = new Property("stop_list", stop);
            withQueryProperty(p);
            return this;
        }


        public FrequencyQuery build() {
            return new FrequencyQuery(this);
        }
    }

    private FrequencyQuery(Builder builder) {
        super(builder);
    }

}
