package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sentence implements Serializable {

    private List<Word> words = new ArrayList<Word>();

    public Sentence() {
    }

    public void addWord(Word word) {
        words.add(word);
    }

    public List<Word> getWords() {
        return words;
    }

    public String getSentence() {
        return words.stream()
                .map(Word::getOrthWithDelimiter)
                .collect(Collectors.joining());
    }
}
