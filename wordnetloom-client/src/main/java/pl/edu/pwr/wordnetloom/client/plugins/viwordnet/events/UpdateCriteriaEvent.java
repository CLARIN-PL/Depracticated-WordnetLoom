package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events;

import pl.edu.pwr.wordnetloom.client.systems.ui.GraphTabbedPane;

public class UpdateCriteriaEvent {

    private GraphTabbedPane panel;
    private GraphTabbedPane.TabInfo tabInfo;

    public UpdateCriteriaEvent(GraphTabbedPane.TabInfo tabInfo){
        this.tabInfo = tabInfo;
    }

    public GraphTabbedPane.TabInfo getTabInfo() {
        return tabInfo;
    }
}
