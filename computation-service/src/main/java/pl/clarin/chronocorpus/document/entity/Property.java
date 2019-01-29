package pl.clarin.chronocorpus.document.entity;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.Serializable;
import java.util.List;

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

    public String getValueAsString() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public JsonObject getAsJson(){
        return Json.createObjectBuilder()
                .add("name", name)
                .add("value",value).build();
    }
    public boolean matches(Property p) {
        return this.name.equals(p.getName()) && getValueAsString().equals(p.getValueAsString());
    }
    public boolean includesIn(List<Property> propertyList){
        return propertyList.stream().anyMatch(p -> p.matches(this));
    }
}