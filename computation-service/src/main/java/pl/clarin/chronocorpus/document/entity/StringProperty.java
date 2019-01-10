package pl.clarin.chronocorpus.document.entity;

public class StringProperty extends Property {

    private String value;

    public StringProperty(String name, String value) {
        super(name);
        this.value = value;
    }

    public String getValueAsString() {
        return value;
    }

    public String getValue() {
        return value;
    }
}
