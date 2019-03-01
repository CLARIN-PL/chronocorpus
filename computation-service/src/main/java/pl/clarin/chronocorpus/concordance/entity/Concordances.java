package pl.clarin.chronocorpus.concordance.entity;


import org.javatuples.Pair;
import pl.clarin.chronocorpus.document.entity.Property;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.Set;

public class Concordances {

    private String documentId;
    private Set<Property> properties;
    private Set<Concordance> concordances;

    public Concordances(Pair<String, Set<Property>> doc, Set<Concordance> concordances) {
        this.documentId = doc.getValue0();
        this.properties = doc.getValue1();
        this.concordances = concordances;
    }

    public JsonObject toJson() {

        JsonArrayBuilder prop = Json.createArrayBuilder();
        properties.forEach(p -> prop.add(p.toJson()));

        JsonArrayBuilder conc = Json.createArrayBuilder();
        concordances.forEach(c -> conc.add(c.toJson()));

        return Json.createObjectBuilder()
                .add("document_id", documentId)
                .add("properties", prop)
                .add("concordances", conc)
                .build();
    }

}
