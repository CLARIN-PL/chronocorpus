package pl.clarin.chronocorpus.wordprofile.entity;

public class WordProfile {

    private String leftWordOrth;

    private String leftWordBase;

    private String word;

    private String rightWordOrth;

    private String rightWordBase;

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

        if (leftWordOrth != null ? !leftWordOrth.equals(that.leftWordOrth) : that.leftWordOrth != null) return false;
        if (leftWordBase != null ? !leftWordBase.equals(that.leftWordBase) : that.leftWordBase != null) return false;
        if (!word.equals(that.word)) return false;
        if (rightWordOrth != null ? !rightWordOrth.equals(that.rightWordOrth) : that.rightWordOrth != null)
            return false;
        if (rightWordBase != null ? !rightWordBase.equals(that.rightWordBase) : that.rightWordBase != null)
            return false;
        return frequency != null ? frequency.equals(that.frequency) : that.frequency == null;
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
