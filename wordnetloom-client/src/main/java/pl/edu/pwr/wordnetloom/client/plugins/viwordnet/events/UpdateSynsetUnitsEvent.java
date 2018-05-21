package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events;

import pl.edu.pwr.wordnetloom.synset.model.Synset;

public class UpdateSynsetUnitsEvent {

    private Synset synset;

    public UpdateSynsetUnitsEvent(Synset synset){
        this.synset = synset;
    }

    public Synset getSynset(){
        return synset;
    }
}
