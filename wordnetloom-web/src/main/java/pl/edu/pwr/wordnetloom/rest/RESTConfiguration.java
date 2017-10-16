package pl.edu.pwr.wordnetloom.rest;

import io.swagger.jaxrs.config.BeanConfig;
import pl.edu.pwr.wordnetloom.rest.dictionary.resource.DictionaryResource;
import pl.edu.pwr.wordnetloom.rest.domain.resource.DomainResource;
import pl.edu.pwr.wordnetloom.rest.partofspeech.resource.PartOfSpeechResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RESTConfiguration extends Application{

    public void RESTConfiguration(){

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/yiddish/api");
        beanConfig.setResourcePackage("pl.edu.pwr.wordnetloom.rest");
        beanConfig.setScan(true);
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DictionaryResource.class);
        classes.add(PartOfSpeechResource.class);
        classes.add(DomainResource.class);
        classes.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        classes.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        return classes;
    }

}
