package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;

public class DocumentQuery extends Query {

    public static class Builder extends Query.Builder<DocumentQuery.Builder> {

        @Override
        public DocumentQuery.Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.document;
        }

        public Builder withDocumentId(String id) {
            Property p = new Property("document_id", id);
            this.withQueryProperty(p);
            return this;
        }
        public DocumentQuery build() {
            return new DocumentQuery(this);
        }
    }

    private DocumentQuery(Builder builder) {
        super(builder);
    }

}
