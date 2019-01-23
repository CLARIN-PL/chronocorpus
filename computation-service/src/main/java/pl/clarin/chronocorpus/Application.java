package pl.clarin.chronocorpus;

import g419.corpus.io.reader.ReaderFactory;
import g419.corpus.structure.Paragraph;
import g419.corpus.structure.Sentence;
import g419.corpus.structure.Token;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Metadata;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Word;
import pl.clarin.chronocorpus.task.boundary.TaskManager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    public Application() {
        if (DocumentStore.getInstance().hasNoStoredDocuments()) {
            try {
                DocumentStore.getInstance().setDocuments(documentsLoader());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String... args) {
        new Application().run();
    }

    public void run() {

        TaskManager.getInstance().submitTask(createConcordanceJson("123", "ludzie"));
        TaskManager.getInstance().submitTask(createConcordanceJson("546", "samolot"));
        TaskManager.getInstance().submitTask(createConcordanceJson("6543", "partie"));
        TaskManager.getInstance().submitTask(createConcordanceJson("14545", "czerwony"));
        TaskManager.getInstance().submitTask(createConcordanceJson("54555", "armia"));
        TaskManager.getInstance().submitTask(createConcordanceJson("4543", "robić"));
        TaskManager.getInstance().submitTask(createConcordanceJson("222123", "palic"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

          TaskManager.getInstance().submitTask(createConcordanceJson("1455555", "czerwony"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TaskManager.getInstance().submitTask(createConcordanceJson("1423455", "ludzie"));

    }

    public String createConcordanceJson(String id, String base) {
        return "" +
                "{" +
                "\"id\":\"" + id + "\"," +
                "\"task_type\":\"concordance\"," +
                "\"user\":\"username\"," +
                "\"corpus\":[\"chronopress\"]," +
                "\"metadata_filter\":[" +
                "{\"name\":\"publication_year\",\"value\":\"1945\"}]," +
//                "{\"name\":\"author\",\"value\":\"Janusz\"}]," +
                "\"publication\":[" +
//                "{\"name\":\"publication_mode\",\"value\":\"collection\"}," +
//                "{\"name\":\"publication_year\",\"value\":\"1946\"}," +
//                "{\"name\":\"publication_year\",\"value\":\"1945\"}]," +
//                "{\"name\":\"publication_mode\",\"value\":\"specific\"}," +
//                "{\"name\":\"publication_day\",\"value\":\"12\"}," +
//                "{\"name\":\"publication_month\",\"value\":\"5\"}]," +
                "{\"name\":\"publication_year\",\"value\":\"1945\"}]," +
                "\"params\":[" +
                "{\"name\":\"base\",\"value\":\"" + base + "\"}]" +
                "}";
    }

    //TODO move to class
    public Set<Document> documentsLoader() throws Exception {

        LOGGER.info("Loading documents from files please wait .....");
        long start = System.currentTimeMillis();
        Set<Document> documents = new HashSet<>();

        try (Stream<Path> paths = Files.walk(Paths.get("ccl/"))) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {

                        try {
                            InputStream is = new FileInputStream(path.toFile());
                            g419.corpus.structure.Document ccl = ReaderFactory.get().getStreamReader("url", is, "ccl")
                                    .nextDocument();


                            Metadata meta = new Metadata("chronopress", "apawłow", true);
                            meta.addProperty(new Property("publication_year", "1945"));

                            Document doc = new Document(UUID.randomUUID().toString(), meta);

                            for (Paragraph p : ccl.getParagraphs()) {
                                for (Sentence s : p.getSentences()) {
                                    pl.clarin.chronocorpus.document.entity.Sentence sentence = new pl.clarin.chronocorpus.document.entity.Sentence();
                                    for (Token t : s.getTokens()) {
                                        t.getDisambTags().stream()
                                                .findFirst()
                                                .map(tag -> new Word(t.getOrth(), tag.getBase(), tag.getCtag(), t.getNoSpaceAfter()))
                                                .ifPresent(sentence::addWord);
                                    }
                                    doc.addSentence(sentence);
                                }
                            }
                            documents.add(doc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
        long time = System.currentTimeMillis() - start;
        LOGGER.info("Loading documents took: "+ time +"ms");
        return documents;
    }

}
