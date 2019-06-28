package pl.clarin.chronocorpus.document.boundary;

import pl.clarin.chronocorpus.document.control.DocumentQueryService;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Optional;
import pl.clarin.chronocorpus.Progress;

public class DocumentTask extends Task {

    public DocumentTask(JsonObject json) {
        super(json);
    }

    private Optional<String> findDocumentIdParameter() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("document_id"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    @Override
    public JsonObject doTask(Progress pr) {

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id);

        JsonArrayBuilder documents = Json.createArrayBuilder();

        findDocumentIdParameter()
                .ifPresent(docId ->
                        DocumentQueryService.getInstance()
                                .findDocumentById(Integer.parseInt(docId))
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
