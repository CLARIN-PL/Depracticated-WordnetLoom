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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphView;
import pl.edu.pwr.wordnetloom.client.systems.listeners.CloseableTabbedPaneListener;
import pl.edu.pwr.wordnetloom.client.systems.ui.CloseableTabbedPane;
import pl.edu.pwr.wordnetloom.client.systems.ui.SplitPaneExt;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractPerspective;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ShortCut;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.View;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * A perspective for wordnet visualization.
 *
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 * @author amusial
 */
public class ViWordNetPerspective extends AbstractPerspective implements
        CloseableTabbedPaneListener, PropertyChangeListener {

    private ViWordNetService service = null;

    public static final int SPLIT_LEFT_VIEW = 0;
    public static final int SPLIT_MAIN_VIEW = 1;
    public static final int SPILT_LOCKER_VIEW = 4;
    public static final int SPLIT_RIGHT_TOP_VIEW = 2;
    public static final int SPLIT_RIGHT_BOTTOM_VIEW = 3;
    public static final int SPLIT_RIGHT_CENTRAL_VIEW = 5;

    JTabbedPane leftView;
    JTabbedPane graphView;
    JTabbedPane secondGraphView;
    JTabbedPane rightTopView;
    JTabbedPane rightBottomView;
    JTabbedPane rightCentralView;
    JTabbedPane locker;

    /**
     * @param name
     * @param workbench
     */
    public ViWordNetPerspective(String name, Workbench workbench) {
        super(name, workbench);
    }

    @Override
    public JComponent getContent() {
        return getFirstSplitter();
    }

    /**
     * init method fired by workbench when it installs perspective should create
     * and add splitters
     *
     */
    @Override
    public void init() {
        // When the perspective panel is fatched for the first time create the
        // panel.
        // Create top and bottom tabbed panel
        leftView = createPane();
        // TODO: tab closing ability gives us great power,
        // but with great power comes great responsibility,
        // every change should be traced (tracked) ;-)
        // but seriously it messes it all up
        graphView = new CloseableTabbedPane(true, true);
        rightTopView = createPane();
        rightBottomView = createPane();
        rightCentralView = createPane();

        secondGraphView = createPane();
        secondGraphView.setMinimumSize(new Dimension(0, 0));

        locker = createPane();

        // events connected with tabs
        ((CloseableTabbedPane) graphView).addCloseableTabbedPaneListener(this);
        graphView.addChangeListener(new TabChangeListener());

        // Create central and bottom views
        SplitPaneExt splitRightBottomHorizontal = new SplitPaneExt(
                JSplitPane.VERTICAL_SPLIT, rightCentralView, rightBottomView);
        splitRightBottomHorizontal.setDividerLocation(0.5D);
        splitRightBottomHorizontal.setResizeWeight(0.5f);

        // Create a horizontaly split panel for holding the right top and
        // central-bottom views.
        SplitPaneExt splitRightHorizontal = new SplitPaneExt(
                JSplitPane.VERTICAL_SPLIT, rightTopView,
                splitRightBottomHorizontal);
        splitRightHorizontal.setStartDividerLocation(230);
        splitRightHorizontal.setResizeWeight(0.0f);

        // GraphViews and one graph view
        final SplitPaneExt splitGraphViewsHorizontal = new SplitPaneExt(
                JSplitPane.HORIZONTAL_SPLIT, graphView, secondGraphView);
        splitGraphViewsHorizontal.setResizeWeight(1.0f);
        splitGraphViewsHorizontal.setDividerLocation(1.0D);
        splitGraphViewsHorizontal.addPropertyChangeListener(
                JSplitPane.DIVIDER_LOCATION_PROPERTY, this);
        splitGraphViewsHorizontal.addPropertyChangeListener(
                JSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY, this);

        // vertical graphs locker splitter
        SplitPaneExt splitGraphsAndLocker = new SplitPaneExt(
                JSplitPane.VERTICAL_SPLIT, splitGraphViewsHorizontal, locker);
        splitGraphsAndLocker.setDividerLocation(0.7D);
        splitGraphsAndLocker.setResizeWeight(1.0D);

        SplitPaneExt splitMainVertical = new SplitPaneExt(
                JSplitPane.HORIZONTAL_SPLIT, splitGraphsAndLocker,
                splitRightHorizontal);
        splitMainVertical.setStartDividerLocation(500);
        splitMainVertical.setResizeWeight(1.0f);

        SplitPaneExt splitSearch = new SplitPaneExt(
                JSplitPane.HORIZONTAL_SPLIT, leftView, splitMainVertical);
        splitSearch.setStartDividerLocation(200);
        splitSearch.setResizeWeight(0.0f);

        this.addSplitter(splitSearch);
        this.addSplitter(splitMainVertical);
        this.addSplitter(splitRightHorizontal);

        // TODO: second graph view actions
        graphView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2
                        && service != null
                        && graphView.getTabCount()
                        + secondGraphView.getTabCount() > 2) {

                    // remember selected view
                    int selected = graphView.getSelectedIndex();
                    ViwnGraphView view = service.getGraphView((JPanel) graphView.getComponentAt(selected));
                    ViwnGraphView fromSecond = null;

                    // empty visualizations should not be moved to second view
                    if (view.getUI().getRootNode() == null) {
                        return;
                    }

                    // get tab from second view if any
                    if (secondGraphView.getTabCount() != 0) {
                        // remember second view
                        fromSecond = service
                                .getGraphView((JPanel) secondGraphView
                                        .getSelectedComponent());
                        // remove it from second view
                        secondGraphView.removeTabAt(secondGraphView
                                .getSelectedIndex());
                        // add to graph view
                        graphView.addTab(fromSecond.getTitle(),
                                fromSecond.getPanel());
                        // set title and tooltip
                        graphView.setTitleAt(graphView
                                .indexOfComponent(fromSecond.getPanel()),
                                fromSecond.getUI().getRootNode().getLabel());
                        graphView.setToolTipTextAt(graphView
                                .indexOfComponent(fromSecond.getPanel()),
                                fromSecond.getUI().getRootNode().getLabel());
                    }

                    // remove selected tab
                    graphView.removeTabAt(selected);

                    // install view in second panel
                    secondGraphView.addTab(view.getTitle(), view.getPanel());
                    secondGraphView.setTitleAt(0, view.getUI().getRootNode()
                            .getLabel());
                    secondGraphView.setToolTipTextAt(0, view.getUI()
                            .getRootNode().getLabel());

                    splitGraphViewsHorizontal.setDividerLocation(0.5D);
                    splitGraphViewsHorizontal.setResizeWeight(0.5D);

                }
            }
        });
        secondGraphView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2 && service != null) {

                    // remember second view
                    ViwnGraphView fromSecond = service
                            .getGraphView((JPanel) secondGraphView
                                    .getSelectedComponent());
                    // remove it from second view
                    secondGraphView.removeAll();
                    if (fromSecond != null) {
                        // add to graph view
                        graphView.addTab(fromSecond.getTitle(),
                                fromSecond.getPanel());
                        // set title and tooltip
                        graphView.setTitleAt(graphView
                                .indexOfComponent(fromSecond.getPanel()),
                                fromSecond.getUI().getRootNode().getLabel());
                        graphView.setToolTipTextAt(graphView
                                .indexOfComponent(fromSecond.getPanel()),
                                fromSecond.getUI().getRootNode().getLabel());
                    }
                    splitGraphViewsHorizontal.setDividerLocation(1.0D);
                    splitGraphViewsHorizontal.setResizeWeight(1.0D);
                }
            }
        });

    }

    @Override
    public void refreshViews() {
        if (service != null && workbench.getActivePerspective() == this) {
        }
    }

    @Override
    public Object getState() {
        return null;
    }

    @Override
    public boolean setState(Object state) {
        return false;
    }

    @Override
    public void installView(View view, int pos) {
        try {
            switch (pos) {
                case SPLIT_MAIN_VIEW:
                    if (service != null) {
                        graphView.addTab(view.getTitle(), view.getPanel());
                        ((ViwnGraphView) view).getUI().getVisualizationViewer()
                                .addMouseListener(new MouseGraphClickListener());
                    } else {
                        graphView.addTab(view.getTitle(), view.getPanel());
                        ((ViwnGraphView) view).getUI().getVisualizationViewer()
                                .addMouseListener(new MouseGraphClickListener());
                        // TODO: check the line below, why shortcuts does not work?
                        shortCuts.add(new ShortCut(graphView, view.getPanel(),
                                KeyEvent.CTRL_MASK, KeyEvent.VK_1 - 1
                                + graphView.getComponentCount()));
                    }
                    break;
                case SPLIT_LEFT_VIEW:
                    leftView.addTab(view.getTitle(), view.getPanel());
                    break;
                case SPLIT_RIGHT_TOP_VIEW:
                    rightTopView.addTab(view.getTitle(), view.getPanel());
                    break;
                case SPLIT_RIGHT_BOTTOM_VIEW:
                    rightBottomView.addTab(view.getTitle(), view.getPanel());
                    break;
                case SPLIT_RIGHT_CENTRAL_VIEW:
                    rightCentralView.addTab(view.getTitle(), view.getPanel());
                    break;
                case SPILT_LOCKER_VIEW:
                    locker.addTab(view.getTitle(), view.getPanel());
                    break;
            }
            shortCuts.addAll(view.getShortCuts());

        } catch (Exception e) {
            Logger.getLogger(ViWordNetPerspective.class).log(Level.ERROR, "Installing view" + e);
        }
    }

    /**
     * Service should be set immediately after constructor if tab events are in
     * use, otherwise events would not work
     *
     * @param s Service connected with this perspective in this plugin
     */
    public void setService(ViWordNetService s) {
        this.service = s;
    }

    /**
     * set tab title and tooltip
     *
     * @param title
     *
     */
    public void setTabTitle(String title) {
        graphView.setTitleAt(graphView.getSelectedIndex(), title);
        graphView.setToolTipTextAt(graphView.getSelectedIndex(), title);
    }

    /**
     * Class handling tab change event
     *
     */
    private class TabChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent ce) {
            try {
                JTabbedPane pane = (JTabbedPane) ce.getSource();
                JPanel sel = (JPanel) pane.getSelectedComponent();
                if (service != null) {
                    service.setActiveGraphView(sel);
                }
            } catch (ClassCastException cce) {
                /*
				 * when add tab is opened, after closing last tab there will be
				 * label not JPanel returned by getSelectedComponent open new
				 * empty ViwnGraphView then
                 */
                service.addGraphView();
            }
        }
    }

    /**
     * <b>MouseGraphClickListener</b> class provide satellite view
     * (<i>ViWordNetService.satelliteView</i>) refresh after clicking at graph
     * view (<i>ViwnGraphViewUI.vv</i>) after any changes in structure of
     * <code>ViwnGraphViewUI</code> this could stop working properly, because of
     * many class castings and strong depending on ui hierarchy<br>
     * </br> Only mouseClicked event is implemented, event mouseEntered was
     * tried, but with clicks work is much more comfortable.
     *
     */
    private class MouseGraphClickListener implements MouseListener {

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (service != null) {
                try {
                    JPanel jp = (JPanel) ((JPanel) ((JPanel) e.getSource()).getParent()).getParent();
                    ViwnGraphView vgv = service.getGraphView(jp);
                    service.setSatteliteOwner(vgv);
                } catch (ClassCastException cce) {
                    System.out.println(cce.getMessage());
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }
    }

    @Override
    public boolean closeTab(int tabIndexToClose) {
        try {
            if (service != null) {
                service.closeGraphView((JPanel) graphView
                        .getSelectedComponent());
            }
        } catch (ClassCastException cce) {
            /*
			 * when add tab is opened, after closing last tab there will be
			 * label not JPanel returned by getSelectedComponent open new empty
			 * ViwnGraphView then
             */
            service.addGraphView();
        }
        return true;
    }

    @Override
    public void tabButtonClicked() {
        if (service != null) {
            service.addGraphView();
        }
    }

    public ViWordNetService getViWordNetService() {
        return service;
    }

    /**
     * main view property, listen for moves of divider between graph views and
     * single second graph view
     *
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
    }

}
