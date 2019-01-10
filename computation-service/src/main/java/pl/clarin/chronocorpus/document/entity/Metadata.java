package pl.clarin.chronocorpus.document.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Metadata {

    private String corpusName;

    private String owner;

    private boolean publicAccess;

    private Date publicationDate;

    private final List<Property> properties = new ArrayList<Property>();

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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<Property> getProperties() {
        return properties;
    }
}
