package pl.clarin.chronocorpus;

import g419.corpus.io.reader.ReaderFactory;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.task.boundary.TaskDelegate;
import pl.clarin.chronocorpus.task.boundary.TaskRunner;

import javax.json.JsonObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void main(String... args) throws Exception {

        String json = "{\"task\":\"concordance\",\"user\":\"username\",\"corpus\":[\"chronopress\"],\"properties\":[" +
                "{\"name\":\"lemma\",\"value\":\"partia\"}]}";

        DocumentStore.getInstance().initializeStore(documentsLoader());

        TaskDelegate taskDelegate = new TaskDelegate();
        taskDelegate.setTaskString(json);

        TaskRunner taskRunner = new TaskRunner(taskDelegate);
        JsonObject result =  taskRunner.doTask();

        System.out.println(result);
    }

    //TODO add loading from ccl files or serialized db
    //TODO check chronopress ccl incompatible with reader
    public static List<Document> documentsLoader() throws Exception {
        File initialFile = new File("ccl/422.ccl");
        InputStream is = new FileInputStream(initialFile);

        g419.corpus.structure.Document d = ReaderFactory.get().getStreamReader("url", is, "ccl")
                .nextDocument();

        System.out.println(d);
        return  new ArrayList<>();
    }

}
