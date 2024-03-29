package pl.clarin.chronocorpus.wordprofile.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Token;
import pl.clarin.chronocorpus.wordprofile.entity.WordProfile;
import pl.clarin.chronocorpus.wordprofile.entity.WordProfileResult;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WordProfileQueryService {

    public static volatile WordProfileQueryService instance;

    private WordProfileQueryService() {
    }

    public static WordProfileQueryService getInstance() {
        if (instance == null) {
            synchronized (WordProfileQueryService.class) {
                if (instance == null) {
                    instance = new WordProfileQueryService();
                }
            }
        }
        return instance;
    }

    public JsonArray findWordProfile(String keyWord,
                                     Integer leftWindowSize,
                                     Integer rightWindowSize,
                                     Integer partOfSpeech,
                                     Integer windowItemPartOfSpeech,
                                     Set<String> stopList,
                                     Set<Property> metadata, boolean byBase) {

        List<WordProfileResult> r = getWordProfileResults(keyWord, leftWindowSize, rightWindowSize, partOfSpeech, windowItemPartOfSpeech, stopList, metadata, byBase);

        JsonArrayBuilder frequency = Json.createArrayBuilder();
        r.stream().map(WordProfileResult::toJson).forEach(frequency::add);

        return frequency.build();
    }

    public List<WordProfileResult> getWordProfileResults(String keyWord, Integer leftWindowSize, Integer rightWindowSize, Integer partOfSpeech, Integer windowItemPartOfSpeech, Set<String> stopList, Set<Property> metadata, boolean byBase) {
        Map<WordProfile, Long> result = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments()) {

            if (d.getMetadata().matches(metadata) && (byBase ? d.isBaseIn(keyWord) : d.isOrthIn(keyWord))) {

                List<Sentence> matching = d.getSentences()
                        .stream()
                        .filter(s -> s.getTokens()
                                .stream()
                                .anyMatch(byBase ? getBasePredicate(keyWord) : getOrthPredicate(keyWord)))
                        .collect(Collectors.toList());

                matching.forEach(s -> {
                    getWordProfile(s.getTokens(), keyWord, byBase, partOfSpeech, windowItemPartOfSpeech, leftWindowSize, rightWindowSize)
                            .forEach((k, v) -> {
                                if (!result.containsKey(k)) {
                                    result.put(k, v);
                                } else {
                                    Long val = result.get(k) + v;
                                    result.replace(k, val);
                                }
                            });
                });
            }
        }

        reMapValues(result);

        List<WordProfile> items = result.keySet().stream()
                .sorted(Comparator.comparing(WordProfile::getFrequency).reversed())
                .collect(Collectors.toList());

        Map<String, List<WordProfile>> colocations = new HashMap<>();
        for (WordProfile item : items) {
            String coloc =  item.getLeftWordBase() != null ? item.getLeftWordBase() : item.getRightWordBase();
            if(!colocations.containsKey(coloc)){
                colocations.put(coloc, new ArrayList<>());
            }
            colocations.get(coloc).add(item);
        }

        List<WordProfileResult> r = colocations.entrySet().stream()
                .filter(e -> !stopList.contains(e.getKey()))
                .map( e -> new WordProfileResult(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(WordProfileResult::getFrequency).reversed())
                .collect(Collectors.toList());

        long sum = r.stream().mapToLong(WordProfileResult::getFrequency).sum();
        r.forEach(i -> i.setPercentage((i.getFrequency()*100f)/sum));
        return r;
    }

    public Map<WordProfile, Long> getWordProfile(List<Token> tokens, String word, boolean byBase, Integer pos,
                                                 Integer windowItemPos, Integer leftWindowSize, Integer rightWindowSize) {
        List<WordProfile> tmp = new ArrayList<>();
        IntStream.range(0, tokens.size()).forEach(i -> {
            if (byBase) {
                if (tokens.get(i).getBase() != null && tokens.get(i).getBase().equals(word) && tokens.get(i).getPos() == pos) {
                    tmp.addAll(findLeftWindow(tokens, tokens.get(i).getBase(), i, windowItemPos, leftWindowSize));
                    tmp.addAll(findRightWindow(tokens, tokens.get(i).getBase(), i,windowItemPos, rightWindowSize));
                }
            } else {
                if (tokens.get(i).getOrth().equals(word) && tokens.get(i).getPos() == pos) {
                    tmp.addAll(findLeftWindow(tokens, tokens.get(i).getOrth(), i, windowItemPos, leftWindowSize));
                    tmp.addAll(findRightWindow(tokens, tokens.get(i).getOrth(), i, windowItemPos, rightWindowSize));
                }
            }
        });
        return tmp.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private List<WordProfile> findRightWindow(List<Token> tokens, String word, int i, Integer pos, Integer rightWindowSize) {
        List<WordProfile> result = new ArrayList<>();
        if (rightWindowSize != null && rightWindowSize > 0) {
            int steps = (i + rightWindowSize) > tokens.size() - 1 ? tokens.size() - 1 : (i + rightWindowSize);
            for (int z = i + 1; z <= steps; z++) {
                if (tokens.get(z).isInterp() &&
                        (tokens.get(z).getOrth().equals(",") || (tokens.get(z).getOrth().equals(".")))) {
                    break;
                }
                if (pos == 0) {
                    if (tokens.get(z).getPos() == 1 || tokens.get(z).getPos() == 2 || tokens.get(z).getPos() == 3 || tokens.get(z).getPos() == 4) {
                        result.add(new WordProfile(null, null, word, tokens.get(z).getOrth(), tokens.get(z).getBase()));
                    }
                } else if (tokens.get(z).getPos() == pos) {
                    result.add(new WordProfile(null, null, word, tokens.get(z).getOrth(), tokens.get(z).getBase()));
                }
            }
        }
        return result;
    }

    private List<WordProfile> findLeftWindow(List<Token> tokens, String word, int i, Integer pos, Integer leftWindowSize) {
        List<WordProfile> result = new ArrayList<>();
        if (leftWindowSize != null && leftWindowSize > 0) {
            int steps = (i - leftWindowSize) < 0 ? 0 : (i - leftWindowSize);
            for (int z = steps; z < i; z++) {
                if (tokens.get(z).isInterp() &&
                        (tokens.get(z).getOrth().equals(",") || (tokens.get(z).getOrth().equals(".")))) {
                    break;
                }
                if (pos == 0) {
                    if (tokens.get(z).getPos() == 1 || tokens.get(z).getPos() == 2 || tokens.get(z).getPos() == 3 || tokens.get(z).getPos() == 4) {
                        result.add(new WordProfile(tokens.get(z).getOrth(), tokens.get(z).getBase(), word, null, null));
                    }
                } else  if (tokens.get(z).getPos() == pos) {
                    result.add(new WordProfile(tokens.get(z).getOrth(), tokens.get(z).getBase(), word, null, null));
                }
            }
        }
        return result;
    }

    private void reMapValues(Map<WordProfile, Long> result) {
        result.forEach(WordProfile::setFrequency);
    }

    public Predicate<Token> getOrthPredicate(String keyWord) {
        return word -> word.getOrth().equals(keyWord);
    }

    public Predicate<Token> getBasePredicate(String keyWord) {
        return word -> keyWord.equals(word.getBase());
    }
}
