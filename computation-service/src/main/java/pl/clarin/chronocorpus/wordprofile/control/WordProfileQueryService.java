package pl.clarin.chronocorpus.wordprofile.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Word;
import pl.clarin.chronocorpus.frequency.entity.FrequencyItem;
import pl.clarin.chronocorpus.wordprofile.entity.WordProfile;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
                                     Set<Property> metadata, boolean byBase) {

        Map<WordProfile,Long> result = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments()) {

            if (d.getMetadata().matches(metadata) && (byBase ? d.isBaseIn(keyWord) : d.isOrthIn(keyWord))) {

                List<Sentence> matching = d.getSentences()
                        .stream()
                        .filter(s -> s.getWords()
                                .stream()
                                .anyMatch(byBase ? getBasePredicate(keyWord) : getOrthPredicate(keyWord)))
                        .collect(Collectors.toList());

                matching.forEach(s -> {
                     s.getWordProfile(keyWord, byBase, partOfSpeech, leftWindowSize, rightWindowSize)
                             .forEach((k,v)->{
                                 if(!result.containsKey(k)){
                                     result.put(k, v);
                                 }else{
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

        JsonArrayBuilder frequency = Json.createArrayBuilder();
        items.stream().map(WordProfile::toJson).forEach(frequency::add);

        return frequency.build();
    }
    private void reMapValues(Map<WordProfile, Long> result){
        result.forEach(WordProfile::setFrequency);
    }

    public Predicate<Word> getOrthPredicate(String keyWord) {
        return word -> word.getOrth().equals(keyWord);
    }

    public Predicate<Word> getBasePredicate(String keyWord) {
        return word -> word.getBase().equals(keyWord);
    }
}
