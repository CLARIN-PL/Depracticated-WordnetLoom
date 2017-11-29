package pl.edu.pwr.wordnetloom.ui.visualisation.listeners;

import pl.edu.pwr.wordnetloom.ui.visualisation.structure.ViwnNode;

/**
 * Defines an object listening for changes in locker
 */
public interface LockerChangeListener {

    /**
     * action made when changes in locker occurs
     *
     * @param vn locker content <code>ViwnNode</code>
     * @return true if changed to already existing <code>ViwnGraphViewUI</code>,
     * false if new <code>ViwnGraphViewUI</code> was created
     */
    ViwnNode lockerSelectionChanged(ViwnNode vn);
}
