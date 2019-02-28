package pl.clarin.chronocorpus.frequency.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.*;

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

    public JsonArray calculateFrequency(Set<Property> metadata, Set<String> stopList, Boolean byBase) {

        Map<String, Long> result = new HashMap<>();

        for (Document d : DocumentStore.getInstance().getDocuments())

            if (d.getMetadata().matches(metadata)) {
                if (byBase) {
                    d.documentBaseFrequency(stopList)
                            .forEach((k, v) -> result.merge(k, v, (aLong, aLong2) -> aLong + aLong2));
                } else {
                    d.documentOrthFrequency(stopList)
                            .forEach((k, v) -> result.merge(k, v, (aLong, aLong2) -> aLong + aLong2));
                }

            }

        JsonArrayBuilder frequency = Json.createArrayBuilder();
        result.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new))
                .forEach((k, v) -> {
                            String[] key = k.split("__");
                            frequency.add(
                                    Json.createObjectBuilder()
                                            .add("word", key[0])
                                            .add("part_of_speech", key[1])
                                            .add("count", v)
                            );
                        }
                );
        return frequency.build();
    }

}
