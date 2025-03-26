package pl.clarin.chronocorpus.similarity.control;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.similarity.entity.SimilarityResult;
import pl.clarin.chronocorpus.wordprofile.control.WordProfileQueryService;
import pl.clarin.chronocorpus.wordprofile.entity.WordProfileResult;

import javax.json.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimilarityQueryService {

    public static volatile SimilarityQueryService instance;
    private static final Integer FREQUENCY_THRESHOLD = 1;

    private SimilarityQueryService() {
    }

    public static SimilarityQueryService getInstance() {
        if (instance == null) {
            synchronized (SimilarityQueryService.class) {
                if (instance == null) {
                    instance = new SimilarityQueryService();
                }
            }
        }
        return instance;
    }

    // dodać opcje log-likehood ratio
    public JsonObject findSimilarity(String firstWord, String secondWord,
                                     Integer leftWindowSize,
                                     Integer rightWindowSize,
                                     Integer firstPartOfSpeech,
                                     Integer secondPartOfSpeech,
                                     Integer windowItemPartOfSpeech,
                                     Set<String> stopList,
                                     Set<Property> metadata, boolean byBase) {

        List<WordProfileResult> first = WordProfileQueryService.getInstance()
                .getWordProfileResults(firstWord, leftWindowSize, rightWindowSize, firstPartOfSpeech, windowItemPartOfSpeech, stopList, metadata, byBase);
        List<WordProfileResult> second = WordProfileQueryService.getInstance()
                .getWordProfileResults(secondWord, leftWindowSize, rightWindowSize, secondPartOfSpeech, windowItemPartOfSpeech, stopList, metadata, byBase);

        Map<String, SimilarityResult> preGraph = new HashMap<>();

        first.stream()
                .filter(w -> w.getFrequency() > FREQUENCY_THRESHOLD)
                .forEach(w -> {
                    if (!preGraph.containsKey(w.getCollocate())) {
                        preGraph.put(w.getCollocate(), new SimilarityResult());
                    }
                    preGraph.get(w.getCollocate()).setW1(w.getFrequency());

                });

        second.stream()
                .filter(w -> w.getFrequency() > FREQUENCY_THRESHOLD)
                .forEach(w -> {
                    if (!preGraph.containsKey(w.getCollocate())) {
                        preGraph.put(w.getCollocate(), new SimilarityResult());
                    }
                    preGraph.get(w.getCollocate()).setW2(w.getFrequency());
                });

        JsonArrayBuilder nodes = Json.createArrayBuilder();
        JsonArrayBuilder edges = Json.createArrayBuilder();

        AtomicInteger idGen = new AtomicInteger(3);

        nodes.add(createNode(1, firstWord, "#7879ff"));
        nodes.add(createNode(2, secondWord, "#ff8178"));

        preGraph.forEach((k, v) -> {
            Integer id = idGen.getAndIncrement();
            String color = "#000000";

            if (v.getW1() > 0) {
                edges.add(createEdge(id, 1, v.getW1()));
                color = "#7879ff";
            }
            if (v.getW2() > 0) {
                edges.add(createEdge(id, 2, v.getW2()));
                color = "#ff8178";
            }
            if (v.getW1() > 0 && v.getW2() > 0) {
                color = "#78ffcb";
            }
            nodes.add(createNode(id, k, color));
        });

        JsonObjectBuilder network = Json.createObjectBuilder();
        network.add("nodes", nodes);
        network.add("edges", edges);

        return network.build();
    }

    private JsonObject createNode(Integer id, String label, String color) {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        jb.add("id", id);
        jb.add("label", label);
        jb.add("color", createColor(color));
        return jb.build();
    }

    private JsonObject createEdge(Integer from, Integer to, Long width) {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        jb.add("from", from);
        jb.add("to", to);
        jb.add("width", width);
        return jb.build();
    }

    private JsonObject createColor(String color) {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        jb.add("background", color);
        jb.add("border", color);
        return jb.build();
    }
}
