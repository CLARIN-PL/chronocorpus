package pl.clarin.chronocorpus.document.boundary;

import pl.clarin.chronocorpus.document.control.DocumentQueryService;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.query.boundary.DocumentQuery;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Optional;

public class DocumentTask extends Task {

    public DocumentTask(JsonObject json) {
        super(json);
    }

    private Optional<String> findDocumentIdParameter() {
        return params.stream()
                .filter(p -> p.getName().equals("document_id"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    @Override
    public JsonObject doTask() {

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id);

        JsonArrayBuilder documents = Json.createArrayBuilder();

        findDocumentIdParameter()
                .ifPresent(docId ->
                        DocumentQueryService.getInstance()
                                .findDocumentById(docId)
                                .ifPresent(d -> documents.add(d.toJson())));

        if (!metadata.isEmpty()) {
            DocumentQueryService.getInstance()
                    .findDocumentsByMetadata(metadata)
                    .forEach(d -> documents.add(d.toJson()));
        }
        json.add("documents", documents);
        return json.build();
    }
}
