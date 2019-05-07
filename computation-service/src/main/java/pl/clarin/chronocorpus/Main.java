package pl.clarin.chronocorpus;

import org.ini4j.Ini;
import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.query.boundary.ConcordanceQuery;
import pl.clarin.chronocorpus.task.boundary.TaskLookUp;
import pl.clarin.chronocorpus.task.boundary.UnknownTaskException;
import pl.clarin.chronocorpus.task.entity.Task;
import pl.clarin.chronocorpus.worker.Service;
import pl.clarin.chronocorpus.worker.Worker;

import javax.json.JsonObject;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tomasz Walkowiak
 */
public class Main extends Worker {
    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());
    private TaskLookUp lookup; 

    @Override
    public void init() throws Exception {
        lookup = new TaskLookUp();
    }

    @Override
    public JsonObject process(JsonObject input) throws Exception {
        Optional<Task> task;
        try {
        task = lookup.getTask(input);
        } catch(RuntimeException e)
        {
            LOGGER.log(Level.FINE,"Error in code", e);
            throw  new Exception("Internal error");
        }
        return task.map(Task::doTask).
                orElseThrow(UnknownTaskException::new);
    }

    public static void main(String[] args) {
        new Service<>(Main.class);
    }

    @Override
    public void static_init(Ini init) throws Exception {
        if (DocumentStore.getInstance().hasNoStoredDocuments()) {
            try {
                DocumentStore.getInstance().setDocuments(DocumentFileLoader.getInstance().load());
            } catch (Exception e) {
               LOGGER.log(Level.SEVERE, "Error while loading documents", e);
            }
        }
    }

}
