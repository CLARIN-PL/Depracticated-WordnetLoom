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
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;

@Path("/")
public class DownloadResource {

    @Path("version")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getVersion(@Context HttpServletRequest request) throws IOException {
        ServletContext context = request.getSession().getServletContext();
        InputStream manifestStream = context.getResourceAsStream("/META-INF/MANIFEST.MF");
        Manifest manifest = new Manifest(manifestStream);

        return Json.createObjectBuilder()
                .add("Implementation-Title", manifest.getMainAttributes().getValue("Implementation-Title"))
                .add("Implementation-Version", manifest.getMainAttributes().getValue("Implementation-Version"))
                .add("Implementation-Jdk", manifest.getMainAttributes().getValue("Build-Jdk"))
                .add("Implementation-Build", manifest.getMainAttributes().getValue("Implementation-Build"))
                .add("Implementation-Build-Time", manifest.getMainAttributes().getValue("Implementation-Build-Time"))
                .build();
    }

    @Path("download")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getDownload(){
        return Json.createObjectBuilder()
                .add("file","wordnetloom.jar").build();
    }
}
