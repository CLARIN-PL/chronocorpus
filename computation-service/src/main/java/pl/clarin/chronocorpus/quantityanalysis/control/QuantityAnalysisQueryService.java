package pl.clarin.chronocorpus.quantityanalysis.control;

import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Token;
import pl.clarin.chronocorpus.frequency.control.FrequencyQueryService;
import pl.clarin.chronocorpus.frequency.entity.FrequencyItem;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationObject;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationType;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationUnit;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
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
        return DocumentStore.getInstance().getDocuments()
                .parallelStream()
                .filter(d -> d.getMetadata().matches(metadata))
                .flatMap(d -> d.getSentences().stream())
                .map(s -> getSentenceCount(s, unit))
                .collect(Collectors.toList());
    }

    public List<Long> getCalculateWordLengths(List<Byte> pos, CalculationUnit unit, Set<Property> metadata) {
        return DocumentStore.getInstance().getDocuments()
                .parallelStream()
                .filter(d -> d.getMetadata().matches(metadata))
                .flatMap(d -> d.getSentences().stream())
                .flatMap(s -> s.getTokens().stream())
                .filter(getTokenPredicate(pos))
                .map(w -> getWordCount(w, unit))
                .collect(Collectors.toList());
    }

    private Predicate<Token> getTokenPredicate(List<Byte> pos) {
        if (pos.isEmpty()) {
            return w -> !Objects.isNull(w) && !w.isInterp();
        }
        return w -> !Objects.isNull(w) && !w.isInterp() && pos.contains(w.getPos());
    }

    private Long getSentenceCount(Sentence s, CalculationUnit unit) {
        switch (unit) {
            case word:
                return s.getSentenceWordCount();
            case letter:
                return s.getSentenceLetterCount();
            case phoneme:
                return s.getSentencePhonemeCount();
            case syllable:
                return s.getSentenceSyllableCount();
            default:
                return 0L;
        }
    }

    private Long getWordCount(Token t, CalculationUnit unit) {
        switch (unit) {
            case letter:
                return (long) t.getOrth().length();
            case syllable:
                return (long) t.getSyllableCount();
            case phoneme:
                return (long) t.getPhonemeCount();
            default:
                return 0L;
        }
    }

    public JsonObject calculate(List<Byte> pos, CalculationUnit calculationUnit, CalculationObject calculationObject,
                                CalculationType calculationType, Set<Property> metadata) {

        AverageLongCalculator cal = null;
        Map<Long, Long> chart = null;

        if (CalculationObject.sentence.equals(calculationObject) && CalculationType.average.equals(calculationType)) {
            List<Long> lengths = getCalculateSentenceLengths(calculationUnit, metadata);

            cal = lengths.stream().collect(AverageLongCalculator::new,
                    AverageLongCalculator::accept, AverageLongCalculator::combine);

            chart = frequencyCalculations(lengths);
        }

        if (CalculationObject.word.equals(calculationObject) && CalculationType.average.equals(calculationType)) {
            List<Long> lengths = getCalculateWordLengths(pos, calculationUnit, metadata);

            cal = lengths.stream().collect(AverageLongCalculator::new,
                    AverageLongCalculator::accept, AverageLongCalculator::combine);

            chart = frequencyCalculations(lengths);
        }

        if (CalculationObject.word.equals(calculationObject) && CalculationType.zipf_histogram.equals(calculationType)) {

            Map<FrequencyItem, Integer> frq = FrequencyQueryService.getInstance().calculateFrequencyByBase(metadata);

            Map<Long, Long> calc = frq.values().stream()
                    .map(Long::valueOf)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            chart = sortByLongKey(calc);
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
            chart.forEach((k, v) ->{
                chartArray.add(Json.createObjectBuilder().add(k.toString(), v));

            });

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
