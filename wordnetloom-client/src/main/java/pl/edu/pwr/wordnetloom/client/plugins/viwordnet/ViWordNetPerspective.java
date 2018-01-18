package pl.edu.pwr.wordnetloom.client.plugins.viwordnet;

import com.alee.laf.tabbedpane.WebTabbedPane;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.MouseGraphClickListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.TabChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphView;
import pl.edu.pwr.wordnetloom.client.systems.listeners.CloseableTabbedPaneListener;
import pl.edu.pwr.wordnetloom.client.systems.ui.CloseableTabbedPane;
import pl.edu.pwr.wordnetloom.client.systems.ui.MSplitPane;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractPerspective;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ShortCut;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.View;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A perspective for wordnet visualization.
 *
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 * @author amusial
 */
public class ViWordNetPerspective extends AbstractPerspective implements
        CloseableTabbedPaneListener, PropertyChangeListener, Loggable {

    private ViWordNetService service = null;

    public static final int SPLIT_LEFT_VIEW = 0;
    public static final int SPLIT_MAIN_VIEW = 1;
    public static final int SPILT_LOCKER_VIEW = 4;
    public static final int SPLIT_RIGHT_TOP_VIEW = 2;
    public static final int SPLIT_RIGHT_BOTTOM_VIEW = 3;
    public static final int SPLIT_RIGHT_CENTRAL_VIEW = 5;

    WebTabbedPane leftView;
    WebTabbedPane graphView;
    WebTabbedPane rightTopView;
    WebTabbedPane rightBottomView;
    WebTabbedPane rightCentralView;
    WebTabbedPane locker;

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

        locker = createPane();

        // events connected with tabs
        ((CloseableTabbedPane) graphView).addCloseableTabbedPaneListener(this);
//        graphView.addChangeListener(new TabChangeListener(service));

        // Create central and bottom views
        MSplitPane splitRightBottomHorizontal = new MSplitPane(
                JSplitPane.VERTICAL_SPLIT, rightCentralView, rightBottomView);
        splitRightBottomHorizontal.setDividerLocation(0.5D);
        splitRightBottomHorizontal.setResizeWeight(0.5f);

        // Create a horizontaly split panel for holding the right top and
        // central-bottom views.
        MSplitPane splitRightHorizontal = new MSplitPane(
                JSplitPane.VERTICAL_SPLIT, rightTopView,
                splitRightBottomHorizontal);
        splitRightHorizontal.setStartDividerLocation(230);
        splitRightHorizontal.setResizeWeight(0.0f);

        // vertical graphs locker splitter
        MSplitPane splitGraphsAndLocker = new MSplitPane(
                JSplitPane.VERTICAL_SPLIT, graphView, locker);
        splitGraphsAndLocker.setDividerLocation(0.7D);
        splitGraphsAndLocker.setResizeWeight(1.0D);

        MSplitPane splitMainVertical = new MSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, splitGraphsAndLocker,
                splitRightHorizontal);
        splitMainVertical.setStartDividerLocation(500);
        splitMainVertical.setResizeWeight(1.0f);

        MSplitPane splitSearch = new MSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, leftView, splitMainVertical);
        splitSearch.setStartDividerLocation(200);
        splitSearch.setResizeWeight(0.0f);

        addSplitter(splitSearch);
        addSplitter(splitMainVertical);
        addSplitter(splitRightHorizontal);
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
                                .addMouseListener(new MouseGraphClickListener(service));
                    } else {
                        graphView.addTab(view.getTitle(), view.getPanel());
                        ((ViwnGraphView) view).getUI().getVisualizationViewer()
                                .addMouseListener(new MouseGraphClickListener(service));
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
            logger().error("Installing view", e);
        }
    }

    /**
     * Service should be set immediately after constructor if tab events are in
     * use, otherwise events would not work
     *
     * @param s Service connected with this perspective in this plugin
     */
    public void setService(ViWordNetService s) {
        service = s;
        graphView.addChangeListener(new TabChangeListener(service));
    }

    /**
     * set tab title and tooltip
     *
     * @param title
     */
    public void setTabTitle(String title) {
        graphView.setTitleAt(graphView.getSelectedIndex(), title);
        graphView.setToolTipTextAt(graphView.getSelectedIndex(), title);
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
     * main view property, listen for moves of divider between visualisation views and
     * single second visualisation view
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
    }

}
