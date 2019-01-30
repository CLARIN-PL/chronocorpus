package pl.clarin.chronocorpus;

import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.query.boundary.ConcordanceQuery;
import pl.clarin.chronocorpus.query.boundary.DocumentQuery;
import pl.clarin.chronocorpus.task.boundary.TaskManager;

import java.util.logging.Logger;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    //TODO pobrać pliki z ws.clarin-pl.eu/public i dodać do ccl
    //TODO ch1000.zip, chronopress_metadata.zip
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
        TaskManager.getInstance().submitTask(new DocumentQuery()
                .withDocumentId("0082b083-b99c-4808-a698-87f147146804").getJsonString());

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withOrth("czerwony")
                .withBase("śnieg")
                .getJsonString());

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withOrth("śniegu")
                .getJsonString());

        TaskManager.getInstance().submitTask(new DocumentQuery()
                .withMetaPublicationYear("1940")
                .getJsonString());
    }

}
