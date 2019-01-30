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
    //TODO Do załadowania ch.zip VM Options -Xms4g -Xmx8g
    //TODO Obecnie załadowany model zajmuje ok.6gb ram-u, zredukować parametry dokumentu do słowników int
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

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withBase("metal")
                .getJsonString());

        TaskManager.getInstance().submitTask(new DocumentQuery()
                .withDocumentId("4ebdbd67-4fea-4262-9ad5-3e6fb4501714").getJsonString());

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withOrth("śniegu")
                .getJsonString());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TaskManager.getInstance().submitTask(new DocumentQuery()
                .withDocumentId("0082b083-b99c-4808-a698-87f147146804").getJsonString());

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withBase("śnieg")
                .getJsonString());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withBase("czerwony")
                .getJsonString());

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withBase("zielony")
                .getJsonString());

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withBase("żółty")
                .getJsonString());

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withBase("młot")
                .getJsonString());

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withOrth("pałka")
                .getJsonString());

        TaskManager.getInstance().submitTask(new ConcordanceQuery()
                .withOrth("partia")
                .getJsonString());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TaskManager.getInstance().submitTask(new DocumentQuery()
                .withMetaPublicationMonth("5")
                .withMetaPublicationYear("1940").getJsonString());
    }

}
