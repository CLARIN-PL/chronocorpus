package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;

public class DictionaryQuery extends Query {

    public static class Builder extends Query.Builder<DictionaryQuery.Builder> {

        @Override
        public DictionaryQuery.Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.dictionaries;
        }

        public DictionaryQuery.Builder findDictionaries(boolean base) {
            Property p = new Property("dictionaries", base);
            withQueryProperty(p);
            return this;
        }

        public DictionaryQuery.Builder propertyValueList(String name) {
            Property p = new Property("property_value_list", name);
            withQueryProperty(p);
            return this;
        }


        public DictionaryQuery build() {
            return new DictionaryQuery(this);
        }
    }

    private DictionaryQuery(Builder builder) {
        super(builder);
    }

}
