package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public Long getSentenceWordCount(){
        return words.stream()
                .filter(w -> !"interp".equals(w.getCtag()))
                .count();
    }

    public Long getSentenceLetterCount(){
        return words.stream()
                .filter(w -> !"interp".equals(w.getCtag()))
                .mapToLong(w -> w.getOrth().length())
                .sum();
    }

    public Long getSentenceSyllabeCount(){
        return words.stream()
                .filter(w -> !"interp".equals(w.getCtag()))
                .mapToLong(Word::getSyllableCount)
                .sum();
    }

    public Long getSentencePhonemeCount(){
        return words.stream()
                .filter(w -> !"interp".equals(w.getCtag()))
                .mapToLong(Word::getPhonemeCount)
                .sum();
    }
}
