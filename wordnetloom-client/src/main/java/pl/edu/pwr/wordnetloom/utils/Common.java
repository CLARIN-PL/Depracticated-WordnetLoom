package pl.edu.pwr.wordnetloom.utils;

import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Synset;

/**
 * Klasa przechowująca stałe, które zostały przeniesione
 * do serwera.
 * 
 * @author Piotr Giedziun
 *
 */
public class Common {

	public static String getSenseAttribute(Sense sense, String key){
		return RemoteUtils.dynamicAttributesRemote.getSenseAttribute(sense, key);
	}

	public static String getSynsetAttribute(Synset synset, String key){
		return RemoteUtils.dynamicAttributesRemote.getSynsetAttribute(synset, key);
	}

}
