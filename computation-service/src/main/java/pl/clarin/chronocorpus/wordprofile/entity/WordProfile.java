package pl.clarin.chronocorpus.wordprofile.entity;

import java.util.Objects;

public class WordProfile {

    private final String leftWordOrth;

    private final String leftWordBase;

    private final String word;

    private final String rightWordOrth;

    private final String rightWordBase;

    private Long frequency;

    public WordProfile(String leftWordOrth, String leftWordBase, String word,
                       String rightWordOrth, String rightWordBase) {
        this.leftWordOrth = leftWordOrth;
        this.leftWordBase = leftWordBase;
        this.word = word;
        this.rightWordOrth = rightWordOrth;
        this.rightWordBase = rightWordBase;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    public String getLeftWordOrth() {
        return leftWordOrth;
    }

    public String getLeftWordBase() {
        return leftWordBase;
    }

    public String getRightWordOrth() {
        return rightWordOrth;
    }

    public String getRightWordBase() {
        return rightWordBase;
    }

    public String getWord() {
        return word;
    }

    public Long getFrequency() {
        return frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordProfile)) return false;

        WordProfile that = (WordProfile) o;

        if (!Objects.equals(leftWordOrth, that.leftWordOrth)) return false;
        if (!Objects.equals(leftWordBase, that.leftWordBase)) return false;
        if (!word.equals(that.word)) return false;
        if (!Objects.equals(rightWordOrth, that.rightWordOrth))
            return false;
        if (!Objects.equals(rightWordBase, that.rightWordBase))
            return false;
        return Objects.equals(frequency, that.frequency);
    }

    @Override
    public int hashCode() {
        int result = leftWordOrth != null ? leftWordOrth.hashCode() : 0;
        result = 31 * result + (leftWordBase != null ? leftWordBase.hashCode() : 0);
        result = 31 * result + word.hashCode();
        result = 31 * result + (rightWordOrth != null ? rightWordOrth.hashCode() : 0);
        result = 31 * result + (rightWordBase != null ? rightWordBase.hashCode() : 0);
        result = 31 * result + (frequency != null ? frequency.hashCode() : 0);
        return result;
    }
}
