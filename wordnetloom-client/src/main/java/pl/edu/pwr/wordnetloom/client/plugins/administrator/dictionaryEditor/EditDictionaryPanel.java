package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import com.alee.extended.panel.WebComponentPanel;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EditDictionaryPanel extends JPanel {

    private JLabel modeLabel;
    private JPanel valuesPanel;
    private JButton saveButton;

    private Dictionary editedDictionary;

    private interface MapIteratorFunc{
        void run(String language, String fieldName, LocalisedComponent component) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
    }

    private Map<String, JTextComponent> notLocalisedComponents;
    private Map<String, Map<String, LocalisedComponent>> localisedComponents;

    public EditDictionaryPanel(int width){
        notLocalisedComponents = new LinkedHashMap<>();
        localisedComponents = new LinkedHashMap<>();

        initView(width);
    }

    private void initView(int width) {
        setLayout(new BorderLayout());

        saveButton = createSaveButton();
        modeLabel = new JLabel(" ");
        add(modeLabel, BorderLayout.NORTH);
        add(saveButton, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(width,getHeight()));
        setEnabled(false);
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
        Map<String, Long> localisedIds = saveLocalisedStrings();
        updateLocalisedValues(dictionary, localisedIds);
        editedDictionary = saveDictionary(dictionary);
    }

    private void updateLocalisedValues(Dictionary dictionary, Map<String, Long> localisedIds) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String fieldName;
        Long id;
        for(Map.Entry<String, Long> entry : localisedIds.entrySet()) {
            fieldName = entry.getKey();
            id = entry.getValue();
            setValue(dictionary, fieldName, Long.class, id);
        }
    }

    /**
     * Save all not empty values.
     * @return ids of LocalisationKey for every field
     */
    private Map<String, Long> saveLocalisedStrings() {
        // result map
        Map<String, Long> idsMap = new LinkedHashMap<>();
        // get ant value
        Map<String, LocalisedComponent> fieldsMap = localisedComponents.entrySet().iterator().next().getValue();
        fieldsMap.forEach((fieldName, component)->{
            List<LocalisedString> localisedStringList = new ArrayList<>();
            localisedComponents.keySet().forEach(language->{
                LocalisedString localisedString = localisedComponents.get(language).get(fieldName).getLocalisedString();

                if(localisedString.getKey().getId() != null || !localisedString.getValue().isEmpty()) {
                    localisedStringList.add(localisedString);
                }
            });
            if(!localisedStringList.isEmpty()){
                Long id = RemoteService.localisedStringServiceRemote.save(localisedStringList);
                idsMap.put(fieldName, id);
            }
        });
        return idsMap;
    }

    private Dictionary saveDictionary(Dictionary dictionary) {
        return RemoteService.dictionaryServiceRemote.save(dictionary);
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
            int LABEL_WIDTH = 50;
            label.setSize(LABEL_WIDTH, label.getHeight());
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

        WebComponentPanel webComponentPanel = new WebComponentPanel();
        webComponentPanel.setElementMargin(ELEMENT_MARGIN);
        webComponentPanel.setReorderingAllowed(REORDERING_ALLOWED);
        for(String language : getLangauges()) {
            Map<String, LocalisedComponent> map = new HashMap<>();
            localisedComponents.put(language, map);

            JPanel valuesPanel = new JPanel(new RiverLayout());
            for(String fieldName : fieldsNames) {
                JLabel label = new JLabel(fieldName);
                JTextComponent component = new JTextField();
                addToPanel(valuesPanel, label, component);
                // TODO zobaczyć, czy nie można zrobić tego w inny sposób
                LocalisedComponent localisedComponent = new LocalisedComponent(component, null, language);
                map.put(fieldName, localisedComponent);
            }
            JPanel languagePanel = new JPanel(new BorderLayout());
            JLabel languageLabel = new JLabel(language);
            languagePanel.add(languageLabel, BorderLayout.WEST);
            languagePanel.add(valuesPanel, BorderLayout.CENTER);

            webComponentPanel.addElement(languagePanel);
        }
        return webComponentPanel;
    }

    private List<String> getLangauges(){
        return Arrays.stream(Language.values())
                .map(Language::getAbbreviation)
                .collect(Collectors.toList());
    }

    public void setEnabled(boolean enabled) {
        notLocalisedComponents.forEach((k,v)->v.setEnabled(enabled));
        localisedComponentsMapIterate((language, fieldName, component)->component.setComponentEnabled(enabled));
        saveButton.setEnabled(enabled);
    }

    private void localisedComponentsMapIterate(MapIteratorFunc func){
        for(Map.Entry<String, Map<String, LocalisedComponent>> languageEntry : localisedComponents.entrySet()){
            for(Map.Entry<String, LocalisedComponent> fieldEntry : languageEntry.getValue().entrySet()){
                try {
                    func.run(languageEntry.getKey(), fieldEntry.getKey(), fieldEntry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load(Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(dictionary == null) {
            clear();
            refreshView();
            return;
        }
        // if this dictionary class is equal old dictionary class, we not rebuild view
        if (editedDictionary== null || editedDictionary.getClass() != dictionary.getClass()){
            editedDictionary = dictionary;
            clear();
            addValuesPanel(createValuesPanel(dictionary));
        }
        editedDictionary = dictionary;
        if(dictionary.getId() != null){
            loadNotLocalisedFields(editedDictionary);
            loadLocalisedFields(editedDictionary);
            // TODO dorobić etykietę
            modeLabel.setText("Edycja");
        } else {
            // TODO dorobić etykietę
            modeLabel.setText("Dodawanie nowej wartosci");
        }

        refreshView();
        setEnabled(true);
    }

    private void refreshView() {
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
        localisedComponentsMapIterate((language, fieldName, component)->{
            Long id = (Long) getValue(dictionary, fieldName);
            LocalisedString localisedString = getLocalisedString(id, language);
            component.setLocalisedString(localisedString);
        });
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

    public void clear(){
        modeLabel.setText("");
        if(valuesPanel != null){
            remove(valuesPanel);
        }
        notLocalisedComponents.clear();
        localisedComponents.forEach((k,v)->v.clear());
        localisedComponents.clear();
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

        public void setComponentEnabled(boolean enabled){
            component.setEnabled(enabled);
        }
    }
}
