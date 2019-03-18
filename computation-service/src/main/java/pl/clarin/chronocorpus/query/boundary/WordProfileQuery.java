package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;
import pl.clarin.chronocorpus.timeseries.entity.TimeUnit;

public class WordProfileQuery extends Query {

    public static class Builder extends Query.Builder<WordProfileQuery.Builder> {

        @Override
        public WordProfileQuery.Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.word_profile;
        }

        public WordProfileQuery.Builder withBase(String base) {
            Property p = new Property("base", base.toLowerCase());
            withQueryProperty(p);
            return this;
        }

        public WordProfileQuery.Builder withOrth(String orth) {
            Property p = new Property("orth", orth);
            withQueryProperty(p);
            return this;
        }

        public WordProfileQuery.Builder withPartOfSpeech(String pos) {
            Property p = new Property("part_of_speech", pos);
            withQueryProperty(p);
            return this;
        }

        public WordProfileQuery.Builder withLeftWindowSize(String size) {
            Property p = new Property("left_window_size", Integer.valueOf(size));
            withQueryProperty(p);
            return this;
        }

        public WordProfileQuery.Builder withRightWindowSize(String size) {
            Property p = new Property("right_window_size", Integer.valueOf(size));
            withQueryProperty(p);
            return this;
        }

        public WordProfileQuery build() {
            return new WordProfileQuery(this);
        }
    }

    private WordProfileQuery(Builder builder) {
        super(builder);
    }

}
