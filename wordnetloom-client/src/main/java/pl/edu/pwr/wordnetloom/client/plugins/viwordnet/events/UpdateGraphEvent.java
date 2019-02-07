package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events;

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphView;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

public class UpdateGraphEvent {

    private Sense sense;
    private ViwnGraphView view;

    public UpdateGraphEvent(Sense sense) {
        this.sense = sense;
    }

    public UpdateGraphEvent(Sense sense, ViwnGraphView view){
        this.sense = sense;
        this.view = view;
    }

    public Sense getSense(){
        return sense;
    }

    public ViwnGraphView getView() {
        return view;
    }
}
