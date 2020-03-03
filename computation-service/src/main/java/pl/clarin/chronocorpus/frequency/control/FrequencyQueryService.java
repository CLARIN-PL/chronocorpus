package pl.clarin.chronocorpus.frequency.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Statistic;
import pl.clarin.chronocorpus.frequency.entity.FrequencyItem;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import pl.clarin.chronocorpus.Progress;

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

    public JsonArray calculateFrequency(Set<Property> metadata, Set<String> stopList, Boolean byBase,Progress pr) {
        
        Map<FrequencyItem, Integer> result = new ConcurrentHashMap<>();
        if (byBase) {
            DocumentStore.getInstance()
                    .getDocuments()
                    .parallelStream()
                    .filter(d -> { pr.update();return d.getMetadata().matches(metadata);})
                    .flatMap(d -> d.documentBaseFrequency().entrySet().stream())
                    .filter(e -> !stopList.contains(e.getKey()))
                    .forEach(e -> calculateFrequency(result, e.getKey(), e.getValue()));
        } else {

            DocumentStore.getInstance()
                    .getDocuments()
                    .parallelStream()
                    .filter(d -> { pr.update(); return d.getMetadata().matches(metadata); })
                    .flatMap(d -> d.documentOrthFrequency().entrySet().stream())
                    .forEach(e -> calculateFrequency(result, e.getKey(), e.getValue()));
        }

        reMapValues(result);

        JsonArrayBuilder frequency = Json.createArrayBuilder();
        result.keySet().parallelStream()
                .sorted(Comparator.comparing(FrequencyItem::getFrequency).reversed())
                .collect(Collectors.toList())
                .stream().map(FrequencyItem::toJson).forEach(frequency::add);

        return frequency.build();
    }

    public Map<FrequencyItem, Integer> calculateFrequencyByBase(Set<Property> metadata) {
        Map<FrequencyItem, Integer> result =  new ConcurrentHashMap<>();
        DocumentStore.getInstance()
                .getDocuments()
                .parallelStream()
                .filter(d -> d.getMetadata().matches(metadata))
                .flatMap(d -> d.documentBaseFrequency().entrySet().stream())
                .forEach(e -> calculateFrequency(result, e.getKey(), e.getValue()));
        return result;
    }
    private void reMapValues(Map<FrequencyItem, Integer> result) {
        result.forEach(FrequencyItem::setFrequency);
    }

    private void calculateFrequency(Map<FrequencyItem, Integer> result, String key, Statistic statistic) {
        if (statistic.getVerbs() > 0) {
            FrequencyItem item = new FrequencyItem(key, (byte) 1, statistic.getVerbs());
            mapItem(result, statistic.getVerbs(), item);
        }

        if (statistic.getNouns() > 0) {
            FrequencyItem item = new FrequencyItem(key, (byte) 2, statistic.getNouns());
            mapItem(result, statistic.getNouns(), item);
        }
        if (statistic.getAdverbs() > 0) {
            FrequencyItem item = new FrequencyItem(key, (byte) 3, statistic.getAdverbs());
            mapItem(result, statistic.getAdverbs(), item);
        }
        if (statistic.getAdjectives() > 0) {
            FrequencyItem item = new FrequencyItem(key, (byte) 4, statistic.getAdjectives());
            mapItem(result, statistic.getAdjectives(), item);
        }
    }

    private void mapItem(Map<FrequencyItem, Integer> result, Integer frq, FrequencyItem item) {
        if (!result.containsKey(item)) {
            result.put(item, frq);
        } else {
            Integer count = result.get(item) + frq;
            result.replace(item, count);
        }
    }

}
