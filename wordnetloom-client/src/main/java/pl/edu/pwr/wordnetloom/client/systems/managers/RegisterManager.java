package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegisterManager {

    private static RegisterManager instance;
    private Map<Long, String> registersMap;

    public static RegisterManager getInstance() {
        if(instance == null)
        {
            instance = new RegisterManager();
        }
        return instance;
    }

    private RegisterManager() {
        registersMap = RemoteService.localisedStringServiceRemote.findAllRegisterTypes(RemoteConnectionProvider.getInstance().getLanguage());
    }

    public String getName(Long id) {
        return registersMap.get(id);
    }

    public List<String> getAllRegisterNames() {
        return new ArrayList<>(registersMap.values());
    }

    public Long getId(String name) {
        for(Map.Entry<Long, String> entry : registersMap.entrySet())
        {
            if(entry.getValue().equals(name))
            {
                return entry.getKey();
            }
        }
        return null;
    }
}
