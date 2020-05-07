package pl.clarin.geocoder.service;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.NominatimClient;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import fr.dudie.nominatim.client.request.NominatimSearchRequest;
import fr.dudie.nominatim.model.Address;
import org.apache.http.client.HttpClient;

import org.apache.http.impl.client.HttpClientBuilder;

public class MapsLocationFinder {

    private static final Logger LOGGER = Logger.getLogger(MapsLocationFinder.class.getName());

    private static volatile MapsLocationFinder instance;
    private NominatimClient nc;
    private MapsLocationStore store = MapsLocationStore.getInstance();

    private MapsLocationFinder() {
        HttpClient client =  HttpClientBuilder.create().build();
        nc = new JsonNominatimClient("http://maps.clarin-pl.eu", client, "admin@clarin-pl.eu");
    }

    public static MapsLocationFinder getInstance() {
        if (instance == null) {
            synchronized (MapsLocationFinder.class) {
                if (instance == null) {
                    instance = new MapsLocationFinder();
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
