package pl.edu.pwr.wordnetloom.sense;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.localisation.LocalisedStringsService;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Locale;

@ApplicationScoped
public class SenseJsonConverter implements EntityJsonConverter<Sense> {

    @Inject
    LocalisedStringsService service;

    @Override
    public Sense convertFrom(final String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final Sense sense, Locale locale) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", sense.getId());
        jsonObject.addProperty("lexicon", sense.getLexicon().getName());
        jsonObject.addProperty("lemma", sense.getWord().getWord());
        jsonObject.addProperty("variant", sense.getVariant());
        jsonObject.addProperty("part_of_speech", service.findString(sense.getPartOfSpeech().getName(), locale));
        jsonObject.addProperty("domain", service.findString(sense.getDomain().getName(), locale));
        return jsonObject;
    }

}