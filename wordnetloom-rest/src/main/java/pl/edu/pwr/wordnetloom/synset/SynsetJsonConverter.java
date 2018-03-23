package pl.edu.pwr.wordnetloom.synset;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.localisation.LocalisedStringsService;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Locale;

@ApplicationScoped
public class SynsetJsonConverter implements EntityJsonConverter<Synset> {

    @Inject
    LocalisedStringsService service;

    @Override
    public Synset convertFrom(String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final Synset synset, Locale locale) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", synset.getId());
        jsonObject.addProperty("lexicon", synset.getLexicon().getName());
        return jsonObject;
    }

}