package pl.clarin.chronocorpus.similarity.control;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.similarity.entity.SimilarityResult;
import pl.clarin.chronocorpus.wordprofile.control.WordProfileQueryService;
import pl.clarin.chronocorpus.wordprofile.entity.WordProfileResult;

import javax.json.*;
import java.util.*;

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

    public JsonArray findSimilarity(String firstWord, String secondWord,
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

        Map<String, SimilarityResult>  preGraph = new HashMap<>();

        first.stream()
                .filter(w -> w.getFrequency() > FREQUENCY_THRESHOLD)
                .forEach( w -> {
                if (!preGraph.containsKey(w.getCollocate())) {
                    preGraph.put(w.getCollocate(), new SimilarityResult());
                }
                preGraph.get(w.getCollocate()).setW1(w.getFrequency());

        });

        second.stream()
                .filter(w -> w.getFrequency() > FREQUENCY_THRESHOLD)
                .forEach( w -> {
            if(!preGraph.containsKey(w.getCollocate())){
                preGraph.put(w.getCollocate(), new SimilarityResult());
            }
            preGraph.get(w.getCollocate()).setW2(w.getFrequency());
        });

        JsonArrayBuilder graph = Json.createArrayBuilder();
        preGraph.forEach((k,v) -> {
            JsonObjectBuilder jb = Json.createObjectBuilder();
            jb.add("collocate", k);
            jb.add(firstWord, v.getW1());
            jb.add(secondWord, v.getW2());
        });
        return graph.build();
    }


}
