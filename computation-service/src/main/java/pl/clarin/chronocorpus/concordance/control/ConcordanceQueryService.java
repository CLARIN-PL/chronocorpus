package pl.clarin.chronocorpus.concordance.control;

import org.javatuples.Pair;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.*;

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

    //TODO needs solution for multi word expressions like Armia Czerwona
    public JsonArray findConcordance(String keyWord, Set<String> responseParams, Set<Property> metadata, boolean byBase) {

        Map<Pair<Integer, Set<Property>>, List<Sentence>> sentences = new HashMap<>();

        DocumentStore.getInstance().getDocuments().parallelStream()
                .filter(getPredicates(metadata, keyWord, byBase).stream().reduce(p -> true, Predicate::and))
                .forEach(d -> {

                    List<Sentence> matching = d.getSentences()
                            .stream()
                            .filter(s -> s.getTokens()
                                    .stream()
                                    .anyMatch(getPredicates(keyWord, byBase).stream().reduce( p-> true, Predicate::and)))
                            .collect(Collectors.toList());

                    Set<Property> responseProperty = d.getMetadata()
                            .getProperties()
                            .stream()
                            .filter(p -> responseParams.contains(p.getName()))
                            .collect(Collectors.toSet());

                    sentences.put(new Pair<>(d.getId(), responseProperty), matching);
                });

        return buildJsonResponse(keyWord, byBase, sentences);
    }

    private JsonArray buildJsonResponse(String keyWord, boolean byBase, Map<Pair<Integer, Set<Property>>, List<Sentence>> sentences) {
        JsonArrayBuilder concordances = Json.createArrayBuilder();
        sentences.forEach((doc, v) ->
                v.stream()
                        .map(s -> ConcordanceMapper
                                .getInstance()
                                .mapSentenceToConcordances(doc, keyWord, s, byBase))
                        .forEach(cc -> concordances.add(cc.toJson())));
        return concordances.build();
    }

    private List<Predicate<Document>> getPredicates(Set<Property> metadata, String word, boolean byBase) {
        List<Predicate<Document>> p = new ArrayList<>();
        if (metadata.size() > 0){
            p.add(document -> document.getMetadata().matches(metadata));
        }
        Arrays.asList(word.split(" "))
                .forEach(w -> {
                    if(byBase){
                        p.add(d -> d.isBaseIn(w));
                    }else{
                        p.add(d -> d.isOrthIn(w));
                    }
                });
        return p;
    }

    private List<Predicate<Token>> getPredicates(String word, boolean byBase){
        List<Predicate<Token>> p = new ArrayList<>();
        Arrays.asList(word.split(" "))
                .forEach(w -> {
                    if(byBase){
                        p.add(getBasePredicate(w));
                    }else{
                        p.add(getOrthPredicate(w));
                    }
                });
        return p;
    }

    private Predicate<Token> getOrthPredicate(String keyWord) {
        return word -> word.getOrth().equals(keyWord);
    }

    private Predicate<Token> getBasePredicate(String keyWord) {
        return word -> word.getBase() != null && word.getBase().equals(keyWord);
    }
}
