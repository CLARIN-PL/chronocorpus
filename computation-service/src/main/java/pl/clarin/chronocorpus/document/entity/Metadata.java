package pl.clarin.chronocorpus.document.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Metadata implements Serializable {

    private String corpusName;

    private String owner;

    private boolean publicAccess;

    private final List<Property> properties = new ObjectArrayList<>();;

    public Metadata(){
    }

    public Metadata(String corpusName, String owner, boolean publicAccess) {
        this.corpusName = corpusName;
        this.owner = owner;
        this.publicAccess = publicAccess;
    }

    public String getProperty(String name){
        return properties.stream()
                .filter(p -> p.getName().equals(name))
                .map(Property::getValueAsString)
                .findFirst().orElse(null);
    }
    public void addProperty(Property p){
        properties.add(p);
    }

    public boolean isPublicAccess() {
        return publicAccess;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public boolean matches(Set<Property> userProperties){
        for (Property p : userProperties)
            if(this.getProperties().stream()
                    .noneMatch(tp -> tp.matches(p))) return false;
        return true;
    }

    public JsonObject toJson(){
        JsonArrayBuilder props = Json.createArrayBuilder();
        properties.forEach(p -> props.add(p.toJson()));
        return Json.createObjectBuilder()
                .add("corpus_name", corpusName)
                .add("is_public_access", isPublicAccess())
                .add("owner",owner)
                .add("properties", props)
                .build();
    }

}
