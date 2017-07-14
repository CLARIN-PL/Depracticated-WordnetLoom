package pl.edu.pwr.wordnetloom.client.utils;

import pl.edu.pwr.wordnetloom.corpusexample.service.CorpusExampleServiceRemote;
import pl.edu.pwr.wordnetloom.domain.service.DomainServiceRemote;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphExtensionServiceRemote;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphServiceRemote;
import pl.edu.pwr.wordnetloom.lexicon.service.LexiconServiceRemote;
import pl.edu.pwr.wordnetloom.partofspeech.service.PartOfSpeechServiceRemote;
import pl.edu.pwr.wordnetloom.relationtest.service.SynsetRelationTestServiceRemote;
import pl.edu.pwr.wordnetloom.relationtype.service.SenseRelationTypeServiceRemote;
import pl.edu.pwr.wordnetloom.relationtype.service.SynsetRelationTypeServiceRemote;
import pl.edu.pwr.wordnetloom.sense.service.SenseServiceRemote;
import pl.edu.pwr.wordnetloom.senserelation.service.SenseRelationServiceRemote;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceRemote;
import pl.edu.pwr.wordnetloom.synsetrelation.service.SynsetRelationServiceRemote;
import pl.edu.pwr.wordnetloom.wordform.service.WordFormServiceRemote;

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
    public static LexiconServiceRemote lexiconServiceRemote = RMIUtils.lookupForService(LexiconServiceRemote.class);
    public static PartOfSpeechServiceRemote partOfSpeechServiceRemote = RMIUtils.lookupForService(PartOfSpeechServiceRemote.class);
    public static CorpusExampleServiceRemote corpusExampleServiceRemote = RMIUtils.lookupForService(CorpusExampleServiceRemote.class);

    private RemoteUtils() {
    }
}
