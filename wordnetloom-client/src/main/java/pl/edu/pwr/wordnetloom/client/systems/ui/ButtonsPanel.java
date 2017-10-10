package pl.edu.pwr.wordnetloom.client.systems.ui;

import org.flywaydb.core.internal.util.scanner.classpath.jboss.JBossVFSv2UrlResolver;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ButtonsPanel extends JPanel{
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private Map<Integer, JButton> m_buttons;

    public static class Builder{
        private List<ButtonItem> buttons;
        private int orientation;
        private boolean enabled;

        private class ButtonItem {
            private int id;
            private JButton button;

            private ButtonItem(int id, JButton button){
                this.id = id;
                this.button = button;
            }

            public int getId() {return id;}
            private JButton getButton() {return button;}
        }

        public Builder(int orientation){
            this.orientation = orientation;
        }

        public Builder addButton(int id,JButton button) {
            return addButton(id, button, null);
        }

        public Builder addButton(int id, JButton button, String tooltipText){
            if(buttons==null) {
                buttons = new ArrayList<>();
            }
            if(tooltipText!= null) {
                button.setToolTipText(tooltipText);
            }
            buttons.add(new ButtonItem(id, button));
            return this;
        }

        /** Określa, czy przyciski w momencie utworzenia panelu mają byc aktywne */
        public Builder setButtonsEnabled(boolean enabled){
            this.enabled = enabled;
            return this;
        }

        public ButtonsPanel build() {
            return new ButtonsPanel(this);
        }
    }

    public ButtonsPanel(Builder builder)
    {
        // VERTICAL == BoxLayout.Y_AXIS
        // HORIZONTAL == BoxLayout.X_AXIS
        this.setLayout(new BoxLayout(this, builder.orientation));
        int buttonsSize = builder.buttons.size();
        m_buttons = new HashMap<>(buttonsSize);
        Builder.ButtonItem item;
        for(int i =0; i<buttonsSize; i++) {
            item = builder.buttons.get(i);
            item.button.setEnabled(builder.enabled);
            m_buttons.put(item.getId(), item.getButton());
            this.add(item.getButton());
        }
    }

    public void enableButton(int id, boolean enabled) {
        JButton button = m_buttons.get(id);
        if(button != null) {
            button.setEnabled(enabled);
        }
    }

    public void enableAllButtons(boolean enabled) {
        m_buttons.forEach((k,v)->v.setEnabled(enabled));
    }

    public void setTooltipText(int buttonId, String label) {
        JButton button = m_buttons.get(buttonId);
        if(button != null) {
            button.setToolTipText(label);
        }
    }
}
