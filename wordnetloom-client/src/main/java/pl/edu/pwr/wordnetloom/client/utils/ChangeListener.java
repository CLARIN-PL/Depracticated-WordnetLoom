package pl.edu.pwr.wordnetloom.client.utils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.List;

public class ChangeListener {
    public interface ChangeInterface{
        void onChange();
    }

    private boolean hasChange = false;
    private List<JComponent> componentsList;
    private ChangeInterface action;

    public ChangeListener(){
        componentsList = new ArrayList<>();
    }

    public void setAction(ChangeInterface action) {
        this.action = action;
    }

    public void add(JComponent... components){
        for(JComponent component : components) {
            componentsList.add(component);
            if(component instanceof JTextComponent){
                ((JTextComponent)component).addCaretListener(e -> action.onChange());
            } else if(component instanceof JButton){
                ((JButton)component).addActionListener(e->action.onChange());
            } else if(component instanceof JComboBox) {
                ((JComboBox)component).addActionListener(e->action.onChange());
            } else if(component instanceof JRadioButton){
                ((JComboBox)component).addActionListener(e->action.onChange());
            }
        }
    }

    public void doAction() {
        hasChange = true;
        action.onChange();
    }

    public boolean hasChange(){
        return hasChange;
    }

    public void reset() {
        hasChange = false;
    }
}
