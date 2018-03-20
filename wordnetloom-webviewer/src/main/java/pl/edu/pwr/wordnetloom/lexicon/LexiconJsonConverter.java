package pl.edu.pwr.wordnetloom.lexicon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LexiconJsonConverter implements EntityJsonConverter<Lexicon> {

    @Override
    public Lexicon convertFrom(final String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final Lexicon lex) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", lex.getId());
        jsonObject.addProperty("name", lex.getName());
        return jsonObject;
    }

}
