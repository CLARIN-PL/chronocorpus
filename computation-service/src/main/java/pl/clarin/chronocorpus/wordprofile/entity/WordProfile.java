package pl.clarin.chronocorpus.wordprofile.entity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class WordProfile {

    private String leftWord;

    private String word;

    private String rightWord;

    private Long frequency;

    public WordProfile(String leftWord, String word, String rightWord) {
        this.leftWord = leftWord;
        this.word = word;
        this.rightWord = rightWord;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    public JsonObject toJson(){
        JsonObjectBuilder b = Json.createObjectBuilder();
        String profile = (leftWord != null ? leftWord +" ":"") + word + (rightWord != null ? " "+rightWord :"");
        b.add("profile", profile);
        b.add("frequency", frequency);
        return b.build();
    }

    public String getLeftWord() {
        return leftWord;
    }

    public String getWord() {
        return word;
    }

    public String getRightWord() {
        return rightWord;
    }

    public Long getFrequency() {
        return frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordProfile)) return false;

        WordProfile that = (WordProfile) o;

        if (leftWord != null ? !leftWord.equals(that.leftWord) : that.leftWord != null) return false;
        if (!word.equals(that.word)) return false;
        return rightWord != null ? rightWord.equals(that.rightWord) : that.rightWord == null;
    }

    @Override
    public int hashCode() {
        int result = leftWord != null ? leftWord.hashCode() : 0;
        result = 31 * result + word.hashCode();
        result = 31 * result + (rightWord != null ? rightWord.hashCode() : 0);
        return result;
    }
}
