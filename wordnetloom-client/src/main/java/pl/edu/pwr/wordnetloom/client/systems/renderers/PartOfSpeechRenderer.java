package pl.edu.pwr.wordnetloom.client.systems.renderers;

import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import javax.swing.*;
import java.awt.*;

public class PartOfSpeechRenderer implements ListCellRenderer{

    private final String NULL_TEXT = Labels.NOT_CHOSEN;

    private DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel)defaultListCellRenderer.getListCellRendererComponent(list,value, index,isSelected,cellHasFocus);
        if( value != null){
            PartOfSpeech partOfSpeech = (PartOfSpeech)value;
            String text = LocalisationManager.getInstance().getLocalisedString(partOfSpeech.getName());
            label.setText(text);
        } else {
            label.setText(NULL_TEXT);
        }
        return label;
    }
}
