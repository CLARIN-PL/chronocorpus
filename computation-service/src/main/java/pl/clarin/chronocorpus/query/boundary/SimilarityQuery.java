package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;

public class SimilarityQuery extends Query {

    public static class Builder extends Query.Builder<SimilarityQuery.Builder> {

        @Override
        public SimilarityQuery.Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.similarity;
        }

        public SimilarityQuery.Builder withFirstBase(String base) {
            Property p = new Property("first_base", base.toLowerCase());
            withQueryProperty(p);
            return this;
        }

        public SimilarityQuery.Builder withSecondBase(String base) {
            Property p = new Property("second_base", base.toLowerCase());
            withQueryProperty(p);
            return this;
        }

        public SimilarityQuery.Builder withFirstPartOfSpeech(String pos) {
            Property p = new Property("first_part_of_speech", pos);
            withQueryProperty(p);
            return this;
        }

        public SimilarityQuery.Builder withSecondPartOfSpeech(String pos) {
            Property p = new Property("second_part_of_speech", pos);
            withQueryProperty(p);
            return this;
        }

        public  SimilarityQuery.Builder withWindowItemPartOfSpeech(String pos){
            Property p = new Property("window_item_part_of_speech", pos);
            withQueryProperty(p);
            return this;
        }

        public SimilarityQuery.Builder withLeftWindowSize(String size) {
            Property p = new Property("left_window_size", Integer.parseInt(size));
            withQueryProperty(p);
            return this;
        }

        public SimilarityQuery.Builder withRightWindowSize(String size) {
            Property p = new Property("right_window_size", Integer.parseInt(size));
            withQueryProperty(p);
            return this;
        }

        public SimilarityQuery.Builder withStopList(String stopList) {
            Property p = new Property("stop_list", stopList);
            withQueryProperty(p);
            return this;
        }

        public SimilarityQuery build() {
            return new SimilarityQuery(this);
        }
    }

    private SimilarityQuery(Builder builder) {
        super(builder);
    }

}
