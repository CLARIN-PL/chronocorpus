package pl.clarin.chronocorpus.task.boundary;

import javax.json.JsonObject;

public interface Task {

    String getId();
    JsonObject doTask();

}
