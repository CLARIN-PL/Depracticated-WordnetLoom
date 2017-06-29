package pl.edu.pwr.wordnetloom.client.utils;

import pl.edu.pwr.wordnetloom.service.DynamicAttributesServiceRemote;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphExtensionServiceRemote;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphServiceRemote;
import pl.edu.pwr.wordnetloom.relation.service.LexicalRelationServiceRemote;
import pl.edu.pwr.wordnetloom.sense.service.LexicalUnitServiceRemote;
import pl.edu.pwr.wordnetloom.common.service.NativeServiceRemote;
import pl.edu.pwr.wordnetloom.relation.service.RelationTypeServiceRemote;
import pl.edu.pwr.wordnetloom.synsetrelation.service.SynsetRelationServiceRemote;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceRemote;
import pl.edu.pwr.wordnetloom.relation.service.TestServiceRemote;
import pl.edu.pwr.wordnetloom.tracker.TrackerServiceRemote;
import pl.edu.pwr.wordnetloom.synset.service.UnitAndSynsetServiceRemote;
import pl.edu.pwr.wordnetloom.relation.service.UnitsRelationsServiceRemote;
import pl.edu.pwr.wordnetloom.service.WordFormServiceRemote;

/**
 * Klasa przechowuje powiązania do wszystkich remotów. Jej użycie nie jest
 * wymagane, ma za zadanie uprościć proces przepisywania aplikacji na interfacy.
 * Dzieki zastosowaniu tego roziwązania nie wymagają każdorazowej inicjalizacji.
 *
 * @author Piotr Giedziun
 *
 */
public class RemoteUtils {

    public static ExtGraphExtensionServiceRemote extGraphExtensionRemote = RMIUtils.lookupForService(ExtGraphExtensionServiceRemote.class);
    public static ExtGraphServiceRemote extGraphRemote = RMIUtils.lookupForService(ExtGraphServiceRemote.class);
    public static LexicalRelationServiceRemote lexicalRelationRemote = RMIUtils.lookupForService(LexicalRelationServiceRemote.class);
    public static LexicalUnitServiceRemote lexicalUnitRemote = RMIUtils.lookupForService(LexicalUnitServiceRemote.class);
    public static RelationTypeServiceRemote relationTypeRemote = RMIUtils.lookupForService(RelationTypeServiceRemote.class);
    public static SynsetRelationServiceRemote synsetRelationRemote = RMIUtils.lookupForService(SynsetRelationServiceRemote.class);
    public static SynsetServiceRemote synsetRemote = RMIUtils.lookupForService(SynsetServiceRemote.class);
    public static TestServiceRemote testRemote = RMIUtils.lookupForService(TestServiceRemote.class);
    public static UnitAndSynsetServiceRemote unitAndSynsetRemote = RMIUtils.lookupForService(UnitAndSynsetServiceRemote.class);
    public static WordFormServiceRemote wordFormsRemote = RMIUtils.lookupForService(WordFormServiceRemote.class);
    public static UnitsRelationsServiceRemote unitsRelationsRemote = RMIUtils.lookupForService(UnitsRelationsServiceRemote.class);
    public static DynamicAttributesServiceRemote dynamicAttributesRemote = RMIUtils.lookupForService(DynamicAttributesServiceRemote.class);
    public static TrackerServiceRemote trackerRemote = RMIUtils.lookupForService(TrackerServiceRemote.class);
    public static NativeServiceRemote nativeRemote = RMIUtils.lookupForService(NativeServiceRemote.class);

    private RemoteUtils() {
    }
}
