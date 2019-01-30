package pl.clarin.chronocorpus.document.entity;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class Metadata implements Serializable {

    private String corpusName;

    private String owner;

    private boolean publicAccess;

    private final List<Property> properties = new ArrayList<Property>();

    public Metadata(){
    }

    public Metadata(String corpusName, String owner, boolean publicAccess) {
        this.corpusName = corpusName;
        this.owner = owner;
        this.publicAccess = publicAccess;
    }

    public void addProperty(Property p){
        properties.add(p);
    }

    public String getCorpusName() {
        return corpusName;
    }

    public void setCorpusName(String corpusName) {
        this.corpusName = corpusName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(boolean publicAccess) {
        this.publicAccess = publicAccess;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public boolean matches(Set<Property> userProperties){
        //sprawdza tylko czy dany parametr jest zgodny
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
    private boolean matchByDatesCollection(List<Property> properties, List<Property> publication){
        if(!getPropertiesByName(publication, "publication_year").isEmpty() &&
                getPropertiesByName(publication, "publication_year").
                        stream()
                        .noneMatch(p -> p.includesIn(properties))){
            return false;
        }
        if(!getPropertiesByName(publication, "publication_month").isEmpty() &&
                getPropertiesByName(publication, "publication_month")
                        .stream()
                        .noneMatch(p -> p.includesIn(properties))){
            return false;
        }
        if(!getPropertiesByName(publication, "publication_day").isEmpty() &&
                getPropertiesByName(publication, "publication_day")
                        .stream()
                        .noneMatch(p -> p.includesIn(properties))){
            return false;
        }
        return true;
    }

    public List<Property> getPropertiesByName(List<Property> l, String name){
        return l.stream()
                .filter(property -> property.getName().equals(name))
                .collect(Collectors.toList());
    }

}
