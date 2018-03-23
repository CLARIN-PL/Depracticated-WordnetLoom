package pl.edu.pwr.wordnetloom.common.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import pl.edu.pwr.wordnetloom.common.exception.InvalidJsonException;

public class JsonReader {

	public static JsonObject readAsJsonObject(final String json) throws InvalidJsonException {
		return readJsonAs(json, JsonObject.class);
	}

	public static JsonArray readAsJsonArray(final String json) throws InvalidJsonException {
		return readJsonAs(json, JsonArray.class);
	}

	public static <T> T readJsonAs(final String json, final Class<T> jsonClass) throws InvalidJsonException {
		if (json == null || json.trim().isEmpty()) {
			throw new InvalidJsonException("Json String can not be null");
		}
		try {
			return new Gson().fromJson(json, jsonClass);
		} catch (final JsonSyntaxException e) {
			throw new InvalidJsonException(e);
		}
	}

	public static Long getLongOrNull(final JsonObject jsonObject, final String propertyName) {
		final JsonElement property = jsonObject.get(propertyName);
		if (isJsonElementNull(property)) {
			return null;
		}
		return property.getAsLong();
	}

	public static Integer getIntegerOrNull(final JsonObject jsonObject, final String propertyName) {
		final JsonElement property = jsonObject.get(propertyName);
		if (isJsonElementNull(property)) {
			return null;
		}
		return property.getAsInt();
	}

	public static String getStringOrNull(final JsonObject jsonObject, final String propertyName) {
		final JsonElement property = jsonObject.get(propertyName);
		if (isJsonElementNull(property)) {
			return null;
		}
		return property.getAsString();
	}

	public static Double getDoubeOrNull(final JsonObject jsonObject, final String propertyName) {
		final JsonElement property = jsonObject.get(propertyName);
		if (isJsonElementNull(property)) {
			return null;
		}
		return property.getAsDouble();
	}

	private static boolean isJsonElementNull(final JsonElement element) {
		return element == null || element.isJsonNull();
	}

}