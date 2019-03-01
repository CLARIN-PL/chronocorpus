package pl.clarin.chronocorpus.wordprofile.boundary;

import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.JsonObject;

public class WordProfileTask extends Task {

    public WordProfileTask(JsonObject json) {
        super(json);
    }

    @Override
    public JsonObject doTask() {
        return null;
    }
}
