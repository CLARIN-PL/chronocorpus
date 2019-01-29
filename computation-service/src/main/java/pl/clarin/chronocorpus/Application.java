package pl.clarin.chronocorpus;

import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.boundary.TaskManager;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.UUID;
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
        TaskManager.getInstance().submitTask(new Query(TaskType.document)
                        .withDocumentId("0082b083-b99c-4808-a698-87f147146804").getJsonString());

        TaskManager.getInstance().submitTask(new Query(TaskType.concordance)
                .withBase("śnieg")
                .getJsonString());

        TaskManager.getInstance().submitTask(new Query(TaskType.concordance)
                .withOrth("śniegu")
                .getJsonString());

        TaskManager.getInstance().submitTask(new Query(TaskType.concordance)
                .withMetaPublicationYear("1940")
                .getJsonString());
    }

    enum TaskType{
        document, concordance
    }

    class Query {

        private JsonObjectBuilder j = Json.createObjectBuilder();
        private JsonArrayBuilder params = Json.createArrayBuilder();
        private JsonArrayBuilder meta = Json.createArrayBuilder();

        public Query(TaskType tt){
            j.add("id", UUID.randomUUID().toString());
            j.add("task_type", tt.name());
        }

        public Query withBase(String base){
            Property p = new Property("base",base);
            params.add(p.getAsJson());
            return this;
        }

        public Query withOrth(String orth){
            Property p = new Property("orth",orth);
            params.add(p.getAsJson());
            return this;
        }

        public Query withDocumentId(String id){
            Property p = new Property("document_id", id);
            params.add(p.getAsJson());
            return this;
        }

        public Query withMetaPublicationYear(String year){
            Property p = new Property("publication_year", year);
            meta.add(p.getAsJson());
            return this;
        }

        public Query withMetaPublicationMonth(String month){
            Property p = new Property("publication_month", month);
            meta.add(p.getAsJson());
            return this;
        }
        public Query withMetaPublicationDay(String day){
            Property p = new Property("publication_dat", day);
            meta.add(p.getAsJson());
            return this;
        }

        public String getJsonString(){
            j.add("metadata_filter", meta);
            j.add("params", params);
            return j.build().toString();
        }
    }
}
