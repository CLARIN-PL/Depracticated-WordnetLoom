package pl.edu.pwr.wordnetloom.rest.synset.converter;

import com.google.gson.JsonElement;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.model.Synset;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SynsetJsonConverter implements EntityJsonConverter<Synset> {

    @Override
    public Synset convertFrom(String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(Synset entity) {
        return null;
    }
}
