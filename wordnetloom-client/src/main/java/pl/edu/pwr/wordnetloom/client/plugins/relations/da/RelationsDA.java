package pl.edu.pwr.wordnetloom.client.plugins.relations.da;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.NodeDrawer;
import pl.edu.pwr.wordnetloom.client.systems.progress.AbstractProgressThread;
import pl.edu.pwr.wordnetloom.client.systems.progress.ProgressFrame;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import java.util.*;

/**
 * klasa pośrednicząca w wymianie danych
 *
 * @author Max
 */
public class RelationsDA {

    static String RELATION_TREE_BUILDING = "Budowanie drzewa relacji";
    static String RELATION_TREE_DATA_READING = "Odczyt danych";

    private RelationsDA() {
    }

    /**
     * odczytane listy synsetow spelnijacych filtr
     *
     * @param filterText   - filtr nazw
     * @param domainStr    - domena do filtrowania
     * @param relationType - typ relacji jakie muszą byc zdefiniowane dla
     *                     wynikowych synsetow
     * @param limitSize    - maksymalna liczba zwroconych elementów
     * @param lexicon
     * @return kolekcja z danymi
     */
    static public Collection<Synset> getSynsets(String filterText, String domainStr, RelationType relationType, int limitSize, List<Long> lexicon) {
        return RelationsDA.getSynsets(filterText, domainStr, relationType, limitSize, limitSize, lexicon);
    }

