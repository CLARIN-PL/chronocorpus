package pl.clarin.chronocorpus.document.entity;

import pl.clarin.chronocorpus.wordprofile.entity.WordProfile;

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

    public WordProfile getWordProfile(String word, boolean byBase, Integer pos, Integer leftWindowSize, Integer rightWindowSize){
        for(int i = 0; i< words.size(); i++){
            if(byBase){
                if(words.get(i).getBase().equals(word)){

                }
            }else{
                if(words.get(i).getOrth().equals(word)){

                }
            }
        }
        return null
                ;
    }
}
