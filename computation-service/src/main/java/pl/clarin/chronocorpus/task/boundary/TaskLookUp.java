package pl.clarin.chronocorpus.task.boundary;

import pl.clarin.chronocorpus.concordance.boundary.ConcordanceTask;
import pl.clarin.chronocorpus.document.boundary.DocumentTask;

import javax.json.JsonObject;

public class TaskLookUp {

    public Task getTask(JsonObject json){

        if(json.containsKey("task_type")){
            if(json.getString("task_type").equalsIgnoreCase("concordance")) return new ConcordanceTask(json);
            if(json.getString("task_type").equalsIgnoreCase("document")) return new DocumentTask(json);
        }
        return null;
    }
}
