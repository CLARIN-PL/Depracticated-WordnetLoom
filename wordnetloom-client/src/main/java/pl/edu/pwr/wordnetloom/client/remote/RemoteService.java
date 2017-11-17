package pl.edu.pwr.wordnetloom.client.remote;

import pl.edu.pwr.wordnetloom.corpusexample.service.CorpusExampleServiceRemote;
import pl.edu.pwr.wordnetloom.domain.service.DomainServiceRemote;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphExtensionServiceRemote;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphServiceRemote;
import pl.edu.pwr.wordnetloom.lexicon.service.LexiconServiceRemote;
import pl.edu.pwr.wordnetloom.localisation.service.LocalisedStringServiceRemote;
import pl.edu.pwr.wordnetloom.partofspeech.service.PartOfSpeechServiceRemote;
import pl.edu.pwr.wordnetloom.relationtest.service.RelationTestServiceRemote;
import pl.edu.pwr.wordnetloom.relationtype.service.RelationTypeServiceRemote;
import pl.edu.pwr.wordnetloom.sense.service.SenseServiceRemote;
import pl.edu.pwr.wordnetloom.senserelation.service.SenseRelationServiceRemote;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceRemote;
import pl.edu.pwr.wordnetloom.synsetrelation.service.SynsetRelationServiceRemote;
import pl.edu.pwr.wordnetloom.user.service.UserServiceRemote;
import pl.edu.pwr.wordnetloom.wordform.service.WordFormServiceRemote;

public final class RemoteService {

    public static ExtGraphExtensionServiceRemote extGraphExtensionRemote = RemoteConnectionProvider.getInstance().lookupForService(ExtGraphExtensionServiceRemote.class);
    public static ExtGraphServiceRemote extGraphRemote = RemoteConnectionProvider.getInstance().lookupForService(ExtGraphServiceRemote.class);
    public static SenseRelationServiceRemote senseRelationRemote = RemoteConnectionProvider.getInstance().lookupForService(SenseRelationServiceRemote.class);
    public static SenseServiceRemote senseRemote = RemoteConnectionProvider.getInstance().lookupForService(SenseServiceRemote.class);
    public static RelationTypeServiceRemote relationTypeRemote = RemoteConnectionProvider.getInstance().lookupForService(RelationTypeServiceRemote.class);
    public static SynsetRelationServiceRemote synsetRelationRemote = RemoteConnectionProvider.getInstance().lookupForService(SynsetRelationServiceRemote.class);
    public static SynsetServiceRemote synsetRemote = RemoteConnectionProvider.getInstance().lookupForService(SynsetServiceRemote.class);
    public static RelationTestServiceRemote relationTestRemote = RemoteConnectionProvider.getInstance().lookupForService(RelationTestServiceRemote.class);
    public static WordFormServiceRemote wordFormsRemote = RemoteConnectionProvider.getInstance().lookupForService(WordFormServiceRemote.class);
    public static DomainServiceRemote domainServiceRemote = RemoteConnectionProvider.getInstance().lookupForService(DomainServiceRemote.class);
    public static LexiconServiceRemote lexiconServiceRemote = RemoteConnectionProvider.getInstance().lookupForService(LexiconServiceRemote.class);
    public static PartOfSpeechServiceRemote partOfSpeechServiceRemote = RemoteConnectionProvider.getInstance().lookupForService(PartOfSpeechServiceRemote.class);
    public static CorpusExampleServiceRemote corpusExampleServiceRemote = RemoteConnectionProvider.getInstance().lookupForService(CorpusExampleServiceRemote.class);
    public static UserServiceRemote userServiceRemote = RemoteConnectionProvider.getInstance().lookupForService(UserServiceRemote.class);
    public static LocalisedStringServiceRemote localisedStringServiceRemote = RemoteConnectionProvider.getInstance().lookupForService(LocalisedStringServiceRemote.class);


    private RemoteService() {
    }
}
