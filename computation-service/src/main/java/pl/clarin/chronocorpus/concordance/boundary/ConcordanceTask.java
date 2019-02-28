package pl.clarin.chronocorpus.concordance.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.concordance.control.ConcordanceQueryService;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.*;

public class ConcordanceTask extends Task {

    public ConcordanceTask(JsonObject json) {
       super(json);
    }

    private Optional<String> findBaseParameter(){
        return queryParameters.stream()
                .filter(p -> p.getName().equals("base"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    private Optional<String> findOrthParameter(){
        return queryParameters.stream()
                .filter(p -> p.getName().equals("orth"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    @Override
    public JsonObject doTask() {
        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id);

        findOrthParameter().ifPresent(w ->  json.add("rows", ConcordanceQueryService.getInstance()
                .findConcordance(w, responseParameters, metadata, false)));

        findBaseParameter().ifPresent(w ->  json.add("rows", ConcordanceQueryService.getInstance()
                .findConcordance(w, responseParameters, metadata, true)));

        return json.build();
    }
}
