package pl.edu.pwr.wordnetloom.localisation.service;

import pl.edu.pwr.wordnetloom.localisation.model.ApplicationLabel;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;

import java.util.List;
import java.util.Map;

public interface LocalisedStringServiceRemote {

    List<ApplicationLabel> findLabelsByLanguage(String locale);

    LocalisedString findStringsByKey(LocalisedKey key);

    List<LocalisedString> findAllStringsByLanguage(String pl);

    Map<Long, String> findAllByLanguageAsMap(String language);

    void update(LocalisedString ls);

    List<ApplicationLabel> findStringInAllLanguages(String key);

    ApplicationLabel save(ApplicationLabel label);

    ApplicationLabel find(String key, String language);
}
