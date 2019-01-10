package pl.clarin.chronocorpus.concordance.entity;

import java.util.UUID;

public class Concordance {

    private UUID documentId;
    private String left;
    private String word;
    private String right;

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

}