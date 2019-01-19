package pl.clarin.chronocorpus.concordance.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.boundary.Task;
import pl.clarin.chronocorpus.concordance.control.ConcordanceQueryService;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.*;
import java.util.stream.Collectors;

public class ConcordanceTask implements Task {

    private String id;
    private Set<Property> metadata;
    private Set<Property> params;

    public ConcordanceTask(JsonObject json) {
        this.id = json.getString("id");

        JsonArray jsonParams = json.getJsonArray("params");
        params = jsonParams.getValuesAs(JsonObject.class)
                .stream()
                .map(j -> new Property(j.getString("name"), j.getString("value")))
                .collect(Collectors.toSet());

        JsonArray jsonMetadata = json.getJsonArray("metadata_filter");
        metadata = jsonParams.getValuesAs(JsonObject.class)
                .stream()
                .map(j -> new Property(j.getString("name"), j.getString("value")))
                .collect(Collectors.toSet());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public JsonObject doTask() {

            Optional<String> lemma = params
                    .stream()
                    .filter(p -> p.getName().equals("base"))
                    .map(Property::getValueAsString)
                    .findFirst();

            return Json.createObjectBuilder()
                    .add("task_id", id)
                    .add("rows", lemma.map(l -> ConcordanceQueryService.getInstance().findConcordanceByBase(l)).
                            orElse(Json.createArrayBuilder().build())).build();

//                        Optional<String> lemma = properties.getValuesAs(JsonObject.class)
//                    .stream()
//                    .filter(p -> p.getString("name").equals("orth"))
//                    .map(l -> l.getString("value"))
//                    .findFirst();
//
//            return lemma.map(l -> ConcordanceQueryService.getInstance().findConcordanceByOrth(l))
//                    .orElse(Json.createObjectBuilder().build());
//
    }
}
