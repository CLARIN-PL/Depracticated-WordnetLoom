package pl.edu.pwr.wordnetloom.rest.dictionary.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.common.json.JsonReader;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.Dictionary;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DictionaryJsonConverter implements EntityJsonConverter<Dictionary> {

    @Override
    public Dictionary convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        final Dictionary d = new Dictionary();
        return d;
    }

    @Override
    public JsonElement convertToJsonElement(final Dictionary dic) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", dic.getId());
        jsonObject.addProperty("name", dic.getName());
        jsonObject.addProperty("description", dic.getDescription());
        return jsonObject;
    }

}
