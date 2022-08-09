package pl.clarin.chronocorpus.document.entity;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.Arrays;

public class Property implements Serializable {

    private String name;
    private String value;

    public Property(){
    }

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Property(String name, int value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public Property(String name, boolean value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public String getValueAsString() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                .add("name", name)
                .add("value",value).build();
    }

    public boolean matches(Property p) {
        if(this.name.equals(p.getName())){
            if(p.getValueAsString().contains(";")){
                return Arrays.asList(p.getValueAsString().split(";")).contains(getValueAsString());
            }
            return getValueAsString().equals(p.getValueAsString());
        }
        return false;
    }

}