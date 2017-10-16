package pl.edu.pwr.wordnetloom.rest.domain.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.common.json.JsonReader;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DomainJsonConverter implements EntityJsonConverter<Domain> {

	@Override
	public Domain convertFrom(final String json) {
		final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

		final Domain domain = new Domain();
		return domain;
	}

	@Override
	public JsonElement convertToJsonElement(final Domain domain) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", domain.getId());
		jsonObject.addProperty("name", domain.getName().getText());
		jsonObject.addProperty("descritption", domain.getDescription().getText());
		return jsonObject;
	}

}