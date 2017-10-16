package pl.edu.pwr.wordnetloom.rest.sense.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pwr.wordnetloom.dao.LexicalUnitDAOLocal;
import pl.edu.pwr.wordnetloom.dto.SenseFilter;


import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/sense")
@Api(value="/api/sense")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SenseResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Context
    UriInfo uriInfo;

    @EJB
    LexicalUnitDAOLocal local;

    @Inject
    SenseJsonConverter senseJsonConverter;

    @GET
    @ApiOperation(value="get the sense by filter")
    public Response findByFilter() {

        final SenseFilter senseFilter = new SenseFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding books using filter: {}", senseFilter);

        //final PaginatedData<Sense> senses = local.findByFilter(senseFilter);

        //final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(senses,
        //        senseJsonConverter);
        //return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
        //      .build();    }
        return null;
    }
}
