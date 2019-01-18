package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sentence implements Serializable {

    private List<Word> words = new ArrayList<Word>();

    public Sentence() {
    }

    public void addWord(Word word){
        words.add(word);
    }

    public List<Word> getWords() {
        return words;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "words=" + words +
                '}';
    }
}
