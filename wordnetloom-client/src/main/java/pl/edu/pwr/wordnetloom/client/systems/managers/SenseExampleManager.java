package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;

import java.util.HashSet;
import java.util.Set;

public class SenseExampleManager {

    private static volatile  SenseExampleManager instance;

//    private final List<String> exampleTypes;
    private final Set<String> exampleTypes;

    private SenseExampleManager() {
//        exampleTypes = RemoteService.senseRemote.findUniqueExampleTypes();
        exampleTypes = new HashSet<>(RemoteService.senseRemote.findUniqueExampleTypes());
    }

    public static SenseExampleManager getInstance() {
        synchronized (SenseExampleManager.class) {
            if(instance == null){
                instance = new SenseExampleManager();
            }
        }
        return instance;
    }

    public Set<String> getTypes(){
        return exampleTypes;
    }

    public boolean contains(String type){
        return exampleTypes.contains(type);
    }
}
