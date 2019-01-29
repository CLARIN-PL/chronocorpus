package pl.clarin.chronocorpus.document.boundary;

import pl.clarin.chronocorpus.document.control.DocumentQueryService;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.boundary.Task;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentTask implements Task {
    private String id;
    private Set<Property> metadata;
    private Set<Property> params;
    private boolean byId;

    public DocumentTask(JsonObject json){
        this.id = json.getString("id");

        JsonArray jsonParams = json.getJsonArray("params");
        params = jsonParams.getValuesAs(JsonObject.class)
                .stream()
                .map(j -> new Property(j.getString("name"), j.getString("value")))
                .collect(Collectors.toSet());

        JsonArray jsonMetadata = json.getJsonArray("metadata_filter");
        metadata = jsonMetadata.getValuesAs(JsonObject.class)
                .stream()
                .map(j -> new Property(j.getString("name"), j.getString("value")))
                .collect(Collectors.toSet());

        byId = params.stream().anyMatch(p -> p.getName().equals("document_id"));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public JsonObject doTask() {

        Optional<String> document_id = null;

        if(byId){
            document_id = params
                    .stream()
                    .filter(p -> p.getName().equals("document_id"))
                    .map(Property::getValueAsString)
                    .findFirst();


            return Json.createObjectBuilder()
                    .add("task_id", id)
                    .add("metadata", document_id.map(l -> DocumentQueryService.getInstance().getDocumentMetadataById(l)).
                            orElse(Json.createArrayBuilder().build()))
                    .add("text", document_id.map(l -> DocumentQueryService.getInstance().getDocumentSentencesById(l)).
                            orElse(Json.createArrayBuilder().build())).build();
        }
        else return Json.createObjectBuilder()
                .add("task_id", id)
                .add("documents", DocumentQueryService.getInstance().getDocumentsData(new ArrayList<>(metadata))).build();
    }
}
