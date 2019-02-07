package pl.edu.pwr.wordnetloom.client.systems.ui;

import com.alee.extended.layout.GroupLayout;
import com.alee.laf.panel.WebPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MComponentGroup extends WebPanel {

    public MComponentGroup(Component... components) {
        setOpaque(false);
        for (Component btn : components) {
            add(btn);
        }
    }

    public MComponentGroup withFirstElementSize(Dimension dim) {
        Arrays.stream(getComponents())
                .findFirst()
                .ifPresent(component -> component.setPreferredSize(dim));
        return this;
    }

    public MComponentGroup withHorizontalLayout() {
        setLayout(new GroupLayout(SwingConstants.HORIZONTAL, 2));
        return this;
    }

    public MComponentGroup withVerticalLayout() {
        setLayout(new GroupLayout(SwingConstants.VERTICAL, 2));
        return this;
    }

    public MComponentGroup withAllComponentsEnabled(boolean enabled) {
        Arrays.stream(getComponents())
                .forEach(c -> c.setEnabled(enabled));
        return this;
    }

    public MComponentGroup withMargin(int margin) {
        setMargin(margin);
        return this;
    }
}
