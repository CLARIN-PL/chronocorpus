package pl.clarin.chronocorpus.concordance.boundary;

import pl.clarin.chronocorpus.task.boundary.Task;
import pl.clarin.chronocorpus.concordance.control.ConcordanceQueryService;

import javax.json.JsonObject;

public class ConcordanceTask implements Task {

    @Override
    public JsonObject doTask(JsonObject json) {
            String lemma = json.getString("lemma");
            return ConcordanceQueryService.getInstance().findConcordance(lemma);
    }
}
