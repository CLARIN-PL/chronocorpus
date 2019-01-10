package pl.clarin.chronocorpus.document.entity;

public abstract class Property {

    private final String name;

    public abstract String getValueAsString();

    public Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
