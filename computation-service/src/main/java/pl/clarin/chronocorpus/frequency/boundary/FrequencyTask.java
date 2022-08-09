package pl.clarin.chronocorpus.frequency.boundary;

import pl.clarin.chronocorpus.administration.control.StatisticsQueryService;
import pl.clarin.chronocorpus.dictionaries.control.DictionaryQueryService;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.frequency.control.FrequencyQueryService;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.clarin.chronocorpus.Progress;

public class FrequencyTask extends Task {

    public FrequencyTask(JsonObject json) {
        super(json);
    }

    private Boolean findCountByBaseParameter() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("count_by_base"))
                .map(p -> Boolean.parseBoolean(p.getValueAsString()))
                .findFirst().orElse(null);
    }

    private Set<String> findStopListParameter() {
        Optional<String> s = queryParameters.stream()
                .filter(p -> p.getName().equals("stop_list"))
                .map(Property::getValueAsString)
                .findFirst();

        return s.map(value -> Stream.of(value.split(";"))
                .collect(Collectors.toSet()))
                .orElseGet(() -> new HashSet<>(DictionaryQueryService.getInstance().defaultStopList()));
    }

    @Override
    public JsonObject doTask(Progress pr) {


        JsonArray frequency = FrequencyQueryService.getInstance()
                .calculateFrequency(metadata, findStopListParameter(), findCountByBaseParameter(), pr);

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id)
                .add("rows", frequency);

        StatisticsQueryService.getInstance().updateFrequencyQueryCount();
        return json.build();
    }
}
