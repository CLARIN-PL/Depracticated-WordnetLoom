package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners;

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;

/**
 * Defines an object which listen for a change of synset selection.
 */
public interface SynsetSelectionChangeListener {

    void synsetSelectionChangeListener(ViwnNode node);

}
