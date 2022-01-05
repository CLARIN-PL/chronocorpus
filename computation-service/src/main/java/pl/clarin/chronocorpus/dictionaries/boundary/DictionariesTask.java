package pl.clarin.chronocorpus.dictionaries.boundary;

import pl.clarin.chronocorpus.dictionaries.control.DictionaryQueryService;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Optional;
import pl.clarin.chronocorpus.Progress;

public class DictionariesTask extends Task {

    public DictionariesTask(JsonObject json) {
        super(json);
    }

    private Boolean findDictionariesParameter() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("dictionaries"))
                .map(p -> Boolean.parseBoolean(p.getValueAsString()))
                .findFirst().orElse(false);
    }

    private Optional<String> findPropertyListParameter() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("property_value_list"))
                .map(Property::getValueAsString)
                .findFirst();
    }

    @Override
    public JsonObject doTask(Progress pr) {

        JsonArrayBuilder partsOfSpeech = Json.createArrayBuilder();
        DictionaryQueryService.getInstance()
                .findAllPartsOfSpeech().forEach(p -> partsOfSpeech.add(p.toJson()));

        JsonArrayBuilder exposition = Json.createArrayBuilder();
        DictionaryQueryService.getInstance()
                .findExpositions().forEach(e -> exposition.add(e.toJson()));

        JsonArrayBuilder propertyNames = Json.createArrayBuilder();
        DictionaryQueryService.getInstance()
                .findAllPropertyNames().forEach(propertyNames::add);

        JsonArrayBuilder defaultStopList = Json.createArrayBuilder();
        DictionaryQueryService.getInstance()
                .defaultStopList().forEach(defaultStopList::add);

        JsonArrayBuilder semanticList = Json.createArrayBuilder();
        DictionaryQueryService.getInstance().getSemanticListNames().forEach(semanticList::add);

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id);

        if (findDictionariesParameter()) {
            JsonObjectBuilder dict = Json.createObjectBuilder()
                    .add("parts_of_speech", partsOfSpeech)
                    .add("exposition", exposition)
                    .add("property_names", propertyNames)
                    .add("semantic_list", semanticList)
                    .add("default_stop_list", defaultStopList);

            json.add("dictionaries", dict);
        } else {
            findPropertyListParameter()
                    .ifPresent(p -> {
                        JsonArrayBuilder properties = Json.createArrayBuilder();
                        DictionaryQueryService.getInstance()
                                .findByPropertyName(p).forEach(properties::add);
                        json.add(p, properties);
                    });
        }
        return json.build();
    }
}
