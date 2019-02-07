package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events;

import pl.edu.pwr.wordnetloom.sense.model.Sense;

public class UpdateUnitRelationsEvent {

    private Sense sense;

    public UpdateUnitRelationsEvent(Sense sense){
        this.sense = sense;
    }

    public Sense getSense(){
        return sense;
    }
}
