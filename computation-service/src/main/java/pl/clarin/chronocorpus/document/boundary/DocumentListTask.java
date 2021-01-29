package pl.clarin.chronocorpus.document.boundary;

import pl.clarin.chronocorpus.Progress;
import pl.clarin.chronocorpus.document.control.DocumentQueryService;
import pl.clarin.chronocorpus.document.entity.DocumentListItem;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Optional;

public class DocumentListTask extends Task {

    public DocumentListTask(JsonObject json) {
        super(json);
    }

    @Override
    public JsonObject doTask(Progress pr) {

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id);

        JsonArrayBuilder documents = Json.createArrayBuilder();

        if (!metadata.isEmpty()) {
            DocumentQueryService.getInstance()
                    .findDocumentsByMetadata(metadata)
                    .stream().map(DocumentListItem::new)
                    .forEach(d -> documents.add(d.toJson()));
        } else {
            DocumentQueryService.getInstance().getDocumentItemList()
                    .stream().map(DocumentListItem::new)
                    .forEach(d -> documents.add(d.toJson()));
        }
        json.add("documents", documents);
        return json.build();
    }
}
