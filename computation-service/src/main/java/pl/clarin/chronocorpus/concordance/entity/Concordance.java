package pl.clarin.chronocorpus.concordance.entity;

import javax.json.Json;
import javax.json.JsonObject;

public class Concordance {

    private String left;
    private String word;
    private String right;

    public Concordance(String left, String word, String right) {
        this.left = left;
        this.word = word;
        this.right = right;
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

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("left", left)
                .add("word", word)
                .add("right", right).build();
    }
}