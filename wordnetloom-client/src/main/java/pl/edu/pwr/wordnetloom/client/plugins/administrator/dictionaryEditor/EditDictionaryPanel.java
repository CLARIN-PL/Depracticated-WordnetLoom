package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import com.alee.extended.panel.WebComponentPanel;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import se.datadosen.component.RiverLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class EditDictionaryPanel extends JPanel {

    private final int LABEL_WIDTH = 50;

    private JPanel otherValuesPanel;
    private JPanel localisedValuesPanel;
    private JButton saveButton;

    private JPanel valuesPanel;

    private Dictionary editedDictionary;

    private Map<String, JTextComponent> notLocalisedComponents;
    private Map<String, Map<String, LocalisedComponent>> localisedComponents;
    private Map<String, Long> localisedFieldsIds;

    public EditDictionaryPanel(){
        notLocalisedComponents = new HashMap<>();
        localisedComponents = new HashMap<>();
        localisedFieldsIds = new HashMap<>();

        initView();
    }

    private void initView() {
        setLayout(new BorderLayout());

        saveButton = createSaveButton();
        add(saveButton, BorderLayout.SOUTH);
    }

    private JButton createSaveButton() {
        JButton button = new JButton(Labels.SAVE);
        button.addActionListener(e -> {
            try {
                save(editedDictionary);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                e1.printStackTrace();
            }
        });
        return button;
    }

    private void save(Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        updateNotLocalisedValues(dictionary);
        updateLocalisedValues(dictionary);
        // TODO zapisać zlokalizowane wartości i słównik.
        // przy zapisywaniu nowych wartośći, dodać ID do mapy
       throw new NotImplementedException();
    }

    private void updateNotLocalisedValues(Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String fieldName;
        String value;
        for(Map.Entry<String, JTextComponent> entry : notLocalisedComponents.entrySet()){
            fieldName = entry.getKey();
            value = entry.getValue().getText();
            setValue(dictionary, fieldName, String.class, value);
        }
    }

    private void updateLocalisedValues(Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String fieldName;
        Long value;
        for(Map.Entry<String, Map<String, LocalisedComponent>> languageEntry : localisedComponents.entrySet()){
            for(Map.Entry<String, LocalisedComponent> entry : languageEntry.getValue().entrySet()){
                fieldName = entry.getKey();
                // TODO to może można jakoś skrócić
                value = entry.getValue().getLocalisedString().getKey().getId();
                setValue(dictionary, fieldName, Long.class, value);
            }
        }
    }

    private void saveLocalisedValue(){
        throw new NotImplementedException();
    }

    private void setValue(Dictionary dictionary, String fieldName, Class parameterClass, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = dictionary.getClass().getMethod("set"+fieldName, parameterClass);
        method.invoke(dictionary, value);
    }

    private JPanel createValuesPanel(Dictionary dictionary){
        List<Method> methods = getMethods(dictionary);
        List<String> notLocalisedFields = new ArrayList<>();
        List<String> localisedFields = new ArrayList<>();
        for(Method method: methods){
            if(method.getReturnType() == Long.class){
                localisedFields.add(getFieldName(method));
            } else if(method.getReturnType() == String.class){
                notLocalisedFields.add(getFieldName(method));
            } else {
                throw new IllegalArgumentException();
            }
        }
        JPanel notLocalisedPanel = createNotLocalisedPanel(notLocalisedFields);
        JPanel localisedPanel = createLocalisedPanel(localisedFields);

        JPanel resultPanel = new JPanel(new RiverLayout());
        resultPanel.add(RiverLayout.HFILL, notLocalisedPanel);
        resultPanel.add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL, localisedPanel);

        return resultPanel;
    }

    private JPanel createNotLocalisedPanel(List<String> fieldsNames){
        JPanel panel = new JPanel(new RiverLayout()); // TODO określić
        for(String field : fieldsNames){
            JLabel label = new JLabel(field);
            JTextComponent component = new JTextField();
            addToPanel(panel, label, component);
            addNotLocalisedComponent(field, component);
        }

        return panel;
    }

    private void addToPanel(JPanel panel, JLabel label, JComponent component){
        panel.add(RiverLayout.LINE_BREAK, label);
        panel.add(RiverLayout.HFILL, component);
    }

    private void addNotLocalisedComponent(String fieldName, JTextComponent component){
        notLocalisedComponents.put(fieldName, component);
    }

    private List<Method> getMethods(Dictionary dictionary) {
        Method[] methods = dictionary.getClass().getMethods();
        List<Method> methodList = Arrays.stream(methods).filter(x->
                x.getName().startsWith("get") && !x.getName().equals("getClass") && !x.getName().equals("getId"))
                .collect(Collectors.toList());
        return methodList;
    }

    private String getFieldName(Method method){
        return method.getName().substring(3, method.getName().length());
    }

    // TODO refactor
    private JPanel createLocalisedPanel(List<String> fieldsNames) {
        final int ELEMENT_MARGIN = 10;
        final boolean REORDERING_ALLOWED = true;

        WebComponentPanel panel = new WebComponentPanel();
        panel.setElementMargin(ELEMENT_MARGIN);
        panel.setReorderingAllowed(REORDERING_ALLOWED);
        for(String language : getLangauges()) {
            Map<String, LocalisedComponent> map = new HashMap<>();
            localisedComponents.put(language, map);
            // TODO przemyśleć to jeszcze
            JPanel languagePanel = new JPanel(new BorderLayout());

            JPanel valuesPanel = new JPanel(new RiverLayout());
            for(String fieldName : fieldsNames) {
                JLabel label = new JLabel(fieldName);
                JTextComponent component = new JTextField();
                addToPanel(valuesPanel, label, component);
                // TODO zobaczyć, czy nie można zrobić tego w inny sposób
                LocalisedComponent localisedComponent = new LocalisedComponent(component, null, language);
                map.put(fieldName, localisedComponent);
            }
            JLabel languageLabel = new JLabel(language);
            languagePanel.add(languageLabel, BorderLayout.WEST);
            languagePanel.add(valuesPanel, BorderLayout.CENTER);

            panel.addElement(languagePanel);
            // TODO dodać te dwa panele do innego panelu
        }
        return panel;
    }

    private List<String> getLangauges(){
        return Arrays.stream(Language.values())
                .map(Language::getAbbreviation)
                .collect(Collectors.toList());
    }

    public void load(@NotNull Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (editedDictionary != dictionary){
            editedDictionary = dictionary;
            clear();
            addValuesPanel(createValuesPanel(dictionary));
        }
        editedDictionary = dictionary;
        loadNotLocalisedFields(editedDictionary);
        loadLocalisedFields(editedDictionary);

        validate();
        repaint();
    }

    private void loadNotLocalisedFields(Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String value;
        for(Map.Entry<String, JTextComponent> entry: notLocalisedComponents.entrySet()){
            value = (String) getValue(dictionary, entry.getKey());
            entry.getValue().setText(value);
        }
    }

    private void loadLocalisedFields(Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Long id;
        for(Map.Entry<String, Map<String, LocalisedComponent>> languageEntry: localisedComponents.entrySet()) {
            String language = languageEntry.getKey();
            for(Map.Entry<String, LocalisedComponent> entry : languageEntry.getValue().entrySet()) {
                String fieldName = entry.getKey();
                id = (Long) getValue(dictionary, fieldName);
                LocalisedString localisedString = getLocalisedString(id, language);
                // TODO zrobić trochę czytelniej z nullem
                entry.getValue().setLocalisedString(localisedString);
                if(id != null) {
                    localisedFieldsIds.put(fieldName, localisedString.getKey().getId());
                }
            }
        }
    }

    private LocalisedString getLocalisedString(Long id, String language){
        if(id == null){
            return null;
        }
        LocalisedKey key = new LocalisedKey(id, language);
        return RemoteService.localisedStringServiceRemote.findStringsByKey(key);
    }

    private Object getValue(Dictionary dictionary, String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = dictionary.getClass().getMethod("get"+fieldName);
        return method.invoke(dictionary);
    }

    private void addValuesPanel(JPanel panel){
        valuesPanel = panel;
        add(valuesPanel, BorderLayout.CENTER);
    }

    private void clear(){
        if(valuesPanel != null){
            remove(valuesPanel);
        }
        notLocalisedComponents.clear();
        localisedComponents.forEach((k,v)->v.clear());
        localisedComponents.clear();
        localisedFieldsIds.clear();
    }


    private class LocalisedComponent{
        private String language;
        private JTextComponent component;
        private LocalisedString localisedString;

        public LocalisedComponent(JTextComponent component, LocalisedString localisedString, String language){
            this.component = component;
            this.language = language;
            this.localisedString = localisedString;
            if(this.localisedString == null){
                this.localisedString = new LocalisedString();
                this.localisedString.addKey(null, language);
            }
        }

        public LocalisedString getLocalisedString(){
            updateLocalisedString();
            return localisedString;
        }

        private void updateLocalisedString(){
            localisedString.setValue(component.getText());
        }

        private void updateComponentValue() {
            component.setText(localisedString.getValue());
        }

        public void setLocalisedString(LocalisedString localisedString){
            this.localisedString = localisedString;
            if(this.localisedString == null) {
                this.localisedString = new LocalisedString();
                this.localisedString.addKey(null, language);
            }
            updateComponentValue();
        }
    }
}
