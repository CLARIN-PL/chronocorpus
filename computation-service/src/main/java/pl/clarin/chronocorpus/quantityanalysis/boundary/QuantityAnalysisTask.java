package pl.clarin.chronocorpus.quantityanalysis.boundary;

import pl.clarin.chronocorpus.administration.control.StatisticsQueryService;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.geographicalpropernames.control.GeographicalQueryService;
import pl.clarin.chronocorpus.quantityanalysis.control.QuantityAnalysisQueryService;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationObject;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationType;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationUnit;
import pl.clarin.chronocorpus.task.entity.Task;

import javax.json.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import pl.clarin.chronocorpus.Progress;

public class QuantityAnalysisTask extends Task {

    public QuantityAnalysisTask(JsonObject json) {
        super(json);
    }

    private List<Byte> findPartsOfSpeech() {
        List<Byte> list = new ArrayList<>();
        Optional<String> l = queryParameters.stream()
                .filter(p -> p.getName().equals("parts_of_speech"))
                .map(Property::getValueAsString)
                .findFirst();
        if (l.isPresent()) {
            list = Arrays.stream(l.get().split(";"))
                    .map(Byte::parseByte)
                    .collect(Collectors.toList());
        }
        return list;
    }

    private Optional<CalculationType> findCalculationType() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("calculation_type"))
                .map(p -> CalculationType.valueOf(p.getValue()))
                .findFirst();
    }

    private Optional<CalculationObject> findCalculationObject() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("calculation_object"))
                .map(p -> CalculationObject.valueOf(p.getValue()))
                .findFirst();
    }

    private Optional<CalculationUnit> findCalculationUnit() {
        return queryParameters.stream()
                .filter(p -> p.getName().equals("calculation_unit"))
                .map(p -> CalculationUnit.valueOf(p.getValue()))
                .findFirst();
    }

    @Override
    public JsonObject doTask(Progress pr) {
        JsonArrayBuilder anal = Json.createArrayBuilder();

        Optional<CalculationUnit> unit  =findCalculationUnit();
        Optional<CalculationObject> object  = findCalculationObject();
        Optional<CalculationType> type  =findCalculationType();

        if(unit.isPresent() && object.isPresent() && type.isPresent()){
            JsonObject r = QuantityAnalysisQueryService.getInstance()
                    .calculate(findPartsOfSpeech(), unit.get(), object.get(), type.get(), metadata);
            anal.add(r);
        }

        JsonObjectBuilder json = Json.createObjectBuilder()
                .add("task_id", id)
                .add("rows", anal);

        StatisticsQueryService.getInstance().updateQuantityAnalysisQueryCount();
        return json.build();
    }
}
