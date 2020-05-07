package pl.clarin.geocoder.service;

import g419.corpus.io.writer.AbstractDocumentWriter;
import g419.corpus.io.writer.WriterFactory;
import g419.corpus.structure.Document;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnnotationWriter {

    private static final Logger LOGGER = Logger.getLogger(AnnotationWriter.class.getName());

    private static volatile AnnotationWriter instance;

    public static AnnotationWriter getInstance() {
        if (instance == null) {
            synchronized (AnnotationWriter.class) {
                if (instance == null) {
                    instance = new AnnotationWriter();
                }
            }
        }
        return instance;
    }

    public void writeDocument(Document doc, Path out){

        try (OutputStream os = Files.newOutputStream(out)) {
            WriterFactory wf = WriterFactory.get();
            AbstractDocumentWriter writer = wf.getStreamWriter(os, "ccl");
            writer.writeDocument(doc);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error writing file", e);
        }
    }
}
