package pl.clarin.geocoder;

import g419.corpus.structure.Annotation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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

    public void process(Path p, int limit) {
        AtomicInteger c = new AtomicInteger(0);
        if (Files.isDirectory(p)) {
            try {
                List<Path> files = Files.walk(p)
                        .map(Path::toAbsolutePath)
                        .collect(Collectors.toList());

                for (Path name : files) {
                    LOGGER.log(Level.INFO, "Processing file:" + c.getAndIncrement() + "/" + files.size());
                    run(name, limit);
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Unable to load file", e);
            }

        } else if (Files.isRegularFile(p)) {
            run(p, limit);
        }
        MapsLocationStore.getInstance().save();
    }

    private void run(Path path, int limit) {
        if (!Files.isDirectory(path)) {
            try (InputStream is = Files.newInputStream(path)) {
                g419.corpus.structure.Document ccl = AnnotationReader.getInstance().loadDocument(is);
                if (ccl != null) {
                    ccl.getParagraphs().forEach(p -> {
                        p.getSentences().forEach(s -> {

                            Map<Integer, List<g419.corpus.structure.Annotation>> ann = s.getChunks()
                                    .stream()
                                    .filter(a ->
                                            a.getType().startsWith("nam_loc") ||
                                                    a.getType().startsWith("nam_fac") ||
                                                    a.getType().startsWith("nam_num"))
                                    .collect(Collectors.groupingBy(g419.corpus.structure.Annotation::getChannelIdx));

                            ann.forEach((k, v) -> {

                                String q = v.stream().map(Annotation::getBaseText)
                                        .collect(Collectors.joining(" "));
                                AtomicReference<String> arg = new AtomicReference<>("nam_loc");
                                v.stream().findFirst().ifPresent(a -> {
                                    arg.set(a.getType());
                                });
                                try {
                                    List<Location> loc = GeoNamesLocationFinder.getInstance().query(q);
                                    if (loc.isEmpty()) {
                                        loc = MapsLocationFinder.getInstance().query(q, limit, "pl");
                                    }

                                    AtomicInteger count = new AtomicInteger(1);
                                    loc.stream().limit(limit).forEach(l -> {
                                        s.getAnnotationInChannel(arg.get(), k).getMetadata()
                                                .put("coord:" + count.get(), l.toPropertyValue());
                                        count.getAndIncrement();
                                    });
                                    count.set(1);
                                } catch (IOException e) {
                                    LOGGER.log(Level.SEVERE, "Geo service error", e);
                                }

                            });
                        });
                    });
                }
                AnnotationWriter.getInstance().writeDocument(ccl, path);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "File error", e);
            }
        }
    }
}
