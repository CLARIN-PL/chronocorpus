package pl.clarin.chronocorpus;

import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.query.boundary.DocumentQuery;
import pl.clarin.chronocorpus.task.boundary.TaskLookUp;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.JsonObject;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    private TaskLookUp lookup;

    public Application() {

        lookup = new TaskLookUp();

        if (DocumentStore.getInstance().hasNoStoredDocuments()) {
            try {
                DocumentStore.getInstance().setDocuments(DocumentFileLoader.getInstance().load());
            } catch (Exception e) {
               LOGGER.log(Level.SEVERE, "Error loading document store");
            }
        }
    }

    public static void main(String... args) {
        System.out.println(new Application().process(new DocumentQuery()
                .withMetaPublicationMonth("5")
                .withMetaPublicationYear("1940").getJson()));
    }

    public JsonObject process(JsonObject json) throws RuntimeException {
        Optional<Task> task = lookup.getTask(json);
        return task.map(Task::doTask).
                orElseThrow(RuntimeException::new);
    }

}
