package pl.clarin.chronocorpus.quantityanalysis.boundary;

import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.JsonObject;

public class QuantityAnalysisTask extends Task {

    public QuantityAnalysisTask(JsonObject json) {
        super(json);
    }

    @Override
    public JsonObject doTask() {
        return null;
    }
}
