package pl.edu.pwr.wordnetloom.rest.synset.resource;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pwr.wordnetloom.common.json.JsonUtils;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.common.model.HttpCode;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.dao.SynsetDAOLocal;
import pl.edu.pwr.wordnetloom.dto.SynsetFilter;
import pl.edu.pwr.wordnetloom.model.Synset;
import pl.edu.pwr.wordnetloom.rest.synset.converter.SynsetJsonConverter;
import pl.edu.pwr.wordnetloom.rest.synset.filter.SynsetFilterExtractorFromUrl;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/synset")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SynsetResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    SynsetDAOLocal local;

    @Context
    UriInfo uriInfo;

    @Inject
    SynsetJsonConverter synsetJsonConverter;

    @GET
    public Response findByFilter() {

        final SynsetFilter synsetFilter = new SynsetFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding synsets using filter: {}", synsetFilter);

        final PaginatedData<Synset> senses = local.findByFilter(synsetFilter);

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(senses, synsetJsonConverter);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }
}
