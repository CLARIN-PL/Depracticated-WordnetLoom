package pl.edu.pwr.wordnetloom.plugins.statistics;

import pl.edu.pwr.wordnetloom.plugins.statistics.frame.StatisticsFrame;
import pl.edu.pwr.wordnetloom.systems.ui.MenuItemExt;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractService;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;

import javax.swing.*;

public class StatisticsService extends AbstractService {

    private JMenuItem statisticsItem = new MenuItemExt(Labels.STATISTICS);

    StatisticsService(final Workbench workbench) {
        super(workbench);
        statisticsItem.addActionListener(e -> showStatisticsWindow());
    }

    @Override
    public void installViews() {}

    @Override
    public void installMenuItems() {
        JMenu other = workbench.getMenu(Labels.OTHER);
        if ( other==null) return;
        other.addSeparator();
        other.add(statisticsItem);
    }

    @Override
    public boolean onClose() {
        return true;
    }

    @Override
    public void onStart() {}

    private void showStatisticsWindow() {
        StatisticsFrame frame = new StatisticsFrame(workbench.getFrame(), Labels.STATISTICS);
        frame.showFrame();
    }
}
