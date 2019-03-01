package pl.clarin.chronocorpus.geographicalpropernames.boundary;

import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.JsonObject;

public class GeographicalProperNamesTask extends Task {

    public GeographicalProperNamesTask(JsonObject json) {
        super(json);
    }

    @Override
    public JsonObject doTask() {
        return null;
    }
}
