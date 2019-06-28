package pl.clarin.chronocorpus.wordprofile.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.Task;
import pl.clarin.chronocorpus.timeseries.control.TimeSeriesQueryService;
import pl.clarin.chronocorpus.timeseries.entity.TimeUnit;
import pl.clarin.chronocorpus.wordprofile.control.WordProfileQueryService;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import pl.clarin.chronocorpus.Progress;

public class WordProfileTask extends Task {

    public WordProfileTask(JsonObject json) {
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

    private Optional<String> findBaseParameter(){
        return queryParameters.stream()
                .filter(p -> p.getName().equals("base"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    private Optional<String> findOrthParameter(){
        return queryParameters.stream()
                .filter(p -> p.getName().equals("orth"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    @Override
    public JsonObject doTask(Progress pr) {

        AtomicReference<JsonArray> frequency = new AtomicReference<>();

        findOrthParameter().ifPresent(w ->
                frequency.set(WordProfileQueryService.getInstance()
                        .findWordProfile(w, findLeftWindowUnit(), findRightWindowUnit(),
                                findPartOfSpeechParameter(),  metadata, false)));

        findBaseParameter().ifPresent(w ->
                frequency.set(WordProfileQueryService.getInstance()
                        .findWordProfile(w, findLeftWindowUnit(), findRightWindowUnit(),
                                findPartOfSpeechParameter(),  metadata, true)));


        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id)
                .add("rows", frequency.get());

        return json.build();
    }
}
