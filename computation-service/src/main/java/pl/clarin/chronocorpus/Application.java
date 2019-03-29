package pl.clarin.chronocorpus;

import org.ini4j.Ini;
import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationObject;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationType;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationUnit;
import pl.clarin.chronocorpus.query.boundary.GeoNamesQuery;
import pl.clarin.chronocorpus.query.boundary.QuantityAnalysisQuery;
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

        QuantityAnalysisQuery anal = new QuantityAnalysisQuery.Builder()
                .calculationObject(CalculationObject.word)
                .calculationType(CalculationType.average)
                .calculationUnit(CalculationUnit.letter)
                .build();

        JsonObject j = anal.getJson();
        System.out.println(j);
        long start4 = System.currentTimeMillis();
        System.out.println(app.process(j));
        long time4 = System.currentTimeMillis() - start4;
        LOGGER.log(Level.INFO, "Task execution took: " + time4 + "ms");

        QuantityAnalysisQuery z = new QuantityAnalysisQuery.Builder()
                .calculationObject(CalculationObject.word)
                .calculationType(CalculationType.zipf_histogram)
                .calculationUnit(CalculationUnit.letter)
                .build();
        j = z.getJson();
        start4 = System.currentTimeMillis();
        System.out.println(app.process(j));
        time4 = System.currentTimeMillis() - start4;
        LOGGER.log(Level.INFO, "Task execution took: " + time4 + "ms");


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
