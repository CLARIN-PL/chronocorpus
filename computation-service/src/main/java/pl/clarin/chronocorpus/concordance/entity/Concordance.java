package pl.clarin.chronocorpus.concordance.entity;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.UUID;

public class Concordance {

    private String documentId;
    private String left;
    private String word;
    private String right;

    public Concordance(String documentId,String lemma) {
        this.documentId = documentId;
        this.word = lemma;
    }

    public Concordance(String id, String left, String word, String right) {
        this.documentId = id;
        this.left = left;
        this.word = word;
        this.right = right;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getLeft() {
        return left;
    }

    public String getWord() {
        return word;
    }

    public String getRight() {
        return right;
    }

    public JsonObject getAsJson(){
        return Json.createObjectBuilder()
                .add("document_id", documentId.toString())
                .add("left",left)
                .add("word", word)
                .add("right",right).build();
    }
}