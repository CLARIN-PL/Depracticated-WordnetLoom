package pl.edu.pwr.wordnetloom.rest.sense.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.model.Sense;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;


@ApplicationScoped
public class SenseJsonConverter  implements EntityJsonConverter<Sense> {

    @Inject
    YiddishExtensionJsonConverter yiddishExtensionJsonConverter;

    @Override
    public Sense convertFrom(final String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final Sense sense) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Id", sense.getId());
        jsonObject.addProperty("Lexicon", sense.getLexicon().getName().getText());
        jsonObject.addProperty("Lemma", sense.getLemma().getWord());
        jsonObject.addProperty("Sense number", sense.getSenseNumber());
        jsonObject.addProperty("Part of speech", sense.getPartOfSpeech().getName().getText());
        jsonObject.addProperty("Domain", sense.getDomain().getName().getText());
        jsonObject.add("Yiddish", yiddishExtensionJsonConverter.convertToJsonElement(new ArrayList<>(sense.getYiddishSenseExtension())));
        return jsonObject;
    }

}
