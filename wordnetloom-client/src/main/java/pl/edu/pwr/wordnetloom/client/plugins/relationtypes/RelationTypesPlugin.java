package pl.edu.pwr.wordnetloom.client.plugins.relationtypes;

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Service;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class RelationTypesPlugin implements Plugin {

    @Override
    public void install(Workbench workbench) {
        Service databasemgr = new RelationTypesService(workbench);
        workbench.installService(databasemgr);
    }

}
