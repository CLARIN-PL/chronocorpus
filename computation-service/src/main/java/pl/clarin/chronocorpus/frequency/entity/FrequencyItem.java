package pl.clarin.chronocorpus.frequency.entity;

import javax.json.Json;
import javax.json.JsonObject;

public class FrequencyItem {

    private final String word;
    private final Byte pos;
    private int frequency;

    public FrequencyItem(String word, Byte pos, int frequency) {
        this.word = word;
        this.pos = pos;
        this.frequency = frequency;
    }

    public Byte getPos() {
        return pos;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getWord() {
        return word;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("word", word)
                .add("part_of_speech", pos)
                .add("count", frequency).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrequencyItem)) return false;

        FrequencyItem that = (FrequencyItem) o;

        if (!word.equals(that.word)) return false;
        return pos.equals(that.pos);
    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + pos.hashCode();
        return result;
    }
}
