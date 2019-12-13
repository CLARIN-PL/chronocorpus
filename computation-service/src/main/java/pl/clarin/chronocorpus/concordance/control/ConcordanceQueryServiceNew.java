package pl.clarin.chronocorpus.concordance.control;

import org.javatuples.Pair;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Token;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConcordanceQueryServiceNew {

    public static volatile ConcordanceQueryServiceNew instance;

    private ConcordanceQueryServiceNew() {
    }

    public static ConcordanceQueryServiceNew getInstance() {
        if (instance == null) {
            synchronized (DocumentStore.class) {
                if (instance == null) {
                    instance = new ConcordanceQueryServiceNew();
                }
            }
        }
        return instance;
    }

    public JsonArray findConcordance(String keyWord, Set<String> responseParams, Set<Property> metadata, boolean byBase) {

        Map<Pair<Integer, Set<Property>>, List<Sentence>> sentences = new HashMap<>();
        Pattern splitPattern = Pattern.compile("\\b"+keyWord+"\\b");
        Pattern searchPattern = Pattern.compile(".*\\b"+keyWord+"\\b.*");

        DocumentStore.getInstance().getDocuments().parallelStream()
                .filter(d -> d.getMetadata().matches(metadata))
                .forEach(d -> {

                    List<Sentence> matching = d.getSentences()
                            .parallelStream()
                            .filter(getPredicates(searchPattern, byBase).stream().reduce(p -> true, Predicate::and))
                            .collect(Collectors.toList());

                    Set<Property> responseProperty = d.getMetadata()
                            .getProperties()
                            .stream()
                            .filter(p -> responseParams.contains(p.getName()))
                            .collect(Collectors.toSet());
                    if(!matching.isEmpty())
                        sentences.put(new Pair<>(d.getId(), responseProperty), matching);
                });

        return buildJsonResponse(splitPattern, byBase, sentences);
    }


    private JsonArray buildJsonResponse(Pattern p, boolean byBase, Map<Pair<Integer, Set<Property>>, List<Sentence>> sentences) {
        JsonArrayBuilder concordances = Json.createArrayBuilder();
        sentences.forEach((doc, v) ->
                v.stream()
                        .map(s -> ConcordanceMapperNew
                                .getInstance()
                                .mapSentenceToConcordances(doc, p, s, byBase))
                        .forEach(cc -> concordances.add(cc.toJson())));
        return concordances.build();
    }

    private List<Predicate<Sentence>> getPredicates(Pattern pattern, boolean byBase) {
        List<Predicate<Sentence>> p = new ArrayList<>();
        if (byBase) {
            p.add(s ->  pattern.matcher(s.getSentenceInBaseForm()).matches());
        } else {
            p.add(s ->  pattern.matcher(s.getSentence()).matches());
        }
        return p;
    }
}
