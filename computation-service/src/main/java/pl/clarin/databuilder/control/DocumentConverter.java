package pl.clarin.databuilder.control;

import pl.clarin.chronocorpus.document.control.PartOfSpeechMapper;
import pl.clarin.chronocorpus.document.entity.*;

import java.util.*;

public class DocumentConverter {
    private static Map<String, Token> wordCache = new HashMap<>();
    private static Map<String, ProperName> properNameCache = new HashMap<>();

    private static Token createWord(final String orth, final String base, final String ctag, final byte pos, final boolean spaceAfter) {
        return wordCache.computeIfAbsent(wordParametersAsString(orth, base, pos, spaceAfter), newParams ->
                new Token(orth, base, ctag, pos, spaceAfter));
    }

    private static ProperName createProperName(final String type, final String base, final String value, final String geoString) {
        return properNameCache.computeIfAbsent(properNameAsString(type, value), newParams ->
                new ProperName(type, base, value, geoString));
    }

    private static String wordParametersAsString(String orth, String base, int pos, boolean spaceAfter) {
        return orth + "_" + base + "_" + pos + "_" + spaceAfter;
    }

    private static String properNameAsString(String name, String value) {
        return name + "_" + value;
    }

    public static Document convert(Document d, DocumentDTO doc) {

        List<Sentence> sentences = new ArrayList<>();
        List<Sentence> toAdd = new ArrayList<>();

        for (SentenceDTO dto : doc.getSentences()) {
            sentences.add(new Sentence(dto.index(), dto.position().start(), dto.position().end()));
        }

        Iterator<Sentence> si = sentences.iterator();

        Sentence current = si.next();

        for (TokenDTO t : doc.getTokens()) {
            if (!(t.position().start() >= current.getSentenceStart() && t.position().end() <= current.getSentenceEnd())) {
                toAdd.add(current);
                current = si.next();
            }
            current.addWord(createWord(t.orth(), t.lemma(), t.partOfSpeech(), PartOfSpeechMapper.getInstance().getPosFromCTag(t.partOfSpeech()), t.ns()));
        }
        toAdd.add(current);

        toAdd.forEach(d::addSentence);

        doc.getEntities().forEach(e -> {
            Optional<GeolocationDTO> g =  doc.getGeolocation().stream()
                    .filter(gg -> gg.objId().equals(e.objid()))
                    .findFirst();

            d.getProperNames().add(
                    new ProperName(createProperName(e.type(),
                            g.map(GeolocationDTO::base).orElse(""), e.text(),
                            g.map(GeolocationDTO::toGeoString).orElse(""))));
        });
        return d;
    }
}
