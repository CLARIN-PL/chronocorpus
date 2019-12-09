package pl.clarin.geocoder;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class GeoNamesLocationFinder {

    private static final Logger LOGGER = Logger.getLogger(GeoNamesLocationFinder.class.getName());

    private static volatile GeoNamesLocationFinder instance;
    private GeoNamesLocationStore store = GeoNamesLocationStore.getInstance();

    private GeoNamesLocationFinder() {
    }

    public static GeoNamesLocationFinder getInstance() {
        if (instance == null) {
            synchronized (GeoNamesLocationFinder.class) {
                if (instance == null) {
                    instance = new GeoNamesLocationFinder();
                }
            }
        }
        return instance;
    }

    public List<Location> query(String query) throws IOException {
       return store.getLocation(query.toLowerCase());
    }
}
