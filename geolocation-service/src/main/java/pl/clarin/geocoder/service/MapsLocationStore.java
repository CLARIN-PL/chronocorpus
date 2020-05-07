package pl.clarin.geocoder.service;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Lists;
import pl.clarin.geocoder.Configuration;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapsLocationStore {

    private static final Logger LOGGER = Logger.getLogger(MapsLocationStore.class.getName());
    private static volatile MapsLocationStore instance;
    private final Kryo kryo = new Kryo();
    private final File f = new File(Configuration.MAPS_LOCATION_CACHE_STORE_FILE);

    private Map<String, List<Location>> store = new HashMap<>();

    private MapsLocationStore() {
        super();
        kryo.register(java.util.HashMap.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(Location.class);
        restore();
    }

    public static MapsLocationStore getInstance() {
        if (instance == null) {
            synchronized (MapsLocationStore.class) {
                if (instance == null) {
                    instance = new MapsLocationStore();
                }
            }
        }
        return instance;
    }

    public List<Location> getLocation(String key){
        if (store.containsKey(key.toLowerCase())) {
            return store.get(key.toLowerCase());
        }
        return Lists.newArrayList();
    }

    public void addLocation(String key, List<Location> loc){
        if(!store.containsKey(key)){
            store.put(key.toLowerCase(), loc);
        }
    }

    private boolean checkBackupFileExists() {
        return f.exists() && !f.isDirectory();
    }

    private void restore() {
        if (checkBackupFileExists()) {
            long start = System.currentTimeMillis();
            LOGGER.info("Restoring location cache ...");

            try (Input in = new Input(new FileInputStream(f))) {
                Object aNewMap = kryo.readClassAndObject(in);

                if (aNewMap instanceof HashMap) {
                    store = (Map<String, List<Location>>) aNewMap;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Restoring location store failure", e);
            }

            long time = System.currentTimeMillis() - start;
            LOGGER.info("Restoring store took: " + time + "ms");
        }
    }

    public void save() {
        try (Output out = new Output(new FileOutputStream(Configuration.MAPS_LOCATION_CACHE_STORE_FILE))) {
            LOGGER.info("Saving cached locations ...");
            kryo.writeClassAndObject(out, store);
            out.flush();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Location cache store backup failure", e);
        }
    }
}
