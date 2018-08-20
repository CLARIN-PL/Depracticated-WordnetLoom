package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners;

import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.SetCriteriaEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateCriteriaEvent;
import pl.edu.pwr.wordnetloom.client.systems.ui.GraphTabbedPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class handling tab change event
 */
public class TabChangeListener implements ChangeListener {

    private final ViWordNetService service;
    private int previousTabIndex = 1;

    public TabChangeListener(ViWordNetService service) {
        this.service = service;
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        try {
            GraphTabbedPane panel = (GraphTabbedPane) ce.getSource();
            int previousTab = previousTabIndex;
            int currentTab = panel.getSelectedIndex();
            Application.eventBus.post(new UpdateCriteriaEvent(panel.getTabInfo(previousTab)));
            Application.eventBus.post(new SetCriteriaEvent(panel.getTabInfo(currentTab)));

            previousTabIndex = currentTab;

            JPanel sel = (JPanel) panel.getSelectedComponent();
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