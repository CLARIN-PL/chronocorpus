package pl.clarin.chronocorpus.concordance.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Word;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.*;
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

    public JsonArray findConcordance(String keyWord, Set<Property> metadata, boolean byBase) {

        Map<String, List<Sentence>> sentences = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments())

            if (d.getMetadata().matches(metadata)) {

                List<Sentence> matching = d.getSentences()
                        .stream()
                        .filter(s -> s.getWords()
                                .stream()
                                .anyMatch(byBase ? getBasePredicate(keyWord):getOrthPredicate(keyWord)))
                        .collect(Collectors.toList());

                sentences.put(d.getId(), matching);

            }

        JsonArrayBuilder concordances = Json.createArrayBuilder();
        sentences.forEach((docId, v) -> {
            v.stream()
                    .map(s -> ConcordanceMapper
                            .getInstance()
                            .mapSentenceToConcordanceList(docId, keyWord, s, byBase))
                    .flatMap(Collection::stream)
                    .forEach(cc -> concordances.add(cc.toJson()));
        });

        return concordances.build();
    }

    public Predicate<Word> getOrthPredicate(String keyWord) {
        return word -> word.getOrth().equals(keyWord);
    }

    public Predicate<Word> getBasePredicate(String keyWord) {
        return word -> word.getBase().equals(keyWord);
    }
}
