package pl.clarin.chronocorpus.task.boundary;

import javax.json.JsonObject;

public interface Task {

    JsonObject doTask(JsonObject json);

}
