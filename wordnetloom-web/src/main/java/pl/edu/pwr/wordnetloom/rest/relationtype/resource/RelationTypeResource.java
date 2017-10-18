package pl.edu.pwr.wordnetloom.rest.relationtype.resource;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pwr.wordnetloom.common.json.JsonUtils;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.common.model.HttpCode;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.dao.RelationTypeDAOLocal;
import pl.edu.pwr.wordnetloom.model.RelationArgument;
import pl.edu.pwr.wordnetloom.model.RelationType;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Path("/relationtype")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelationTypeResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @EJB
    RelationTypeDAOLocal local;

    @Inject
    RelationTypeJsonConverter relationTypeJsonConverter;

    @GET
    @Path("/sense")
    public Response findAllLexical() {

        List<RelationType> all = local.dbGetLeafs(RelationArgument.LEXICAL, Arrays.asList(1l, 2l, 3l, 4l));

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(
                new PaginatedData<>(all.size(), all), relationTypeJsonConverter);
        logger.debug("Returning the operation result after getAll: ", all);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

    @GET
    @Path("/synset")
    public Response findAllSynset() {

        List<RelationType> all = local.dbGetLeafs(RelationArgument.SYNSET, Arrays.asList(1l, 2l, 3l, 4l));

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(
                new PaginatedData<>(all.size(), all), relationTypeJsonConverter);
        logger.debug("Returning the operation result after getAll: ", all);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

}
