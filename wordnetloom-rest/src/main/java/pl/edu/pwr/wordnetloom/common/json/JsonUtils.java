package pl.edu.pwr.wordnetloom.common.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;

import java.util.Locale;

public final class JsonUtils {

	private JsonUtils() {
	}

	public static JsonElement getJsonElementWithId(final Long id) {
		final JsonObject idJson = new JsonObject();
		idJson.addProperty("id", id);

		return idJson;
	}

	public static <T> JsonElement getJsonElementWithPagingAndEntries(final PaginatedData<T> paginatedData,
			final EntityJsonConverter<T> entityJsonConverter, Locale locale) {
		final JsonObject jsonWithEntriesAndPaging = new JsonObject();

		final JsonObject jsonPaging = new JsonObject();
		jsonPaging.addProperty("totalRecords", paginatedData.getNumberOfRows());

		jsonWithEntriesAndPaging.add("paging", jsonPaging);
		jsonWithEntriesAndPaging.add("entries", entityJsonConverter.convertToJsonElement(paginatedData.getRows(), locale));

		return jsonWithEntriesAndPaging;
	}

}