package pl.edu.pwr.wordnetloom.client.utils;

import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

public class Common {

    public static String getSenseAttribute(Sense sense, String key) {
        return RemoteUtils.dynamicAttributesRemote.getSenseAttribute(sense, key);
    }

    public static String getSynsetAttribute(Synset synset, String key) {
        return RemoteUtils.dynamicAttributesRemote.getSynsetAttribute(synset, key);
    }

}
