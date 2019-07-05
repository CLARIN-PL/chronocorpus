package pl.clarin.geocoder;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.NominatimClient;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import fr.dudie.nominatim.client.request.NominatimSearchRequest;
import fr.dudie.nominatim.model.Address;
import org.apache.http.client.HttpClient;

import org.apache.http.impl.client.HttpClientBuilder;

public class LocationFinder {

    private static final Logger LOGGER = Logger.getLogger(LocationFinder.class.getName());

    private static volatile LocationFinder instance;
    private NominatimClient nc;
    private LocationStore store = LocationStore.getInstance();

    private LocationFinder() {
        HttpClient client =  HttpClientBuilder.create().build();
        nc = new JsonNominatimClient(client, "admin@clarin-pl.eu");
    }

    public static LocationFinder getInstance() {
        if (instance == null) {
            synchronized (LocationFinder.class) {
                if (instance == null) {
                    instance = new LocationFinder();
                }
            }
        }
        return instance;
    }

    public List<Location> query(String query, int limit, String lang) throws IOException {

        List<Location> locations =  store.getLocation(query.toLowerCase());

        if(locations.isEmpty()) {
            NominatimSearchRequest req = new NominatimSearchRequest();
            req.setAcceptLanguage(lang);
            req.setLimit(limit);
            req.setQuery(query.toLowerCase());

            final List<Address> adr = nc.search(req);
            adr.forEach(a -> {
                locations.add(new Location(
                        a.getDisplayName(),
                        a.getElementType(),
                        a.getLatitude(),
                        a.getLongitude(),
                        a.getImportance()
                ));
            });
            if (!locations.isEmpty()){
                store.addLocation(query.toLowerCase(), locations);
            }
        }
        return locations;
    }
}
