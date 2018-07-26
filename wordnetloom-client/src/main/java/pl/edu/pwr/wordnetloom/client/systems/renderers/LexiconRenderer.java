package pl.edu.pwr.wordnetloom.client.systems.renderers;

import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import javax.swing.*;
import java.awt.*;

public class LexiconRenderer implements ListCellRenderer{

    private final String NULL_TEXT = Labels.NOT_CHOSEN;

    private ListCellRenderer defaultListCellRenderer = new JComboBox().getRenderer();
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) defaultListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(value != null){
            Lexicon lexicon = (Lexicon)value;
            label.setText(lexicon.getName());
        } else {
            label.setText(NULL_TEXT);
        }

        return label;
    }
}
