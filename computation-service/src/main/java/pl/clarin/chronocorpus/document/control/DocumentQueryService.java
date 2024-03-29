package pl.clarin.chronocorpus.document.control;

import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentQueryService {

    public static volatile DocumentQueryService instance;

    private DocumentQueryService() {
    }

    public static DocumentQueryService getInstance() {
        if (instance == null) {
            synchronized (DocumentStore.class) {
                if (instance == null) {
                    instance = new DocumentQueryService();
                }
            }
        }
        return instance;
    }

    public List<Document> findDocumentsByMetadata(Set<Property> metadata){
        return DocumentStore.getInstance().getDocuments()
                .stream()
                .filter(d -> d.getMetadata().matches(metadata))
                .collect(Collectors.toList());
    }

    public List<Document> getDocumentItemList(){
        return new ArrayList<>(DocumentStore.getInstance().getDocuments());
    }

    public Optional<Document> findDocumentById(int id) {
        return DocumentStore.getInstance()
                .getDocuments()
                .stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }

}


