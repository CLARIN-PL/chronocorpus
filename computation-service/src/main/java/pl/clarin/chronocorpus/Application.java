package pl.clarin.chronocorpus;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.task.boundary.TaskDelegate;
import pl.clarin.chronocorpus.task.boundary.TaskRunner;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void main(String... args){

        DocumentStore.getInstance().initializeStore(documentsLoader());

        TaskDelegate taskDelegate = new TaskDelegate();
        taskDelegate.setTaskString("Json String zadania");

        TaskRunner taskRunner = new TaskRunner(taskDelegate);
        JsonObject result =  taskRunner.doTask();

        System.out.println(result);
    }

    //TODO add loading from ccl files or serialized db
    public static List<Document> documentsLoader(){
        return  new ArrayList<>();
    }

}
