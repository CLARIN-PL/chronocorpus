package pl.clarin.chronocorpus.concordance.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Sentence;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public JsonObject findConcordanceByOrth(String orth) {

        Map<String, List<Sentence>> sentences = new HashMap<>();


        for (Document d : DocumentStore.getInstance().getDocuments()) {

            List<Sentence> matching = d.getSentences()
                    .stream()
                    .filter(s -> s.getWords()
                            .stream()
                            .anyMatch(word -> word.getOrth().equals(orth)))
                    .collect(Collectors.toList());
            sentences.put(d.getId(), matching);
        }

        JsonArrayBuilder concordances = Json.createArrayBuilder();
        sentences.forEach((k, v) -> {
            v.stream()
                    .map(s -> ConcordanceMapper
                            .getInstance()
                            .mapSentenceToConcordanceListByOrth(k, orth, s))
                    .flatMap(Collection::stream)
                    .forEach(cc -> concordances.add(cc.getAsJson()));
        });

        return Json.createObjectBuilder()
                .add("rows", concordances)
                .build();
    }

    public JsonArray findConcordanceByBase(String base) {

        Map<String, List<Sentence>> sentences = new HashMap<>();


        for (Document d : DocumentStore.getInstance().getDocuments()) {

            List<Sentence> matching = d.getSentences()
                    .stream()
                    .filter(s -> s.getWords()
                            .stream()
                            .anyMatch(word -> word.getBase().equals(base)))
                    .collect(Collectors.toList());
            sentences.put(d.getId(), matching);
        }

        JsonArrayBuilder concordances = Json.createArrayBuilder();
        sentences.forEach((k, v) -> {
            v.stream()
                    .map(s -> ConcordanceMapper
                            .getInstance()
                            .mapSentenceToConcordanceListByBase(k, base, s))
                    .flatMap(Collection::stream)
                    .forEach(cc -> concordances.add(cc.getAsJson()));
        });

        return concordances.build();
    }
}
