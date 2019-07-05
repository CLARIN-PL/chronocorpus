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

    public List<pl.clarin.geocoder.Annotation> loadSingleDocumentAnnotations(g419.corpus.structure.Document ccl) throws IOException {
        List<pl.clarin.geocoder.Annotation> annotations = new ArrayList<>();

        LOGGER.info("Reading file ... ");
        if(ccl != null) {
            for (Annotation a : ccl.getAnnotations()) {
                if (
                        a.getType().startsWith("nam_loc") ||
                                a.getType().startsWith("nam_fac") ||
                                a.getType().startsWith("nam_num")
                ) {
                    pl.clarin.geocoder.Annotation ann = new pl.clarin.geocoder.Annotation(a.getSentence().getId(), a.getChannelIdx());
                    a.getTokenTokens().forEach(t -> {
                        Token tt = new Token(t.getOrth(), t.getDisambTag().getBase(), a.getType());
                        ann.addToken(tt);
                    });
                    annotations.add(ann);
                }
            }
        }
        return annotations;
    }

}
