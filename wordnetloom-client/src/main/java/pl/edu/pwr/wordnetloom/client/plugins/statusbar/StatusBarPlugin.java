package pl.edu.pwr.wordnetloom.client.plugins.statusbar;

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * Plugin do autostatusów
 *
 * @author Max
 *
 */
public class StatusBarPlugin implements Plugin {

    @Override
    public void install(Workbench workbench) {
        workbench.installService(new StatusBarService(workbench));
    }

}
