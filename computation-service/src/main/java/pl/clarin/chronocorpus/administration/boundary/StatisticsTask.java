package pl.clarin.chronocorpus.administration.boundary;

import pl.clarin.chronocorpus.Progress;
import pl.clarin.chronocorpus.administration.control.StatisticsQueryService;
import pl.clarin.chronocorpus.concordance.control.ConcordanceQueryServiceNew;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsTask extends Task {

    public StatisticsTask(JsonObject json) {
       super(json);
    }

    @Override
    public JsonObject doTask(Progress pr) {
        return StatisticsQueryService.getInstance().getStatistics();
    }
}
