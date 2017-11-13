package pl.edu.pwr.wordnetloom.client.systems.ui;

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
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        return this;
    }

    public MButtonPanel withVeritcalLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        return this;
    }

    public MButtonPanel withAllButtonsEnabled(boolean enabled) {
        Arrays.asList(getComponents())
                .stream()
                .forEach(c -> c.setEnabled(enabled));
        return this;
    }
}
