package pl.clarin.chronocorpus.geographicalpropernames.control;

import pl.clarin.chronocorpus.dictionaries.control.DictionaryQueryService;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.ProperName;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.geographicalpropernames.entity.GeoProperty;
import pl.clarin.chronocorpus.geographicalpropernames.entity.LocationCount;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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

    public JsonArray findTimelapse(Set<Property> metadata, int frequencyTrashHold) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        DictionaryQueryService.getInstance()
                .findByPropertyName("publication_year")
                .forEach(y -> {
                    builder.add(findTimelapseByYear(y, metadata, frequencyTrashHold));
                });
        return builder.build();
    }

    private JsonArrayBuilder findTimelapseByYear(String year, Set<Property> metadata, int frequencyTrashHold) {

        Map<String, LocationCount> map = new ConcurrentHashMap<>();

        DocumentStore.getInstance().getDocuments()
                .parallelStream()
                .filter(d -> d.getMetadata().matches(metadata))
                .filter(d -> d.getMetadata().getProperty("publication_year").equals(year))
                .flatMap(d -> d.getProperNames().stream())
                .filter(p -> p.getType().startsWith("nam_loc")
                                            && !checkTypeList(p.getType())
                                            && !p.getBase().isEmpty()
                                            && !p.getGeoString().isEmpty())
                .forEach(p -> {
                    if (!map.containsKey(p.getBase())) {
                        map.put(p.getBase(), new LocationCount(new AtomicInteger(1), p.getLon(), p.getLat()));
                    } else {
                        map.get(p.getBase()).count().incrementAndGet();
                    }
                });

        JsonArrayBuilder geoNames = Json.createArrayBuilder();
        // [1801, "Białystok", 2000, 23.16433, 53.13333],
        map.forEach((k, v) -> {
            if (v.count().get() > frequencyTrashHold) {
                JsonArrayBuilder n = Json.createArrayBuilder();
                n.add(Integer.parseInt(year));
                n.add(k);
                n.add(v.count().get());
                n.add(v.lat());
                n.add(v.lon());
                geoNames.add(n);
            }
        });

        return geoNames;
    }

    private boolean checkTypeList(String type) {
      List<String> names = Arrays.asList(
                "nam_loc_astronomical",
                "nam_loc_gpe_admin",
                "nam_loc_gpe_admin1",
                "nam_loc_gpe_admin2",
                "nam_loc_gpe_admin3",
                "nam_loc_gpe_district",
                "nam_loc_gpe_subdivision",
                "nam_loc_hydronym",
                "nam_loc_hydronym_bay",
                "nam_loc_hydronym_lagoon",
                "nam_loc_hydronym_lake",
                "nam_loc_hydronym_ocean",
                "nam_loc_hydronym_river",
                "nam_loc_hydronym_sea",
                "nam_loc_land",
                "nam_loc_land_cape",
                "nam_loc_land_continent",
                "nam_loc_land_desert",
                "nam_loc_land_island",
                "nam_loc_land_mountain",
                "nam_loc_land_peak",
                "nam_loc_land_peninsula",
                "nam_loc_land_protected_area",
                "nam_loc_land_region",
                "nam_loc_land_sandspit");
      return names.contains(type);
    }

}



