package pl.clarin.databuilder.control;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import pl.clarin.chronocorpus.Configuration;
import pl.clarin.chronocorpus.document.entity.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentStoreBuilder {

    private static final Logger LOGGER = Logger.getLogger(DocumentStoreBuilder.class.getName());
    private static volatile DocumentStoreBuilder instance;
    private final Kryo kryo = new Kryo();

    private Set<Document> documents = new ObjectOpenHashSet<>();

    private DocumentStoreBuilder() {
        super();

        kryo.register(ObjectArrayList.class);
        kryo.register(Object2ObjectOpenHashMap.class);
        kryo.register(ObjectOpenHashSet.class);
        kryo.register(Document.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Sentence.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Statistic.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.WordPart.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.ProperName.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Property.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Metadata.class);
        kryo.register(pl.clarin.chronocorpus.document.entity.Token.class);
    }

    public static DocumentStoreBuilder getInstance() {
        if (instance == null) {
            synchronized (DocumentStoreBuilder.class) {
                if (instance == null) {
                    instance = new DocumentStoreBuilder();
                }
            }
        }
        return instance;
    }

    public void saveCorpora(Set<Document> documents,  String path) {

        try (Output out = new Output(new FileOutputStream(path+"store.dat"))) {
            LOGGER.info("Serializing documents to file ...");
            long start = System.currentTimeMillis();
            kryo.writeClassAndObject(out, documents);
            out.flush();
            long time = System.currentTimeMillis() - start;
            LOGGER.info("Serializing documents took: " + time + "ms");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Serializing documents failure", e);
        }

    }

    public Set<Document> getDocuments() {
        return documents;
    }

}
