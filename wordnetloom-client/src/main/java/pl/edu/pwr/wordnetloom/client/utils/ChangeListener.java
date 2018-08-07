package pl.edu.pwr.wordnetloom.client.utils;

import com.sun.javaws.exceptions.InvalidArgumentException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.lang.model.type.UnknownTypeException;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeListener {
    public interface ChangeInterface{
        void onChange();
    }

    private boolean hasChange = false;
    private Map<JComponent, Object> componentsValues;
    private ChangeInterface action;

    public ChangeListener(){
        componentsValues = new HashMap<>();
    }

    private void onChange(){
        hasChange = true;
        if(action != null){
            action.onChange();
        }
    }

    public void setListener(ChangeInterface action){
        this.action = action;
        // TODO zrobić słuchanie
    }

    public void addComponents(JComponent... components){
        for(JComponent component : components){
            addComponent(component);
        }
    }

    public void addComponent(JComponent component) {
        Object value = getValue(component);
        componentsValues.put(component, value);
    }

    private Object getValue(JComponent component) {
        if(component instanceof JTextComponent){
            return ((JTextComponent) component).getText();
        }
        if(component instanceof JComboBox){
            return ((JComboBox) component).getSelectedItem();
        }
        if(component instanceof JRadioButton){
            return ((JRadioButton) component).isSelected();
        }
        // TODO dorzucić także inne elementy
        return null;
    }

    public void updateValues() {
        for(Map.Entry<JComponent, Object> entry: componentsValues.entrySet()){
            Object value = getValue(entry.getKey());
            componentsValues.put(entry.getKey(), value);
        }
        hasChange = false;
    }

    public void resetComponents(){
        componentsValues.forEach((component,value)->setValue(component));
        hasChange = false;
    }

    private void setValue(JComponent component){
        Object value = componentsValues.get(component);
        if(component instanceof JTextComponent){
            ((JTextComponent) component).setText((String) value);
        } else if(component instanceof JComboBox) {
            ((JComboBox) component).setSelectedItem(value);
        } else if(component instanceof JRadioButton){
            ((JRadioButton) component).setSelected((Boolean) value);
        }
    }

    public boolean isChanged(){
        if(hasChange == true){
            return true;
        }
        for(Map.Entry<JComponent, Object> entry : componentsValues.entrySet()){
            if(!entry.getValue().equals(getValue(entry.getKey()))){
                return true;
            }
        }
        return false;
    }

    public void setChanged(boolean changed){
        this.hasChange = changed;
    }
}
