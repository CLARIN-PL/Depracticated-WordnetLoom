package pl.edu.pwr.wordnetloom.dictionary;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.common.json.JsonReader;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.localisation.LocalisedStringsService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Locale;

@ApplicationScoped
public class DictionaryJsonConverter implements EntityJsonConverter<Dictionary> {

    @Inject
    LocalisedStringsService service;

    @Override
    public Dictionary convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final Dictionary dic, Locale locale) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", dic.getId());
        jsonObject.addProperty("name", service.findString(dic.getName(),locale));
        jsonObject.addProperty("description", service.findString(dic.getDescription(), locale));
        return jsonObject;
    }

}