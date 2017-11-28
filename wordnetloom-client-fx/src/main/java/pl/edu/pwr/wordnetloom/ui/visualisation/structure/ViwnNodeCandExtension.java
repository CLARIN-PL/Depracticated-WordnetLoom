package pl.edu.pwr.wordnetloom.ui.visualisation.structure;

import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.ui.visualisation.ViwnGraphViewUI;

import java.awt.*;

public class ViwnNodeCandExtension extends ViwnNodeCand {

    public ViwnNodeCandExtension(Synset synset, ExtGraph ext, ExtGraphExtension graphext, boolean added, ViwnGraphViewUI ui) {
        super(synset, ext, added, ui);
        this.ext = graphext;
    }

    private String relName;
    private Color color;
    private ExtGraphExtension ext;

    public ViwnNodeCandExtension(Synset synset, ExtGraphExtension ext, ViwnGraphViewUI ui) {
        super(synset, new ExtGraph(), false, ui);
        this.ext = ext;
    }

    public String getRelName() {
        return relName;
    }

    public ExtGraphExtension getExtGraphExtension() {
        return ext;
    }

    public void setRelName(String relName) {
        this.relName = relName;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
