package pl.edu.pwr.wordnetloom.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.common.json.JsonReader;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.localisation.LocalisedStringsService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Locale;

@ApplicationScoped
public class DomainJsonConverter implements EntityJsonConverter<Domain> {

	@Inject
	LocalisedStringsService service;

	@Override
	public Domain convertFrom(final String json) {
		final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

		final Domain domain = new Domain();
		return domain;
	}

	@Override
	public JsonElement convertToJsonElement(final Domain domain, Locale locale) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", domain.getId());
		jsonObject.addProperty("name", service.findString(domain.getName(), locale));
		jsonObject.addProperty("description", service.findString(domain.getDescription(),locale));
		return jsonObject;
	}

}