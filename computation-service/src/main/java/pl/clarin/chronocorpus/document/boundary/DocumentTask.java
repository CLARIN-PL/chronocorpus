package pl.clarin.chronocorpus.document.boundary;

import pl.clarin.chronocorpus.document.control.DocumentQueryService;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.boundary.Task;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentTask implements Task {
    private String id;
    private Set<Property> params;

    public DocumentTask(JsonObject json){
        this.id = json.getString("id");

        JsonArray jsonParams = json.getJsonArray("params");
        params = jsonParams.getValuesAs(JsonObject.class)
                .stream()
                .map(j -> new Property(j.getString("name"), j.getString("value")))
                .collect(Collectors.toSet());
        System.out.println(jsonParams);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public JsonObject doTask() {

        Optional<String> lemma = params
                .stream()
                .filter(p -> p.getName().equals("document_id"))
                .map(Property::getValueAsString)
                .findFirst();
        
        return Json.createObjectBuilder()
                .add("task_id", id)
                .add("text", lemma.map(l -> DocumentQueryService.getInstance().findDocument(l)).
                        orElse(Json.createArrayBuilder().build())).build();
            }
}
