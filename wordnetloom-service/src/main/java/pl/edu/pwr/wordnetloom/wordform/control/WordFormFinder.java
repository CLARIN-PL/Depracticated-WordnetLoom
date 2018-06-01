package pl.edu.pwr.wordnetloom.wordform.control;

import pl.edu.pwr.wordnetloom.wordform.model.WordForm;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class WordFormFinder {

    private Client client;
    private WebTarget target;

    @PostConstruct
    private void init(){
        client = ClientBuilder.newClient();
        target = client.target("http://lexp.clarin-pl.eu/lexp/");
    }

    public Set<WordForm> getWordForms(final String lemma){
       Set<WordForm> forms = new HashSet<>();
       JsonObject j = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(buildEntity(lemma).toString(), MediaType.APPLICATION_JSON))
                .readEntity(JsonObject.class);
        if(j.containsKey("results")) {
            JsonObject results = j.getJsonObject("results");
            JsonArray generate = results.getJsonArray("generate");
            generate.forEach( e -> {
                  JsonArray z = (JsonArray) e;
                  String form = z.getString(0);
                  String tag = z.getString(2);
                  forms.add(new WordForm(lemma, tag, form));
            } );
        }
        return forms;
    }

    public JsonObject buildEntity(String lemma) {
        return Json.createObjectBuilder()
                .add("function", "get")
                .add("element", Json.createObjectBuilder()
                        .add("lang","pl")
                        .add("lemma",lemma)
                        .add("type","lemma")
                )
                .add("resource", "morfeusz")
                .build();
    }
}
