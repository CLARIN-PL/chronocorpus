package pl.clarin.chronocorpus.timeseries.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.Task;
import pl.clarin.chronocorpus.timeseries.control.TimeSeriesQueryService;
import pl.clarin.chronocorpus.timeseries.entity.TimeUnit;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import pl.clarin.chronocorpus.Progress;

public class TimeSeriesTask extends Task {

    public TimeSeriesTask(JsonObject json) {
        super(json);
    }

    private Optional<TimeUnit> findTimeUnit() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("time_unit"))
                .map(p -> TimeUnit.valueOf(p.getValueAsString()))
                .findFirst();
    }

    private Optional<Integer> findPartOfSpeechParameter(){
        return queryParameters.stream()
                .filter(p -> p.getName().equals("part_of_speech"))
                .map( p -> Integer.parseInt(p.getValueAsString()))
                .findFirst();
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
        AtomicReference<JsonObject> jsonObject = new AtomicReference<>();

        findOrthParameter().ifPresent(w ->
                jsonObject.set(TimeSeriesQueryService.getInstance()
                        .findTimeSeries(w, findPartOfSpeechParameter(), findTimeUnit(), metadata, false)));

        findBaseParameter().ifPresent(w ->
                jsonObject.set(TimeSeriesQueryService.getInstance()
                        .findTimeSeries(w, findPartOfSpeechParameter(), findTimeUnit(), metadata, true)));

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id)
                .add("rows", Json.createArrayBuilder().add(jsonObject.get()));

        return json.build();
    }
}
