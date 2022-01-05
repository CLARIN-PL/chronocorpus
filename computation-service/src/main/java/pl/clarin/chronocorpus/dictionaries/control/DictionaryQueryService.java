package pl.clarin.chronocorpus.dictionaries.control;

import pl.clarin.chronocorpus.Configuration;
import pl.clarin.chronocorpus.dictionaries.entity.Dictionary;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Property;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DictionaryQueryService {

    public static volatile DictionaryQueryService instance;

    private static List<String> propertyNames;
    private static Map<String, List<String>> semanticLists;

    private DictionaryQueryService() {
        propertyNames = DocumentStore.getInstance()
                .getDocuments()
                .stream()
                .flatMap(d -> d.getMetadata().getProperties().stream())
                .map(Property::getName)
                .collect(Collectors.toSet())
                .stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        semanticLists = loadSemanticLists();
    }

    public static DictionaryQueryService getInstance() {
        if (instance == null) {
            synchronized (DictionaryQueryService.class) {
                if (instance == null) {
                    instance = new DictionaryQueryService();
                }
            }
        }
        return instance;
    }

    public List<Dictionary> findAllPartsOfSpeech() {
        return Stream.of(
                new Dictionary(0, "unknown"),
                new Dictionary(1, "verb"),
                new Dictionary(2, "noun"),
                new Dictionary(3, "adverb"),
                new Dictionary(4, "adjective"))
                .collect(Collectors.toList());
    }

    public List<Dictionary> findExpositions() {
        return Stream.of(
                new Dictionary(1, "first page"),
                new Dictionary(2, "middle page"),
                new Dictionary(3, "last page"))
                .collect(Collectors.toList());
    }

    public List<String> findByPropertyName(String property) {
        return DocumentStore.getInstance()
                .getDocuments()
                .stream()
                .flatMap(d -> d.getMetadata().getProperties().stream())
                .filter(p -> p.getName().equals(property))
                .map(Property::getValue)
                .collect(Collectors.toSet())
                .stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    public List<String> defaultStopList(){
        return Stream.of("w", "i", "to", "z", "na", "że", "po", "pod", "za",
                "który", "być", "się", "nie", "do", "o", "on", "ten", "a", "też")
                .sorted()
                .collect(Collectors.toList());
    }

    private Map<String, List<String>> loadSemanticLists(){
        Map<String, List<String>> result = new HashMap<>();
        try (Stream<Path> walk = Files.walk(Paths.get(Configuration.SEMANTIC_LISTS_DIR))) {
             walk.filter(Files::isRegularFile).forEach(f -> {
                 String key = f.getFileName().toString().replace(".txt","");
                 List<String> value = new ArrayList<>();
                 try (Stream<String> lines = Files.lines(f)) {
                     value = lines.collect(Collectors.toList());
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 result.put(key, value);
             });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public Set<String> getSemanticListNames(){
        return semanticLists.keySet();
    }
    public List<String> getSemanticListByName(String name){
        return semanticLists.get(name);
    }

    public List<String> findAllPropertyNames() {
        return propertyNames;
    }
}
