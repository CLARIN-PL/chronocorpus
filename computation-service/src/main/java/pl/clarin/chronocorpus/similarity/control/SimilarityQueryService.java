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

    public JsonObject findSimilarity(String firstWord, String secondWord,
                                     Integer leftWindowSize,
                                     Integer rightWindowSize,
                                     Integer partOfSpeech,
                                     Integer windowItemPartOfSpeech,
                                     Set<String> stopList,
                                     Set<Property> metadata, boolean byBase) {

        List<WordProfileResult> first = WordProfileQueryService.getInstance().getWordProfileResults(firstWord, leftWindowSize, rightWindowSize, partOfSpeech, windowItemPartOfSpeech, stopList, metadata, byBase);
        List<WordProfileResult> second = WordProfileQueryService.getInstance().getWordProfileResults(secondWord, leftWindowSize, rightWindowSize, partOfSpeech, windowItemPartOfSpeech, stopList, metadata, byBase);

        Map<String, SimilarityResult>  preGraph = new HashMap<>();

        first.forEach( w -> {
            if(!preGraph.containsKey(w.getCollocate())){
                preGraph.put(w.getCollocate(), new SimilarityResult());
            }
            preGraph.get(w.getCollocate()).setW1(w.getFrequency());
        });

        second.forEach( w -> {
            if(!preGraph.containsKey(w.getCollocate())){
                preGraph.put(w.getCollocate(), new SimilarityResult());
            }
            preGraph.get(w.getCollocate()).setW2(w.getFrequency());
        });

        JsonArrayBuilder nodes = Json.createArrayBuilder();
        JsonArrayBuilder edges = Json.createArrayBuilder();

        AtomicInteger idGen = new AtomicInteger(3);

        nodes.add(createNode(1, firstWord));
        nodes.add(createNode(2, secondWord));

        preGraph.forEach((k,v) -> {
            Integer id = idGen.getAndIncrement();
            nodes.add(createNode(id, k));
            if(v.getW1() > 0){
                edges.add(createEdge(id, 1, v.getW1()));
            }
            if(v.getW2() > 0){
                edges.add(createEdge(id, 2, v.getW2()));
            }
        });

        JsonObjectBuilder network = Json.createObjectBuilder();
        network.add("nodes", nodes);
        network.add("edges", edges);

        return network.build();
    }

    private JsonObject createNode(Integer id, String label){
        JsonObjectBuilder jb = Json.createObjectBuilder();
        jb.add("id", id);
        jb.add("label", label);
        return jb.build();
    }

    private JsonObject createEdge(Integer from, Integer to, Long width){
        JsonObjectBuilder jb = Json.createObjectBuilder();
        jb.add("from", from);
        jb.add("to", to);
        jb.add("width", width);
        return jb.build();
    }

}
