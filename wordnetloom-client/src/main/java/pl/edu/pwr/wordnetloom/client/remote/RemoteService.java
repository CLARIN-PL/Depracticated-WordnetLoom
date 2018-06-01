package pl.edu.pwr.wordnetloom.client.remote;

import pl.edu.pwr.wordnetloom.corpusexample.service.CorpusExampleServiceRemote;
import pl.edu.pwr.wordnetloom.dictionary.service.DictionaryServiceRemote;
import pl.edu.pwr.wordnetloom.domain.service.DomainServiceRemote;
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

    public static SenseRelationServiceRemote senseRelationRemote = ConnectionProvider.getInstance().lookupForService(SenseRelationServiceRemote.class);
    public static SenseServiceRemote senseRemote = ConnectionProvider.getInstance().lookupForService(SenseServiceRemote.class);
    public static RelationTypeServiceRemote relationTypeRemote = ConnectionProvider.getInstance().lookupForService(RelationTypeServiceRemote.class);
    public static SynsetRelationServiceRemote synsetRelationRemote = ConnectionProvider.getInstance().lookupForService(SynsetRelationServiceRemote.class);
    public static SynsetServiceRemote synsetRemote = ConnectionProvider.getInstance().lookupForService(SynsetServiceRemote.class);
    public static RelationTestServiceRemote relationTestRemote = ConnectionProvider.getInstance().lookupForService(RelationTestServiceRemote.class);
    public static DomainServiceRemote domainServiceRemote = ConnectionProvider.getInstance().lookupForService(DomainServiceRemote.class);
    public static LexiconServiceRemote lexiconServiceRemote = ConnectionProvider.getInstance().lookupForService(LexiconServiceRemote.class);
    public static PartOfSpeechServiceRemote partOfSpeechServiceRemote = ConnectionProvider.getInstance().lookupForService(PartOfSpeechServiceRemote.class);
    public static CorpusExampleServiceRemote corpusExampleServiceRemote = ConnectionProvider.getInstance().lookupForService(CorpusExampleServiceRemote.class);
    public static UserServiceRemote userServiceRemote = ConnectionProvider.getInstance().lookupForService(UserServiceRemote.class);
    public static LocalisedStringServiceRemote localisedStringServiceRemote = ConnectionProvider.getInstance().lookupForService(LocalisedStringServiceRemote.class);
    public static DictionaryServiceRemote dictionaryServiceRemote = ConnectionProvider.getInstance().lookupForService(DictionaryServiceRemote.class);
    public static WordFormServiceRemote wordFormServiceRemote = ConnectionProvider.getInstance().lookupForService(WordFormServiceRemote.class);

    private RemoteService() {
    }
}
