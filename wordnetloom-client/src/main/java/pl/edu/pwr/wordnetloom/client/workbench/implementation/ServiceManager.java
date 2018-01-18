package pl.edu.pwr.wordnetloom.client.workbench.implementation;

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

public class ServiceManager {

    private static String VI_WORD_NET_SERVICE = "pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService";

    public static ViWordNetService getViWordNetService(Workbench workbench) {
        assert workbench != null;
        return (ViWordNetService) workbench.getService(VI_WORD_NET_SERVICE);
    }
}
