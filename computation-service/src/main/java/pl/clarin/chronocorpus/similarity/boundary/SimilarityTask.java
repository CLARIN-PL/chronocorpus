package pl.clarin.chronocorpus.similarity.boundary;

import pl.clarin.chronocorpus.Progress;
import pl.clarin.chronocorpus.administration.control.StatisticsQueryService;
import pl.clarin.chronocorpus.dictionaries.control.DictionaryQueryService;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.similarity.control.SimilarityQueryService;
import pl.clarin.chronocorpus.task.entity.Task;
import pl.clarin.chronocorpus.wordprofile.control.WordProfileQueryService;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimilarityTask extends Task {

    public SimilarityTask(JsonObject json) {
        super(json);
    }

    private Integer findLeftWindowUnit() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("left_window_size"))
                .map(p -> Integer.parseInt(p.getValueAsString()))
                .findFirst().orElse(null);
    }

    private Integer findRightWindowUnit() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("right_window_size"))
                .map(p -> Integer.parseInt(p.getValueAsString()))
                .findFirst().orElse(null);
    }

    private Integer findPartOfSpeechParameter(){
        return queryParameters.stream()
                .filter(p -> p.getName().equals("part_of_speech"))
                .map( p -> Integer.parseInt(p.getValueAsString()))
                .findFirst().orElse(0);
    }

    private Integer findWindowItemPartOfSpeechParameter(){
        return queryParameters.stream()
                .filter(p -> p.getName().equals("window_item_part_of_speech"))
                .map( p -> Integer.parseInt(p.getValueAsString()))
                .findFirst().orElse(0);
    }

    private Optional<String> findFirstBaseParameter(){
        return queryParameters.stream()
                .filter(p -> p.getName().equals("first_base"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    private Optional<String> findSecondBaseParameter(){
        return queryParameters.stream()
                .filter(p -> p.getName().equals("second_base"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    private Set<String> findStopListParameter() {
        Set<String> stopList;

        Optional<String> s = queryParameters.stream()
                .filter(p -> p.getName().equals("stop_list"))
                .map(Property::getValueAsString)
                .findFirst();
        if (s.isPresent()) {
            stopList = Stream.of(s.get().split(";")).collect(Collectors.toSet());
        } else {
            stopList = new HashSet<>(DictionaryQueryService.getInstance().defaultStopList());
        }
        return stopList;
    }


    @Override
    public JsonObject doTask(Progress pr) {

        AtomicReference<JsonArray> similarity = new AtomicReference<>();

        if(findFirstBaseParameter().isPresent() && findSecondBaseParameter().isPresent()){
            similarity.set(SimilarityQueryService.getInstance().findSimilarity(
                    findFirstBaseParameter().get(), findSecondBaseParameter().get(), findLeftWindowUnit(), findRightWindowUnit(),
                    findPartOfSpeechParameter(), findWindowItemPartOfSpeechParameter(),
                    findStopListParameter(), metadata, true
            ));
        }

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id)
                .add("rows", similarity.get());

        StatisticsQueryService.getInstance().updateSimilarityQueryCount();
        return json.build();
    }
}
