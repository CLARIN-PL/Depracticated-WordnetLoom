///*
//    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
//                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
//                       Radosław Ramocki, Michał Stanek
//    Part of the WordnetLoom
//
//    This program is free software; you can redistribute it and/or modify it
//under the terms of the GNU General Public License as published by the Free
//Software Foundation; either version 3 of the License, or (at your option)
//any later version.
//
//    This program is distributed in the hope that it will be useful, but
//WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//or FITNESS FOR A PARTICULAR PURPOSE. 
//
//    See the LICENSE and COPYING files for more details.
//*/
//
//package pl.wroc.pwr.ci.plwordnet.plugins.notepad;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.util.Collection;
//import java.util.Vector;
//
//import javax.swing.JCheckBoxMenuItem;
//import javax.swing.JFrame;
//import javax.swing.JMenu;
//import javax.swing.event.MenuEvent;
//import javax.swing.event.MenuListener;
//
//import Sense;
//import SenseRelation;
//import Synset;
//import SynsetRelation;
//import CoreService;
//import ModelWrapper;
//import NotepadFrame;
//import AbstractService;
//import Workbench;
//
///**
// * Service provided access to notepad
// * @author Max
// */
package pl.edu.pwr.wordnetloom.plugins.notepad;
public class NotepadService {
	
}
//public class NotepadService extends AbstractService {
//
//	private static final int	FRAME_HEIGHT		= 500;
//	private static final int	FRAME_WIDTH			= 300;
//	private static final String	FRAME_TITLE			= "Notes";
//	private static final String	SHOW_NOTEPAD_LABEL	= "Pokaż notes";
//
//	final private ModelWrapper	modelWrapper;
//	NotepadFrame				notepadFrame;
//
//	/**
//	 * @param workbench
//	 */
//	public NotepadService(Workbench workbench) {
//		super(workbench);
//		modelWrapper = new ModelWrapper();
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see Service#installViews()
//	 */
//	public void installViews() {
//		/***/
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see Service#onClose()
//	 */
//	public boolean onClose() {
//		if (notepadFrame != null) {
//			notepadFrame.dispose();
//			notepadFrame = null;
//		}
//		return true;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see Service#onStart()
//	 */
//	public void onStart() {
//		notepadFrame = new NotepadFrame(workbench, FRAME_TITLE, FRAME_WIDTH, FRAME_HEIGHT, modelWrapper);
//		modelWrapper.loadData();
//		notepadFrame.setVisible(false);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see Service#installMenuItems()
//	 */
//	public void installMenuItems() {
//		// Get window menu
//		JMenu window = workbench.getMenu(CoreService.MENU_LABEL_WINDOW);
//		if (window == null) {
//			throw new RuntimeException("Missing menu '" + CoreService.MENU_LABEL_WINDOW + "'.");
//		}
//
//		// Add new position
//		final JCheckBoxMenuItem showNotepad = new JCheckBoxMenuItem(SHOW_NOTEPAD_LABEL);
//		showNotepad.setMnemonic(KeyEvent.VK_N);
//		showNotepad.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
//				if (notepadFrame == null) {
//					onStart();
//				}
//				if (notepadFrame != null) {
//					notepadFrame.setVisible(item.isSelected());
//				}
//			}
//		});
//		window.add(showNotepad);
//
//		// Install select listener to update state of the menu when it's being showed
//		window.addMenuListener(new MenuListener() {
//			/*
//			 * (non-Javadoc)
//			 * @see javax.swing.event.MenuListener#menuSelected(javax.swing.event.MenuEvent)
//			 */
//			public void menuSelected(MenuEvent e) {
//				showNotepad.setState(notepadFrame.isVisible());
//			}
//
//			/*
//			 * (non-Javadoc)
//			 * @see javax.swing.event.MenuListener#menuDeselected(javax.swing.event.MenuEvent)
//			 */
//			public void menuDeselected(MenuEvent e) {
//				/***/
//			}
//
//			/*
//			 * (non-Javadoc)
//			 * @see javax.swing.event.MenuListener#menuCanceled(javax.swing.event.MenuEvent)
//			 */
//			public void menuCanceled(MenuEvent e) {
//				/***/
//			}
//		});
//	}
//
//	/**
//	 * @param relations
//	 * @param comments
//	 */
//	public void addLexicalRelations(Collection<SenseRelation> relations,
//			String comments) {
//		modelWrapper.addLexicalRelations(relations, comments);
//		notepadFrame.setVisible(true);
//	}
//
//	/**
//	 * @param relations
//	 * @param comments
//	 */
//	public void addSynsetRelations(Collection<SynsetRelation> relations,
//			String comments) {
//		modelWrapper.addSynsetRelations(relations, comments);
//		notepadFrame.setVisible(true);
//	}
//
//	/**
//	 * @param units
//	 * @param comments
//	 */
//	public void addUnits(Collection<Sense> units, String comments) {
//		modelWrapper.addUnits(units, comments);
//		notepadFrame.setVisible(true);
//	}
//
//	/**
//	 * @param synsets
//	 * @param comments
//	 */
//	public void addSynsets(Collection<Synset> synsets,
//			String comments) {
//		modelWrapper.addSynsets(synsets, comments);
//		notepadFrame.setVisible(true);
//	}
//
//	static private <T> Collection<T> makeCollection(T o) {
//		Collection<T> c = new Vector<T>();
//		c.add(o);
//		return c;
//	}
//	
//	/**
//	 * Add objct to notepad
//	 * @param object
//	 * @param comments
//	 */
//	public void add(Object object, String comments) {
//		if (object instanceof Synset) {
//			modelWrapper.addSynsets(makeCollection((Synset)object), comments);
//		} else if (object instanceof Sense) {
//			modelWrapper.addUnits(makeCollection((Sense)object), comments);
//		} else if (object instanceof SynsetRelation) {
//			modelWrapper.addSynsetRelations(makeCollection((SynsetRelation)object), comments);
//		} else if (object instanceof SenseRelation) {
//			modelWrapper.addLexicalRelations(makeCollection((SenseRelation)object), comments);
//		} 
//	}
//	
//	public JFrame getFrame(){		
//		return this.notepadFrame.getFrame();
//	}
//	
//	public NotepadFrame getNotepadFrame(){
//		return this.notepadFrame;
//	}
//
//}
