package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da;

import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import java.util.List;

//TODO REFAKTOR eliminacj tej klasy
public class LexicalDA {

    private LexicalDA() {
    }

    /**
     * odczytanie jednostek z bazy danych w trybie pelnym
     *
     * @param testLemma - filtr
     * @param lexicons
     * @return lita jednostek
     */
    public static List<Sense> getFullLexicalUnits(String testLemma, List<Long> lexicons) {
        return null; //RemoteUtils.lexicalUnitRemote.dbFullGetUnits(testLemma, lexicons);
    }

    /**
     * odczytanie czesci mowy synsetu
     *
     * @param synset   - synset
     * @param lexicons
     * @return czesc mowy
     */
    public static PartOfSpeech getPos(Synset synset, List<Long> lexicons) {
        return null;//RemoteUtils.synsetRemote.dbGetPos(synset, lexicons);
    }

    /**
     * odswiezenie synsetu
     *
     * @param synset - synset
     * @return Synset
     */
    public static Synset refresh(Synset synset) {
        return null;// RemoteUtils.synsetRemote.dbGet(synset.getId());
    }

    /**
     * odswiezenie jednostki
     *
     * @param unit - unit
     * @return Sense
     */
    public static Sense refresh(Sense unit) {
        return null; //RemoteUtils.lexicalUnitRemote.dbGet(unit.getId());
    }

    /**
     * odczytanie domeny jednostki z bazy danych
     *
     * @param unit - jednostka
     * @return domena
     */
    public static Domain getDomain(Sense unit) {
        // unit = RemoteUtils.lexicalUnitRemote.dbGet(unit.getId());
        return unit.getDomain();
    }

    /**
     * Sprawdza, czy jednostka należy do jakiegokolwiek synsetu
     *
     * @param unit - jednostka
     * @return TRUE jeśli należy do synsetu
     */
    public static boolean checkIfInAnySynset(Sense unit) {
        return false;// RemoteUtils.lexicalUnitRemote.dbInAnySynset(unit);
    }
}
