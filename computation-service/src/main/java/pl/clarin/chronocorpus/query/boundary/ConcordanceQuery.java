package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;

public class ConcordanceQuery extends Query {

    public static class Builder extends Query.Builder<Builder> {
        @Override
        public Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.concordance;
        }

        public Builder withBase(String base) {
            Property p = new Property("base", base.toLowerCase());
            withQueryProperty(p);
            return this;
        }

        public Builder withOrth(String orth) {
            Property p = new Property("orth", orth);
            withQueryProperty(p);
            return this;
        }

        public ConcordanceQuery build() {
            return new ConcordanceQuery(this);
        }
    }

    private ConcordanceQuery(Builder builder) {
        super(builder);
    }

}
