package pl.edu.pwr.wordnetloom.rest.relationtype.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.model.RelationType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RelationTypeJsonConverter implements EntityJsonConverter<RelationType> {

    @Override
    public RelationType convertFrom(final String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final RelationType rt) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", rt.getId());
        jsonObject.addProperty("name", rt.getName().getText());
        jsonObject.addProperty("short", rt.getShortDisplayText().getText());
        jsonObject.addProperty("display", rt.getDisplayText().getText());
        return jsonObject;
    }

}
