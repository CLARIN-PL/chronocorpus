package pl.clarin.chronocorpus.timeseries.boundary;

import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.JsonObject;

public class TimeSeriesTask extends Task {

    public TimeSeriesTask(JsonObject json) {
        super(json);
    }

    @Override
    public JsonObject doTask() {
        return null;
    }
}
