package pl.edu.pwr.wordnetloom.application.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service
public class RemoteService {

    @Autowired
    EJBConnectionProviderService service;

    public SenseServiceRemote senseServiceRemote() {
        return service.lookupForService(SenseServiceRemote.class);
    }

    public ExtGraphExtensionServiceRemote extGraphExtensionRemote() {
        return service.lookupForService(ExtGraphExtensionServiceRemote.class);
    }

    public ExtGraphServiceRemote extGraphRemote() {
        return service.lookupForService(ExtGraphServiceRemote.class);
    }

    public SenseRelationServiceRemote senseRelationRemote() {
        return service.lookupForService(SenseRelationServiceRemote.class);
    }

    public RelationTypeServiceRemote relationTypeRemote() {
        return service.lookupForService(RelationTypeServiceRemote.class);
    }

    public SynsetRelationServiceRemote synsetRelationRemote() {
        return service.lookupForService(SynsetRelationServiceRemote.class);
    }

    public SynsetServiceRemote synsetRemote() {
        return service.lookupForService(SynsetServiceRemote.class);
    }

    public RelationTestServiceRemote relationTestRemote() {
        return service.lookupForService(RelationTestServiceRemote.class);
    }

    public WordFormServiceRemote wordFormsRemote() {
        return service.lookupForService(WordFormServiceRemote.class);
    }

    public DomainServiceRemote domainServiceRemote() {
        return service.lookupForService(DomainServiceRemote.class);
    }

    public LexiconServiceRemote lexiconServiceRemote() {
        return service.lookupForService(LexiconServiceRemote.class);
    }

    public PartOfSpeechServiceRemote partOfSpeechServiceRemot() {
        return service.lookupForService(PartOfSpeechServiceRemote.class);
    }

    public CorpusExampleServiceRemote corpusExampleServiceRemote() {
        return service.lookupForService(CorpusExampleServiceRemote.class);
    }

    public UserServiceRemote userServiceRemote() {
        return service.lookupForService(UserServiceRemote.class);
    }

    public LocalisedStringServiceRemote localisedStringServiceRemote() {
        return service.lookupForService(LocalisedStringServiceRemote.class);
    }
}
