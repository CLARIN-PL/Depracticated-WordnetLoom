package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events;

import pl.edu.pwr.wordnetloom.synset.model.Synset;

public class UpdateGraphAfterRemovingEvent {

    private Synset synset;

    public UpdateGraphAfterRemovingEvent(Synset synset){
        this.synset = synset;
    }

    public Synset getSynset(){
        return synset;
    }
}
