package pl.clarin.chronocorpus.document.control;

import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.List;


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
    public JsonArray getDocumentSentencesById(String id){
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        findDocumentById(id).getSentences().stream().map(sentence -> DocumentMapper.getInstance().getDocumentContent(sentence)).forEach(x ->jsonArrayBuilder.add(x));
        return jsonArrayBuilder.build();
    }


    public JsonArray getDocumentMetadataById(String id){
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Property p : findDocumentById(id).getMetadata().getProperties())
            jsonArrayBuilder.add(p.getAsJson());
        return jsonArrayBuilder.build();
    }

    private Document findDocumentById(String id) {
        return DocumentStore.getInstance().getDocuments().stream().filter(d -> d.getId().equals(id)).findFirst().get();
    }

    public JsonArrayBuilder getDocumentsData(List<Property> metadata){
        JsonArrayBuilder documentsData = Json.createArrayBuilder();
        for (Document d : DocumentStore.getInstance().getDocuments())
            if (d.getMetadata().matches(metadata))
                documentsData.add(getDocumentData(d.getId()));
        return documentsData;
    }

    JsonObjectBuilder getDocumentData(String id){
        JsonObjectBuilder documentData = Json.createObjectBuilder();
        documentData.add("id", id)
                .add("metadata", getDocumentMetadataById(id))
//                .add("text", getDocumentSentencesById(id));
                .add("text", "sentences"); //na potrzeby testów
        return documentData;

    }

}


