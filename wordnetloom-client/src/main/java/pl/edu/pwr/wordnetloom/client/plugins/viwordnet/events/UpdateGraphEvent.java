package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events;

import pl.edu.pwr.wordnetloom.sense.model.Sense;

public class UpdateGraphEvent {

    private Sense sense;

    public UpdateGraphEvent(Sense synset) {
        this.sense = synset;

    }

    public Sense getSense(){
        return sense;
    }

}
