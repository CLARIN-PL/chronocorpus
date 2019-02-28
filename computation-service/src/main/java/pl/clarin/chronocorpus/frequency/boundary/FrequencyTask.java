package pl.clarin.chronocorpus.frequency.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.frequency.control.FrequencyQueryService;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.HashSet;
import java.util.Set;

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

    private String findStopListParameter() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("stop_list"))
                .map(Property::getValueAsString)
                .findFirst().orElse(null);
    }

    @Override
    public JsonObject doTask() {
        Set<String> stopList = new HashSet<>();
        JsonArray frequency = FrequencyQueryService.getInstance()
                .calculateFrequency(metadata, stopList, findCountByBaseParameter());

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id)
                .add("rows", frequency);

        return json.build();
    }
}
