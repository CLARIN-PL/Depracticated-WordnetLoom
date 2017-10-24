package pl.edu.pwr.wordnetloom.rest.sense.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.dto.SenseGraphDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SenseGraphJsonConverter implements EntityJsonConverter<SenseGraphDTO> {

    @Inject
    SenseDTOJsonConverter senseDTOJsonConverter;

    @Override
    public SenseGraphDTO convertFrom(String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(SenseGraphDTO entity) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Id", entity.getId());
        jsonObject.addProperty("Lexicon", "");
        jsonObject.addProperty("Lemma", entity.getLemma());
        jsonObject.addProperty("PartOfSpeech", entity.getPartOfSpeech());
        jsonObject.add("Incoming", senseDTOJsonConverter.convertToJsonElement(entity.getIncomming()));
        jsonObject.add("Outgoing", senseDTOJsonConverter.convertToJsonElement(entity.getOutgoing()));

        return jsonObject;
    }
}
