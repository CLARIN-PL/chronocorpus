package pl.clarin.chronocorpus.document.control;

import g419.corpus.io.reader.ReaderFactory;
import g419.corpus.structure.Annotation;
import g419.corpus.structure.Paragraph;
import g419.corpus.structure.Sentence;
import g419.corpus.structure.Token;
import pl.clarin.chronocorpus.Configuration;
import pl.clarin.chronocorpus.document.entity.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DocumentFileLoader {

    private static final Logger LOGGER = Logger.getLogger(DocumentFileLoader.class.getName());

    private static volatile DocumentFileLoader instance;

    private static Map<String, Word> wordCache = new HashMap<>();
    private static Map<String, Property> propertyCache = new HashMap<>();
    private static Map<String, ProperName> properNameCache = new HashMap<>();

    public static Word createWord(final String orth, final String base, final String ctag, final byte pos, final boolean spaceAfter) {
        return wordCache.computeIfAbsent(wordParametersAsString(orth, base, ctag, pos, spaceAfter), newParams -> new Word(orth, base, ctag, pos, spaceAfter));
    }

    public static Property createProperty(final String name, final String value) {
        return propertyCache.computeIfAbsent(propertyParameterAsString(name, value), newParams ->
                new Property(name, value));
    }

    public static ProperName createProperName(final String type, final String value) {
        return properNameCache.computeIfAbsent(propertyParameterAsString(type, value), newParams ->
                new ProperName(type, value));
    }

    private static String wordParametersAsString(String orth, String base, String ctag, int pos, boolean spaceAfter) {
        return orth + "_" + base + "_" + ctag + "_" + pos + "_" + spaceAfter;
    }

    private static String propertyParameterAsString(String name, String value){
        return name+"_"+value;
    }

    public static DocumentFileLoader getInstance() {
        if (instance == null) {
            synchronized (DocumentFileLoader.class) {
                if (instance == null) {
                    instance = new DocumentFileLoader();
                }
            }
        }
        return instance;
    }

    //TODO normalizacja danych słownikowych
    private Map<String, Metadata> loadMetadata(String metadataZipFile) throws IOException {
        LOGGER.log(Level.INFO, "Loading metadata documents from files please wait .....");
        long start = System.currentTimeMillis();
        Map<String, Metadata> metadata = new HashMap<>();
        ZipFile zipFile = new ZipFile(String.valueOf(Paths.get(metadataZipFile)));
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        AtomicInteger totalItems = new AtomicInteger();
        AtomicInteger partItems = new AtomicInteger();
        while (entries.hasMoreElements()) {
            try {
                ZipEntry entry = entries.nextElement();
                InputStream is = zipFile.getInputStream(entry);
                String id = entry.getName().replace(".json", "");
                JsonReader reader = Json.createReader(is);
                JsonObject obj = reader.readObject();

                JsonArray properties = obj.getJsonArray("metadata");

                Metadata m = new Metadata("chronopress", "apawłow", true);
                properties.forEach(jsonValue -> {
                    if (jsonValue instanceof JsonObject) {
                        String name = ((JsonObject) jsonValue).getString("name");
                        String value = ((JsonObject) jsonValue).getString("value");
                        String type = ((JsonObject) jsonValue).getString("type");
                        if ("DATE".equals(type) && "publication_date".equals(name)) {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date date = df.parse(value);
                                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                m.addProperty(createProperty("publication_year", String.valueOf(localDate.getYear())));
                                m.addProperty(createProperty("publication_month", String.valueOf(localDate.getMonthValue())));
                                m.addProperty(createProperty("publication_day", String.valueOf(localDate.getDayOfMonth())));
                            } catch (ParseException e) {
                                LOGGER.log(Level.SEVERE, "Unable to parse publication date", e);
                            }
                        } else {
                            m.addProperty(createProperty(name, value));
                        }
                    }
                });
                metadata.put(id, m);
                partItems.getAndIncrement();
                if (partItems.get() == 5000) {
                    totalItems.getAndAdd(partItems.get());
                    partItems.getAndSet(0);
                    LOGGER.info("Loaded files: " + totalItems.get() + "/" + zipFile.size());
                }
                is.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Loading document failed", e);
            }
        }
        zipFile.close();
        long time = System.currentTimeMillis() - start;
        LOGGER.log(Level.INFO, "Loading metadata documents took: " + time + "ms");
        return metadata;
    }

    public Set<Document> load() throws IOException {

        long start = System.currentTimeMillis();

        Set<Document> documents = new HashSet<>();

        Map<String, Metadata> metadata = loadMetadata(Configuration.METADATA_ZIP_FILE);

        LOGGER.info("Loading documents from files please wait .....");
        ZipFile zipFile = new ZipFile(String.valueOf(Paths.get(Configuration.DATA_ZIP_FILE)));

        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        LOGGER.info("Total files in zip: " + zipFile.size());
        AtomicInteger totalItems = new AtomicInteger();
        AtomicInteger partItems = new AtomicInteger();
        while (entries.hasMoreElements()) {
            try {
                ZipEntry entry = entries.nextElement();
                InputStream is = zipFile.getInputStream(entry);

                String id = entry.getName().replace(".txt", "");
                g419.corpus.structure.Document ccl = ReaderFactory.get()
                        .getStreamReader("url", is, "ccl")
                        .nextDocument();
                is.close();

                Metadata meta = metadata.get(id);
                Document doc = new Document(id, meta);

                for(Annotation a : ccl.getAnnotations()){
                    if(a.getType().startsWith("nam")){
                        doc.getProperNames().add(createProperName(a.getType(), a.getText()));
                    }
                }

                for (Paragraph p : ccl.getParagraphs()) {
                    for (Sentence s : p.getSentences()) {

                        pl.clarin.chronocorpus.document.entity.Sentence sentence = new pl.clarin.chronocorpus.document.entity.Sentence();
                        for (Token t : s.getTokens()) {
                            t.getDisambTags().stream()
                                    .findFirst()
                                    .map(tag -> createWord(
                                            t.getOrth(),
                                            tag.getBase(),
                                            tag.getCtag(),
                                            PartOfSpeechMapper.getInstance().getPartOfSpeech(tag.getPos()),
                                            t.getNoSpaceAfter()))
                                    .ifPresent(sentence::addWord);
                        }
                        doc.addSentence(sentence);
                    }
                }
                documents.add(doc);
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
        zipFile.close();
        long time = System.currentTimeMillis() - start;
        LOGGER.info("Loading documents took: " + time + "ms");
        return documents;
    }
}
