package pl.edu.pwr.wordnetloom;

import io.swagger.annotations.SwaggerDefinition;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@SwaggerDefinition(basePath = "/resources")
@ApplicationPath("/resources")
public class RESTConfiguration extends Application {
}
