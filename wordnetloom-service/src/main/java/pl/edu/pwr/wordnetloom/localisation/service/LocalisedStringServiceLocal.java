package pl.edu.pwr.wordnetloom.localisation.service;

import java.util.Map;

public interface LocalisedStringServiceLocal extends LocalisedStringServiceRemote {

    Map<String, Map<Long, String>> finaAll();

}
