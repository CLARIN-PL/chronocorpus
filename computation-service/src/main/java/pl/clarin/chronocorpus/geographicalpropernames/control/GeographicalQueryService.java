package pl.clarin.chronocorpus.geographicalpropernames.control;

import pl.clarin.chronocorpus.dictionaries.control.DictionaryQueryService;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.ProperName;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.geographicalpropernames.entity.GeoProperty;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GeographicalQueryService {

    public static volatile GeographicalQueryService instance;

    private GeographicalQueryService() {
    }

    public static GeographicalQueryService getInstance() {
        if (instance == null) {
            synchronized (DocumentStore.class) {
                if (instance == null) {
                    instance = new GeographicalQueryService();
                }
            }
        }
        return instance;
    }

    public JsonArray findGeoNames(Set<Property> metadata) {
        Map<ProperName, Integer> map = new ConcurrentHashMap<>();

        DocumentStore.getInstance().getDocuments()
                .parallelStream()
                .filter(d -> d.getMetadata().matches(metadata))
                .flatMap(d -> d.getProperNames().stream())
                .filter(p -> p.getType().startsWith("nam_loc"))
                .forEach(p -> {
                    if (!map.containsKey(p)) {
                        map.put(p, 1);
                    } else {
                        Integer val = map.get(p) + 1;
                        map.replace(p, val);
                    }
                });
        JsonArrayBuilder geoNames = Json.createArrayBuilder();
        map.forEach((k, v) -> {
            if (k.getGeoString() != null) {
                GeoProperty gp = new GeoProperty(k.getValue(), k.getType(), v);
                gp.setLan(k.getLat());
                gp.setLon(k.getLon());
                geoNames.add(gp.toJson());
            }
        });

        return geoNames.build();
    }

    public JsonArray findTimelapse(Set<Property> metadata, int frequencyTrashHold){
        JsonArrayBuilder builder = Json.createArrayBuilder();
        DictionaryQueryService.getInstance()
                .findByPropertyName("publication_year")
                .forEach(y -> {
                    builder.add(findTimelapseByYear(y, metadata, frequencyTrashHold));
                });
        return builder.build();
    }

    private JsonArrayBuilder findTimelapseByYear(String year, Set<Property> metadata, int frequencyTrashHold) {
        Map<ProperName, Integer> map = new ConcurrentHashMap<>();

        DocumentStore.getInstance().getDocuments()
                .parallelStream()
                .filter(d -> d.getMetadata().matches(metadata))
                .filter(d -> d.getMetadata().getProperty("publication_year").equals(year))
                .flatMap(d -> d.getProperNames().stream())
                .filter(p -> p.getType().startsWith("nam_loc"))
                .forEach(p -> {
                    if (!map.containsKey(p)) {
                        map.put(p, 1);
                    } else {
                        Integer val = map.get(p) + 1;
                        map.replace(p, val);
                    }
                });

        JsonArrayBuilder geoNames = Json.createArrayBuilder();
        // [1801, "Białystok", 2000, 23.16433, 53.13333],
        map.forEach((k, v) -> {
            if (k.getGeoString() != null && v > frequencyTrashHold) {
                JsonArrayBuilder n = Json.createArrayBuilder();
                n.add(Integer.parseInt(year));
                n.add(k.getValue());
                n.add(v);
                n.add(k.getLat());
                n.add(k.getLon());
                geoNames.add(n);
            }
        });

        return geoNames;
    }
}



