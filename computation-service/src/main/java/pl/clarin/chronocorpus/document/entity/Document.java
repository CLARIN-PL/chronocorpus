package pl.clarin.chronocorpus.document.entity;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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


}
