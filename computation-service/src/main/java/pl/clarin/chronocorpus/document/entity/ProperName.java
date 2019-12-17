package pl.clarin.chronocorpus.document.entity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.Serializable;

public class ProperName implements Serializable {

    private String type;
    private String value;
    private String geoString;

    public ProperName() {
    }

    public ProperName(String type, String value, String geoString) {
        this.type = type;
        this.value = value;
        this.geoString = geoString;
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
        return geoString.split(";")[3];
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder.add("type", type);
        builder.add("value", value);

        if (getGeoString() != null) {
            JsonObjectBuilder objBuilder = Json.createObjectBuilder();
            objBuilder.add("type", getSubType());
            objBuilder.add("name", getLocationName());
            objBuilder.add("lon", getLon());
            objBuilder.add("lat", getLat());

            builder.add("geo_location", objBuilder);
        }
        return builder.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProperName)) return false;

        ProperName that = (ProperName) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
