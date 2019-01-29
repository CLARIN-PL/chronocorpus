package pl.clarin.chronocorpus.document.control;

import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Word;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.*;
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
    public JsonArray findDocument(String id){

        Document document = DocumentStore.getInstance().getDocuments().stream().filter(d -> d.getId().equals(id)).findFirst().get();

        //document.getMetadata();

//        for(Sentence s : sentences){
//            System.out.println(s);
//        }

        JsonArrayBuilder documentSentences = Json.createArrayBuilder();


        //document.getSentences().stream().forEach(sentence -> documentSentences.add(DocumentMapper.getInstance().getDocumentContent(sentence)));
        document.getSentences()
                .stream()
                .map(sentence -> DocumentMapper.getInstance().getDocumentContent(sentence))
                .forEach(documentSentences::add);

        return documentSentences.build();
        };
    }

