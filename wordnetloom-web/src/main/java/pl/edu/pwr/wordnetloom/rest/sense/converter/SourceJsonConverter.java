package pl.edu.pwr.wordnetloom.rest.sense.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SourceDictionary;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SourceJsonConverter implements EntityJsonConverter<SourceDictionary> {

    @Override
    public SourceDictionary convertFrom(final String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final SourceDictionary s) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", s.getId());
        jsonObject.addProperty("name", s.getName());
        return jsonObject;
    }
}
