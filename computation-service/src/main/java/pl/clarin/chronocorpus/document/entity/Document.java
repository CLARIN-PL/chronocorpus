package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Document implements Serializable {

    private String id;

    private Metadata metadata;

    private List<Sentence> sentences = new ArrayList<>();

    public Document() {
    }

    public Document(String id, Metadata metadata) {
        this.id = id;
        this.metadata = metadata;
    }

    public void addSentence(Sentence s){
        sentences.add(s);
    }

    public String getId() {
        return id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

}
