package pl.edu.pwr.wordnetloom.rest.sense.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.common.json.JsonReader;
import pl.edu.pwr.wordnetloom.model.Sense;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimpleSenseJsonConverter implements EntityJsonConverter<Sense> {

	@Override
	public Sense convertFrom(final String json) {
		final JsonObject jsonObject = JsonReader.readAsJsonObject(json);
		final Sense s = new Sense();
		return s;
	}

	@Override
	public JsonElement convertToJsonElement(final Sense sense) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", sense.getId());
		jsonObject.addProperty("lemma", sense.toString());
		return jsonObject;
	}

}