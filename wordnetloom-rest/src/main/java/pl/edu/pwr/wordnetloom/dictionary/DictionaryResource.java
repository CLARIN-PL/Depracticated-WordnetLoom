package pl.edu.pwr.wordnetloom.dictionary;

import com.google.gson.JsonElement;
import pl.edu.pwr.wordnetloom.common.json.JsonUtils;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.common.model.HttpCode;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.dictionary.model.*;
import pl.edu.pwr.wordnetloom.dictionary.service.DictionaryServiceLocal;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Path("dictionary")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DictionaryResource {

    @Inject
    DictionaryJsonConverter dicJsonConverter;

    @Inject
    DictionaryServiceLocal local;

    @GET
    @Path("status")
    public Response getAllStatus(@HeaderParam("Accept-Language") Locale locale) {
        List<Dictionary> all = new ArrayList<>(local.findDictionaryByClass(StatusDictionary.class));
        return buildDictionaryJson(all, locale);
    }

    @GET
    @Path("emotion")
    public Response getAllEmotion(@HeaderParam("Accept-Language") Locale locale) {
        List<Dictionary> all = new ArrayList<>(local.findDictionaryByClass(EmotionDictionary.class));
        return buildDictionaryJson(all, locale);
    }

    @GET
    @Path("register")
    public Response getAllRegister(@HeaderParam("Accept-Language") Locale locale) {
        List<Dictionary> all = new ArrayList<>(local.findDictionaryByClass(RegisterDictionary.class));
        return buildDictionaryJson(all, locale);
    }

    @GET
    @Path("valuation")
    public Response getAllValuation(@HeaderParam("Accept-Language") Locale locale) {
        List<Dictionary> all = new ArrayList<>(local.findDictionaryByClass(ValuationDictionary.class));
        return buildDictionaryJson(all, locale);
    }

    @GET
    @Path("markedness")
    public Response getAllMarkedness(@HeaderParam("Accept-Language") Locale locale) {
        List<Dictionary> all = new ArrayList<>(local.findDictionaryByClass(MarkednessDictionary.class));
        return buildDictionaryJson(all, locale);
    }

    private Response buildDictionaryJson(List<Dictionary> dic, Locale locale) {
        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(
                new PaginatedData<>(dic.size(), dic), dicJsonConverter, locale);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }
}