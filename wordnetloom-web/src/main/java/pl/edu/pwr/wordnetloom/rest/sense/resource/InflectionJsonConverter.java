package pl.edu.pwr.wordnetloom.rest.sense.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.model.yiddish.Inflection;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InflectionJsonConverter implements EntityJsonConverter<Inflection> {

    @Override
    public Inflection convertFrom(final String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final Inflection inf) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", inf.getId());
        jsonObject.addProperty("prefix", inf.getInflectionDictionary() != null ? inf.getInflectionDictionary().getName() : "");
        jsonObject.addProperty("text", inf.getText());
        return jsonObject;
    }
}
