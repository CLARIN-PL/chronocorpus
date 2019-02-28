package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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

    public Map<String, Long> sentenceBaseFrequency(Set<String> stopList){
        return words.stream()
                .filter(w -> !"interp".equals(w.getCtag()))
                .filter(w -> !stopList.contains(w))
                .map(w -> w.getBase()+"__"+w.getPos())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public Map<String, Long> sentenceOrthFrequency(Set<String> stopList){
        return words.stream()
                .filter(w -> !"interp".equals(w.getCtag()))
                .filter(w -> !stopList.contains(w))
                .map(w -> w.getOrth()+"__"+w.getPos())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
