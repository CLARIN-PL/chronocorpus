package pl.clarin.chronocorpus.concordance.entity;

import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Word;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.UUID;
import java.util.stream.Collectors;

public class Concordance {

    private UUID documentId;
    private String left;
    private String word;
    private String right;

    public Concordance(UUID documentId,String lemma) {
        this.documentId = documentId;
        this.word = lemma;
    }

    public Concordance(UUID id, String left, String word, String right) {
        this.documentId = id;
        this.left = left;
        this.word = word;
        this.right = right;
    }

    public UUID getDocumentId() {
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

    public Concordance mapSentenceToConcordance(Sentence s){
        String[] sentence = s.getWords().stream()
                .map(Word::getBase)
                .collect(Collectors.joining(" ")).split(word);
        this.left = sentence.length > 0 ? sentence[0] : "";
        this.right = sentence.length > 1 ? sentence[2] : "";
        return this;
    }
}