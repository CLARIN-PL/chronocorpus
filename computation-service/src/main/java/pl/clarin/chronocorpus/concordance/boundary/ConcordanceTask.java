package pl.clarin.chronocorpus.concordance.boundary;

import pl.clarin.chronocorpus.administration.control.StatisticsQueryService;
import pl.clarin.chronocorpus.administration.entity.Statistics;
import pl.clarin.chronocorpus.concordance.control.ConcordanceQueryServiceNew;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.concordance.control.ConcordanceQueryService;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.*;
import pl.clarin.chronocorpus.Progress;

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

    private Optional<String> findWindowModeParameter(){
        return queryParameters.stream()
                .filter(p -> p.getName().equals("window_mode"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    @Override
    public JsonObject doTask(Progress pr) {
        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id);

        findOrthParameter().ifPresent(w ->  json.add("rows", ConcordanceQueryServiceNew.getInstance()
                .findConcordance(w, responseParameters, metadata, false)));

        findBaseParameter().ifPresent(w ->  json.add("rows", ConcordanceQueryServiceNew.getInstance()
                .findConcordance(w, responseParameters, metadata, true)));
        StatisticsQueryService.getInstance().updateConcordanceQueryCount();
        return json.build();
    }
}
