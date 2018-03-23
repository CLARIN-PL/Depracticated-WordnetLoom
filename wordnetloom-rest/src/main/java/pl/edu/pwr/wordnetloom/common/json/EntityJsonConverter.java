package pl.edu.pwr.wordnetloom.common.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import javax.ejb.Local;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface EntityJsonConverter<T> {

    T convertFrom(String json);

    JsonElement convertToJsonElement(T entity, Locale locale);


    default JsonElement convertToJsonElement(final List<T> entities, Locale locale) {
        final JsonArray jsonArray = new JsonArray();

        for (final T entity : entities) {
            jsonArray.add(convertToJsonElement(entity, locale));
        }

        return jsonArray;
    }

    default JsonElement convertToJsonElement(final Set<T> entities, Locale locale) {
        final JsonArray jsonArray = new JsonArray();

        for (final T entity : entities) {
            jsonArray.add(convertToJsonElement(entity, locale));
        }

        return jsonArray;
    }

}