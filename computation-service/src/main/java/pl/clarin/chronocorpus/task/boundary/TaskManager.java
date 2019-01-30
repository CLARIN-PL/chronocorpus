package pl.clarin.chronocorpus.task.boundary;

import pl.clarin.chronocorpus.Configuration;

import javax.json.JsonObject;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class TaskManager {

    private static final Logger LOGGER = Logger.getLogger(TaskManager.class.getName());
    private static final Map<String, Future<JsonObject>> activeTasks = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(Configuration.SCHEDULER_THREAD_POOL);
    private static final int INITIAL_DELAY = 0;
    private static final int PERIOD = 1;

    public static volatile TaskManager instance;

    private TaskManager(){
        Runnable activeTaskListener = getDefaultSchedulerTask();
        executor.scheduleAtFixedRate(activeTaskListener, INITIAL_DELAY, PERIOD, TimeUnit.SECONDS);
    }

    public void submitTask(String json){
        TaskDelegate taskDelegate = new TaskDelegate();
        taskDelegate.setTaskString(json);

        TaskRunner taskRunner = new TaskRunner(taskDelegate);
        activeTasks.put(taskDelegate.getTaskId(), taskRunner.doTask());
    }

    private Runnable getDefaultSchedulerTask() {
        return () -> {
                if(!activeTasks.isEmpty()){
                    activeTasks.forEach((key, value) -> {
                        if (value.isDone()) {
                            try {
                                //TODO wynik zadania przekazac dalej gdzie?
                                System.out.println(value.get());
                                activeTasks.remove(key);
                            } catch (InterruptedException | ExecutionException ee) {
                                ee.printStackTrace();
                            }
                        }
                    });
                }
            };
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            synchronized (TaskManager.class) {
                if (instance == null) {
                    instance = new TaskManager();
                }
            }
        }
        return instance;
    }
}
