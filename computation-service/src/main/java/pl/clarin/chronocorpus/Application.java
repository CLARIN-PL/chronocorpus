package pl.clarin.chronocorpus;

import org.ini4j.Ini;
import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.query.boundary.ConcordanceQuery;
import pl.clarin.chronocorpus.query.boundary.DocumentQuery;
import pl.clarin.chronocorpus.query.boundary.FrequencyQuery;
import pl.clarin.chronocorpus.task.boundary.TaskLookUp;
import pl.clarin.chronocorpus.task.boundary.UnknownTaskException;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    private TaskLookUp lookup;

    public static void main(String... args) {
        Application app = new Application();

        FrequencyQuery frq = new FrequencyQuery.Builder()
                .countByBase(true)
                .build();

        JsonObject j = frq.getJson();
        System.out.println(j.toString());
        long start = System.currentTimeMillis();
        System.out.println(app.process(j));
        long time = System.currentTimeMillis() - start;
        LOGGER.log(Level.INFO,"Task execution took: " + time + "ms");
/*
        ConcordanceQuery concord = new ConcordanceQuery.Builder()
                .withOrth("Armia Czerwona")
                .build();

        j = concord.getJson();
        System.out.println(j.toString());
        System.out.println(app.process(j));*/
    }

    public Application() {
        loadConfiguration();
        lookup = new TaskLookUp();
        if (DocumentStore.getInstance().hasNoStoredDocuments()) {
            try {
                DocumentStore.getInstance().setDocuments(DocumentFileLoader.getInstance().load());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error loading document store");
            }
        }
    }

    private void loadConfiguration() {
        try {
            Ini ini = new Ini(new File("config.ini"));
            Configuration.init(ini);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Problems with init file", ex);
            return;
        }
    }

    public JsonObject process(JsonObject json) throws RuntimeException {
        Optional<Task> task = lookup.getTask(json);
        return task.map(Task::doTask).
                orElseThrow(UnknownTaskException::new);
    }
}
