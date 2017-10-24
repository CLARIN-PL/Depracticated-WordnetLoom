package pl.edu.pwr.wordnetloom.rest.sense.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.model.yiddish.YiddishDomain;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class YiddishDomainJsonConverter implements EntityJsonConverter<YiddishDomain> {

    @Override
    public YiddishDomain convertFrom(final String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final YiddishDomain yd) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", yd.getId());
        jsonObject.addProperty("domain", yd.getDomain() != null ? yd.getDomain().getName() : "");
        jsonObject.addProperty("modifier", yd.getModifier() != null ? yd.getModifier().getName() : "");
        return jsonObject;
    }
}
