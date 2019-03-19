package pl.clarin.chronocorpus.document.entity;

import pl.clarin.chronocorpus.wordprofile.entity.WordProfile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public Map<WordProfile, Long> getWordProfile(String word, boolean byBase, Integer pos, Integer leftWindowSize, Integer rightWindowSize) {
        List<WordProfile> tmp = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            if (byBase) {
                if (words.get(i).getBase().equals(word)) {
                    tmp.addAll(findLeftWindow(words.get(i).getBase(),i,pos,leftWindowSize));
                    tmp.addAll(findRightWindow(words.get(i).getBase(),i,pos,rightWindowSize));
                }
            } else {
                if (words.get(i).getOrth().equals(word)) {
                    tmp.addAll(findLeftWindow(words.get(i).getOrth(),i,pos,leftWindowSize));
                    tmp.addAll(findRightWindow(words.get(i).getOrth(),i,pos,rightWindowSize));
                }
            }
        }
        return tmp.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private List<WordProfile> findRightWindow(String word, int i, Integer pos, Integer rightWindowSize) {
        List<WordProfile> result = new ArrayList<>();
        if (rightWindowSize != null && rightWindowSize > 0) {
            int steps = (i + rightWindowSize) > words.size()-1 ? words.size()-1 : (i + rightWindowSize);
            for (int z = i + 1; z <= steps; z++) {
                if (words.get(z).getPos() == pos) {
                    result.add(new WordProfile(null, word, words.get(z).getOrth()));
                }
            }
        }
        return result;
    }

    private List<WordProfile> findLeftWindow(String word, int i, Integer pos, Integer leftWindowSize) {
        List<WordProfile> result = new ArrayList<>();
        if (leftWindowSize != null && leftWindowSize > 0) {
            int steps = (i - leftWindowSize) < 0 ? 0 : (i - leftWindowSize);
            for (int z = steps; z < i; z++) {
                if (words.get(z).getPos() == pos) {
                    result.add(new WordProfile(words.get(z).getOrth(), word, null ));
                }
            }
        }
        return result;
    }
}
