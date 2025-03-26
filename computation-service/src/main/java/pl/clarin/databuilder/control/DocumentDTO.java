package pl.clarin.databuilder.control;

import java.util.ArrayList;
import java.util.List;

public class DocumentDTO {

    private String id;
    private String text;
    private List<SentenceDTO> sentences = new ArrayList();
    private List<TokenDTO> tokens = new ArrayList<>();
    private List<EntityDTO> entities = new ArrayList<>();
    private List<GeolocationDTO> geolocation = new ArrayList();

    public DocumentDTO(){};
    public DocumentDTO(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public void addToken(TokenDTO t){
        tokens.add(t);
    }

    public void addEntity(EntityDTO e){
        entities.add(e);
    }

    public void addGeolocation(GeolocationDTO geo){
        geolocation.add(geo);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TokenDTO> getTokens() {
        return tokens;
    }

    public List<SentenceDTO> getSentences() {
        return sentences;
    }

    public List<GeolocationDTO> getGeolocation() {
        return geolocation;
    }

    public void setTokens(List<TokenDTO> tokens) {
        this.tokens = tokens;
    }

    public List<EntityDTO> getEntities() {
        return entities;
    }

    public void setEntities(List<EntityDTO> entities) {
        this.entities = entities;
    }


    @Override
    public String toString() {
        return "DocumentDTO{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", sentences=" + sentences +
                ", tokens=" + tokens +
                ", entities=" + entities +
                ", geolocation=" + geolocation +
                '}';
    }
}