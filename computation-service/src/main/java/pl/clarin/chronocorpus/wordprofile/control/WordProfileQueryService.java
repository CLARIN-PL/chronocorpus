package pl.clarin.chronocorpus.wordprofile.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Word;

import javax.json.JsonArray;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
                                     Optional<Integer> leftWindowSize,
                                     Optional<Integer> rightWindowSize,
                                     Optional<Integer> partOfSpeech,
                                     Set<Property> metadata, boolean byBase) {

        for (Document d : DocumentStore.getInstance().getDocuments()) {

            if (d.getMetadata().matches(metadata) && (byBase ? d.isBaseIn(keyWord) : d.isOrthIn(keyWord))) {

                List<Sentence> matching = d.getSentences()
                        .stream()
                        .filter(s -> s.getWords()
                                .stream()
                                .anyMatch(byBase ? getBasePredicate(keyWord) : getOrthPredicate(keyWord)))
                        .collect(Collectors.toList());


            }
        }

        return null;
    }

    public Predicate<Word> getOrthPredicate(String keyWord) {
        return word -> word.getOrth().equals(keyWord);
    }

    public Predicate<Word> getBasePredicate(String keyWord) {
        return word -> word.getBase().equals(keyWord);
    }
}
