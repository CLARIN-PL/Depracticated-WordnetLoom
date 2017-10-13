package pl.edu.pwr.wordnetloom.rest.partofspeech.resource;

import javax.enterprise.context.ApplicationScoped;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.rest.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.rest.common.json.JsonReader;
import pl.edu.pwr.wordnetloom.model.PartOfSpeech;

@ApplicationScoped
public class PartOfSpeechJsonConverter implements EntityJsonConverter<PartOfSpeech> {

	@Override
	public PartOfSpeech convertFrom(final String json) {
		final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

		final PartOfSpeech pos = new PartOfSpeech();
		return pos;
	}

	@Override
	public JsonElement convertToJsonElement(final PartOfSpeech pos) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", pos.getId());
		jsonObject.addProperty("name", pos.getName().getText());
		return jsonObject;
	}

}