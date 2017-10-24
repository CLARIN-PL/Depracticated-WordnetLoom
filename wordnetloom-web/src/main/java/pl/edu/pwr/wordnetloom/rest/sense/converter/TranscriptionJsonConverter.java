package pl.edu.pwr.wordnetloom.rest.sense.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.model.yiddish.Transcription;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TranscriptionJsonConverter implements EntityJsonConverter<Transcription> {

    @Override
    public Transcription convertFrom(final String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final Transcription trans) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", trans.getId());
        jsonObject.addProperty("type", trans.getTranscriptionDictionary() != null ? trans.getTranscriptionDictionary().getName() : "");
        jsonObject.addProperty("value", trans.getPhonography());
        return jsonObject;
    }
}
