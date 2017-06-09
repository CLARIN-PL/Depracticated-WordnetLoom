package pl.edu.pwr.wordnetloom.client.utils;

import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;

/**
 * Klasa przechowująca stałe, które zostały przeniesione do serwera.
 *
 * @author Piotr Giedziun
 *
 */
public class Common {

    public static String getSenseAttribute(Sense sense, String key) {
        return RemoteUtils.dynamicAttributesRemote.getSenseAttribute(sense, key);
    }

    public static String getSynsetAttribute(Synset synset, String key) {
        return RemoteUtils.dynamicAttributesRemote.getSynsetAttribute(synset, key);
    }

}
