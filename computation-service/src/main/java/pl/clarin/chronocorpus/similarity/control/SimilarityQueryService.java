package pl.clarin.chronocorpus.similarity.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Token;
import pl.clarin.chronocorpus.similarity.entity.SimilarityResult;
import pl.clarin.chronocorpus.wordprofile.control.WordProfileQueryService;
import pl.clarin.chronocorpus.wordprofile.entity.WordProfile;
import pl.clarin.chronocorpus.wordprofile.entity.WordProfileResult;

import javax.json.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public JsonArray findSimilarity(String firstWord, String secondWord,
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
