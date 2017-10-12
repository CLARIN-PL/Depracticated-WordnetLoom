package pl.edu.pwr.wordnetloom.plugins.statistics.panel;

import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;

import javax.swing.*;
import java.awt.*;

public class DomainStatisticsPanel extends JPanel{
    private JLabel domainPCount;

    public DomainStatisticsPanel() {
        init();
        setupFrame();
        setStatistics();
    }

    private void init(){
        setLayout(new GridLayout(0,2));
        setBorder(BorderFactory.createTitledBorder(Labels.STATS_DOMAIN));
    }

    private void setupFrame(){
        // utworzenie nazw kolumn
        JLabel domainColumnLabel = new JLabel("Domain");
        JLabel countColumnLabel = new JLabel("Count");
        countColumnLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(domainColumnLabel);
        add(countColumnLabel);

        // utworzenie staystyk
        JLabel domainPLabel = new JLabel("P.");
        setFontStyle(domainPLabel, false);
        domainPCount = new JLabel();
        setFontStyle(domainPCount, false);
        domainPCount.setHorizontalAlignment(JLabel.RIGHT);

        add(domainPLabel);
        add(domainPCount);
    }

    /** Zmienia styl czcionki dla określonej etykiety
     * Metoda pozwala ustawić etykiete z nie pogrubioną czcionką
     * @param label etykieta, dla której ma zostac zmieniony styl czcionki
     * @param bold określa, czy czonka ma być pogrubiona czy nie
     */
    private void setFontStyle(JLabel label, boolean bold) {
        Font font = label.getFont();
        label.setFont(font.deriveFont(font.getStyle() | (bold ? Font.BOLD : ~Font.BOLD)));
    }

    private void setStatistics(){
        long pCount = RemoteUtils.lexicalUnitRemote.dbGetUnitCountByDomain("P.");
        domainPCount.setText(String.valueOf(pCount));
    }
}
