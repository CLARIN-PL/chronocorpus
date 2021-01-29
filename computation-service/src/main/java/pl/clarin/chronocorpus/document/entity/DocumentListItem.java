package pl.clarin.chronocorpus.document.entity;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.Serializable;

public class DocumentListItem implements Serializable {

    private int id;

    private Metadata metadata;

    public DocumentListItem() {
    }

    public DocumentListItem(Document doc) {
        this.id = doc.getId();
        this.metadata = doc.getMetadata();
    }

    public JsonObject toJson() {
        JsonObject m = metadata == null ? Json.createObjectBuilder().build() : metadata.toForListJson();
        return Json.createObjectBuilder()
                .add("id", id)
                .add("metadata", m)
                .build();
    }

}
