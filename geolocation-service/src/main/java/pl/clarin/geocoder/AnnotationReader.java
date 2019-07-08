package pl.clarin.geocoder;

import g419.corpus.io.reader.ReaderFactory;
import g419.corpus.structure.Annotation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnnotationReader {

    private static final Logger LOGGER = Logger.getLogger(AnnotationReader.class.getName());

    private static volatile AnnotationReader instance;

    public static AnnotationReader getInstance() {
        if (instance == null) {
            synchronized (AnnotationReader.class) {
                if (instance == null) {
                    instance = new AnnotationReader();
                }
            }
        }
        return instance;
    }

    public g419.corpus.structure.Document loadDocument(InputStream is) {
        try {
            g419.corpus.structure.Document ccl = ReaderFactory.get()
                    .getStreamReader("url", is, "ccl")
                    .nextDocument();
            is.close();
            return ccl;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error unable to load file", e);
        }
        return null;
    }

}
