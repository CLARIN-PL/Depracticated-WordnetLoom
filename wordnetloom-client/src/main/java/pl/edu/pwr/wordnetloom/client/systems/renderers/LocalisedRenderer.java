package pl.edu.pwr.wordnetloom.client.systems.renderers;

import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComboBox;
import pl.edu.pwr.wordnetloom.client.utils.Labels;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class LocalisedRenderer implements ListCellRenderer{

    private final String NULL_TEXT = Labels.NOT_CHOSEN;

    private String nullText = NULL_TEXT;

    private ListCellRenderer listCellRenderer = new JComboBox().getRenderer();

    public LocalisedRenderer(){}

    public LocalisedRenderer(String nullText){
        this.nullText = nullText;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel)listCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String text = nullText;
        if(value != null){
            text = getLocalisedText(value);
        }
        label.setText(text);
        return label;
    }

    private String getLocalisedText(Object value){
        final String NAME_METHOD = "getName";
        try {
            Long name = (Long) value.getClass().getMethod(NAME_METHOD)
                    .invoke(value);
            return LocalisationManager.getInstance().getLocalisedString(name);
        } catch (NoSuchMethodException  | IllegalAccessException | InvocationTargetException e) {
            return "";
        }
    }

    public void setNullText(String text){
        nullText = text;
    }
}
