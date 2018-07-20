package pl.edu.pwr.wordnetloom.client.systems.renderers;

import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.domain.model.Domain;

import javax.swing.*;
import java.awt.*;

public class DomainRenderer implements ListCellRenderer{

    private final String NULL_TEXT = Labels.NOT_CHOSEN;

    private DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) defaultListCellRenderer.getListCellRendererComponent(list,value,index, isSelected, cellHasFocus);
        if(value != null){
            Domain domain = (Domain)value;
            String text = LocalisationManager.getInstance().getLocalisedString(domain.getName());
            label.setText(text);
        } else {
            label.setText(NULL_TEXT);
        }
        return label;
    }
}
