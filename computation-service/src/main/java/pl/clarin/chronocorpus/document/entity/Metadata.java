package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
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

    public boolean matches(List<Property> prop){
        return true;
    }
}
