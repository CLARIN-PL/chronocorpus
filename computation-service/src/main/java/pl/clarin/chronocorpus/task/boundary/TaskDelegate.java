package pl.clarin.chronocorpus.task.boundary;

import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskDelegate {

    private static final Logger LOGGER = Logger.getLogger(TaskDelegate.class.getName());
    private TaskLookUp lookupService = new TaskLookUp();
    private Task task;
    private JsonObject taskJson;

    public void setTaskString(String taskString){
        JsonReader reader = Json.createReader(new StringReader(taskString));
        try {
            this.taskJson = reader.readObject();
            task = lookupService.getTask(taskJson);
        } catch (Exception ex){
            LOGGER.log(Level.SEVERE, "Unable to parse json", ex);
        }
    }

    public JsonObject doTask() {
        return task.doTask();
    }

    public String getTaskType(){
        return task.getClass().getSimpleName();
    }

    public String getTaskId(){
        return task.getId();
    }

}
