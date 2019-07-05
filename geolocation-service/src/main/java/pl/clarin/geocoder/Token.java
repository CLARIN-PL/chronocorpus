package pl.clarin.geocoder;

public class Token {

    private String orth;
    private String base;
    private String type;

    public Token(String orth, String base, String type) {
        this.orth = orth;
        this.base = base;
        this.type = type;
    }

    public String getOrth() {
        return orth;
    }

    public String getBase() {
        return base;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Token{" +
                "orth='" + orth + '\'' +
                ", base='" + base + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
