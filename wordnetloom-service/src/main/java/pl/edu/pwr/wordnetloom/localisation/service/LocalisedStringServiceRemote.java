package pl.edu.pwr.wordnetloom.localisation.service;

import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;

import java.util.List;
import java.util.Map;

public interface LocalisedStringServiceRemote {

    Map<String, String> findLabelsByLanguage(String locale);

    LocalisedString findStringsByKey(LocalisedKey key);

    List<LocalisedString> findAllStringsByLanguage(String pl);

    Map<Long, String> findAllByLanguageAsMap(String language);

    void update(LocalisedString ls);
}
