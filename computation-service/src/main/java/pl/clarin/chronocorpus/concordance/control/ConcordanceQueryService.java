package pl.clarin.chronocorpus.concordance.control;

import org.javatuples.Pair;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Word;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConcordanceQueryService {

    public static volatile ConcordanceQueryService instance;

    private ConcordanceQueryService() {
    }

    public static ConcordanceQueryService getInstance() {
        if (instance == null) {
            synchronized (DocumentStore.class) {
                if (instance == null) {
                    instance = new ConcordanceQueryService();
                }
            }
        }
        return instance;
    }

    public JsonArray findConcordance(String keyWord, Set<String> responseParams, Set<Property> metadata, boolean byBase) {

        Map<Pair<String, Set<Property>>, List<Sentence>> sentences = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments())

            if (d.getMetadata().matches(metadata) && (byBase ? d.isBaseIn(keyWord) : d.isOrthIn(keyWord))) {

                List<Sentence> matching = d.getSentences()
                        .stream()
                        .filter(s -> s.getWords()
                                .stream()
                                .anyMatch(byBase ? getBasePredicate(keyWord) : getOrthPredicate(keyWord)))
                        .collect(Collectors.toList());

                Set<Property> responseProperty = d.getMetadata()
                        .getProperties()
                        .stream()
                        .filter(p -> responseParams.contains(p.getName()))
                        .collect(Collectors.toSet());

                sentences.put(new Pair<>(d.getId(), responseProperty), matching);
            }

        JsonArrayBuilder concordances = Json.createArrayBuilder();
        sentences.forEach((doc, v) ->
                v.stream()
                        .map(s -> ConcordanceMapper
                                .getInstance()
                                .mapSentenceToConcordances(doc, keyWord, s, byBase))
                        .forEach(cc -> concordances.add(cc.toJson())));

        return concordances.build();
    }

    public Predicate<Word> getOrthPredicate(String keyWord) {
        return word -> word.getOrth().equals(keyWord);
    }

    public Predicate<Word> getBasePredicate(String keyWord) {
        return word -> word.getBase().equals(keyWord);
    }
}
