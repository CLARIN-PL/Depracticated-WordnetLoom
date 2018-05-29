package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events;

import pl.edu.pwr.wordnetloom.sense.model.Sense;

public class UpdateGraphEvent {

    private Sense sense;

    public UpdateGraphEvent(Sense sense) {
        this.sense = sense;

    }

    public Sense getSense(){
        return sense;
    }

}
