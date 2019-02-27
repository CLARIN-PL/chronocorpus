package pl.clarin.chronocorpus;

import org.ini4j.Ini;
import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.query.boundary.DocumentQuery;
import pl.clarin.chronocorpus.task.boundary.TaskLookUp;
import pl.clarin.chronocorpus.task.entity.Task;
import pl.clarin.chronocorpus.worker.Service;
import pl.clarin.chronocorpus.worker.Worker;

import javax.json.JsonObject;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application extends Worker {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    private TaskLookUp lookup;

    public static void main(String... args) {
        new Service<>(Application.class);
        System.out.println(new DocumentQuery()
                .withMetaPublicationMonth("5")
                .withMetaPublicationYear("1940").getJson());
    }

    @Override
    public void static_init(Ini ini) throws Exception {
        Configuration.init(ini);
        if (DocumentStore.getInstance().hasNoStoredDocuments()) {
            try {
                DocumentStore.getInstance().setDocuments(DocumentFileLoader.getInstance().load());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error loading document store");
            }
        }
    }

    @Override
    public void init() throws Exception {
        lookup = new TaskLookUp();
    }

    @Override
    public JsonObject process(JsonObject json) throws RuntimeException {
        Optional<Task> task = lookup.getTask(json);
        return task.map(Task::doTask).
                orElseThrow(RuntimeException::new);
    }

}
