package pl.edu.pwr.wordnetloom.plugins.statistics;

import pl.edu.pwr.wordnetloom.workbench.interfaces.Plugin;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

public class StatisticsPlugin implements Plugin {
    @Override
    public void install(Workbench workbench) {
        workbench.installService(new StatisticsService(workbench));
    }
}
