package pl.clarin.chronocorpus.document.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Document {

    private UUID id;

    private Metadata metadata;

    private List<Sentence> sentences = new ArrayList<>();

    public Document(UUID id, Metadata metadata) {
        this.id = id;
        this.metadata = metadata;
    }

    public void addSentence(Sentence s){
        sentences.add(s);
    }

    public UUID getId() {
        return id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }
}
