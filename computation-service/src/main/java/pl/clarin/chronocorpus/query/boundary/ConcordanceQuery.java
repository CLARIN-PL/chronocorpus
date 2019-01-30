package pl.clarin.chronocorpus.query.boundary;

import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.task.entity.TaskType;

public class ConcordanceQuery extends Query {

    public ConcordanceQuery() {
        super(TaskType.concordance);
    }

    public ConcordanceQuery withBase(String base) {
        Property p = new Property("base", base);
        params.add(p.toJson());
        return this;
    }

    public ConcordanceQuery withOrth(String orth) {
        Property p = new Property("orth", orth);
        params.add(p.toJson());
        return this;
    }
}
