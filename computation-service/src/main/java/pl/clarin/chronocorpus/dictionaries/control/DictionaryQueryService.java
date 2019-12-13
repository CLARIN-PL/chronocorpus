package pl.clarin.chronocorpus.dictionaries.control;

import pl.clarin.chronocorpus.dictionaries.entity.Dictionary;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Property;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DictionaryQueryService {

    public static volatile DictionaryQueryService instance;

    private static List<String> propertyNames;

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
                .collect(Collectors.toList());
    }

    public List<String> findAllPropertyNames() {
        return propertyNames;
    }
}
