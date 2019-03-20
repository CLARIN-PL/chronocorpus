package pl.clarin.chronocorpus.administration;

import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ReloadModelTask extends Task {

    public ReloadModelTask(JsonObject json) {
        super(json);
    }

    //TODO Must stop running other tasks
    @Override
    public JsonObject doTask() {

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id);

        json.add("status", "complete");

        return json.build();
    }
}
