package pl.edu.pwr.wordnetloom.plugins.statistics.frame;

import pl.edu.pwr.wordnetloom.plugins.statistics.panel.DomainStatisticsPanel;
import pl.edu.pwr.wordnetloom.plugins.statistics.panel.EtymologicalRootStatisticsPanel;
import pl.edu.pwr.wordnetloom.systems.ui.IconDialog;
import se.datadosen.component.RiverLayout;

import javax.swing.*;

public class StatisticsFrame extends IconDialog {

    public StatisticsFrame(JFrame baseFrame, String title) {
        super(baseFrame, title);
        init(baseFrame);

        final String LINE_BREAK_HFILL = RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL; //od nowej lini, szerokość rodzica
        final String LINE_BREAK_FULL_FILL = LINE_BREAK_HFILL + " " + RiverLayout.VFILL; //do nowej lini, szerokość i wyskokość rodzica

        JPanel domainStatisticsPanel = createDomainStatisticsPanel();
        this.add(LINE_BREAK_HFILL, domainStatisticsPanel);
        JPanel rootStatisticsPanel = createEtymologicalRootStatisticsPanel();
        this.add(LINE_BREAK_FULL_FILL, rootStatisticsPanel);
        pack();
    }

    private void init(JFrame baseFrame) {
        this.setLocationRelativeTo(baseFrame);
        this.setLayout(new RiverLayout());
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private JPanel createDomainStatisticsPanel() {
        return new DomainStatisticsPanel();
    }

    private JPanel createEtymologicalRootStatisticsPanel() {
        return new EtymologicalRootStatisticsPanel();
    }

    public void showFrame() {
        setVisible(true);
    }
}
