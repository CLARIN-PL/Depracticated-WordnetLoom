package pl.edu.pwr.wordnetloom.rest.sense.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.edu.pwr.wordnetloom.common.json.EntityJsonConverter;
import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;

@ApplicationScoped
public class YiddishExtensionJsonConverter implements EntityJsonConverter<YiddishSenseExtension> {

    @Inject
    YiddishDomainJsonConverter yiddishDomainJsonConverter;

    @Inject
    SourceJsonConverter sourceJsonConverter;

    @Inject
    InflectionJsonConverter inflectionJsonConverter;

    @Inject
    TranscriptionJsonConverter transcriptionJsonConverter;

    @Override
    public YiddishSenseExtension convertFrom(final String json) {
        return null;
    }

    @Override
    public JsonElement convertToJsonElement(final YiddishSenseExtension yse) {

        final JsonObject xt = new JsonObject();

        xt.addProperty("Yiddish variant", yse.getVariant().name());
        xt.addProperty("Latin spelling", yse.getLatinSpelling());
        xt.addProperty("Yiddish spelling", yse.getYiddishSpelling());
        xt.addProperty("Dialectal", yse.getDialectalDictionary() != null ? yse.getDialectalDictionary().getName() : "");
        xt.addProperty("Grammatical gender", yse.getGrammaticalGender() != null ? yse.getGrammaticalGender().getName() : "");
        xt.addProperty("Meaning", yse.getMeaning());
        xt.addProperty("YIVO spelling", yse.getYivoSpelling());
        xt.addProperty("Lexical Characteristic", yse.getLexicalCharcteristic() != null ? yse.getLexicalCharcteristic().getName() : "");
        xt.addProperty("Style", yse.getStyle() != null ? yse.getStyle().getName() : "");
        xt.addProperty("Status", yse.getStatus() != null ? yse.getStatus().getName() : "");
        xt.addProperty("Age", yse.getAge() != null ? yse.getAge().getName() : "");
        xt.add("Transcription", transcriptionJsonConverter.convertToJsonElement(new ArrayList(yse.getTranscriptions())));
        xt.add("Semantic filed", yiddishDomainJsonConverter.convertToJsonElement(new ArrayList(yse.getYiddishDomains())));
        xt.add("Inflection", inflectionJsonConverter.convertToJsonElement(new ArrayList(yse.getInflection())));
        xt.add("Source", sourceJsonConverter.convertToJsonElement(new ArrayList(yse.getSource())));

        return xt;

    }
}
