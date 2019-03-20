package pl.clarin.chronocorpus.document.entity;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Document implements Serializable {

    private String id;

    private Metadata metadata;

    private List<Sentence> sentences = new ArrayList<>();

    private Map<String, Statistic> bases = new HashMap<>();
    private Map<String, Statistic> orths = new HashMap<>();

    private List<ProperName> properNames = new ArrayList<>();

    public Document() {
    }

    public Document(String id, Metadata metadata) {
        this.id = id;
        this.metadata = metadata;
    }

    public void addSentence(Sentence s) {
        sentences.add(s);
        for (Word w : s.getWords()) {
            if (!w.getCtag().equals("interp")) {
                map(w, w.getBase(), bases);
                map(w, w.getOrth(), orths);
            }
        }
    }

    private void map(Word w, String key, Map<String, Statistic> bases) {
        if (!bases.containsKey(key)) {
            Statistic s = new Statistic();
            s.addValue(w.getPos(),1);
            bases.put(key, s);
        } else {
            Statistic old = bases.get(key);
            old.addValue(w.getPos(),1);
            bases.replace(key, old);
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
        return bases.containsKey(word);

    }

    public boolean isOrthIn(String word) {
        return orths.containsKey(word);

    }

    public Map<String, Statistic> documentBaseFrequency() {
        return bases;
    }

    public Map<String, Statistic> documentOrthFrequency() {
        return orths;
    }

    public List<ProperName> getProperNames() {
        return properNames;
    }
}
