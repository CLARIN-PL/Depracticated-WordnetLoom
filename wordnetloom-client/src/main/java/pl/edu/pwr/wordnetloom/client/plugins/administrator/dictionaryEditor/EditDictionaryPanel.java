package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import com.alee.extended.panel.WebComponentPanel;
import com.sun.deploy.panel.JavaPanel;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.errors.ValidationManager;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import se.datadosen.component.RiverLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EditDictionaryPanel extends JPanel {

    private JPanel otherValuesPanel;
    private JPanel localisedValuesPanel;
    private JButton saveButton;

    // TODO dodać mapy przechowujące informacje o komponentach

    public EditDictionaryPanel() {

    }

    private class LocalisedComponent{
        private String language;
        private Map<String, JTextComponent> componentMap;
        private Map<String, LocalisedString> localisedStringMap;

        public LocalisedComponent(String language){
            this.language = language;
            componentMap = new HashMap<>();
            localisedStringMap = new HashMap<>();
        }

        public void clear(){
            throw new NotImplementedException();
        }

        public JPanel getPanel(){
            throw new NotImplementedException();
        }

        public void addField(Field field){
            // TODO
        }
    }

    private class Field{
        private String name;
        private Class type;

        public Field(Method method){
            String methodName = method.getName();
            this.name = methodName.substring(3, methodName.length());
            this.type = method.getReturnType();
        }

        public String getName(){
            return name;
        }

        public Class getType(){
            return type;
        }
    }
}
