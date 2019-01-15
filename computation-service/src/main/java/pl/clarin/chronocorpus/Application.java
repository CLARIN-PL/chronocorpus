package pl.clarin.chronocorpus;

import g419.corpus.io.reader.ReaderFactory;
import g419.corpus.structure.Paragraph;
import g419.corpus.structure.Sentence;
import g419.corpus.structure.Token;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Word;
import pl.clarin.chronocorpus.task.boundary.TaskDelegate;
import pl.clarin.chronocorpus.task.boundary.TaskRunner;

import javax.json.JsonObject;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class Application {

    public static void main(String... args) throws Exception {

        String concordanceJson = "{\"task\":\"concordance\",\"user\":\"username\",\"corpus\":[\"chronopress\"],\"params\":[" +
                "{\"name\":\"orth\",\"value\":\"gimnazjum\"}]}";

        DocumentStore.getInstance().initializeStore(documentsLoader());

        TaskDelegate taskDelegate = new TaskDelegate();
        taskDelegate.setTaskString(concordanceJson);

        TaskRunner taskRunner = new TaskRunner(taskDelegate);
        JsonObject result = taskRunner.doTask();

        System.out.println(result);
    }

    //TODO data loaded from folder add db
    public static List<Document> documentsLoader() throws Exception {

        List<Document> documents = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get("ccl/"))) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {

                        try {
                            InputStream is = new FileInputStream(path.toFile());
                            g419.corpus.structure.Document ccl = ReaderFactory.get().getStreamReader("url", is, "ccl")
                                    .nextDocument();

                            Document doc = new Document(UUID.randomUUID(), null);

                            for(Paragraph p : ccl.getParagraphs()){
                                for(Sentence s: p.getSentences()){
                                    pl.clarin.chronocorpus.document.entity.Sentence sentence = new pl.clarin.chronocorpus.document.entity.Sentence();
                                    for(Token t : s.getTokens()){
                                        t.getDisambTags().stream()
                                                .findFirst()
                                                .map(tag -> new Word(t.getOrth(), tag.getBase(), tag.getCtag()))
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

        return documents;
    }

}
