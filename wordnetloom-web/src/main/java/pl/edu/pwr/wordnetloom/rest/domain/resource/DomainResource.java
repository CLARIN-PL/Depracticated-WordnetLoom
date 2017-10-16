package pl.edu.pwr.wordnetloom.rest.domain.resource;

import com.google.gson.JsonElement;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pwr.wordnetloom.dao.DomainDaoLocal;
import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.common.json.JsonUtils;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.common.model.HttpCode;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.model.PartOfSpeech;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/domain")
@Api(value="/api/domain")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DomainResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @EJB
    DomainDaoLocal local;

    @Inject
    DomainJsonConverter domainJsonConverter;

    @GET
    @ApiOperation(value="get the all domains")
    public Response findAll(){

        List<Domain> all = local.getAllDomains();

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(
                new PaginatedData<>(all.size(), all), domainJsonConverter);
        logger.debug("Returning the operation result after getAll: ", all);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

}
