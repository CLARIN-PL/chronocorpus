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

    public void test(String word, int pos, int windowSize){
       Map<String, Integer> res = new HashMap<>();

        IntStream.range(0 , words.size())
                .forEach(i -> {

                });

       for(int i = 0; i < words.size(); i++ ){
           if(words.get(i).getBase().equals(word)){
                if(i - windowSize >=0){
                    String prof =
                }
           }
       }
    }
}
