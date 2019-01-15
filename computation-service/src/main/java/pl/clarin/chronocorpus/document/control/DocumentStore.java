package pl.clarin.chronocorpus.document.control;

import pl.clarin.chronocorpus.document.entity.Document;

import java.util.HashSet;
import java.util.Set;

public class DocumentStore {

     Set<Document> documents = new HashSet<>();

    public DocumentStore() {
        super();
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}
