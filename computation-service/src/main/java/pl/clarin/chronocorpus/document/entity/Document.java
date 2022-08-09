package pl.clarin.chronocorpus.document.entity;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import pl.clarin.chronocorpus.dictionaries.control.DictionaryQueryService;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Document implements Serializable {

    private int id;

    private Metadata metadata;

    private final List<Sentence> sentences = new ObjectArrayList<>();

    private final Map<String, Statistic> bases = new Object2ObjectOpenHashMap<>();
    private final Map<String, Statistic> orths = new Object2ObjectOpenHashMap<>();


    private final List<ProperName> properNames = new ObjectArrayList<>();

    public Document() {
    }

    public Document(int id, Metadata metadata) {
        this.id = id;
        this.metadata = metadata;
    }

    public void addSentence(Sentence s) {
        sentences.add(s);
        for (Token w : s.getTokens()) {
            if (!w.isInterp()) {
                map(w, w.getBase(), bases);
                map(w, w.getOrth(), orths);
            }
        }
    }

    private Map<String, Integer> getWordCloudByBase(){
        List<String> stop = DictionaryQueryService.getInstance().defaultStopList();
        Map<String, Integer> result = new HashMap<>();
        bases.entrySet().stream()
                .filter( e -> !stop.contains(e.getKey()))
                .forEach(e -> {
                    result.put(e.getKey(), e.getValue().sumAll());
                });
        return result;
    }

    private void map(Token w, String key, Map<String, Statistic> bases) {
        if (!bases.containsKey(key)) {
            Statistic s = new Statistic();
            s.addValue(w.getPos(), 1);
            bases.put(key, s);
        } else {
            Statistic old = bases.get(key);
            old.addValue(w.getPos(), 1);
            bases.replace(key, old);
        }
    }

    public int getId() {
        return id;
    }

    public JsonObject toJson() {

        JsonArrayBuilder pn = Json.createArrayBuilder();
        properNames.forEach(n -> pn.add(n.toJson()));

        JsonArrayBuilder wc = Json.createArrayBuilder();
        getWordCloudByBase().forEach((k, v) -> {
            wc.add(Json.createObjectBuilder()
                    .add("name", k)
                    .add("value", v));
        });

        return Json.createObjectBuilder()
                .add("id", id)
                .add("metadata", metadata.toJson())
                .add("text", toText())
                .add("proper_names", pn)
                .add("word_cloud", wc)
                .build();
    }

    public String toText() {
        return sentences.stream()
                .map(Sentence::getSentence)
                .collect(Collectors.joining());
    }


    public int findSubsequenceCountForOrthText(String find){
        Pattern PATTERN =
                Pattern.compile("("+find+"+)");

        Matcher countEmailMatcher = PATTERN.matcher(toText());

        int count = 0;
        while (countEmailMatcher.find()) {
            count++;
        }
        return count;
    }

    public int findSubsequenceCountForBaseText(String find){
        Pattern PATTERN =
                Pattern.compile("("+find+"+)");

        Matcher countEmailMatcher = PATTERN.matcher(toTextInBase().toLowerCase());

        int count = 0;
        while (countEmailMatcher.find()) {
            count++;
        }
        return count;
    }


    public String toTextInBase() {
        return sentences.stream()
                .map(Sentence::getSentenceInBaseForm)
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
