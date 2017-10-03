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

package pl.edu.pwr.wordnetloom.plugins.relations.views;

import java.util.Collection;

import pl.edu.pwr.wordnetloom.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Synset;

/**
 * pasek narzedzi
 * @author Max
 */
public class ToolbarView extends AbstractView {

	/**
	 * kontruktor dla klasy
	 * @param workbench - wskaznik dla workbencha
	 * @param title - etykieta dla okienka
	 */
	public ToolbarView(Workbench workbench, String title) {
		super(workbench,title,new ToolbarViewUI());
	}

	/**
	 * odswieżenie przyciksów
	 * @param mainSynset - głowny synset
	 * @param selectedUnits - wybrane jednostki w głównym synsecie
	 * @param descSynset - docelowy synset
	 */
	public void refreshButtons(Synset mainSynset, Collection<Sense> selectedUnits, Synset descSynset) {
		ToolbarViewUI view=(ToolbarViewUI)getUI();
		view.refreshButtons(mainSynset,selectedUnits,descSynset);
	}
	
	/**
	 * dodanie obiektu nasluchujacego gdy potrzeba odswiezenia jednostek
	 * w synsecie
	 * @param newListener - sluchacz
	 */
	public void addRefreshUnitsInSynsetListener(SimpleListenerInterface newListener) {
		((ToolbarViewUI)getUI()).addRefreshUnitsInSynsetListener(newListener);
	}
	
	/**
	 * dodanie obiektu nasluchujacego gdy potrzeba ponownego pokazania synsetu
	 * @param newListener - sluchacz
	 */
	public void addShowSynsetListener(SimpleListenerInterface newListener) {
		((ToolbarViewUI)getUI()).addShowSynsetListener(newListener);
	}

	/**
	 * dodanie obiektu nasluchujacego gdy potrzeba odswiezenia relacji
	 * @param newListener - sluchacz
	 */
	public void addRefreshRelationListener(SimpleListenerInterface newListener) {
		((ToolbarViewUI)getUI()).addRefreshRelationListener(newListener);
	}
	
	/**
	 * dodanie obiektu nasluchujacego gdy potrzeba odswiezenia relacji
	 * @param newListener - sluchacz
	 */
	public void addSynsetChangedListener(SimpleListenerInterface newListener) {
		((ToolbarViewUI)getUI()).addSynsetChangedListener(newListener);
	}

}
