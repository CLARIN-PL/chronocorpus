package pl.clarin.chronocorpus.geographicalpropernames.boundary;

import pl.clarin.chronocorpus.geographicalpropernames.control.GeographicalQueryService;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class GeographicalProperNamesTask extends Task {

    public GeographicalProperNamesTask(JsonObject json) {
        super(json);
    }

    @Override
    public JsonObject doTask() {

        JsonArray geo = GeographicalQueryService.getInstance()
                .findGeoNames(metadata);

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id)
                .add("rows", geo);

        return json.build();
    }
}
