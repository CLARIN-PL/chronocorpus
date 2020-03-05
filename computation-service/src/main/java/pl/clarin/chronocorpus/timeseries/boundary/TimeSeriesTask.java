package pl.clarin.chronocorpus.timeseries.boundary;

import pl.clarin.chronocorpus.Progress;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.Task;
import pl.clarin.chronocorpus.timeseries.control.TimeSeriesQueryService;
import pl.clarin.chronocorpus.timeseries.entity.TimeUnit;

import javax.json.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

    private Optional<Integer> findPartOfSpeechParameter() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("part_of_speech"))
                .map(p -> Integer.parseInt(p.getValueAsString()))
                .findFirst();
    }

    private Optional<String> findBaseParameter() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("base"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    private Optional<String> findOrthParameter() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("orth"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    @Override
    public JsonObject doTask(Progress pr) {

        List<JsonObject> items = new ArrayList<>();

        findOrthParameter().ifPresent(w -> {
            String[] words = w.split(";");
            Arrays.asList(words).forEach(i -> {
                items.add(TimeSeriesQueryService.getInstance()
                        .findTimeSeries(i, findPartOfSpeechParameter(), findTimeUnit(), metadata, false));
            });
        });

        findBaseParameter().ifPresent(w -> {
            String[] words = w.split(";");
            Arrays.asList(words).forEach(i -> {
                items.add(TimeSeriesQueryService.getInstance()
                        .findTimeSeries(i, findPartOfSpeechParameter(), findTimeUnit(), metadata, true));
            });
        });

        JsonArrayBuilder rows = Json.createArrayBuilder();
        items.forEach(rows::add);

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id)
                .add("rows", rows);

        return json.build();
    }
}
