package pl.clarin.chronocorpus.concordance.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;

import javax.json.Json;
import javax.json.JsonObject;

public class ConcordanceQueryService {

    public static volatile ConcordanceQueryService instance;

    private ConcordanceQueryService(){
    }

    public static ConcordanceQueryService getInstance() {
        if (instance == null) {
            synchronized (DocumentStore.class) {
                if (instance == null) {
                    instance = new ConcordanceQueryService();
                }
            }
        }
        return instance;
    }

    public JsonObject findConcordance(String lemma){
        return Json.createObjectBuilder().build();
    }
}
