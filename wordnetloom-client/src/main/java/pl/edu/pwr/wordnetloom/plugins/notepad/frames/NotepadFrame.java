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
//package pl.wroc.pwr.ci.plwordnet.plugins.notepad.frames;
//
//import java.awt.Dimension;
//import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTabbedPane;
//import javax.swing.ListSelectionModel;
//import javax.swing.event.CaretEvent;
//import javax.swing.event.CaretListener;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//
//import Entry;
//import ModelWrapper;
//import LexicalRelationCandidatesPanel;
//import DialogBox;
//import ToolTipGenerator;
//import ToolTipList;
//import ButtonExt;
//import IconFrame;
//import TextAreaPlain;
//import Perspective;
//import Workbench;
//import se.datadosen.component.RiverLayout;
//
///**
// * Frame for notepad
// * @author Max
// * 
// */
package pl.edu.pwr.wordnetloom.plugins.notepad.frames;
public class NotepadFrame {
	
}
//public class NotepadFrame {
//	private static final String	BUTTON_SAVE							= "Zapisz";
//	private static final String	BUTTON_SAVE_TOOLTIP					= "Zapisanie wprowadzonego komentarza";
//	private static final String	BUTTON_GO_LABEL						= "Wybierz";
//	private static final String	BUTTON_GO_TOOLTIP					= "Ustawienie wybranego elementy jako głównego w aktywnej perspektywie.";
//	private static final String	BUTTON_DELETE_LABEL					= "Usuń";
//	private static final String	CANNOT_SET_ACTIVE_ELEMENT_MESSAGE	= "Wybrany element nie może zostać automatycznie ustawiony jako główny w aktywnej perspektywnie.";
//	private static final String	BUTTON_DELETE_TOOLTIP				= "Usunięcie zaznaczonych elementów z notesu";
//	private static final int	MARGIN								= 10;
//	final IconFrame				frame;
//	final ModelWrapper			modelWrapper;
//	private final Workbench		workbench;
//	private final JTabbedPane 	tabbedPane;
//
//	/**
//	 * @param workbench
//	 * @param title
//	 * @param width
//	 * @param height
//	 * @param modelWrapper
//	 */
//	public NotepadFrame(final Workbench workbench, String title, int width,
//			int height, final ModelWrapper modelWrapper) {
//		this.workbench = workbench;
//		this.modelWrapper = modelWrapper;
//
//		// Build window
//		frame = new IconFrame(title, width, height);
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		frame.setLocation(screenSize.width - MARGIN - frame.getSize().width, MARGIN);
//		frame.setDefaultCloseOperation(IconFrame.HIDE_ON_CLOSE);
//
//		// Create list
//		final ToolTipList entriesList = new ToolTipList(workbench, modelWrapper.getModel(), ToolTipGenerator.getGenerator(), false);
//		entriesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//
//		// Add delete button
//		final ButtonExt buttonDelete = new ButtonExt(BUTTON_DELETE_LABEL, new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int[] values = entriesList.getSelectedIndices();
//				modelWrapper.remove(values);
//			}
//
//		});
//		buttonDelete.setEnabled(false);
//		buttonDelete.setToolTipText(BUTTON_DELETE_TOOLTIP);
//
//		final ButtonExt buttonGo = new ButtonExt(BUTTON_GO_LABEL, new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Perspective p = getWorkbench().getActivePerspective();
//				if (!p.setState(modelWrapper.getModel().getObjectAt(entriesList.getSelectedIndex()).getObject())) {
//					DialogBox.showInformation(CANNOT_SET_ACTIVE_ELEMENT_MESSAGE);
//				}
//			}
//
//		});
//		buttonGo.setEnabled(false);
//		buttonDelete.setToolTipText(BUTTON_GO_TOOLTIP);
//
//		final TextAreaPlain commentValue = new TextAreaPlain("");
//
//		// Add save button
//		final ButtonExt buttonSave = new ButtonExt(BUTTON_SAVE, new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int[] values = entriesList.getSelectedIndices();
//				if (values.length == 1) {
//					Entry entry = modelWrapper.getModel().getObjectAt(values[0]).getEntry();
//					entry.setEntryComments(commentValue.getText());
//					commentValue.setText(commentValue.getText());
//					modelWrapper.save();
//				}
//				ButtonExt b = (ButtonExt) e.getSource();
//				b.setEnabled(false);
//			}
//
//		});
//		buttonSave.setEnabled(false);
//		buttonSave.setToolTipText(BUTTON_SAVE_TOOLTIP);
//
//		// Area for comments
//		commentValue.addCaretListener(new CaretListener() {
//			public void caretUpdate(CaretEvent e) {
//				if (e.getSource() instanceof TextAreaPlain) {
//					TextAreaPlain field = (TextAreaPlain) e.getSource();
//					buttonSave.setEnabled(entriesList.getSelectedIndices().length == 1 && field.wasTextChanged());
//					return;
//				}
//				buttonSave.setEnabled(false);
//			}
//		});
//		commentValue.setRows(3);
//
//		// Add selection listener
//		entriesList.addListSelectionListener(new ListSelectionListener() {
//			public void valueChanged(ListSelectionEvent e) {
//				if (e != null && e.getValueIsAdjusting()) {
//					return;
//				}
//				int[] values = entriesList.getSelectedIndices();
//				final int count = values.length;
//				buttonDelete.setEnabled(count > 0);
//				buttonGo.setEnabled(count == 1);
//				if (count == 1) {
//					commentValue.setEnabled(true);
//					commentValue.setText(modelWrapper.getModel().getObjectAt(values[0]).getEntry().getEntryComments());
//				} else {
//					commentValue.setEnabled(false);
//					commentValue.setText("");
//				}
//			}
//
//		});
//		entriesList.addMouseListener(new MouseListener() {
//			public void mouseReleased(MouseEvent e) {
//				/***/
//			}
//
//			public void mousePressed(MouseEvent e) {
//				/***/
//			}
//
//			public void mouseExited(MouseEvent e) {
//				/***/
//			}
//
//			public void mouseEntered(MouseEvent e) {
//				/***/
//			}
//
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2 && buttonGo.isEnabled()) {
//					buttonGo.doClick();
//				}
//			}
//		});
//
//		JPanel tab1 = new JPanel();
//		tab1.setLayout(new RiverLayout());
//		tab1.add("hfill vfill", new JScrollPane(entriesList));
//		tab1.add("br hfill", new JScrollPane(commentValue));
//		tab1.add("br center", buttonGo);
//		tab1.add("", buttonDelete);
//		tab1.add("", new JLabel(" "));
//		tab1.add("", buttonSave);
//
//		// @czuk
//		tabbedPane = new JTabbedPane();
//		tabbedPane.addTab("Różne", tab1);		
//		tabbedPane.addTab("Powiązania", new LexicalRelationCandidatesPanel());
//				
//		// Add elements to the frame
//		frame.add("hfill vfill", tabbedPane);
//	}
//
//	/**
//	 * Set visibility of the window
//	 * @param visibility
//	 */
//	public void setVisible(boolean visibility) {
//		frame.setVisible(visibility);
//		if (visibility) {
//			frame.setExtendedState(IconFrame.NORMAL);
//		}
//	}
//
//	/**
//	 * @return visibility of the window
//	 */
//	public boolean isVisible() {
//		return frame.getExtendedState() != IconFrame.ICONIFIED && frame.isVisible();
//	}
//
//	/**
//	 * Dispose the object
//	 */
//	public void dispose() {
//		if (frame != null) {
//			frame.setVisible(false);
//			frame.dispose();
//		}
//	}
//
//	Workbench getWorkbench() {
//		return workbench;
//	}
//	
//	public JFrame getFrame() {
//		return this.frame;
//	}
//	
//	public void setVisiblePane(int n){
//		this.tabbedPane.setSelectedIndex(n);
//	}
//}
