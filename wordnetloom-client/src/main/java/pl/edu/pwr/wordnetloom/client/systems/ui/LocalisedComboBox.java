package pl.edu.pwr.wordnetloom.client.systems.ui;

import pl.edu.pwr.wordnetloom.client.systems.renderers.LocalisedRenderer;

import javax.swing.*;
import java.util.List;

public class LocalisedComboBox<T> extends JComboBox<T>{

    public LocalisedComboBox(){
        init(null);
    }

    public LocalisedComboBox(String emptyElementText){
        init(emptyElementText);
    }

    private void init(String emptyElementText){
        LocalisedRenderer renderer = new LocalisedRenderer();
        if(emptyElementText != null){
            renderer.setNullText(emptyElementText);
        }
        setRenderer(renderer);
    }

    public void setItems(List<T> items){
        this.removeAllItems();
        addItem(null);
        for(T item : items){
            addItem(item);
        }
    }

    public LocalisedComboBox withItems(List<T> items){
        setItems(items);
        return this;
    }
}
