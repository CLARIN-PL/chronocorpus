package pl.clarin.chronocorpus.geographicalpropernames.entity;

import javax.json.Json;
import javax.json.JsonObject;

public class GeoProperty {

    private String name;
    private String type;
    private int frequency;
    private double lon;
    private double lan;

    public GeoProperty(String name, String type, int frequency) {
        this.name = name;
        this.type = type;
        this.frequency = frequency;
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                .add("name", name)
                .add("type", type)
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
}
