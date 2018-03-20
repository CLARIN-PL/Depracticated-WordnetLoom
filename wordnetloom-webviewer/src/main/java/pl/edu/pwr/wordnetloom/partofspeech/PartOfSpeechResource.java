package pl.edu.pwr.wordnetloom.partofspeech;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pwr.wordnetloom.common.json.JsonUtils;
import pl.edu.pwr.wordnetloom.common.json.JsonWriter;
import pl.edu.pwr.wordnetloom.common.model.HttpCode;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.partofspeech.service.PartOfSpeechServiceLocal;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/partsofspeech")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartOfSpeechResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @EJB
    PartOfSpeechServiceLocal local;

    @Inject
    PartOfSpeechJsonConverter posJsonConverter;

    @GET
    public Response findAll(){

        List<PartOfSpeech> all = local.findAll();

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(
                new PaginatedData<>(all.size(), all), posJsonConverter);
        logger.debug("Returning the operation result after getAll: ", all);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }

}
