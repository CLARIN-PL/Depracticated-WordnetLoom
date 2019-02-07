package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events;

import pl.edu.pwr.wordnetloom.client.systems.ui.GraphTabbedPane;

public class SetCriteriaEvent {

    private GraphTabbedPane.TabInfo tabInfo;

    public SetCriteriaEvent(GraphTabbedPane.TabInfo tabInfo){
        this.tabInfo = tabInfo;
    }

    public GraphTabbedPane.TabInfo getTabInfo() {
        return tabInfo;
    }
}
