package pl.edu.pwr.wordnetloom.common.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Set;

public interface EntityJsonConverter<T> {

    T convertFrom(String json);

    JsonElement convertToJsonElement(T entity);


    default JsonElement convertToJsonElement(final List<T> entities) {
        final JsonArray jsonArray = new JsonArray();

        for (final T entity : entities) {
            jsonArray.add(convertToJsonElement(entity));
        }

        return jsonArray;
    }

    default JsonElement convertToJsonElement(final Set<T> entities) {
        final JsonArray jsonArray = new JsonArray();

        for (final T entity : entities) {
            jsonArray.add(convertToJsonElement(entity));
        }

        return jsonArray;
    }

}