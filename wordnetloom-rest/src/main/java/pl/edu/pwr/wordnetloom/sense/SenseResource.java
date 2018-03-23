package pl.edu.pwr.wordnetloom.sense;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pwr.wordnetloom.common.json.JsonUtils;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.common.json.OperationResultJsonWriter;
import pl.edu.pwr.wordnetloom.common.model.HttpCode;
import pl.edu.pwr.wordnetloom.common.model.OperationResult;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.common.filter.SearchFilter;
import pl.edu.pwr.wordnetloom.common.resource.SearchFilterExtractorFromUrl;
import pl.edu.pwr.wordnetloom.sense.dto.SenseJson;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.service.SenseServiceLocal;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Path("/sense")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SenseResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Context
    UriInfo uriInfo;

    @Inject
    SenseServiceLocal local;

    @Inject
    SenseJsonConverter senseJsonConverter;

    @Inject
    SimpleSenseJsonConverter simpleSenseJsonConverter;

    @Resource(name = "DefaultManagedExecutorService")
    ManagedExecutorService executor;

    @GET
    public Response findByFilter(@HeaderParam("Accept-Language") Locale locale) {

        final SearchFilter searchFilter = new SearchFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding senses using filter: {}", searchFilter);

        Future<List<SenseJson>> senses = executor.submit(() ->  local.findByFilter(searchFilter));
        Future<Long> count = executor.submit(() ->  local.countByFilter(searchFilter));

        final JsonElement jsonWithPagingAndEntries;
        try {
            jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(new PaginatedData<>(count.get(),senses.get()),
                    simpleSenseJsonConverter, locale);
        } catch (InterruptedException | ExecutionException e) {
          logger.error("Search executor error", e);
          return Response.status(HttpCode.SERVICE_UNAVAILABLE.getCode()).build();
        }
        logger.debug("List found: {}", jsonWithPagingAndEntries);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@HeaderParam("Accept-Language") Locale locale,
                             @PathParam("id") final Long id) {

        Response.ResponseBuilder responseBuilder;
        try {
            final Sense sense = local.fetchSense(id);
            final OperationResult result = OperationResult.success(senseJsonConverter.convertToJsonElement(sense, locale));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("Sense found: {}", sense);
        } catch (final SenseNotFoundException e) {
            logger.error("No sense found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }
        return responseBuilder.build();
    }

/*  @GET
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
    }*/
}