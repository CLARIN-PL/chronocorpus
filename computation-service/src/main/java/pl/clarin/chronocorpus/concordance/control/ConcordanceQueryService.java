package pl.clarin.chronocorpus.concordance.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Metadata;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.*;
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

    public JsonArray findConcordance(String keyWord, List<Property> metadata, List<Property> publication, Boolean byBase) {

        Map<String, List<Sentence>> sentences = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments())

            if (d.getMetadata().matches(metadata, publication)) {

                if (byBase) {
                    List<Sentence> matching = d.getSentences()
                            .stream()
                            .filter(s -> s.getWords()
                                    .stream()
                                    .anyMatch(word -> word.getBase().equals(keyWord)))
                            .collect(Collectors.toList());

                    sentences.put(d.getId(), matching);

                } else {
                    List<Sentence> matching = d.getSentences()
                            .stream()
                            .filter(s -> s.getWords()
                                    .stream()
                                    .anyMatch(word -> word.getOrth().equals(keyWord)))
                            .collect(Collectors.toList());
                    sentences.put(d.getId(), matching);

                }



            }

        JsonArrayBuilder concordances = Json.createArrayBuilder();
        sentences.forEach((k, v) -> {
            v.stream()
                    .map(s -> ConcordanceMapper
                            .getInstance()
                            .mapSentenceToConcordanceList(k, keyWord, s, byBase))
                    .flatMap(Collection::stream)
                    .forEach(cc -> concordances.add(cc.getAsJson()));
        });

        return concordances.build();
    }
}
