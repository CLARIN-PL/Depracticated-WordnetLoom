package pl.edu.pwr.wordnetloom.localisation;

import pl.edu.pwr.wordnetloom.localisation.service.LocalisedStringServiceLocal;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;

@Startup
@Singleton
public class LocalisedStringsService {

    @Inject
    LocalisedStringServiceLocal local;

    Map<String, Map<Long, String>> localisedStrings;

    @PostConstruct
    void init() {
        localisedStrings = local.finaAll();
    }

    public String findString(final Long id, final Locale locale){
        String lang = "pl";

        if(locale != null){
            String l = locale.getLanguage().substring(0,2);
            lang = localisedStrings.containsKey(l) ? l : "pl";
        }

        if(localisedStrings.get(lang).containsKey(id)){
            return localisedStrings.get(lang).get(id);
        }
        return id != null ? id.toString() : null;
    }
}
