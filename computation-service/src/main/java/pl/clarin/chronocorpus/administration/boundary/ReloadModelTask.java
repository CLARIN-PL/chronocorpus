package pl.clarin.chronocorpus.administration.boundary;

import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import pl.clarin.chronocorpus.Progress;

public class ReloadModelTask extends Task {

    public ReloadModelTask(JsonObject json) {
        super(json);
    }

    //TODO Must stop running other tasks
    @Override
    public JsonObject doTask(Progress pr) {

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id);

        json.add("status", "complete");

        return json.build();
    }
}
