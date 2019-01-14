package pl.clarin.chronocorpus.concordance.boundary;

import pl.clarin.chronocorpus.task.boundary.Task;
import pl.clarin.chronocorpus.concordance.control.ConcordanceQueryService;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.Optional;

public class ConcordanceTask implements Task {

    @Override
    public JsonObject doTask(JsonObject json) {
            JsonArray properties = json.getJsonArray("properties");

            Optional<String> lemma = properties.getValuesAs(JsonObject.class)
                    .stream()
                    .filter(p -> p.getString("name").equals("lemma"))
                    .map(l -> l.getString("value"))
                    .findFirst();

            return lemma.map(l -> ConcordanceQueryService.getInstance().findConcordance(l))
                    .orElse(Json.createObjectBuilder().build());
    }
}
