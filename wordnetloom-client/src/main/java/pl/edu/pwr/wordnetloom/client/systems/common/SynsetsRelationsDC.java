package pl.edu.pwr.wordnetloom.client.systems.common;

import java.util.ArrayList;
import java.util.Collection;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.relation.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relation.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

/**
 * klasa przechowujaca relacje oraz powiazana synsetow
 *
 * @author Max
 *
 */
@Deprecated
public class SynsetsRelationsDC extends AbstractRelationsDC<SynsetRelation> {

    /**
     * konstruktor
     *
     * @param relationType - typ relacji
     * @param relations - relacje
     */
    public SynsetsRelationsDC(RelationType relationType,
            Collection<SynsetRelation> relations) {
        super(relationType, relations);
    }

    /**
     * odczytanie relacji danego typu dla synsetow
     *
     * @param synset - synset
     * @param templateType - typ relacji albo NULL jesli brac wszystkie
     * @param synsetType - IS_PARENT oznacza, ze synset ma być jako nadrzedny,
     * IS_CHILD oznacza, ze ma byc jako podrzedny
     * @param hideAutoReverse - hide auto reverse relations
     * @return kolekcja relacji
     */
    static public Collection<SynsetsRelationsDC> dbGetRelations(Synset synset,
            RelationType templateType, int synsetType, boolean hideAutoReverse) {
        if (synset == null) {
            return new ArrayList<SynsetsRelationsDC>();
        }
        // kolekcja wszystkich relacji dla danego synsetu
        Collection<SynsetRelation> relationsOfSynset;
        if (synsetType == SynsetsRelationsDC.IS_PARENT) {
            relationsOfSynset = RemoteUtils.synsetRelationRemote
                    .dbGetSubRelations(synset, templateType, LexiconManager
                            .getInstance().getLexicons());
        } else {
            relationsOfSynset = RemoteUtils.synsetRelationRemote
                    .dbGetUpperRelations(synset, templateType, LexiconManager
                            .getInstance().getLexicons());
        }
        Collection<SynsetsRelationsDC> result = new ArrayList<SynsetsRelationsDC>();
        Collection<RelationType> relationTypes = new ArrayList<RelationType>();
        // wiecej niz jedna relacja
        if (templateType == null) {
            relationTypes.addAll(RemoteUtils.relationTypeRemote.dbGetLeafs(
                    RelationArgument.SYNSET, LexiconManager.getInstance()
                    .getLexicons()));
        } else {
            relationTypes.add(templateType);
        }

        if (relationsOfSynset.size() > 0) {
            for (RelationType relType : relationTypes) {
                // Hide auto reverse relations
                if (hideAutoReverse
                        && RemoteUtils.relationTypeRemote.isReverseRelation(
                                relationTypes, relType)) {
                    continue;
                }

                Collection<SynsetRelation> finalRelations = new ArrayList<SynsetRelation>();
                for (SynsetRelation synsetRelation : relationsOfSynset) { // po
                    // wsystkich
                    // typach
                    // jest wlasciwego typu, tj czy maja ten sam typ
                    if (synsetRelation.getRelation().getId().longValue() == relType
                            .getId().longValue()) {
                        finalRelations.add(synsetRelation); // dodanie dolisty

                    }
                }
                if (finalRelations.size() > 0) {
                    result.add(new SynsetsRelationsDC(relType, finalRelations));
                }
            }
        }
        return result;
    }

    /**
     * odczytanie relacji danego typu dla synsetow, odczytywany jest tylko id
     * oraz opis relacji
     *
     * Funkcja została zoptymalizowana - nie pobiera już wszystkich (235k)
     * unitsstr. Pierwsza iteracja ustala używane ID, następnie generowany jest
     * select pobierający je.
     *
     * @param synset - synset
     * @param synsetType - IS_PARENT oznacza, ze synset ma być jako nadrzedny,
     * IS_CHILD oznacza, ze ma byc jako podrzedny
     * @param hideAutoReverse - hide auto reverse relations
     * @return kolekcja relacji
     */
    static public Collection<SynsetsRelationsDC> dbFastGetRelations(
            Synset synset, int synsetType, boolean hideAutoReverse) {
        if (synset == null) {
            return new ArrayList<SynsetsRelationsDC>();
        }

        // kolekcja wszystkich relacji dla danego synsetu
        Collection<SynsetRelation> relationsOfSynset;
        if (synsetType == SynsetsRelationsDC.IS_PARENT) {
            relationsOfSynset = RemoteUtils.synsetRelationRemote
                    .dbGetSubRelations(synset, null, LexiconManager
                            .getInstance().getLexicons());
        } else {
            relationsOfSynset = RemoteUtils.synsetRelationRemote
                    .dbGetUpperRelations(synset, null, LexiconManager
                            .getInstance().getLexicons());
        }
        Collection<SynsetsRelationsDC> result = new ArrayList<SynsetsRelationsDC>();

        if (relationsOfSynset.size() > 0) {
            Collection<RelationType> relTypesList = RemoteUtils.relationTypeRemote
                    .dbGetLeafs(RelationArgument.SYNSET, LexiconManager
                            .getInstance().getLexicons());

            /**
             * Ustalenie listy sysnetów, dla których trzeba będzie poprać
             * unitsstr.
             */
            ArrayList<Long> idx = new ArrayList<Long>();
            for (RelationType relType : relTypesList) {
                // Hide auto reverse relations
                if (hideAutoReverse
                        && RemoteUtils.relationTypeRemote.isReverseRelation(
                                relTypesList, relType)) {
                    continue;
                }
                for (SynsetRelation synsetRelation : relationsOfSynset) {
                    if (synsetRelation.getRelation().getId().longValue() == relType
                            .getId().longValue()) {
                        idx.add(synsetRelation.getSynsetTo().getId());
                        idx.add(synsetRelation.getSynsetFrom().getId());
                    }
                }
            }

            for (RelationType relType : relTypesList) {
                // Hide auto reverse relations
                if (hideAutoReverse
                        && RemoteUtils.relationTypeRemote.isReverseRelation(
                                relTypesList, relType)) {
                    continue;
                }

                Collection<SynsetRelation> finalRelations = new ArrayList<SynsetRelation>();
                for (SynsetRelation synsetRelation : relationsOfSynset) { // po
                    // wsystkich
                    // relacjach
                    // jest wlasciwego typu, tj czy maja ten sam typ
                    if (synsetRelation.getRelation().getId().longValue() == relType
                            .getId().longValue()) {
                        // odczytanie nadrzednego elementu relacji
                        Synset parentItem = new Synset();
                        parentItem
                                .setId(synsetRelation.getSynsetFrom().getId());
                        synsetRelation.setSynsetFrom(parentItem);

                        // oczytanie podrzednego elementu relacji
                        Synset childItem = new Synset();
                        childItem.setId(synsetRelation.getSynsetTo().getId());
                        synsetRelation.setSynsetTo(childItem);

                        // ustawienie typu relacji
                        synsetRelation.setRelation(relType);
                        finalRelations.add(synsetRelation); // dodanie dolisty
                    }
                }
                if (finalRelations.size() > 0) {
                    result.add(new SynsetsRelationsDC(relType, finalRelations));
                }
            }
        }
        return result;
    }
}
