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

package pl.edu.pwr.wordnetloom.workbench.abstracts;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JTabbedPane;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import pl.edu.pwr.wordnetloom.systems.ui.SplitPaneExt;
import pl.edu.pwr.wordnetloom.workbench.implementation.ShortCut;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Perspective;
import pl.edu.pwr.wordnetloom.workbench.interfaces.View;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

/**
 * Abstrakcyjna klasa perspektywy, ułatwiająca implementację
 * gdyż zawiera w sobie lwią część implementacji
 * @author Max
 */
abstract public class AbstractPerspective implements Perspective, MouseListener {

	private static final String PANEL_NAME = "%s (Ctrl %s)";
	private String perspectiveName=null;      // nazwa perspektywy
    private int indexOfNextView=0;            // indeks dla kolenego instalowanego indeksu

    private List<SplitPaneExt> splitters = new Vector<SplitPaneExt>();
    private List<JTabbedPane> panes = new Vector<JTabbedPane>(); 

	// konfiguracja perspektywy
	protected Workbench workbench=null;

	// skróty klawiszowe
	protected Collection<ShortCut> shortCuts=new ArrayList<ShortCut>();

	// modyfikacja dla skrótów oraz skrót bazowy
	static final protected int MODIFIERS=KeyEvent.CTRL_DOWN_MASK;
	static final protected int KEY_CODE=KeyEvent.VK_1;

	protected JTabbedPane createPane() {
		JTabbedPane pane = new JTabbedPane();
		pane.addMouseListener(this);
		synchronized (panes) {
			panes.add(pane);
		}
		return pane;
	}

	final protected void addSplitter(SplitPaneExt splitter) {
		synchronized (splitters) {
			splitters.add(splitter);
		}
	}
	
	final protected SplitPaneExt getFirstSplitter() {
		SplitPaneExt result = null;
		synchronized (splitters) {
			if (splitters.size() > 0)
				result = splitters.get(0); 
		} 
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see Perspective#installView(View, int)
	 */
	public void installView(View view, int pos) {
		try {
			installPane(view, panes.get(pos));
		} catch (Exception e) {
			Logger.getLogger(AbstractPerspective.class).log(Level.ERROR, "While installing pane " + e);
		}
	}

	/**
	 * Konstruktor perspektywy
	 * @param name - nazwa perspektywy
	 * @param workbench - workbench dla perspektywy
	 */
	public AbstractPerspective(String name,Workbench workbench) {
		super();
		this.perspectiveName=name;
		this.workbench=workbench;
	}

	/*
	 * (non-Javadoc)
	 * @see Perspective#getName()
	 */
	public String getName() {
		return this.perspectiveName;
	}

	/*
	 * (non-Javadoc)
	 * @see Perspective#init()
	 */
	public void init() {
		this.getContent(); // zbudowanie iu perspektywy
	}

	/*
	 *  (non-Javadoc)
	 * @see Perspective#getShortCuts()
	 */
	final public Collection<ShortCut> getShortCuts() {
		return shortCuts;
	}

	/**
	 * dwuklik myszką w zakładke powoduje zwiniecie
	 */
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getClickCount()!=2) return; // czy to byl dwuklik

		// przejście po wszystkich splitterach
		for (SplitPaneExt splitter : splitters) {
			// czy funkcja jest wspierana
			if (!splitter.isOneTouchExpandable()) continue;
			int count=splitter.getComponentCount();
			// przejście po wszystkich komponentach należących do splitera
			for (int i=0;i<count;i++) {
				// jeśli sygnał pochodzi od komponentu
				// to zwinięcie widoku
				if (splitter.getComponent(i)==arg0.getSource()) {
					splitter.collapse(i);
					return;
				}
			}
		}
	}

	/**
	 * Instalacja panelu oraz podłączenie skrótów klawiszowych
	 * z nim związanych
	 * @param view - widok
	 * @param pane - panel do dołączenia
	 */
    protected void installPane(View view, JTabbedPane pane) {
    	if (pane!=null) {
    		pane.addTab(String.format(PANEL_NAME,view.getTitle(),new Integer(indexOfNextView + 1)),view.getPanel());
    		shortCuts.add(new ShortCut(pane,view.getRootComponent(),MODIFIERS,KEY_CODE+indexOfNextView));
    		indexOfNextView++;
    	}
    	shortCuts.addAll(view.getShortCuts());
    }

    /*
     *  (non-Javadoc)
     * @see Perspective#resetViews()
     */
	public void resetViews() {
		synchronized (splitters) {
			for (int i=0;i<2;i++) {
				for (SplitPaneExt splitter : splitters) {
					splitter.resetDividerLocation();
				}
			}
		}
	}

	public void mousePressed(MouseEvent arg0) {/***/}
	public void mouseReleased(MouseEvent arg0) {/***/}
	public void mouseEntered(MouseEvent arg0) {/***/}
	public void mouseExited(MouseEvent arg0) {/***/}

}
