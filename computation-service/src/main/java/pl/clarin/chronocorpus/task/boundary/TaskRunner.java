package pl.clarin.chronocorpus.task.boundary;

import javax.json.JsonObject;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class TaskRunner {

    private static final Logger LOGGER = Logger.getLogger(TaskRunner.class.getName());
    TaskDelegate taskDelegate;
    ExecutorService executor = Executors.newFixedThreadPool(3);

    public TaskRunner(TaskDelegate task) {
        this.taskDelegate = task;
    }

    public Future<JsonObject>  doTask() {
        Callable<JsonObject> task = () -> {
            long start = System.currentTimeMillis();
            JsonObject r = taskDelegate.doTask();
            long end = System.currentTimeMillis();
            LOGGER.info("Execution of "+taskDelegate.getTaskType()+" id:"+taskDelegate.getTaskId() + " took: " + (end-start) + "ms");
            return r;
        };
        return executor.submit(task);
    }

}
