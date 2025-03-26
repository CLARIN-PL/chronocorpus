package pl.clarin.chronocorpus;

import org.ini4j.Ini;
import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.geographicalpropernames.boundary.GeographicalProperNamesTask;
import pl.clarin.chronocorpus.query.boundary.*;
import pl.clarin.chronocorpus.task.boundary.TaskLookUp;
import pl.clarin.chronocorpus.task.boundary.UnknownTaskException;
import pl.clarin.chronocorpus.task.entity.Task;
import pl.clarin.chronocorpus.timeseries.entity.TimeUnit;


import javax.json.JsonObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    private final TaskLookUp lookup;

    public static void main(String... args) {

        Application app = new Application();

/*        SimilarityQuery sim = new SimilarityQuery.Builder()
                .withFirstBase("komunizm")
                .withSecondBase("socjalizm")
                .withFirstPartOfSpeech("2")
                .withSecondPartOfSpeech("2")
                .withLeftWindowSize("1")
                .withRightWindowSize("1")
                .withWindowItemPartOfSpeech("0")
                .build();
        app.testWithTimer(sim, "Sim");*/

        GeoNamesQuery mm = new GeoNamesQuery.Builder()
                .withMetaPublicationYear("1966")
                .build();
        app.testWithTimer(mm, "Dic");

/*        DictionaryQueryService.getInstance().getSemanticListByName("colors")
                .forEach(System.out::println);*/
/*
        DictionaryQuery dic = new DictionaryQuery.Builder()
                .findDictionaries(true)
                .build();
        app.testWithTimer(dic, "Dic");*/
//
/*
       ConcordanceQuery con = new ConcordanceQuery.Builder()
                .withBase("osoba")
                .build();
        app.testWithTimer(con, "Conc");
*/

/*        StatisticsQuery stat = new StatisticsQuery.Builder()
                .build();
        app.testWithTimer(stat, "Stats");*/


       DocumentStore.getInstance()
                .getDocuments()
               .stream()
               .forEach(d -> {
            System.out.println(d);
            System.out.println(d.toText());
            System.out.println("-******-");
            System.out.println(d.getMetadata().toJson());
            System.out.println("---");
        });
       // app.testWithTimer(doc, "Document");

/*       TimeSeriesQuery ser = new TimeSeriesQuery.Builder()
                .withBase("wiosna;lato;zima;jesień")
                .withPartOfSpeech("2")
                .withUnit(TimeUnit.month)
                .build();

        app.testWithTimer(ser, "TS");

/*        FrequencyQuery fq  = new FrequencyQuery.Builder()
                .countByBase(true)
                .withMetaJurnalTitle("Chłopi")
                .build();

        app.testWithTimer(fq, "FQ");*/

/*      WordProfileQuery wp = new WordProfileQuery.Builder()
                .withRightWindowSize("1")
                .withLeftWindowSize("1")
                .withPartOfSpeech("2")
                .withWindowItemPartOfSpeech("0")
                .withBase("komunizm")
              .withStopList("być;który;jak;mieć")
              .build();

        app.testWithTimer(wp, "WP");*/
/*
        TimeSeriesQuery ser = new TimeSeriesQuery.Builder()
                .withSemanticList("colors")
                .withPartOfSpeech("0")
                .withUnit(TimeUnit.year)
                .build();

        app.testWithTimer(ser, "TS");*/


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

    public void testWithTimerSaveToFile(Query q, String name, String meta , PrintWriter printWriter){
        JsonObject j = q.getJson();
        LOGGER.log(Level.INFO, name+" query json:"+ j);

        long start4 = System.currentTimeMillis();
        String r = this.process(j).toString();
        LOGGER.log(Level.INFO, name+" response json:"+ r);
        printWriter.println(meta+ r);
        long time4 = System.currentTimeMillis() - start4;
        LOGGER.log(Level.INFO, name + " execution took: " + time4 + "ms");
    }
}
