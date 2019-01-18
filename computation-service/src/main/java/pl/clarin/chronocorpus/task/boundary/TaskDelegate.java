package pl.clarin.chronocorpus.task.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class TaskDelegate {

    private TaskLookUp lookupService = new TaskLookUp();
    private Task task;
    private JsonObject taskJson;

    public void setTaskString(String taskString){
        JsonReader reader = Json.createReader(new StringReader(taskString));
        try {
            this.taskJson = reader.readObject();
            task = lookupService.getTask(taskJson);
        } catch (Exception ex){
            ex.printStackTrace();
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
