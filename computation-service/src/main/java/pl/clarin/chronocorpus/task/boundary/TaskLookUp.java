package pl.clarin.chronocorpus.task.boundary;

import pl.clarin.chronocorpus.concordance.boundary.ConcordanceTask;
import pl.clarin.chronocorpus.dictionaries.boundary.DictionariesTask;
import pl.clarin.chronocorpus.document.boundary.DocumentListTask;
import pl.clarin.chronocorpus.document.boundary.DocumentTask;
import pl.clarin.chronocorpus.frequency.boundary.FrequencyTask;
import pl.clarin.chronocorpus.geographicalpropernames.boundary.GeographicalProperNamesTask;
import pl.clarin.chronocorpus.quantityanalysis.boundary.QuantityAnalysisTask;
import pl.clarin.chronocorpus.task.entity.Task;
import pl.clarin.chronocorpus.timeseries.boundary.TimeSeriesTask;
import pl.clarin.chronocorpus.wordprofile.boundary.WordProfileTask;

import javax.json.JsonObject;
import java.util.Optional;

public class TaskLookUp {

    public Optional<Task> getTask(JsonObject json){

        if(json.containsKey("task_type")){
            if(json.getString("task_type").equalsIgnoreCase("concordance")) return Optional.of(new ConcordanceTask(json));
            if(json.getString("task_type").equalsIgnoreCase("document")) return Optional.of(new DocumentTask(json));
            if(json.getString("task_type").equalsIgnoreCase("document_list")) return Optional.of(new DocumentListTask(json));
            if(json.getString("task_type").equalsIgnoreCase("frequency")) return Optional.of(new FrequencyTask(json));
            if(json.getString("task_type").equalsIgnoreCase("dictionaries")) return Optional.of(new DictionariesTask(json));
            if(json.getString("task_type").equalsIgnoreCase("time_series")) return Optional.of(new TimeSeriesTask(json));
            if(json.getString("task_type").equalsIgnoreCase("word_profile")) return Optional.of(new WordProfileTask(json));
            if(json.getString("task_type").equalsIgnoreCase("geo_proper_names")) return Optional.of(new GeographicalProperNamesTask(json));
            if(json.getString("task_type").equalsIgnoreCase("quantity_analysis")) return Optional.of(new QuantityAnalysisTask(json));
        }
        return Optional.empty();
    }
}
