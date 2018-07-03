package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import com.alee.extended.panel.WebComponentPanel;
import org.jboss.naming.remote.client.ejb.RemoteNamingStoreEJBClientHandler;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.GroupView;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComponentGroup;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import se.datadosen.component.RiverLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Local;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class EditDictionaryPanel extends JPanel {

    private JPanel otherValuesPanel;
    private JPanel localisedValuesPanel;
    private JButton saveButton;

    private JPanel valuesPanel;

    private Dictionary editedDictionary;

    // <language, component>
    private Map<String, LocalisedComponent> localisedComponentMap;
    private Map<String, JTextComponent> otherComponentMap;
    // TODO dodać mapy przechowujące informacje o komponentach

    public EditDictionaryPanel() {
        localisedComponentMap = new HashMap<>();
        otherComponentMap = new HashMap<>();
        initView();
    }

    private void initView() {
        setLayout(new BorderLayout());
        valuesPanel = initValuesView();
        add(valuesPanel, BorderLayout.CENTER);
        saveButton = new JButton(Labels.SAVE);
        add(saveButton, BorderLayout.SOUTH);
    }

    private JPanel initValuesView(){
        JPanel panel = new JPanel(new RiverLayout());
        List<Field> fields = getFields();
        List<String> localisedFieldsNames = new ArrayList<>();
        List<String> stringFieldsNames = new ArrayList<>();
        for(Field field: fields){
            if(field.getType() == Long.class){
                localisedFieldsNames.add(field.getName());
            } else {
                stringFieldsNames.add(field.getName());
            }
        }
        // TODO wstawić pojedyńcze
        for(String field : stringFieldsNames){
            JLabel label = new JLabel(field);
            panel.add(RiverLayout.LINE_BREAK, label);
            JTextComponent component = new JTextField();
            panel.add(RiverLayout.HFILL, component);
            otherComponentMap.put(field, component);
        }

        WebComponentPanel webComponentPanel = new WebComponentPanel();
        webComponentPanel.setElementMargin(10);
        webComponentPanel.setReorderingAllowed(true);
        for(String language : getLanguages()) {
            LocalisedComponent localisedComponent = new LocalisedComponent(language, localisedFieldsNames);
            JPanel localisedComponentPanel = localisedComponent.getPanel();
            webComponentPanel.addElement(localisedComponentPanel);

            localisedComponentMap.put(language, localisedComponent);
        }
        panel.add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL, webComponentPanel);
        validate();
        repaint();

        return panel;
    }


    private List<String> getLanguages(){
        List<String> languagesList = new ArrayList<>();
        for(Language language : Language.values()){
            languagesList.add(language.getAbbreviation());
        }
        return languagesList;
    }

    private List<Field> getFields(){
        if(editedDictionary == null){
            return new ArrayList<>();
        }
        Class clazz = editedDictionary.getClass();

        Method[] methods = clazz.getMethods();
        List<Method> methodsList = new ArrayList<>();
        String name;
        // extract fields name, exclue id field
        for(Method method: methods) {
            name = method.getName();
            if(name.startsWith("get")
                    && !name.equals("getClass")
                    && !name.equals("getId")){
                methodsList.add(method);
            }
        }

        List<Field> fields = new ArrayList<>();
        for(Method method : methodsList) {
           fields.add(new Field(method));
        }

        return fields;
    }

    public void load(Dictionary dictionary){
        if(editedDictionary != dictionary){
            editedDictionary = dictionary;
//            removeAll();
            remove(valuesPanel);


            localisedComponentMap.clear();
            valuesPanel = initValuesView();
            add(valuesPanel, BorderLayout.CENTER);
        }
        editedDictionary = dictionary;
        if(editedDictionary == null){
            return;
        }
        otherComponentMap.forEach((k,v)->{
            try {
                v.setText(getValue(editedDictionary, k));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        localisedComponentMap.forEach((k,v)->{
            v.updateValues(dictionary);
        });

    }

    private String getValue(Dictionary dictionary, String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = dictionary.getClass().getMethod("get"+fieldName);
        return (String) method.invoke(dictionary);
    }

    private class LocalisedComponent{
        private final int LABEL_WIDTH = 50;

        private String language;
        private Map<String, JTextComponent> componentMap;
        private Map<String, LocalisedString> localisedStringMap;

        public LocalisedComponent(String language, List<String> fields){
            this.language = language;
            componentMap = new HashMap<>();
            localisedStringMap = new HashMap<>();

            createFields(fields);
        }

        private void createFields(List<String> fields){
            for(String field : fields){
                addField(field);
            }
        }

        private void addField(String fieldName){
            // TODO jeżeli description to JTextArea
            JTextComponent component = new JTextField();
            componentMap.put(fieldName, component);
        }

        public void clear(){
            throw new NotImplementedException();
        }

        public JPanel getPanel(){
             JPanel panel = new JPanel(new RiverLayout());
             componentMap.forEach((name,component)->{
                 JLabel label = new JLabel(name);
                 label.setSize(LABEL_WIDTH, label.getHeight());

                 panel.add(RiverLayout.LINE_BREAK, label);
                 panel.add(RiverLayout.HFILL, component);
             });
            return panel;
        }

        public void updateValues(Dictionary dictionary) {
            localisedStringMap.clear();
            componentMap.forEach((k,v)->{
                // TODO przypatrzeć się temu
                Long id = null;
                try {
                    id = getValue(dictionary, k);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }
                if(id != null){
                    LocalisedKey key = new LocalisedKey(id, language);
                    LocalisedString localisedString = RemoteService.localisedStringServiceRemote.findStringsByKey(key);
                    localisedStringMap.put(k, localisedString);

                    v.setText(localisedString.getValue());
                } else {
                    v.setText("");
                }

            });
        }

        private Long getValue(Dictionary dictionary, String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method method = dictionary.getClass().getMethod("get"+fieldName);
            return (Long)method.invoke(dictionary);
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
