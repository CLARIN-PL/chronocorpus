package pl.clarin.geocoder;

import java.io.Serializable;
import java.util.Arrays;

public class Location implements Serializable {

    private String name;
    private String type;
    private String alternateNames;
    private double latitude;
    private double longitude;
    private double importance;

    public Location() {
    }

    public Location(String name, String type, double latitude, double longitude, double importance) {
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.importance = importance;
    }

    public Location(String name, String alternateNames, String type, double latitude, double longitude) {
        this.name = name;
        this.type = type;
        this.alternateNames = alternateNames;
        this.latitude = latitude;
        this.longitude = longitude;
        this.importance = 100;
    }

    public String getAlternateNames() {
        return alternateNames;
    }

    public boolean isStringInAlternateNames(String text){
        return Arrays.asList(alternateNames.toLowerCase().split(",")).contains(text.toLowerCase());
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getImportance() {
        return importance;
    }

    public String toPropertyValue() {
        return longitude + ";" + latitude + ";"+type+";" + name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", importance=" + importance +
                '}';
    }
}
