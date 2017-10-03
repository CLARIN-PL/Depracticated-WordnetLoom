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

import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import pl.edu.pwr.wordnetloom.utils.GUIUtils;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractViewUI;
import se.datadosen.component.RiverLayout;

public class ViwnExamplesViewUI extends AbstractViewUI {

	private JScrollPane scroll;
	private JTextPane ta;
	
	@Override
	public JComponent getRootComponent() {
		return null;
	}

	@Override
	protected void initialize(JPanel content) {
		getContent().setLayout(new RiverLayout());
		ta = new JTextPane();
		ta.setContentType("text/html");
		ta.setEditable(false);
		scroll = new JScrollPane(ta);
		getContent().add(scroll, "hfill vfill");
	}

	public void make_tree(final String text) {
		Runnable run = new Runnable() {
			public void run() {
				ta.setText(text);
				ta.setCaretPosition(0);
			}
		};

		try {
			GUIUtils.delegateToEDT(run);
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
		}
	}
}
