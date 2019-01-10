package pl.clarin.chronocorpus.document.control;

import pl.clarin.chronocorpus.document.entity.Document;

import java.util.Collections;
import java.util.List;

public class DocumentStore {

    public static volatile DocumentStore instance;

    private static List<Document> store;

    private DocumentStore(){
    }

    public static DocumentStore getInstance() {
        if (instance == null) {
            synchronized (DocumentStore.class) {
                if (instance == null) {
                    instance = new DocumentStore();
                }
            }
        }
        return instance;
    }

    public void initializeStore(List<Document> documents){
        store = Collections.unmodifiableList(documents);
    }

    public List<Document> getStore(){
        return  store;
    }
}
