package pl.edu.pwr.wordnetloom.client;

import org.glassfish.jersey.jsonp.JsonProcessingFeature;

import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Application {

    Client client = ClientBuilder.newBuilder().register(JsonProcessingFeature.class)
            .property(JsonGenerator.PRETTY_PRINTING, true).build();

    public static void main(String...args) {

        String host = System.getenv("WORDNETLOOM_SERVER_HOST") != null ? System.getenv("WORDNETLOOM_SERVER_HOST") : "127.0.0.1";
        String port = System.getenv("WORDNETLOOM_SERVER_PORT") != null ? System.getenv("WORDNETLOOM_SERVER_PORT") : "8080";

        Application app = new Application();
            String installedVersion = app.findVersionFromFile();
            System.out.println("Installed version: " +installedVersion);
            String newVersion = app.findCurrentVersion(host,port);
            System.out.println("Current version: " + newVersion);
            if(!installedVersion.equals(newVersion)){
                System.out.println("Downloading new version please wait...");
                app.downloadFile(host, port);
                app.saveToFile(newVersion);
            }
    }

    private  String findCurrentVersion(String host, String port){

        WebTarget path = client.target("http://"+host+":"+port+"/download/resources/version");
        JsonObject json = path.request().get(JsonObject.class);
        return json.getString("Implementation-Build");
    }

    private  String findVersionFromFile() {
        try {
            return  new String(Files.readAllBytes(Paths.get("lib/version")));
        } catch (IOException e) {
            return "";
        }
    }

    private void saveToFile(String version){
        FileWriter fw = null;
        try {
            fw = new FileWriter("lib/version", false);
            fw.append(version);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fw !=null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void downloadFile(String host, String port){
        WebTarget path = client.target("http://"+host+":"+port+"/download/resources/download");
        try {
            FileOutputStream out = new FileOutputStream("lib/wordnetloom-client.jar");
            InputStream is = path.request().get(InputStream.class);
            int len = 0;
            byte[] buffer = new byte[4096];
            while((len = is.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
