package pl.clarin.chronocorpus.wordprofile.entity;

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
}
