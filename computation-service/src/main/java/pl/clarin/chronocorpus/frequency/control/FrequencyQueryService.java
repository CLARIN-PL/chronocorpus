package pl.clarin.chronocorpus.frequency.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class FrequencyQueryService {

    public static volatile FrequencyQueryService instance;

    private FrequencyQueryService() {
    }

    public static FrequencyQueryService getInstance() {
        if (instance == null) {
            synchronized (DocumentStore.class) {
                if (instance == null) {
                    instance = new FrequencyQueryService();
                }
            }
        }
        return instance;
    }

    //TODO poor performance all documents 9s
    public JsonArray calculateFrequency(Set<Property> metadata, Set<String> stopList, Boolean byBase) {

        Map<String, Map<Integer, Long>> result = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments()) {
            if (d.getMetadata().matches(metadata)) {
                if (byBase) {
                    d.documentBaseFrequency()
                            .forEach((key , value) ->{
                                if(!result.containsKey(key)){
                                    result.put(key, value);
                                }else{
                                    value.forEach((x,y) -> result.get(key).merge(x ,y , (aLong, aLong2) -> aLong + aLong2));
                                }
                            });
                } else {
                    d.documentOrthFrequency()
                            .forEach( (key , value) ->{
                                if(!result.containsKey(key)){
                                    result.put(key, value);
                                }else{
                                    value.forEach((x,y) -> result.get(key).merge(x ,y , (aLong, aLong2) -> aLong + aLong2));
                                }
                            });
                }
            }
        }

        JsonArrayBuilder frequency = Json.createArrayBuilder();
        result.entrySet()
                .stream()
                .filter(e -> !stopList.contains(e.getKey()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new))
                .forEach((k, v) ->  v.forEach((pos, cnt) -> frequency.add(
                                    Json.createObjectBuilder()
                                            .add("word", k)
                                            .add("part_of_speech", pos)
                                            .add("count", cnt)))
                );
        return frequency.build();
    }

}
