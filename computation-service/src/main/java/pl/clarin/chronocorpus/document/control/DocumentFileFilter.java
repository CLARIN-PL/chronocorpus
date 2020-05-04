package pl.clarin.chronocorpus.document.control;

import g419.corpus.io.reader.ReaderFactory;
import g419.corpus.structure.Paragraph;
import g419.corpus.structure.Sentence;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import pl.clarin.chronocorpus.document.entity.Document;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DocumentFileFilter {

    private static final Logger LOGGER = Logger.getLogger(DocumentFileFilter.class.getName());

    private static volatile DocumentFileFilter instance;

    List<String> keys = Arrays.asList(
            "zarażony", "zakazić",
            "chory", "zainfekować",
            "zawirusować", "infekcja",
            "nosiciel", "choroba", "koronawirus",
            "coronawirus", "coronavirus",
            "koronavirus", "wirus",
            "virus", "Covid", "zapalenie płuco",
            "powikłanie", "corona",
            "Korona", "Wuhan", "Bergamo", "Lombardia",
            "choroba", "zakaźny", "zarazić", "zainfekować",
            "skazić", "epidemia", "pandemia", "zaraza",
            "plaga", "drobnoustrój", "lockdown", "shutdown",
            "kwarantanna", "izolacja", "zolatorium", "transmisja",
            "stan wyjątkowy", "stan nadzwyczajny",
            "myć ręka", "maseczka", "szczepionka", "ozdrowieniec",
            "ministerstwo zdrowie", "minister zdrowie",
            "łukasz szumowski", "państwowy inspekcja sanitarny",
            "sanepid", "główny inspektorat sanitarny",
            "główny inspektor sanitarny",
            "gis", "jarosław pinkas", "epidemiolog",
            "epidemiologia", "zagrożenie epidemiczny",
            "zagrożenie epidemiologiczny", "who",
            "światowy organizacja zdrowie",
            "służba medyczny", "medyk", "ochrona zdrowie",
            "służba zdrowia", "lekarz", "pielęgniarka",
            "ratownik medyczny", "doktor", "medycyna",
            "przyłbica", "rękawiczka", "sars"
    );

    public static DocumentFileFilter getInstance() {
        if (instance == null) {
            synchronized (DocumentFileFilter.class) {
                if (instance == null) {
                    instance = new DocumentFileFilter();
                }
            }
        }
        return instance;
    }


    public Set<Document> filter() throws IOException {

        long start = System.currentTimeMillis();

        Set<Document> documents = new ObjectOpenHashSet<>();

        LOGGER.info("Loading documents from files please wait .....");
        ZipFile zipFile = new ZipFile(String.valueOf(Paths.get("/home/tnaskret/Downloads/monco_2020-1-3/ccl.zip")));

        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        LOGGER.info("Total files in zip: " + zipFile.size());
        AtomicInteger totalItems = new AtomicInteger();
        AtomicInteger partItems = new AtomicInteger();
        AtomicInteger filtred = new AtomicInteger();

        Path path = Paths.get("/home/tnaskret/Downloads/monco_2020-1-3/files.txt");
        try (BufferedWriter out = Files.newBufferedWriter(path)) {

            while (entries.hasMoreElements()) {
                try {
                    ZipEntry entry = entries.nextElement();
                    InputStream is = zipFile.getInputStream(entry);

                    String file = entry.getName();
                    g419.corpus.structure.Document ccl = ReaderFactory.get()
                            .getStreamReader("url", is, "ccl")
                            .nextDocument();
                    is.close();

                    List<String> bases = new ArrayList<>();


                    for (Paragraph p : ccl.getParagraphs()) {
                        for (Sentence s : p.getSentences()) {
                            for (g419.corpus.structure.Token t : s.getTokens()) {
                                t.getDisambTags().stream()
                                        .findFirst()
                                        .map(tag -> tag.getBase().toLowerCase())
                                        .ifPresent(bases::add);
                            }
                        }
                    }

                    String txt = String.join(" ", bases);

                    boolean cw = containsWords(txt);
                    if (cw) {
                        out.write(file + "\n");
                        filtred.incrementAndGet();
                    }

                    partItems.getAndIncrement();
                    if (partItems.get() == 5000) {
                        totalItems.getAndAdd(partItems.get());
                        partItems.getAndSet(0);
                        LOGGER.info("Loaded files: " + totalItems.get() + "/" + zipFile.size());
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Loading document failed", e);
                }
            }

        } catch (FileNotFoundException e) {

        }
        zipFile.close();
        long time = System.currentTimeMillis() - start;
        LOGGER.info("Loading documents took: " + time + "ms");
        LOGGER.info("Filtered files: " + filtred.get());
        return documents;
    }


    public boolean containsWords(String inputString) {
        boolean found = false;
        for (String item : keys) {
            if (inputString.contains(item)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
