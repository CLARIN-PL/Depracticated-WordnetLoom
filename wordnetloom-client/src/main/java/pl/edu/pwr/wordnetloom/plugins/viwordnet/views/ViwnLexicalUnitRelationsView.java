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

package pl.edu.pwr.wordnetloom.plugins.viwordnet.views;

import pl.edu.pwr.wordnetloom.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.model.Sense;

/**
 * [View User Interface]
 * Displays a list of lexical relations for given unit.
 * 
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 *
 */
public class ViwnLexicalUnitRelationsView extends AbstractView {

	/**
	 * @param workbench
	 * @param title
	 */
	public ViwnLexicalUnitRelationsView(Workbench workbench, String title) {
		super(workbench, title, new ViwnLexicalUnitRelationsViewUI(workbench));
	}
	
	public void addUnitChangeListener(SimpleListenerInterface newListener) {
		getUI().addActionListener(newListener);
	}
	/**
	 * @param unit
	 */
	public void loadLexicalUnit(Sense unit){
		((ViwnLexicalUnitRelationsViewUI)this.getUI()).doAction(unit, 1);
	}
	
	public void refresh() {
		((ViwnLexicalUnitRelationsViewUI)this.getUI()).refresh();
	}

}
