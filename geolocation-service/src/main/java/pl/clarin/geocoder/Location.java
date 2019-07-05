package pl.clarin.geocoder;

import java.io.Serializable;

public class Location implements Serializable {

    private String name;
    private String type;
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
        return longitude + ";" + latitude + ";" + name;
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
