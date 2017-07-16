package pl.edu.pwr.wordnetloom.client.plugins.core;

import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Service;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class CorePlugin implements Plugin {

    @Override
    public void install(final Workbench workbench) {
        Service core = new CoreService(workbench);
        workbench.installService(core);
    }

}
