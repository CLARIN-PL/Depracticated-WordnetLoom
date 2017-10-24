package pl.edu.pwr.wordnetloom.rest.sense.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.dto.SenseDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class SenseDTOJsonConverter implements EntityJsonConverter<SenseDTO> {

    @Inject
    RelationJsonConverter relationJsonConverter;

    @Override
    public SenseDTO convertFrom(final String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final SenseDTO sense) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Id", sense.getId());
        jsonObject.addProperty("Lexicon", "");
        jsonObject.addProperty("Lemma", sense.getLemma());
        jsonObject.addProperty("Part of speech", sense.getPartOfSpeech());
        jsonObject.add("Incoming", relationJsonConverter.convertToJsonElement(sense.getIncomming()));
        jsonObject.add("Outgoing", relationJsonConverter.convertToJsonElement(sense.getOutgoing()));
        return jsonObject;
    }

}
