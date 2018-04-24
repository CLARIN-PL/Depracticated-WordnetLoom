package pl.edu.pwr.wordnetloom.business.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.Manifest;

import static java.lang.System.in;

@Path("/")
public class DownloadResource {

    @Path("version")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getVersion(@Context HttpServletRequest request) throws IOException {
        ServletContext context = request.getSession().getServletContext();

        InputStream buildVersionInput = context.getResourceAsStream("/webapp/version");
        InputStreamReader isReader = new InputStreamReader(buildVersionInput);
        BufferedReader reader = new BufferedReader(isReader);
        String buildVersion = reader.readLine();

        return Json.createObjectBuilder()
                .add("Implementation-Build", buildVersion)
                .build();
    }

    @Path("download")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getDownload(@Context HttpServletRequest request){

        ServletContext context = request.getSession().getServletContext();

        InputStream jar = context.getResourceAsStream("/webapp/wordnetloom-client.jar");

        return Response.ok(jar)
                .header("Content-Disposition", "attachment; filename=wordnetloom.jar")
                .build();

    }
}
