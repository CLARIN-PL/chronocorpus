package pl.clarin.chronocorpus.wordprofile.entity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.List;

public class WordProfileResult {

   private String collocate;
   private String matching;
   private long frequency;
   private float percentage;

    public WordProfileResult(String collocate, List<WordProfile> matches) {
        this.collocate = collocate;
        StringBuilder match = new StringBuilder();

        for(WordProfile m : matches){
           if(m.getLeftWordOrth() != null){
               match.append(m.getLeftWordOrth()).append("_").append(m.getWord())
                       .append("(").append(m.getFrequency()).append("),");
           }
           if(m.getRightWordOrth() != null){
               match.append(m.getWord()).append("_").append(m.getRightWordOrth())
                       .append("(").append(m.getFrequency()).append("),");
           }
           this.frequency = this.frequency + m.getFrequency();
        }
        this.matching = match.toString().substring(0, match.toString().length()-1);
    }

    public JsonObject toJson(){
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("collocate", collocate);
        b.add("matching", matching);
        b.add("frequency", frequency);
        b.add("percentage", percentage);
        return b.build();
    }


    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public String getCollocate() {
        return collocate;
    }

    public String getMatching() {
        return matching;
    }

    public long getFrequency() {
        return frequency;
    }

    public float getPercentage() {
        return percentage;
    }
}
