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
package pl.edu.pwr.wordnetloom.plugins.relations;
//
//import java.awt.BorderLayout;
//
//import javax.swing.BorderFactory;
//import javax.swing.JComponent;
//import javax.swing.JPanel;
//import javax.swing.JSplitPane;
//import javax.swing.JTabbedPane;
//import javax.swing.border.BevelBorder;
//
//import SplitPaneExt;
//import AbstractPerspective;
//import View;
//import Workbench;
//import se.datadosen.component.RiverLayout;
//
///**
// * perpektywy do edycji relacji pomiedzy synsetami
// * @author Max
// */
public class RelationsPerspective{
	
}
//
//	// elementy pozycjonowania widoku
//	private JPanel globalPanel;
//	private JPanel toolBar;
//	private JTabbedPane top1, top2, top3, top4;
//	private JTabbedPane bottomView;
//	private RelationsService service = null;
//
//	/**
//	 * @param name
//	 * @param workbench
//	 */
//	public RelationsPerspective(String name, Workbench workbench) {
//		super(name, workbench);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see Perspective#getContent()
//	 */
//	public JComponent getContent() {
//		if (globalPanel == null) {
//			top1 = createPane();
//			top2 = createPane();
//			top3 = createPane();
//			top4 = createPane();
//			bottomView = createPane();
//
//			// splitter dla 1 i 2
//			SplitPaneExt split0 = new SplitPaneExt(JSplitPane.HORIZONTAL_SPLIT, top1, top2);
//			split0.setStartDividerLocation(200);
//			split0.setResizeWeight(1.0f);
//
//			// splitter dla 3 i 4
//			SplitPaneExt split1 = new SplitPaneExt(JSplitPane.HORIZONTAL_SPLIT, top3, top4);
//			split1.setStartDividerLocation(200);
//			split1.setResizeWeight(1.0f);
//
//			// splitter dla gory
//			SplitPaneExt split2 = new SplitPaneExt(JSplitPane.HORIZONTAL_SPLIT, split0, split1);
//			split2.setStartDividerLocation(400);
//			split2.setResizeWeight(0.5f);
//
//			// utworzenie podzialu glownego dla widoku gora i dol
//			SplitPaneExt split3 = new SplitPaneExt(JSplitPane.VERTICAL_SPLIT, split2, bottomView);
//			split3.setStartDividerLocation(300);
//			split3.setResizeWeight(0.5f);
//
//			globalPanel = new JPanel(new BorderLayout());
//			toolBar = new JPanel();
//			toolBar.setLayout(new RiverLayout(0, 0));
//			toolBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
//
//			globalPanel.add(toolBar, BorderLayout.NORTH);
//			globalPanel.add(split3, BorderLayout.CENTER);
//
//			addSplitter(split0);
//			addSplitter(split1);
//			addSplitter(split2);
//			addSplitter(split3);
//		}
//		return globalPanel;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see AbstractPerspective#installView(View,
//	 *      int)
//	 */
//	@Override
//	public void installView(View view, int pos) {
//		if (pos == 5) {
//			toolBar.add(view.getPanel());
//		} else {
//			super.installView(view, pos);
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see Perspective#refreshViews()
//	 */
//	public void refreshViews() {
//		if (service == null)
//			service = (RelationsService) workbench.getService(RelationsService.class.getName());
//		if (service != null && workbench.getActivePerspective() == this) { // czy
//																			// jest
//																			// aktywna
//			service.refreshSynsetsView(1);
//			service.refreshSynsetsView(2);
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see AbstractPerspective#resetViews()
//	 */
//	@Override
//	public void resetViews() {
//		bottomView.setSelectedIndex(0);
//		super.resetViews();
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see Perspective#getState()
//	 */
//	public Object getState() {
//		return null;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see Perspective#setState(java.lang.Object)
//	 */
//	public boolean setState(Object state) {
//		if (service==null)
//			service=(RelationsService)workbench.getService(RelationsService.class.getName());
//		if (service!=null && workbench.getActivePerspective()==this) { 
//			return service.setState(state);
//		}
//		return false;
//	}
//}
