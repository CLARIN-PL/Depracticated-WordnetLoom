package pl.edu.pwr.wordnetloom.sense;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.common.json.JsonReader;
import pl.edu.pwr.wordnetloom.localisation.LocalisedStringsService;
import pl.edu.pwr.wordnetloom.sense.dto.SenseJson;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Locale;

@ApplicationScoped
public class SimpleSenseJsonConverter implements EntityJsonConverter<SenseJson> {

    @Inject
    LocalisedStringsService service;

    @Override
    public SenseJson convertFrom(final String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);
        final SenseJson s = new SenseJson();
        return s;
    }

    @Override
    public JsonElement convertToJsonElement(final SenseJson sense, final Locale locale) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", sense.getId());
        jsonObject.addProperty("label", buildLabel(sense, locale));
        return jsonObject;
    }

    private String buildLabel(final SenseJson sense, final Locale locale){
        StringBuilder sb = new StringBuilder();
        sb.append(sense.getWord())
                .append(" ")
                .append(sense.getVariant())
                .append(" (").append(service.findString(sense.getDomain(), locale)).append(")")
                .append(" ").append(sense.getLexicon());

        return sb.toString();
    }
}