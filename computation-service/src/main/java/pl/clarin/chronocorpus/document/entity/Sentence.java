package pl.clarin.chronocorpus.document.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sentence implements Serializable {

    private List<Token> tokens = new ObjectArrayList<>();

    public Sentence() {
    }

    public void addWord(Token token) {
        tokens.add(token);
    }

    public List<Token> getTokens() {
        return tokens;
    }


    public String getSentence() {
        return tokens.stream()
                .map(Token::getOrthWithDelimiter)
                .collect(Collectors.joining());
    }

    public Long getSentenceWordCount() {
        return tokens.stream()
                .filter(w -> !w.isInterp())
                .count();
    }

    public Long getSentenceLetterCount() {
        return tokens.stream()
                .filter(w -> !w.isInterp())
                .mapToLong(w -> w.getOrth().length())
                .sum();
    }

    public Long getSentenceSyllableCount() {
        return tokens.stream()
                .filter(w -> !w.isInterp())
                .mapToLong(Token::getSyllableCount)
                .sum();
    }

    public Long getSentencePhonemeCount() {
        return tokens.stream()
                .filter(w -> !w.isInterp())
                .mapToLong(Token::getPhonemeCount)
                .sum();
    }
}
