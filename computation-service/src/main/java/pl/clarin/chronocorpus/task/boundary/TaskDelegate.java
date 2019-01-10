package pl.clarin.chronocorpus.task.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class TaskDelegate {

    private TaskLookUp lookupService = new TaskLookUp();
    private Task task;
    private JsonObject taskJson;

    //TODO error handling
    public void setTaskString(String taskString){
        JsonReader reader = Json.createReader(new StringReader(taskString));
        this.taskJson = reader.readObject();
    }

    public JsonObject doTask() {
        task = lookupService.getTask(taskJson);
        return task.doTask(taskJson);
    }
}
