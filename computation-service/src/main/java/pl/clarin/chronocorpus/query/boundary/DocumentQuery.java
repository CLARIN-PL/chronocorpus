package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;

public class DocumentQuery extends Query{

    public DocumentQuery() {
        super(TaskType.document);
    }

    public DocumentQuery withDocumentId(String id) {
        Property p = new Property("document_id", id);
        params.add(p.toJson());
        return this;
    }

}
