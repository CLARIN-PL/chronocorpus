package pl.clarin.chronocorpus.administration.boundary;

import pl.clarin.chronocorpus.Progress;
import pl.clarin.chronocorpus.administration.control.StatisticsQueryService;
import pl.clarin.chronocorpus.task.entity.Task;


import javax.json.JsonObject;

public class StatisticsTask extends Task {

    public StatisticsTask(JsonObject json) {
       super(json);
    }

    @Override
    public JsonObject doTask(Progress pr) {
        return StatisticsQueryService.getInstance().getStatistics();
    }
}
