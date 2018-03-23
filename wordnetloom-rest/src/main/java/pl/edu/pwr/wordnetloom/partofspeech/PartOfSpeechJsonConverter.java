package pl.edu.pwr.wordnetloom.partofspeech;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.common.json.JsonReader;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.localisation.LocalisedStringsService;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.util.Locale;


@ApplicationScoped
public class PartOfSpeechJsonConverter implements EntityJsonConverter<PartOfSpeech> {

	@Inject
	LocalisedStringsService service;

	@Override
	public PartOfSpeech convertFrom(final String json) {
		final JsonObject jsonObject = JsonReader.readAsJsonObject(json);
		final PartOfSpeech pos = new PartOfSpeech();
		return pos;
	}

	@Override
	public JsonElement convertToJsonElement(final PartOfSpeech pos, Locale locale) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", pos.getId());
		jsonObject.addProperty("name", service.findString(pos.getName(), locale));
		return jsonObject;
	}

}