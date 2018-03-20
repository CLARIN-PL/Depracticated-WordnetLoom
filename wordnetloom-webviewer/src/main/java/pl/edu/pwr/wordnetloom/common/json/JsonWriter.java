package pl.edu.pwr.wordnetloom.common.json;

import com.google.gson.Gson;

public final class JsonWriter {

	private JsonWriter() {
	}

	public static String writeToString(final Object object) {
		if (object == null) {
			return "";
		}

		return new Gson().toJson(object);
	}

}