package pl.clarin.geocoder;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Lists;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeoNamesLocationStore {

    private static final Logger LOGGER = Logger.getLogger(GeoNamesLocationStore.class.getName());
    private static volatile GeoNamesLocationStore instance;
    private final Kryo kryo = new Kryo();
    private final File f = new File(Configuration.GEO_NAMES_LOCATION_CACHE_STORE_FILE);
    private final File geoNamesSourceFile = new File(Configuration.GEO_NAMES_SOURCE_FILE);

    private Map<String, List<Location>> store = new HashMap<>();

    private GeoNamesLocationStore() {
        super();
        kryo.register(HashMap.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(Location.class);
        restore();
    }

    public static GeoNamesLocationStore getInstance() {
        if (instance == null) {
            synchronized (GeoNamesLocationStore.class) {
                if (instance == null) {
                    instance = new GeoNamesLocationStore();
                }
            }
        }
        return instance;
    }

    public List<Location> getLocation(String key) {
        if (store.containsKey(key.toLowerCase())) {
            return store.get(key.toLowerCase());
        } else {
            Map<String, List<Location>> s = store.entrySet()
                    .stream()
                    .filter(e -> e.getValue().stream().anyMatch(l -> l.isStringInAlternateNames(key)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            List<Location> result = Lists.newArrayList();
            s.values().stream().findFirst().ifPresent(result::addAll);
            return result;
        }
    }

    public void addLocation(String key, List<Location> loc) {
        if (!store.containsKey(key)) {
            store.put(key.toLowerCase(), loc);
        }
    }

    private boolean checkBackupFileExists() {
        return f.exists() && !f.isDirectory();
    }

    private boolean checkSrcFileExists() {
        return geoNamesSourceFile.exists() && !geoNamesSourceFile.isDirectory();
    }

    private void restore() {
        if (checkBackupFileExists()) {
            long start = System.currentTimeMillis();
            LOGGER.info("Restoring GeoNames location cache ...");

            try (Input in = new Input(new FileInputStream(f))) {
                Object aNewMap = kryo.readClassAndObject(in);

                if (aNewMap instanceof HashMap) {
                    store = (Map<String, List<Location>>) aNewMap;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Restoring GeoNames location store failure", e);
            }

            long time = System.currentTimeMillis() - start;
            LOGGER.info("Restoring store took: " + time + "ms");
        } else {
            loadFromSourceFiles();
            save();
        }
    }

    public void loadFromSourceFiles() {
        if (checkSrcFileExists()) {
            long start = System.currentTimeMillis();
            LOGGER.info("Loading GeoNames source file ...");
            try (Stream<String> stream = Files.lines(Paths.get(geoNamesSourceFile.toURI()))) {

                stream.forEach(line -> {
                    String[] item = line.split("\t");

                    if (!store.containsKey(item[1])) {
                        Location loc = new Location(item[1], item[3], item[6], Double.parseDouble(item[4]), Double.parseDouble(item[5]));
                        List<Location> list = new ArrayList<>();
                        list.add(loc);
                        addLocation(item[1].toLowerCase(), list);
                    } else {
                        Location loc = new Location(item[1], item[3], item[6], Double.parseDouble(item[4]), Double.parseDouble(item[5]));
                        store.get(item[1].toLowerCase()).add(loc);
                    }
                });

                store.forEach((k, v) -> System.out.println(v));
            } catch (IOException e) {
                e.printStackTrace();
            }
            long time = System.currentTimeMillis() - start;
            LOGGER.info("Loading GeoNames source file took: " + time + "ms");
        }
    }

    public void save() {
        try (Output out = new Output(new FileOutputStream(Configuration.GEO_NAMES_LOCATION_CACHE_STORE_FILE))) {
            LOGGER.info("Saving cached GeoNames locations ...");
            kryo.writeClassAndObject(out, store);
            out.flush();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "GeoNames location cache store backup failure", e);
        }
    }
}
