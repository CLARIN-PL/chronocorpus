package pl.clarin.chronocorpus.frequency.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.frequency.entity.FrequencyItem;
import pl.clarin.chronocorpus.query.boundary.FrequencyQuery;

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

        Map<FrequencyItem, Long> result = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments()) {
            if (d.getMetadata().matches(metadata)) {
                if (byBase) {
                    d.documentBaseFrequency()
                            .forEach((key , value) -> {
                                if(!stopList.contains(key))
                                    calculateFrequency(result, key, value);
                            });
                } else {
                    d.documentOrthFrequency()
                            .forEach( (key , value) -> {
                                if(!stopList.contains(key))
                                    calculateFrequency(result, key, value);
                            });
                }
            }
        }
        reMapValues(result);

        List<FrequencyItem> items = result.keySet().stream()
                .sorted(Comparator.comparing(FrequencyItem::getFrequency).reversed())
                .collect(Collectors.toList());

        JsonArrayBuilder frequency = Json.createArrayBuilder();
        items.stream().map(FrequencyItem::toJson).forEach(frequency::add);

        return frequency.build();
    }

    private void reMapValues(Map<FrequencyItem, Long> result){
        result.forEach(FrequencyItem::setFrequency);
    }

    private void calculateFrequency(Map<FrequencyItem, Long> result, String key, Map<Integer, Long> value) {
        value.forEach((pos, frq) -> {
            FrequencyItem item = new FrequencyItem(key, pos, frq);
            if(!result.containsKey(item)){
                result.put(item, frq);
            }else{
                Long count = result.get(item) + frq;
                result.replace(item, count);
            }
        });
    }

}
