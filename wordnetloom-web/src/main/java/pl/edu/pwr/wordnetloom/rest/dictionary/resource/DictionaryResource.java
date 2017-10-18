package pl.edu.pwr.wordnetloom.rest.dictionary.resource;

import com.google.gson.JsonElement;
import pl.edu.pwr.wordnetloom.common.json.JsonUtils;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.common.model.HttpCode;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.dao.DictionaryDaoLocal;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.Dictionary;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("dictionary")
@Produces(MediaType.APPLICATION_JSON)
public class DictionaryResource {

    @EJB
    DictionaryDaoLocal local;

    @Inject
    DictionaryJsonConverter dicJsonConverter;

    @GET
    @Path("style")
    public Response getAllStyles() {

        List<Dictionary> all = new ArrayList<>(local.findAllStyleDictionary());
        return buildDictionaryJson(all);
    }

    @GET
    @Path("status")
    public Response getAllStatus() {

        List<Dictionary> all = new ArrayList<>(local.findAllStatusDictionary());
        return buildDictionaryJson(all);
    }

    @GET
    @Path("grammatical-gender")
    public Response getAllGrammaticalGender() {

        List<Dictionary> all = new ArrayList<>(local.findAllGrammaticalGenderDictionary());
        return buildDictionaryJson(all);
    }

    @GET
    @Path("source")
    public Response getAllSource() {

        List<Dictionary> all = new ArrayList<>(local.findAllSourceDictionary());
        return buildDictionaryJson(all);

    }

    @GET
    @Path("age")
    public Response getAllAge() {

        List<Dictionary> all = new ArrayList<>(local.findAllAgeDictionary());
        return buildDictionaryJson(all);

    }

    @GET
    @Path("transcription")
    public Response getAllTranscription() {

        List<Dictionary> all = new ArrayList<>(local.findAllTranscriptionsDictionary());
        return buildDictionaryJson(all);

    }

    @GET
    @Path("semantic-field")
    public Response getAllSemanticFiled() {

        List<Dictionary> all = new ArrayList<>(local.findAllDomainDictionary());
        return buildDictionaryJson(all);

    }

    @GET
    @Path("semantic-field/modifier")
    public Response getAllSemanticFiledModifiers() {

        List<Dictionary> all = new ArrayList<>(local.findAllDomainModifierDictionary());
        return buildDictionaryJson(all);

    }

    @GET
    @Path("lexical-characteristic")
    public Response getAllLexicalCharacteristic() {

        List<Dictionary> all = new ArrayList<>(local.findAllCharacteristicDictionary());
        return buildDictionaryJson(all);
    }


    private Response buildDictionaryJson(List<Dictionary> dic) {
        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(
                new PaginatedData<>(dic.size(), dic), dicJsonConverter);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }
}
