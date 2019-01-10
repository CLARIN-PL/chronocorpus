package pl.clarin.chronocorpus.document.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Document {

    private UUID id;

    private Metadata metadata;

    private String text;

    public Document(UUID id, Metadata metadata, String text) {
        this.id = id;
        this.metadata = metadata;
        this.text = text;
    }

    private List<Sentence> sentences = new ArrayList<>();

    public void addSentence(Sentence s){
        sentences.add(s);
    }

    public UUID getId() {
        return id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public String getText() {
        return text;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }
}
