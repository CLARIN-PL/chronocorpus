package pl.clarin.databuilder.control;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;

public class ClarinJsonReader {
    public static DocumentDTO read(InputStream is) {
        JsonReader reader = Json.createReader(is);
        JsonObject obj = reader.readObject();

        var filename = obj.getString("filename");
        var text = obj.getString("text");
        var spans = obj.getJsonObject("spans");
        var records = obj.getJsonObject("records");
        var tokens = obj.getJsonObject("tokens");

        DocumentDTO d = new DocumentDTO(filename, text);

        spans.getJsonArray("sentence").forEach(json -> {
            if (json instanceof JsonObject sentence) {
                PositionDTO pp = new PositionDTO(sentence.getInt("start"), sentence.getInt("stop"));
                SentenceDTO sentenceDTO = new SentenceDTO(sentence.getInt("id"), pp);
                d.getSentences().add(sentenceDTO);
            }
        });

        tokens.getJsonArray("default").forEach(json -> {
            if (json instanceof JsonObject o) {
                JsonArray l = o.getJsonArray("lexemes");
                JsonObject f = l.getJsonObject(0);
                PositionDTO pp = new PositionDTO(o.getInt("start"), o.getInt("stop"));
                String orth = text.substring(o.getInt("start"), o.getInt("stop"));
                boolean ns = false;
                int space = o.getInt("stop");
                if(space <= text.length()-1){
                    if(text.charAt(space) != ' '){
                        ns = true;
                    }
                }

                d.addToken(new TokenDTO(orth, f.getString("lemma"), f.getString("pos"), pp, ns));
            }
        });

        spans.getJsonArray("ner").forEach(json -> {
            JsonObject o = (JsonObject) json;
            PositionDTO pp = new PositionDTO(o.getInt("start"), o.getInt("stop"));
            String t = text.substring(o.getInt("start"), o.getInt("stop"));
            d.addEntity(new EntityDTO(o.getString("id"), t, "", o.getString("type"), pp));
        });

        records.getJsonArray("geolocation").forEach(json -> {
            JsonObject o = (JsonObject) json;
            double lon = o.getJsonNumber("lon").bigDecimalValue().doubleValue();
            double lan = o.getJsonNumber("lat").bigDecimalValue().doubleValue();
            d.addGeolocation(new GeolocationDTO(o.getString("obj-id"), o.getString("name"),o.getString("base"), o.getString("type"), lon, lan));
        });

        return d;
    }
}