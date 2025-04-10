package pl.clarin.chronocorpus.document.entity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.Serializable;
import java.util.Objects;

public class ProperName implements Serializable {

    private String type;
    private String value;
    private String base;
    private String geoString;

    public ProperName() {
    }

    public ProperName(String type, String base, String value, String geoString) {
        this.type = type;
        this.base = base;
        this.value = value;
        this.geoString = geoString;
    }

    public ProperName(ProperName pn) {
        this.type = pn.type;
        this.base = pn.base;
        this.value = pn.value;
        this.geoString = pn.geoString;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getGeoString() {
        return geoString;
    }

    public String getBase() {
        return base;
    }

    public double getLon() {
        return Double.parseDouble(geoString.split(";")[0]);
    }

    public double getLat() {
        return Double.parseDouble(geoString.split(";")[1]);
    }

    public String getSubType() {
        return geoString.split(";")[2];
    }

    public String getLocationName() {
        return geoString.split(";")[3].split(",")[0];
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder.add("type", type);
        builder.add("value", value);

        if (getGeoString() != null && !geoString.isEmpty()) {
            JsonObjectBuilder objBuilder = Json.createObjectBuilder();
            objBuilder.add("type", getSubType());
            objBuilder.add("base", base);
            objBuilder.add("name", getLocationName());
            objBuilder.add("lon", getLon());
            objBuilder.add("lat", getLat());

            builder.add("geo_location", objBuilder);
        }
        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProperName that = (ProperName) o;
        return Objects.equals(type, that.type) && Objects.equals(value, that.value) && Objects.equals(base, that.base) && Objects.equals(geoString, that.geoString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value, base, geoString);
    }

    @Override
    public String toString() {
        return "ProperName{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", base='" + base + '\'' +
                ", geoString='" + geoString + '\'' +
                '}';
    }
}
