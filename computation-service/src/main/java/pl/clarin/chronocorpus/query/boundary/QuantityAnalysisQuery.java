package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationObject;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationType;
import pl.clarin.chronocorpus.quantityanalysis.entity.CalculationUnit;
import pl.clarin.chronocorpus.task.entity.TaskType;

public class QuantityAnalysisQuery extends Query {

    public static class Builder extends Query.Builder<QuantityAnalysisQuery.Builder> {

        @Override
        public QuantityAnalysisQuery.Builder getThis() {
            return this;
        }

        @Override
        public TaskType getTaskType() {
            return TaskType.quantity_analysis;
        }

        public QuantityAnalysisQuery.Builder partsOfSpeech(String pos) {
            Property p = new Property("parts_of_speech", pos);
            withQueryProperty(p);
            return this;
        }

        public QuantityAnalysisQuery.Builder calculationType(CalculationType calculationType) {
            Property p = new Property("calculation_type", calculationType.name());
            withQueryProperty(p);
            return this;
        }

        public QuantityAnalysisQuery.Builder calculationObject(CalculationObject calculationObject) {
            Property p = new Property("calculation_object", calculationObject.name());
            withQueryProperty(p);
            return this;
        }

        public QuantityAnalysisQuery.Builder calculationUnit(CalculationUnit calculationUnit) {
            Property p = new Property("calculation_unit", calculationUnit.name());
            withQueryProperty(p);
            return this;
        }

        public QuantityAnalysisQuery build() {
            return new QuantityAnalysisQuery(this);
        }
    }

    private QuantityAnalysisQuery(Builder builder) {
        super(builder);
    }

}
