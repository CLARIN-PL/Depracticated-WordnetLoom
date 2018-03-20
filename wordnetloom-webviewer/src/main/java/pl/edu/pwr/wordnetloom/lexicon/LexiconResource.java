package pl.edu.pwr.wordnetloom.lexicon;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pwr.wordnetloom.common.json.JsonUtils;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.common.model.HttpCode;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.lexicon.service.LexiconServiceLocal;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/lexicon")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LexiconResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @EJB
    LexiconServiceLocal local;

    @Inject
    LexiconJsonConverter lexiconJsonConverter;

    @GET
    public Response findAll() {

        List<Lexicon> all = local.findAll();

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(
                new PaginatedData<>(all.size(), all), lexiconJsonConverter);
        logger.debug("Returning the operation result after getAll: ", all);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

}
