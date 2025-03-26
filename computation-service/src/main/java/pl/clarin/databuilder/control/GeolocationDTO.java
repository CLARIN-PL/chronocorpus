package pl.clarin.databuilder.control;

public record GeolocationDTO(String objId, String name, String base,  String type, double lon, double lat) {

    public String  toGeoString(){
        return String.format("%s;%s;%s;%s", lon, lat, type, name);
    }
}
