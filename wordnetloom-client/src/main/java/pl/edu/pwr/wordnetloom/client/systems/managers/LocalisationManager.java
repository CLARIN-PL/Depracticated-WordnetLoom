package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.localisation.model.ApplicationLabel;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocalisationManager {

    private static volatile LocalisationManager instance = null;

    private final Map<String, String> labelsMap = new HashMap<>();
    private final Map<Long, String> localisedStringsMap = new HashMap<>();
    private static Locale locale;

    private LocalisationManager() {
    }

    public static LocalisationManager getInstance() {
        if (instance == null) {
            synchronized (LexiconManager.class) {
                instance = new LocalisationManager();
            }
        }
        return instance;
    }

    public void load(Language lang) {
        if (lang == null) {
            locale = new Locale(Language.English.getAbbreviation());
        } else {
            locale = new Locale(lang.getAbbreviation());
        }
        init(locale);
    }

    private void init(Locale locale) {
        List<ApplicationLabel> labels = RemoteService.localisedStringServiceRemote.findLabelsByLanguage(locale.getLanguage());
        for(ApplicationLabel label : labels){
            labelsMap.put(label.getKey(), label.getValue());
        }
        localisedStringsMap.putAll(RemoteService.localisedStringServiceRemote.findAllByLanguageAsMap(locale.getLanguage()));
    }

    public String getResource(String key) {
        if (labelsMap.containsKey(key)) {
            return labelsMap.get(key);
        }
        return "";
    }

    public String getLocalisedString(Long key) {
        if (localisedStringsMap.containsKey(key)) {
            return localisedStringsMap.get(key);
        }
        return "";
    }

    public void updateLocalisedString(Long key, String value){
        LocalisedString ls = RemoteService.localisedStringServiceRemote.findStringsByKey(new LocalisedKey(key, locale.getLanguage()));
        ls.setValue(value);
        RemoteService.localisedStringServiceRemote.update(ls);
        localisedStringsMap.replace(key, value);
    }


    public void removeLocalisationString(Long key){
        localisedStringsMap.remove(key);
    }
}
