package pl.clarin.chronocorpus.task.boundary;

import javax.json.JsonObject;

public class TaskRunner {

        TaskDelegate taskDelegate;

        public TaskRunner(TaskDelegate task){
            this.taskDelegate  = task;
        }

        public JsonObject doTask(){
            return taskDelegate.doTask();
        }

}
