package pl.clarin.chronocorpus.quantityanalysis.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Document;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Word;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationObject;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationType;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationUnit;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.*;
import java.util.stream.Collectors;

public class QuantityAnalysisQueryService {

    public static volatile QuantityAnalysisQueryService instance;

    private QuantityAnalysisQueryService() {
    }

    public static QuantityAnalysisQueryService getInstance() {
        if (instance == null) {
            synchronized (DocumentStore.class) {
                if (instance == null) {
                    instance = new QuantityAnalysisQueryService();
                }
            }
        }
        return instance;
    }

    public List<Long> getCalculateSentenceLengths(CalculationUnit unit, Set<Property> metadata) {
        List<Long> count = new ArrayList<>();
        for (Document d : DocumentStore.getInstance().getDocuments()) {
            if (d.getMetadata().matches(metadata)) {
                d.getSentences().forEach(s -> {
                    if (CalculationUnit.word.equals(unit)) {
                        count.add(s.getSentenceWordCount());
                    }
                    if (CalculationUnit.letter.equals(unit)) {
                        count.add(s.getSentenceLetterCount());
                    }
                    if (CalculationUnit.syllable.equals(unit)) {
                        count.add(s.getSentenceSyllabeCount());
                    }
                    if (CalculationUnit.phoneme.equals(unit)) {
                        count.add(s.getSentencePhonemeCount());
                    }
                });

            }
        }
        return count;
    }

    public List<Long> getCalculateWordLengths(List<Byte> pos, CalculationUnit unit, Set<Property> metadata) {
        List<Long> count = new ArrayList<>();
        for (Document d : DocumentStore.getInstance().getDocuments()) {
            if (d.getMetadata().matches(metadata)) {
                d.getSentences().forEach(s -> {
                    for (Word w : s.getWords()) {
                        if (!"interp".equals(w.getCtag())) {
                            if (!pos.isEmpty()) {
                                if (pos.contains(w.getPos())) {
                                    if (CalculationUnit.letter.equals(unit)) {
                                        count.add((long) w.getOrth().length());
                                    }
                                    if (CalculationUnit.phoneme.equals(unit)) {
                                        count.add((long) w.getPhonemeCount());
                                    }
                                    if (CalculationUnit.syllable.equals(unit)) {
                                        count.add((long) w.getSyllableCount());
                                    }
                                }
                            } else {
                                if (CalculationUnit.letter.equals(unit)) {
                                    count.add((long) w.getOrth().length());
                                }
                                if (CalculationUnit.phoneme.equals(unit)) {
                                    count.add((long) w.getPhonemeCount());
                                }
                                if (CalculationUnit.syllable.equals(unit)) {
                                    count.add((long) w.getSyllableCount());
                                }
                            }
                        }

                    }
                });
            }
        }
        return count;
    }


    public JsonObject calculate(List<Byte> pos, CalculationUnit calculationUnit, CalculationObject calculationObject,
                                CalculationType calculationType, Set<Property> metadata) {

        AverageLongCalulator cal = null;
        Map<Long, Long> chart = null;

        if (CalculationObject.sentence.equals(calculationObject) && CalculationType.average.equals(calculationType)) {
            List<Long> lengths = getCalculateSentenceLengths(calculationUnit, metadata);

            cal = lengths.stream().collect(AverageLongCalulator::new,
                    AverageLongCalulator::accept, AverageLongCalulator::combine);

            chart = frequencyCalculations(lengths);
        }

        if (CalculationObject.word.equals(calculationObject) && CalculationType.average.equals(calculationType)) {
            List<Long> lengths = getCalculateWordLengths(pos, calculationUnit, metadata);

            cal = lengths.stream().collect(AverageLongCalulator::new,
                    AverageLongCalulator::accept, AverageLongCalulator::combine);

            chart = frequencyCalculations(lengths);
        }

        if (CalculationObject.word.equals(calculationObject) && CalculationType.zipf_histogram.equals(calculationType)) {
            List<Long> lengths = getCalculateWordLengths(pos, calculationUnit, metadata);

            Map<Long, Long> freq = frequencyCalculations(lengths);
            chart = frequencyCalculations(freq.values());
        }

        JsonObjectBuilder j = Json.createObjectBuilder();
        j.add("calculation_object", calculationObject.name());
        j.add("calculation_type", calculationType.name());
        j.add("calculation_unit", calculationUnit.name());
        if (cal != null) {
            j.add("average_length", cal.getAverage());
            j.add("standard_deviation", cal.getStandardDeviation());
            j.add("coefficient_of_variation", cal.getCoefficientOfVariation());
            j.add("skewness", cal.getSkewness());
            j.add("kurtosis", cal.getKurtoze());
        }

        JsonArrayBuilder chartArray = Json.createArrayBuilder();

        if (chart != null)
            chart.forEach((k, v) -> chartArray.add(Json.createObjectBuilder().add(k.toString(), v)));

        j.add("chart", chartArray);
        return j.build();

    }

    public Map<Long, Long> frequencyCalculations(final Collection<Long> list) {
        Map<Long, Long> map = list.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        return sortByLongKey(map);
    }

    public static Map<Long, Long> sortByLongKey(Map<Long, Long> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

}
