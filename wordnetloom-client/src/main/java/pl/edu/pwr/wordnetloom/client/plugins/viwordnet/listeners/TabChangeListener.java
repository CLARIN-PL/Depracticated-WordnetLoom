package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners;

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class handling tab change event
 */
public class TabChangeListener implements ChangeListener {

    private final ViWordNetService service;

    public TabChangeListener(ViWordNetService service) {
        this.service = service;
    }

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