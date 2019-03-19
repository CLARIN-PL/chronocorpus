package pl.clarin.chronocorpus.frequency.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Statistic;
import pl.clarin.chronocorpus.frequency.entity.FrequencyItem;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.*;
import java.util.stream.Collectors;

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

        Map<FrequencyItem, Integer> result = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments()) {
            if (d.getMetadata().matches(metadata)) {
                if (byBase) {
                    d.documentBaseFrequency()
                            .forEach((key, value) -> {
                                if (!stopList.contains(key))
                                    calculateFrequency(result, key, value);
                            });
                } else {
                    d.documentOrthFrequency()
                            .forEach((key, value) -> {
                                if (!stopList.contains(key))
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

    private void reMapValues(Map<FrequencyItem, Integer> result) {
        result.forEach(FrequencyItem::setFrequency);
    }

    private void calculateFrequency(Map<FrequencyItem, Integer> result, String key, Statistic statistic) {
        if(statistic.getVerbCount() > 0) {
            FrequencyItem item = new FrequencyItem(key, (byte) 1, statistic.getVerbCount());
            mapItem(result, statistic.getVerbCount(), item);
        }

        if(statistic.getNounCount() > 0) {
            FrequencyItem item = new FrequencyItem(key, (byte) 2, statistic.getNounCount());
            mapItem(result, statistic.getNounCount(), item);
        }
        if(statistic.getAdvCount() > 0) {
            FrequencyItem item = new FrequencyItem(key, (byte) 3, statistic.getAdvCount());
            mapItem(result, statistic.getAdvCount(), item);
        }
        if(statistic.getAdjCount() > 0) {
            FrequencyItem item = new FrequencyItem(key, (byte) 4, statistic.getAdjCount());
            mapItem(result, statistic.getAdjCount(), item);
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
