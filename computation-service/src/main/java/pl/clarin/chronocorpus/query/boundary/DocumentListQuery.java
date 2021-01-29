package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.task.entity.TaskType;

public class DocumentListQuery extends Query {

    public static class Builder extends Query.Builder<DocumentListQuery.Builder> {

        @Override
        public DocumentListQuery.Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.document_list;
        }

        public DocumentListQuery build() {
            return new DocumentListQuery(this);
        }
    }

    private DocumentListQuery(Builder builder) {
        super(builder);
    }

}
