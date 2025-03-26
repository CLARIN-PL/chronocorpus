package pl.clarin.databuilder.control;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import pl.clarin.chronocorpus.document.control.DocumentFileLoader;
import pl.clarin.chronocorpus.document.entity.Metadata;
import pl.clarin.chronocorpus.document.entity.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DocumentBuilderFileLoader {

    private static final Logger LOGGER = Logger.getLogger(DocumentBuilderFileLoader.class.getName());

    private static volatile DocumentBuilderFileLoader instance;

    public static DocumentBuilderFileLoader getInstance() {
        if (instance == null) {
            synchronized (DocumentBuilderFileLoader.class) {
                if (instance == null) {
                    instance = new DocumentBuilderFileLoader();
                }
            }
        }
        return instance;
    }

    public Set<Document> load(File metadataFile, File dataFile) throws IOException {

        long start = System.currentTimeMillis();

        Set<Document> documents = new ObjectOpenHashSet<>();

        LOGGER.info("Loading metadata please wait .....");
        Map<String, Metadata> metadata = DocumentFileLoader.getInstance().loadMetadata(metadataFile.toString());

        LOGGER.info("Loaded " + metadata.size() + " metadata information");
        LOGGER.info("Loading documents from files please wait .....");

        ZipFile zipFile = new ZipFile(dataFile);
        LOGGER.info("Processing zip file ...");

        AtomicInteger documentId = new AtomicInteger(1);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            try {
                ZipEntry entry = entries.nextElement();
                InputStream is = zipFile.getInputStream(entry);
                DocumentDTO dto = ClarinJsonReader.read(is);
                String id = entry.getName().replace(".json","").trim();
                if(metadata.containsKey(id)) {
                    Document document = DocumentConverter.convert(
                            new Document(documentId.getAndIncrement(), metadata.get(id)), dto);
                    documents.add(document);
                }
                is.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE,"Loading document failed", e);
            }
        }
        zipFile.close();
        long time = System.currentTimeMillis() - start;
        LOGGER.info("Loading documents took: " + time + "ms");

        return documents;
    }
}
