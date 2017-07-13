package pl.edu.pwr.wordnetloom.client.utils;

import pl.edu.pwr.wordnetloom.domain.service.DomainServiceRemote;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphExtensionServiceRemote;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphServiceRemote;
import pl.edu.pwr.wordnetloom.relationtest.service.SynsetRelationTestServiceRemote;
import pl.edu.pwr.wordnetloom.relationtype.service.SenseRelationTypeServiceRemote;
import pl.edu.pwr.wordnetloom.relationtype.service.SynsetRelationTypeServiceRemote;
import pl.edu.pwr.wordnetloom.sense.service.SenseServiceRemote;
import pl.edu.pwr.wordnetloom.senserelation.service.SenseRelationServiceRemote;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceRemote;
import pl.edu.pwr.wordnetloom.synsetrelation.service.SynsetRelationServiceRemote;
import pl.edu.pwr.wordnetloom.wordform.service.WordFormServiceRemote;

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
    public static SenseRelationServiceRemote senseRelationRemote = RMIUtils.lookupForService(SenseRelationServiceRemote.class);
    public static SenseServiceRemote senseRemote = RMIUtils.lookupForService(SenseServiceRemote.class);
    public static SynsetRelationTypeServiceRemote synsetRelationTypeRemote = RMIUtils.lookupForService(SynsetRelationTypeServiceRemote.class);
    public static SynsetRelationServiceRemote synsetRelationRemote = RMIUtils.lookupForService(SynsetRelationServiceRemote.class);
    public static SynsetServiceRemote synsetRemote = RMIUtils.lookupForService(SynsetServiceRemote.class);
    public static SynsetRelationTestServiceRemote synsetRelationTestRemote = RMIUtils.lookupForService(SynsetRelationTestServiceRemote.class);
    public static WordFormServiceRemote wordFormsRemote = RMIUtils.lookupForService(WordFormServiceRemote.class);
    public static SenseRelationTypeServiceRemote senseRelationTypeRemote = RMIUtils.lookupForService(SenseRelationTypeServiceRemote.class);
    public static DomainServiceRemote domainServiceRemote = RMIUtils.lookupForService(DomainServiceRemote.class);
   
    private RemoteUtils() {
    }
}
