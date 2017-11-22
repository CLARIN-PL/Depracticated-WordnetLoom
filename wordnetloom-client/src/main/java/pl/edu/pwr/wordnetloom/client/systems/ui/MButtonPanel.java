package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.extended.layout.GroupLayout;
import com.alee.laf.panel.WebPanel;

import javax.swing.*;
import java.util.Arrays;

public class MButtonPanel extends WebPanel {

    public MButtonPanel(JButton... buttons) {
        for (JButton btn : buttons) {
            add(btn);
        }
    }

    public MButtonPanel withHorizontalLayout() {
        setLayout(new GroupLayout(SwingConstants.HORIZONTAL, 2));
        return this;
    }

    public MButtonPanel withVerticalLayout() {
        setLayout(new GroupLayout(SwingConstants.VERTICAL, 2));
        return this;
    }

    public MButtonPanel withAllButtonsEnabled(boolean enabled) {
        Arrays.asList(getComponents())
                .stream()
                .forEach(c -> c.setEnabled(enabled));
        return this;
    }

    public MButtonPanel withMargin(int margin) {
        setMargin(margin);
        return this;
    }
}
