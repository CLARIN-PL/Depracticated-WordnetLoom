package pl.edu.pwr.wordnetloom.client.systems.listeners;

import java.util.EventListener;

public interface SimpleListenerInterface extends EventListener {

    void doAction(Object object, int tag);
}
