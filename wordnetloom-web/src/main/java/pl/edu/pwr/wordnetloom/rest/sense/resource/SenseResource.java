package pl.edu.pwr.wordnetloom.rest.sense.resource;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pwr.wordnetloom.common.json.JsonUtils;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.common.json.OperationResultJsonWriter;
import pl.edu.pwr.wordnetloom.common.model.HttpCode;
import pl.edu.pwr.wordnetloom.common.model.OperationResult;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.dao.LexicalUnitDAOLocal;
import pl.edu.pwr.wordnetloom.dto.SenseFilter;
import pl.edu.pwr.wordnetloom.model.Sense;


import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/sense")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SenseResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Context
    UriInfo uriInfo;

    @EJB
    LexicalUnitDAOLocal local;

    @Inject
    SimpleSenseJsonConverter simpleSenseJsonConverter;

    @Inject
    SenseJsonConverter senseJsonConverter;

    @GET
    public Response findByFilter() {

        final SenseFilter senseFilter = new SenseFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding books using filter: {}", senseFilter);

        final PaginatedData<Sense> senses = local.findByFilter(senseFilter);

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(senses,
                simpleSenseJsonConverter);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
              .build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") final Long id) {
        Response.ResponseBuilder responseBuilder;
        try {
            final Sense sense = local.dbGetWithYiddish(id);
            final OperationResult result = OperationResult.success(senseJsonConverter.convertToJsonElement(sense));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("Sense found: {}", sense);
        } catch (final SenseNotFoundException e) {
            logger.error("No sense found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }
        return responseBuilder.build();
    }
}
