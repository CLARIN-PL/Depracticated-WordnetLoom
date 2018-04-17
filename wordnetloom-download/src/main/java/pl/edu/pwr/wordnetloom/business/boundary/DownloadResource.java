package pl.edu.pwr.wordnetloom.business.boundary;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

@Path("/")
public class DownloadResource {

    @Path("version")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVersion(HttpServletRequest request) throws IOException {
        ServletContext context = request.getSession().getServletContext();
        InputStream manifestStream = context.getResourceAsStream("/META-INF/MANIFEST.MF");
        Manifest manifest = new Manifest(manifestStream);

        Map<String, String> response = new HashMap<>();
        response.put("Implementation-Vendor", manifest.getMainAttributes().getValue("Implementation-Vendor"));
        response.put("Implementation-Title", manifest.getMainAttributes().getValue("Implementation-Title"));
        response.put("Implementation-Version", manifest.getMainAttributes().getValue("Implementation-Version"));
        response.put("Implementation-Jdk", manifest.getMainAttributes().getValue("Build-Jdk"));
        response.put("Implementation-Build", manifest.getMainAttributes().getValue("Implementation-Build"));
        response.put("Implementation-Build-Time", manifest.getMainAttributes().getValue("Implementation-Build-Time"));


        return Response.ok().entity(response).build();
    }

    @Path("download")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getDownload(){
        return Json.createObjectBuilder()
                .add("file","wordnetloom.jar").build();
    }
}
