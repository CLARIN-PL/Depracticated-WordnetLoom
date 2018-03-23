package pl.edu.pwr.wordnetloom.synset;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pwr.wordnetloom.common.filter.SearchFilter;
import pl.edu.pwr.wordnetloom.common.json.JsonUtils;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.common.model.HttpCode;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.common.resource.SearchFilterExtractorFromUrl;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceLocal;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Locale;

@Path("/synset")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SynsetResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    SynsetServiceLocal local;

    @Context
    UriInfo uriInfo;

    @Inject
    SynsetJsonConverter synsetJsonConverter;

    @GET
    public Response findByFilter(@HeaderParam("Accept-Language") Locale locale) {

        final SearchFilter synsetFilter = new SearchFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding synsets using filter: {}", synsetFilter);

        final PaginatedData<Synset> senses = local.findByFilter(synsetFilter);

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(senses, synsetJsonConverter, locale);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }
}