package pl.clarin.chronocorpus.document.control;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import pl.clarin.chronocorpus.Configuration;
import pl.clarin.chronocorpus.document.entity.Document;

import java.io.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentStore {

    private static final Logger LOGGER = Logger.getLogger(DocumentStore.class.getName());
    private static volatile DocumentStore instance;
    private Set<Document> documents = new HashSet<>();

    private DocumentStore() {
        super();
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
        this.documents = Collections.unmodifiableSet(documents);
        backup();
    }

    public boolean hasNoStoredDocuments() {
        return documents.isEmpty();
    }

    private void restore() {
        try {
            File f = new File(Configuration.DATA_STORE_FILE);
            if(f.exists() && !f.isDirectory()) {
                long start = System.currentTimeMillis();
                LOGGER.info("Restoring documents ...");
                Kryo kryo = new Kryo();
                Input in = new Input(new FileInputStream(f));
                Set<Document> aNewSet = kryo.readObject(in, HashSet.class);
                setDocuments(aNewSet);
                long time = System.currentTimeMillis() - start;
                LOGGER.info("Restoring documents took: "+ time +"ms");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Restoring document store failure", e);
        }
    }

    private void backup() {
        Kryo kryo = new Kryo();
        Output out = null;
        try {
            out = new Output(new FileOutputStream(Configuration.DATA_STORE_FILE));
            kryo.writeObject(out, documents);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Document store backup failure", e);
        } finally {
            if(out != null){
                out.close();
            }
        }
    }
}
