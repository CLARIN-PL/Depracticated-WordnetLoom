package pl.edu.pwr.wordnetloom.client.plugins.relations.da;

import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

//TODO Refaktor do usuniecia
public class RelationsDA {

    private RelationsDA() {
    }


    /**
     * przeniesienie jednostek do istniejacego synsetu
     *
     * @param mainSynset    - glowny synset
     * @param selectedUnits - zaznaczone jednostki
     * @param descSynset    - docelowy synset
     */
    public static void moveUnitsToExistenSynset(Synset mainSynset, Collection<Sense> selectedUnits, Synset descSynset) {

        for (Sense unit : selectedUnits) {
            // RemoteUtils.unitAndSynsetRemote.dbDeleteConnection(unit, mainSynset);
            // RemoteUtils.unitAndSynsetRemote.dbAddConnection(unit, descSynset, false);
        }
    }

    /**
     * odczytanie jednostek synsetu
     *
     * @param synset   - sysnet
     * @param lexicons
     * @return jednostki synsetu
     */
    public static Collection<Sense> getUnits(Synset synset, List<Long> lexicons) {
        return null;//RemoteUtils.synsetRemote.dbFastGetUnits(synset, lexicons);
    }


    public static void mergeUnits(Sense src, Sense dst) {
        if (src == null || dst == null || src.getId() == -1 || dst.getId() == -1) {
            return;
        }

        throw new RuntimeException("//TODO: FIXME"); // TODO: FIXME
    }

    //TODO Nie działa łączenie synsetów
    public static void mergeSynsets(Synset src, Synset dst, List<Long> lexicons) {
        if (src == null || dst == null || src.getId() == -1 || dst.getId() == -1) {
            return;
        }

        HashMap<String, Sense> dst_lemma_to_str = new HashMap<>();
        for (Sense unit : RelationsDA.getUnits(dst, lexicons)) {
            dst_lemma_to_str.put(unit.getWord().getWord(), unit);
        }

        ArrayList<Sense> units_to_move = new ArrayList<>();

        RelationsDA.getUnits(src, lexicons).stream().forEach((unit_src) -> {
            Sense unit_dst = dst_lemma_to_str.get(unit_src.getWord());
            if (unit_dst == null) {
                units_to_move.add(unit_src);
            } else {
                mergeUnits(unit_src, unit_dst);
            }
        });

        RelationsDA.moveUnitsToExistenSynset(src, units_to_move, dst);
        //  RemoteUtils.synsetRemote.dbDelete(src);
    }

}
