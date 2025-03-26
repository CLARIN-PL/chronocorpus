package pl.clarin.chronocorpus.document.control;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import pl.clarin.chronocorpus.Configuration;
import pl.clarin.chronocorpus.document.entity.Document;

import java.io.*;
import java.nio.file.Files;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentStore {

    private static final Logger LOGGER = Logger.getLogger(DocumentStore.class.getName());
    private static volatile DocumentStore instance;
    private final Kryo kryo = new Kryo();
    private final File f = new File(Configuration.DATA_STORE_FILE);

    private Set<Document> documents = new ObjectOpenHashSet<>();

    private DocumentStore() {
        super();

        kryo.register(ObjectArrayList.class);
        kryo.register(Object2ObjectOpenHashMap.class);
        kryo.register(ObjectOpenHashSet.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Document.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Sentence.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Statistic.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.WordPart.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.ProperName.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Property.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Metadata.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Token.class);
        restore();
    }

    public static DocumentStore getInstance() {
        if (instance == null) {
            synchronized (DocumentStore.class) {
                if (instance == null) {
                    instance = new DocumentStore();
                }
            }
        }
        return instance;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
        if(!checkBackupFileExists()) {
            backup();
        }
    }

    public boolean hasNoStoredDocuments() {
        return documents.isEmpty();
    }

    private boolean checkBackupFileExists() {
        return f.exists() && !f.isDirectory();
    }

    private void restore() {
        if (checkBackupFileExists()) {
            long start = System.currentTimeMillis();
            LOGGER.info("Restoring documents ...");

            try (Input in = new Input(Files.newInputStream(f.toPath()))) {
                Object aNewSet = kryo.readClassAndObject(in);

                if (aNewSet instanceof ObjectOpenHashSet) {
                    setDocuments((Set<Document>) aNewSet);
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Restoring document store failure", e);
            }

            long time = System.currentTimeMillis() - start;
            LOGGER.info("Restoring documents took: " + time + "ms");
        }
    }

    private void backup() {

        try (Output out = new Output(new FileOutputStream(Configuration.DATA_STORE_FILE))) {
            LOGGER.info("Backing up documents ...");
            long start = System.currentTimeMillis();
            kryo.writeClassAndObject(out, documents);
            out.flush();
            long time = System.currentTimeMillis() - start;
            LOGGER.info("Backing up documents took: " + time + "ms");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Document store backup failure", e);
        }
    }
}
