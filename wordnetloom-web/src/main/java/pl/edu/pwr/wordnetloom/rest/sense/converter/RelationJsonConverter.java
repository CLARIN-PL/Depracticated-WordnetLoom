package pl.edu.pwr.wordnetloom.rest.sense.converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.dto.RelationDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class RelationJsonConverter implements EntityJsonConverter<RelationDTO> {

    @Override
    public RelationDTO convertFrom(String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(RelationDTO entity) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("target", entity.getTarget());
        jsonObject.addProperty("lemma", entity.getLemma());
        return jsonObject;
    }

    public JsonElement convertToJsonElement(Map<String, Set<RelationDTO>> entities) {

        final JsonArray jsonArray = new JsonArray();

        entities.forEach((k, v) -> {
            final JsonObject jsonObject = new JsonObject();
            final JsonArray array = new JsonArray();

            for (final RelationDTO entity : v) {
                array.add(convertToJsonElement(entity));
            }
            jsonObject.add(k, array);
            jsonArray.add(jsonObject);
        });

        return jsonArray;
    }
}
