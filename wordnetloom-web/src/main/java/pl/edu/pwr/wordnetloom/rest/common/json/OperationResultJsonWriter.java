package pl.edu.pwr.wordnetloom.rest.common.json;

import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.rest.common.model.OperationResult;

public final class OperationResultJsonWriter {

	private OperationResultJsonWriter() {
	}

	public static String toJson(final OperationResult operationResult) {
		return JsonWriter.writeToString(getJsonObject(operationResult));
	}

	private static Object getJsonObject(final OperationResult operationResult) {
		if (operationResult.isSuccess()) {
			return getJsonSucess(operationResult);
		}
		return getJsonError(operationResult);
	}

	private static Object getJsonSucess(final OperationResult operationResult) {
		return operationResult.getEntity();
	}

	private static JsonObject getJsonError(final OperationResult operationResult) {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("errorIdentification", operationResult.getErrorIdentification());
		jsonObject.addProperty("errorDescription", operationResult.getErrorDescription());

		return jsonObject;
	}

}