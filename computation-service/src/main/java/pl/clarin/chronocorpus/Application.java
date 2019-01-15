package pl.clarin.chronocorpus;

import g419.corpus.io.reader.ReaderFactory;
import g419.corpus.structure.Paragraph;
import g419.corpus.structure.Sentence;
import g419.corpus.structure.Token;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.control.JetstreamDB;
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
import java.util.*;
import java.util.stream.Stream;

public class Application {

    public Application(){
        DocumentStore store = JetstreamDB.INSTANCE.root();
        if(store.getDocuments().isEmpty()){
            try {
                store.setDocuments(documentsLoader());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String... args){
        new Application().run();
    }

    public void run(){
        String concordanceJson = "{\"task\":\"concordance\",\"user\":\"username\",\"corpus\":[\"chronopress\"],\"params\":[" +
                "{\"name\":\"orth\",\"value\":\"gimnazjum\"}]}";

        TaskDelegate taskDelegate = new TaskDelegate();
        taskDelegate.setTaskString(concordanceJson);

        TaskRunner taskRunner = new TaskRunner(taskDelegate);
        JsonObject result = taskRunner.doTask();

        System.out.println(result);

    }

    public Set<Document> documentsLoader() throws Exception {

        System.out.println("Loading from files please wait .....");
        Set<Document> documents = new HashSet<>();

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
