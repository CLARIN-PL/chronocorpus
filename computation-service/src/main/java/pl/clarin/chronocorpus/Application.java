package pl.clarin.chronocorpus;

import org.ini4j.Ini;
import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationObject;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationType;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationUnit;
import pl.clarin.chronocorpus.query.boundary.*;
import pl.clarin.chronocorpus.task.boundary.TaskLookUp;
import pl.clarin.chronocorpus.task.boundary.UnknownTaskException;
import pl.clarin.chronocorpus.task.entity.Task;
import pl.clarin.chronocorpus.timeseries.entity.TimeUnit;
import pl.clarin.chronocorpus.wordprofile.entity.WordProfile;


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
/*
        ConcordanceQuery con = new ConcordanceQuery.Builder()
                .withOrth("powietrza")
                .build();
        app.testWithTimer(con, "CLimate");
        */

/*
        DocumentListQuery l = new DocumentListQuery.Builder()
                .withMetaPublicationYear("1953")
                .build();
        app.testWithTimer(l, "List doc:");
*/

 /*
        DocumentQuery doc = new DocumentQuery.Builder()
                .withDocumentId("23157")
                .build();

        app.testWithTimer(doc, "Document");


        GeoNamesQuery g = new GeoNamesQuery.Builder().build();
        app.testWithTimer(g, "map");

*/

        TimeSeriesQuery ana1l = new TimeSeriesQuery.Builder()
                .withBase("dom;mieszkanie;chałupa")
                .withPartOfSpeech("2")
                .withUnit(TimeUnit.day)
                .build();
        app.testWithTimer(ana1l, "Time series");

//        FrequencyQuery fq  = new FrequencyQuery.Builder()
//                .countByBase(true)
//                .build();
//
//        app.testWithTimer(fq, "FQ");

/*       WordProfileQuery wp = new WordProfileQuery.Builder()
                .withRightWindowSize("1")
                .withLeftWindowSize("1")
                .withPartOfSpeech("2")
                .withWindowItemPartOfSpeech("4")
                .withOrth("Szczecin")
                .build();

        app.testWithTimer(wp, "Word Profile");*/



/*        QuantityAnalysisQuery qa = new QuantityAnalysisQuery.Builder()
                .calculationUnit(CalculationUnit.letter)
                .calculationType(CalculationType.zipf_histogram)
                .calculationObject(CalculationObject.word)
                .build();

        app.testWithTimer(qa, "QAQ");*/
    }

    public Application() {
        loadConfiguration();
        lookup = new TaskLookUp();
        if (DocumentStore.getInstance().hasNoStoredDocuments()) {
            try {
                DocumentStore.getInstance().setDocuments(DocumentFileLoader.getInstance().load());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error loading document store", e);
            }
        }
    }

    private void loadConfiguration() {
        try {
            Ini ini = new Ini(new File("config.ini"));
            Configuration.init(ini);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Problems with init file", ex);
        }
    }

    public JsonObject process(JsonObject json) throws RuntimeException {
        Optional<Task> task = lookup.getTask(json);
        return task.map(Task::doTask).
                orElseThrow(UnknownTaskException::new);
    }

    public void testWithTimer(Query q, String name){
        JsonObject j = q.getJson();
        LOGGER.log(Level.INFO, name+" query json:"+ j);

        long start4 = System.currentTimeMillis();
        LOGGER.log(Level.INFO, name+" response json:"+ this.process(j));
        long time4 = System.currentTimeMillis() - start4;
        LOGGER.log(Level.INFO, name + " execution took: " + time4 + "ms");
    }
}
