package pl.clarin.chronocorpus;

import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.task.boundary.TaskManager;

import java.util.logging.Logger;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    public Application() {
        if (DocumentStore.getInstance().hasNoStoredDocuments()) {
            try {
               DocumentStore.getInstance().setDocuments(DocumentFileLoader.getInstance().load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String... args) {
        new Application().run();
    }

    public void run() {
         TaskManager.getInstance().submitTask(createDocumentJson("123", "0082b083-b99c-4808-a698-87f147146804"));
         TaskManager.getInstance().submitTask(createConcordanceJson("1232121", "śnieg"));
         TaskManager.getInstance().submitTask(createConcordanceJson("546", "samolot"));
         TaskManager.getInstance().submitTask(createConcordanceJson("6543", "partia"));
//        TaskManager.getInstance().submitTask(createConcordanceJson("14545", "czerwony"));
//        TaskManager.getInstance().submitTask(createConcordanceJson("54555", "armia"));
//        TaskManager.getInstance().submitTask(createConcordanceJson("4543", "robić"));
//        TaskManager.getInstance().submitTask(createConcordanceJson("222123", "palic"));
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//          TaskManager.getInstance().submitTask(createConcordanceJson("1455555", "czerwony"));
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        TaskManager.getInstance().submitTask(createConcordanceJson("1423455", "ludzie"));

    }

    public String createDocumentJson(String id, String documentId) {
        return "" +
                "{" +
                "\"id\":\"" + id + "\"," +
                "\"task_type\":\"document\"," +
                "\"user\":\"username\"," +
                "\"corpus\":[\"chronopress\"]," +
                "\"metadata_filter\":[" +
                "{\"name\":\"publication_year\",\"value\":\"1945\"}]," +
                "\"params\":[" +
                "{\"name\":\"document_id\",\"value\":\"" + documentId + "\"}]" +
                "}";
    }

    public String createConcordanceJson(String id, String base) {
        return "" +
                "{" +
                "\"id\":\"" + id + "\"," +
                "\"task_type\":\"concordance\"," +
                "\"user\":\"username\"," +
                "\"corpus\":[\"chronopress\"]," +
                "\"metadata_filter\":[]," +
 //               "{\"name\":\"publication_year\",\"value\":\"1945\"}]," +
//                "{\"name\":\"author\",\"value\":\"Janusz\"}]," +
//                "\"publication\":[" +
//                "{\"name\":\"publication_mode\",\"value\":\"collection\"}," +
//                "{\"name\":\"publication_year\",\"value\":\"1946\"}," +
//                "{\"name\":\"publication_year\",\"value\":\"1945\"}]," +
//                "{\"name\":\"publication_mode\",\"value\":\"specific\"}," +
//                "{\"name\":\"publication_day\",\"value\":\"12\"}," +
//                "{\"name\":\"publication_month\",\"value\":\"5\"}]," +
//                "{\"name\":\"publication_year\",\"value\":\"1945\"}]," +
                "\"params\":[" +
                "{\"name\":\"base\",\"value\":\"" + base + "\"}]" +
                "}";
    }

}
