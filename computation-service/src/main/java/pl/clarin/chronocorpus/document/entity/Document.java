package pl.clarin.chronocorpus.document.entity;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

public class Document implements Serializable {

    private String id;

    private Metadata metadata;

    private List<Sentence> sentences = new ArrayList<>();

    private Set<String> bases = new HashSet<>();
    private Set<String> orths = new HashSet<>();

    public Document() {
    }

    public Document(String id, Metadata metadata) {
        this.id = id;
        this.metadata = metadata;
    }

    public void addSentence(Sentence s) {
        sentences.add(s);
        for (Word w : s.getWords()) {
            if(!w.getCtag().equals("interp")) {
                bases.add(w.getBase());
                orths.add(w.getOrth());
            }
        }

    }

    public String getId() {
        return id;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("metadata", metadata.toJson())
                .add("text", toText()).build();
    }

    private String toText() {
        return sentences.stream()
                .map(Sentence::getSentence)
                .collect(Collectors.joining());
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public boolean isBaseIn(String word) {
        return bases.contains(word);

    }

    public boolean isOrthIn(String word) {
        return orths.contains(word);

    }

    public Map<String, Long> documentBaseFrequency(Set<String> stopList){
        return sentences.stream()
                .flatMap(m -> m.sentenceBaseFrequency(stopList).entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey, summingLong(Map.Entry::getValue)));
    }

    public Map<String, Long> documentOrthFrequency(Set<String> stopList){
        return sentences.stream()
                .flatMap(m -> m.sentenceOrthFrequency(stopList).entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey, summingLong(Map.Entry::getValue)));
    }

}
