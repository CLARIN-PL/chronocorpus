package pl.clarin.databuilder.control;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.Objects;

public class Geolocation implements Serializable {

    private String name;
    private String type;
    private String base;
    private int frequency;
    private double lon;
    private double lan;

    public Geolocation() {
    }

    public Geolocation(String name, String base, String type, int frequency) {
        this.name = name;
        this.type = type;
        this.frequency = frequency;
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                .add("name", name)
                .add("type", type)
                .add("base", base)
                .add("frequency", frequency)
                .add("lon", lon)
                .add("lan", lan)
                .build();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getType() {
        return type;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geolocation that = (Geolocation) o;
        return frequency == that.frequency && Double.compare(lon, that.lon) == 0 && Double.compare(lan, that.lan) == 0 && Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, frequency, lon, lan);
    }
}
