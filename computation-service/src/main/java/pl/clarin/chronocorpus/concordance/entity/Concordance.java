package pl.clarin.chronocorpus.concordance.entity;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Objects;

public class Concordance {

    private final String left;
    private final String word;
    private final String right;

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

    public int getLeftTokenCount(){
        if(left.equals("")) return 0;
        return left.split(" ").length;
    }

    public int getRightTokenCount(){
        if(right.equals("")) return 0;
        return right.split(" ").length;
    }

    public int getWordTokenCount(){
        return word.split(" ").length;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("left", left)
                .add("word", word)
                .add("right", right).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concordance)) return false;

        Concordance that = (Concordance) o;

        if (!Objects.equals(left, that.left)) return false;
        if (!Objects.equals(word, that.word)) return false;
        return Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
}