    /**
     * odczytane listy synsetow spelnijacych filtr, dodane filtrowanie po części
     * mowy
     *
     * @param filterText   - filtr nazw
     * @param domainStr    - domena do filtrowania
     * @param relationType - typ relacji jakie muszą byc zdefiniowane dla
     *                     wynikowych synsetow
     * @param limitSize    - maksymalna liczba zwroconych elementów
     * @param posIndex     - indeks części mowy (-1 wszystkie, 0 nieznany, itd.
     *                     zgodnie z enum Pos)
     * @param lexicons
     * @return kolekcja z danymi
     */
    static public Collection<Synset> getSynsets(String filterText, String domainStr, RelationType relationType, int limitSize, int posIndex, List<Long> lexicons) {
        Domain domain = null;
        if (domainStr != null) {
            domain = DomainManager.getInstance().decode(domainStr);
        }
        List<Synset> synsets = new ArrayList<>();//RemoteUtils.synsetRemote.dbFastGetSynsets(filterText, domain, relationType, limitSize, posIndex, lexicons);

        return synsets;
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
     * usuniecie relacji
     *
     * @param relation - relacja do usuniecia
     */
    public static void delete(SynsetRelation relation) {
        //RemoteUtils.synsetRelationRemote.dbDelete(relation);
    }

    /**
     * sprawdzenie czy podana relacja już istnieje
     *
     * @param parent - element nadrzedny
     * @param child  - element podrzedny
     * @param rel    - typ relacji
     * @return TRUE jesli relacja istnieje
     */
    public static boolean checkIfRelationExists(Synset parent, Synset child, RelationType rel) {
        return false; //RemoteUtils.synsetRelationRemote.dbRelationExists(parent, child, rel);
    }

    /**
     * utworzenie relacji
     *
     * @param parent - element nadrzedny
     * @param child  - element podrzedny
     * @param rel    - typ relacji
     */
    public static void makeRelation(Synset parent, Synset child, RelationType rel) {
        if (parent == null || child == null || parent.getId() == -1 || child.getId() == -1) {
            return;
        }

        //RemoteUtils.synsetRelationRemote.dbMakeRelation(parent, child, rel);
    }

    /**
     * odczytanie pierwszej jednostki synsetu
     *
     * @param synset - synset
     * @return pierwsza jednostka
     */
    public static Sense getFirstUnit(Synset synset) {
//        Collection<Sense> units = RemoteUtils.lexicalUnitRemote.dbFullGetUnits(synset, 1, LexiconManager.getInstance().getLexicons());
//        for (Sense unit : units) {
//            return unit;
//        }
        return null;
    }

    /**
     * utworzenie nowego synsetu
     *
     * @return nowy synset
     */
    public static Synset newSynset() {
        Synset newSynset = new Synset();
        newSynset.setSplit(1);
        return null;//RemoteUtils.synsetRemote.updateSynset(newSynset);
    }

    /**
     * odczytanie czesci mowy synsetu
     *
     * @param synset   - synset
     * @param lexicons
     * @return czesc mowy
     */
    public static PartOfSpeech getPos(Synset synset, List<Long> lexicons) {
        return null; //RemoteUtils.synsetRemote.dbGetPos(synset, lexicons);
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

    /**
     * odczytanie jednostek synsetu
     *
     * @param synset       - synset
     * @param ignoredUnits - lista jednostke ktore maja zostac zignorowane przy
     *                     oczycie
     * @param lexicons
     * @return jednostki synsetu
     */
    public static Collection<Sense> getUnits(Synset synset, Collection<Sense> ignoredUnits, List<Long> lexicons) {
        Collection<Sense> temp = getUnits(synset, lexicons);
        Collection<Sense> result = new ArrayList<>();
        // sprawdzenie wszytkich jednostek
        for (Sense synsetUnit : temp) {
            boolean found = false;
            for (Sense ignoredUnit : ignoredUnits) {
                if (synsetUnit.equals(ignoredUnit)) { // czy nie sa takie same
                    found = true;
                    break;
                }
            }
            if (!found) {
                result.add(synsetUnit);
            }
        }
        return result;
    }

    /**
     * odczytanie relacji odwrotnej
     *
     * @param rel - relacja
     * @return relacja odwrotna
     */
    public static RelationType getReverseRelation(RelationType rel) {
//        SynsetRelationType reverse = RemoteUtils.relationTypeRemote.dbGetReverseByRelationType(rel);
//        return RemoteUtils.relationTypeRemote.dbGet(reverse.getId());
        return null;
    }

    /**
     * odczytanie opisu relacji
     *
     * @param rel - relacja dla ktorej ma zostac odczytany opis
     * @return pelna nazwa relacji
     */
    public static String getRelationName(RelationType rel) {
        if (rel != null) {
            if (rel.getParent() != null) { // ma rodzica
//                SynsetRelationType parent = RemoteUtils.relationTypeRemote.dbGet(rel.getParent().getId());
//                parent = RelationTypeManager.get(parent.getId()).getRelationType();
//
//                if (parent != null) {
//                    return parent.getName() + " / " + rel.getName();
//                }
            }
            return RelationTypeManager.getInstance().getFullName(rel.getId());
        }
        return null;
    }

    /**
     * odczytanie wszystkich typow relacji dla danego synsetu ktorymi jest
     * zwiazany z innymi
     *
     * @param synset - synset
     * @return lista typow relacji
     */
    public static Collection<RelationType> getRelationsTypes(Synset synset) {
        Collection<RelationType> relations = null;
        if (synset != null) {
            //relations = RemoteUtils.synsetRelationRemote.dbGetRelationTypesOfSynset(synset);
        } else {
            relations = new ArrayList<>();
        }

        return relations;
    }

    static void buildRelationsTreeIdx(Long synsetId, Collection<SynsetRelation> relations, Collection<Long> usedSynsetsID, ArrayList<Long> idx) {
        if (synsetId == null) {
            return;
        }
        idx.add(synsetId);
        if (!usedSynsetsID.contains(synsetId)) {
            usedSynsetsID.add(synsetId);

            for (SynsetRelation item : relations) {
                if (item.getParent().getId().longValue() == synsetId.longValue()) {
                    buildRelationsTreeIdx(item.getChild().getId(), relations, usedSynsetsID, idx); // zbudowanie podrzednego
                }
            }
        }
    }

    /**
     * zbudowanie drzewa relacji synsetow
     *
     * @param synsetId
     * @param relations
     * @param usedSynsetsID
     * @param synsetsDescriptions
     * @param progress             - okno z informacja o postepie
     * @param refreshProgressDepth
     * @return drzewo relacji synsetow
     */
    static NodeDrawer buildRelationsTree(Long synsetId, Collection<SynsetRelation> relations, Collection<Long> usedSynsetsID, Map<Long, String> synsetsDescriptions, ProgressFrame progress, int refreshProgressDepth) {
        if (synsetId == null) {
            return new NodeDrawer("---");
        }
        String desc = synsetsDescriptions.get(synsetId);
        if (desc == null) {
            return new NodeDrawer("---");
        }
        NodeDrawer mainNode = new NodeDrawer(desc);
        if (refreshProgressDepth > 0) {
            progress.setProgressParams(100, 100, desc);
        }
        mainNode.setTag(synsetId);
        if (!usedSynsetsID.contains(synsetId)) {
            usedSynsetsID.add(synsetId);

            for (SynsetRelation item : relations) {
                if (item.getParent().getId().longValue() == synsetId.longValue()) {
                    NodeDrawer subItem = buildRelationsTree(item.getChild().getId(), relations, usedSynsetsID, synsetsDescriptions, progress, refreshProgressDepth - 1); // zbudowanie podrzednego
                    mainNode.addNode(subItem); // dodanie do drzewa
                    if (progress.isCanceled()) {
                        break;
                    }
                }
            }
        }
        return mainNode;
    }

    /**
     * zbudowanie drzewa relacji synsetow
     * <p>
     * Funkcja zmodyfikowana w celu redukcji pobieranych rekordów synset
     * description. Pobierane są tylko potrzebne ID, zamiast całej listy.
     *
     * @param parent
     * @param synset
     * @param relationType
     * @param lexicons
     * @return drzewo relacji synsetow
     */
    public static NodeDrawer buildRelationsTree(WebFrame parent, Synset synset, RelationType relationType, List<Long> lexicons) {
        AbstractProgressThread progress = new AbstractProgressThread(parent, RELATION_TREE_BUILDING, null, true) {
            @Override
            protected void mainProcess() {
                progress.setGlobalProgressParams(1, 2);
                progress.setProgressParams(0, 100, RELATION_TREE_DATA_READING);

                // odczytanie relacji aby miec w buforze
                //  Collection<SynsetRelation> relations = RemoteUtils.synsetRelationRemote.dbFastGetRelations(relationType);

                // przerobienie kolekcji na hashmape
                HashMap<Long, ArrayList<Long>> map = new HashMap<>();
//                for (SynsetRelation item : relations) {
//                    ArrayList<Long> values = map.get(item.getSynsetFrom().getId());
//                    if (values == null) {
//                        values = new ArrayList<>();
//                        map.put(item.getSynsetFrom().getId(), values);
//                    }
//                    values.add(item.getSynsetTo().getId());
//                }

                // generate synsets IDx
                ArrayList<Long> idx = new ArrayList<>();
                // buildRelationsTreeIdx(synset != null ? synset.getId() : null, relations, new ArrayList<>(), idx);

                //Map<Long, String> synsetsDescriptions = RemoteUtils.synsetRemote.dbGetSynsetsDescriptionIdx(idx, lexicons);

                // this.tag = buildRelationsTree(synset != null ? synset.getId() : null, relations, new ArrayList<>(), synsetsDescriptions, progress, 2);
            }
        };
        if (progress.getTag() != null) {
            return (NodeDrawer) progress.getTag();
        }
        return new NodeDrawer("---");
    }

    public static void mergeUnits(Sense src, Sense dst) {
        if (src == null || dst == null || src.getId() == -1 || dst.getId() == -1) {
            return;
        }

        throw new RuntimeException("//TODO: FIXME"); // TODO: FIXME
    }

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

    /**
     * odczytanie domeny jednostki z bazy danych
     *
     * @param unit - jednostka
     * @return domena
     */
    public static Domain getDomain(Sense unit) {
        // RemoteUtils.lexicalUnitRemote.dbGet(unit.getId());
        return unit.getDomain();
    }

    public static Synset refreshSynset(Synset synset) {
        return null; //RemoteUtils.synsetRemote.dbGet(synset.getId());
    }
}
