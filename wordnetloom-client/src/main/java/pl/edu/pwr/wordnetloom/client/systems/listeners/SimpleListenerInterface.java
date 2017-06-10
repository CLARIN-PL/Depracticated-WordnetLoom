package pl.edu.pwr.wordnetloom.client.systems.listeners;

import java.util.EventListener;

public interface SimpleListenerInterface extends EventListener {

    abstract public void doAction(Object object, int tag);
}
