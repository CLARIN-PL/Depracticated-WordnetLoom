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
import pl.edu.pwr.wordnetloom.dao.LexicalRelationDAOLocal;
import pl.edu.pwr.wordnetloom.dao.LexicalUnitDAOLocal;
import pl.edu.pwr.wordnetloom.dto.RelationDTO;
import pl.edu.pwr.wordnetloom.dto.SenseFilter;
import pl.edu.pwr.wordnetloom.dto.SenseGraphDTO;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.rest.sense.converter.RelationJsonConverter;
import pl.edu.pwr.wordnetloom.rest.sense.converter.SenseGraphJsonConverter;
import pl.edu.pwr.wordnetloom.rest.sense.converter.SenseJsonConverter;
import pl.edu.pwr.wordnetloom.rest.sense.converter.SimpleSenseJsonConverter;
import pl.edu.pwr.wordnetloom.rest.sense.exception.SenseNotFoundException;
import pl.edu.pwr.wordnetloom.rest.sense.filter.SenseFilterExtractorFromUrl;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Map;
import java.util.Set;


@Path("/sense")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SenseResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Context
    UriInfo uriInfo;

    @EJB
    LexicalUnitDAOLocal local;

    @EJB
    LexicalRelationDAOLocal relationLocal;

    @Inject
    SimpleSenseJsonConverter simpleSenseJsonConverter;

    @Inject
    SenseJsonConverter senseJsonConverter;

    @Inject
    RelationJsonConverter senseRelationJsonConverter;

    @Inject
    private SenseGraphJsonConverter senseGraphJsonConverter;

    @GET
    public Response findByFilter() {

        final SenseFilter senseFilter = new SenseFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding senses using filter: {}", senseFilter);

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

    @GET
    @Path("/{id}/relations/outgoing")
    public Response getSenseRelationsWhereIsParent(@PathParam("id") final Long id) {
        Map<String, Set<RelationDTO>> sr = relationLocal.dbGetSubRelations(id);
        final OperationResult result = OperationResult.success(senseRelationJsonConverter.convertToJsonElement(sr));
        Response.ResponseBuilder responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
        return responseBuilder.build();
    }

    @GET
    @Path("/{id}/relations/incoming")
    public Response getSenseRelationsWhereIsChild(@PathParam("id") final Long id) {
        Map<String, Set<RelationDTO>> sr = relationLocal.dbGetUpperRelations(id);
        final OperationResult result = OperationResult.success(senseRelationJsonConverter.convertToJsonElement(sr));
        Response.ResponseBuilder responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
        return responseBuilder.build();
    }

    @GET
    @Path("/{id}/graph")
    public Response getSenseGraph(@PathParam("id") final Long id) {
        SenseGraphDTO dto = local.getGraphForSense(id);
        final OperationResult result = OperationResult.success(senseGraphJsonConverter.convertToJsonElement(dto));
        Response.ResponseBuilder responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
        return responseBuilder.build();
    }
}
