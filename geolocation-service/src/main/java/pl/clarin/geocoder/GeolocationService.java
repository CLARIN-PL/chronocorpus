package pl.clarin.geocoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GeolocationService {
    private static final Logger LOGGER = Logger.getLogger(GeolocationService.class.getName());

    private static volatile GeolocationService instance;

    public static GeolocationService getInstance() {
        if (instance == null) {
            synchronized (GeolocationService.class) {
                if (instance == null) {
                    instance = new GeolocationService();
                }
            }
        }
        return instance;
    }

    public void process(Path path) throws IOException {

        try (InputStream is = Files.newInputStream(path)) {
            g419.corpus.structure.Document ccl = AnnotationReader.getInstance().loadDocument(is);
            if (ccl != null) {
                for (g419.corpus.structure.Annotation a : ccl.getAnnotations()) {
                    if (a.getType().startsWith("nam_loc") || a.getType().startsWith("nam_fac") ||
                                    a.getType().startsWith("nam_num")) {
                        String q = a.getTokenTokens().stream().map(t -> t.getDisambTag().getBase()).collect(Collectors.joining(" "));
                        List<Location> loc = LocationFinder.getInstance().query(q, 5, "pl");
                        AtomicInteger count = new AtomicInteger(1);
                        loc.forEach(l -> {
                            a.getTokenTokens().stream().findFirst()
                                    .ifPresent( t -> {
                                        a.getMetadata().put(a.getType()+":coord:"+count.get(), l.toPropertyValue());
                                        t.getProps().put(a.getType()+":coord:"+count.get(), l.toPropertyValue());
                                    });
                            count.getAndIncrement();
                        });
                        count.set(1);
                    }
                }
            }
            AnnotationWriter.getInstance().writeDocument(ccl);
            LocationStore.getInstance().save();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File error", e);
        }
    }
}
