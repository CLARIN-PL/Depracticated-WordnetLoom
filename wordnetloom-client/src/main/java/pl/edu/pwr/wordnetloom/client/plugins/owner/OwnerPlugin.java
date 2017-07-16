package pl.edu.pwr.wordnetloom.client.plugins.owner;

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Service;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * plugin dostarczający obsluge danych użytkownika
 *
 * @author Max
 */
public class OwnerPlugin implements Plugin {

    @Override
    public void install(Workbench workbench) {
        Service databasemgr = new OwnerService(workbench);
        workbench.installService(databasemgr);
    }

}
