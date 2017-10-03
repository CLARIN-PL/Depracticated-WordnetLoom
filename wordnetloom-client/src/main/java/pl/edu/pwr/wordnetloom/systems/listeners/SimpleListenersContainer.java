/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. 

    See the LICENSE and COPYING files for more details.
*/

package pl.edu.pwr.wordnetloom.systems.listeners;

import javax.swing.event.EventListenerList;

/**
 * klasa zawierająca kontener obłsługujący słuchaczy
 * @author Max
 *
 */
public class SimpleListenersContainer {

	// kontener na słuchaczy
	private EventListenerList listeners=new EventListenerList();

	/**
	 * dodanie obsługi zdarzenia zmiany danych
	 * @param newListener - nowy słuchacz
	 */
	public void add(SimpleListenerInterface newListener) {
		this.listeners.add(SimpleListenerInterface.class,newListener);
	}
	
	/**
	 * usuniecie obsługi zdarzenia zmiany danych
	 * @param listener - słuchacz do usuniecia
	 */
	public void remove(SimpleListenerInterface listener) {
		this.listeners.remove(SimpleListenerInterface.class,listener);
	}
	
	/**
	 * powiadomienie wszystkich zainteresowanych o tym, ze dane sie zmienily
	 * @param object - obiekt do wyslania 
	 * @param tag - tag do przekazania
	 */
	public void notifyAllListeners(Object object,int tag) {
		// wywołanie wszystkich nasłuchujących
		for (SimpleListenerInterface simpleListener: this.listeners.getListeners(SimpleListenerInterface.class)) 
			simpleListener.doAction(object, tag);
	}
	
	/**
	 * powiadomienie wszystkich zainteresowanych o tym, ze dane sie zmienily
	 * @param object - obiekt do wyslania 
	 */
	public void notifyAllListeners(Object object) {
		// wywołanie wszystkich nasłuchujących
		for (SimpleListenerInterface simpleListener: this.listeners.getListeners(SimpleListenerInterface.class)) 
			simpleListener.doAction(object, 0);
	}
}
