package pl.clarin.databuilder;

import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.databuilder.control.DocumentBuilderFileLoader;
import pl.clarin.databuilder.control.DocumentStoreBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class RunBuilder {

    public static void main(String... args) {
            try {

                String pathToMeta = "/home/tomasz/Projects/chronocorpus/ccl/meta.zip";
                String pathToData = "/home/tomasz/Projects/chronocorpus/ccl/test-json.zip";
                String out = "/home/tomasz/Projects/chronocorpus/";

                File metaFile = new File(pathToMeta);
                File dataFile = new File(pathToData);

                Set<Document> doc = DocumentBuilderFileLoader.getInstance().load(metaFile, dataFile);
                DocumentStoreBuilder.getInstance().saveCorpora(doc, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

