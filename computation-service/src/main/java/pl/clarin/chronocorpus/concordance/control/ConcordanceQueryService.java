package pl.clarin.chronocorpus.concordance.control;

import pl.clarin.chronocorpus.concordance.entity.Concordance;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Sentence;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.*;
import java.util.stream.Collectors;

public class ConcordanceQueryService {

    public static volatile ConcordanceQueryService instance;

    private ConcordanceQueryService(){
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

    public JsonObject findConcordance(String lemma){

        Map<UUID, List<Sentence>> sentences = new HashMap<>();

        for(Document d: DocumentStore.getInstance().getStore()){

            List<Sentence> matching = d.getSentences()
                    .stream()
                    .filter(s -> s.getWords()
                            .stream()
                            .anyMatch(word -> word.getBase().equals(lemma)))
                    .collect(Collectors.toList());
            sentences.put(d.getId(), matching);
        }

        JsonArrayBuilder concordances = Json.createArrayBuilder();
        sentences.forEach((k, v) -> {
            v.stream()
                    .map(s -> new Concordance(k, lemma).mapSentenceToConcordance(s))
                    .forEach(cc -> concordances.add(cc.getAsJson()));
        });

        return Json.createObjectBuilder()
                .add("rows", concordances)
                .build();
    }
}
