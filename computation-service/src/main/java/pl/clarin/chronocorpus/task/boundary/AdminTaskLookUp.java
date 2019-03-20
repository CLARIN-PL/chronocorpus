package pl.clarin.chronocorpus.task.boundary;

import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.JsonObject;
import java.util.Optional;

public class AdminTaskLookUp {

    public Optional<Task> getTask(JsonObject json) {

        if (json.containsKey("task_type")) {

        }
        return Optional.empty();
    }
}
