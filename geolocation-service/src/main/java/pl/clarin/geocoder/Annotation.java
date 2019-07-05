package pl.clarin.geocoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Annotation {

    private String sentenceId;
    private int channel;
    private List<Token> tokens = new ArrayList();
    private List<Location> locations = new ArrayList();

    public Annotation(String sentenceId, int channel) {
        this.sentenceId = sentenceId;
        this.channel = channel;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public int getChannel() {
        return channel;
    }

    public void addToken(Token t){
        tokens.add(t);
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public  void addLocation(Location l){
        locations.add(l);
    }

    public String getBaseQueryString(){
        return tokens.stream().map(Token::getBase).collect(Collectors.joining(" "));
    }
    public String getOrthQueryString(){
        return tokens.stream().map(Token::getOrth).collect(Collectors.joining(" "));
    }

    @Override
    public String toString() {
        return "Annotation{" +
                "sentenceId='" + sentenceId + '\'' +
                ", channel='" + channel + '\'' +
                ", tokens=" + tokens +
                ", locations=" + locations +
                '}';
    }
}
