package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import com.alee.extended.panel.WebComponentPanel;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.errors.ValidationManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.ColorPanel;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.MessageInfo;
import pl.edu.pwr.wordnetloom.client.utils.ReflectionUtil;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class EditDictionaryPanel extends JPanel {

    public interface SaveListener {
        void onSave(Dictionary dictionary);
    }

    private JLabel modeLabel;
    private JPanel valuesPanel;
    private JButton saveButton;

    private SaveListener listener;

    private Dictionary editedDictionary;
    private ValidationManager validationManager;
    private Map<String, JComponent> notLocalisedComponents;
    private Map<String, Map<String, LocalisedComponent>> localisedComponents;


    public EditDictionaryPanel(int width, SaveListener listener) {
        this.listener = listener;
        notLocalisedComponents = new LinkedHashMap<>();
        localisedComponents = new LinkedHashMap<>();
        initView(width);
    }

    private ValidationManager initValidation() {
        final String NAME_FIELD = "Name";
        ValidationManager validationManager = new ValidationManager();
        localisedComponentsMapIterate((language, fieldName, component) -> {
            // TODO dorobić etykietę
            // field must be filled in all languages
            validationManager.registerError(component.getComponent()
                    , "Pole nie może być puste, jeżeli jest wypełnione w innych językach",
                    () -> {
                        JTextComponent comp = component.getComponent();
                        boolean isFilled = false;
                        if (comp.getText().isEmpty()) {
                            for (String lang : localisedComponents.keySet()) {
                                if (!localisedComponents.get(lang).get(fieldName).getComponent().getText().isEmpty()) {
                                    isFilled = true;
                                }
                            }
                        }
                        return isFilled;
                    });
            // name field is required
            if (fieldName.equals(NAME_FIELD)) {
                validationManager.registerError(component.getComponent(),
                        "Pole nie może być puste",
                        () -> component.getComponent().getText().isEmpty());
            }
        });

        return validationManager;
    }

    private void initView(int width) {
        final Font MODE_LABEL_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);

        setLayout(new BorderLayout());

        saveButton = createSaveButton();
        modeLabel = new JLabel(" ", SwingConstants.CENTER);
        modeLabel.setFont(MODE_LABEL_FONT);
        add(modeLabel, BorderLayout.NORTH);
        add(saveButton, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(width, getHeight()));
        setEnabled(false);
    }

    private JButton createSaveButton() {
        JButton button = new JButton(Labels.SAVE);
        button.addActionListener(e -> save(editedDictionary));
        return button;
    }

    private void save(Dictionary dictionary) {
        if (validationManager.validate()) {
            updateNotLocalisedValues(dictionary);
            Map<String, Long> localisedIds = saveLocalisedStrings();
            updateLocalisedValues(dictionary, localisedIds);
            editedDictionary = saveDictionary(dictionary);
            listener.onSave(editedDictionary);
            MessageInfo.showInfo("Zapisano", saveButton, MessageInfo.InfoType.SUCCESS);
        }
    }

    private void updateLocalisedValues(Dictionary dictionary, Map<String, Long> localisedIds){
        String fieldName;
        Long id;
        for (Map.Entry<String, Long> entry : localisedIds.entrySet()) {
            fieldName = entry.getKey();
            id = entry.getValue();
            ReflectionUtil.setValue(dictionary, fieldName, id, Long.class);
        }
    }

    /**
     * Save all not empty values.
     *
     * @return ids of LocalisationKey for every field
     */
    private Map<String, Long> saveLocalisedStrings() {
        // result map
        Map<String, Long> idsMap = new LinkedHashMap<>();
        // get any value
        Map<String, LocalisedComponent> fieldsMap = localisedComponents.entrySet().iterator().next().getValue();
        fieldsMap.forEach((fieldName, component) -> {
            List<LocalisedString> localisedStringList = new ArrayList<>();
            localisedComponents.keySet().forEach(language -> {
                LocalisedString localisedString = localisedComponents.get(language).get(fieldName).getLocalisedString();

                if (localisedString.getKey().getId() != null || !localisedString.getValue().isEmpty()) {
                    localisedStringList.add(localisedString);
                }
            });
            if (!localisedStringList.isEmpty()) {
                Long id = RemoteService.localisedStringServiceRemote.save(localisedStringList);
                idsMap.put(fieldName, id);
            }
        });
        return idsMap;
    }

    private Dictionary saveDictionary(Dictionary dictionary) {
        return RemoteService.dictionaryServiceRemote.save(dictionary);
    }

    private void updateNotLocalisedValues(Dictionary dictionary){
        String fieldName;
        String value;
        for (Map.Entry<String, JComponent> entry : notLocalisedComponents.entrySet()) {
            fieldName = entry.getKey();
            value = getText(entry.getValue());
            ReflectionUtil.setValue(dictionary, fieldName, value, String.class);
        }
    }

    private JPanel createValuesPanel(Dictionary dictionary) {
        List<Method> notLocalisedFields = new ArrayList<>();
        List<Method> localisedFields = new ArrayList<>();

        for(Method method: ReflectionUtil.getMethods(dictionary)){
            if(method.getReturnType() == Long.class){
                localisedFields.add(method);
            } else if (method.getReturnType() == String.class || method.getReturnType() == Boolean.class){
                notLocalisedFields.add(method);
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

    private JPanel createNotLocalisedPanel(List<Method> methods){
        final int LABEL_WIDTH = 50;
        final String COLOR_FIELD = "Color";
        JPanel panel = new JPanel(new RiverLayout());
        String field;
        JLabel label;
        for (Method method : methods) {
            field = getFieldName(method);
            label = new JLabel(field);
            label.setSize(LABEL_WIDTH, label.getHeight());
            if(field.contains(COLOR_FIELD)){
                ColorPanel colorPanel = new ColorPanel();
                addToPanel(panel,label, colorPanel);
                addNotLocalisedComponent(field, colorPanel.getColorField());
            } else {
                JComponent component = null;
                if(method.getReturnType() == String.class){
                    component = new JTextField();
                } else if (method.getReturnType() == Boolean.class){
                    component = new JCheckBox();
                } else {
                    throw new IllegalArgumentException();
                }
                addToPanel(panel, label, component);
                addNotLocalisedComponent(field, component);
            }
        }
        return panel;
    }

    private void addToPanel(JPanel panel, JLabel label, JComponent component) {
        panel.add(RiverLayout.LINE_BREAK, label);
        panel.add(RiverLayout.HFILL, component);
    }

    private void addNotLocalisedComponent(String fieldName, JComponent component) {
        notLocalisedComponents.put(fieldName, component);
    }

    private String getFieldName(Method method) {
        return method.getName().substring(3, method.getName().length());
    }

    private JPanel createLocalisedPanel(List<Method> methods){
        final int ELEMENT_MARGIN = 10;
        final boolean REORDERING_ALLOWED = true;

        WebComponentPanel webComponentPanel = new WebComponentPanel();
        webComponentPanel.setElementMargin(ELEMENT_MARGIN);
        webComponentPanel.setReorderingAllowed(REORDERING_ALLOWED);
        for (String language : getLangauges()) {
            Map<String, LocalisedComponent> map = new LinkedHashMap<>();
            localisedComponents.put(language, map);
            JPanel languagePanel = createLanguagePanel(methods, language, map);
            webComponentPanel.addElement(languagePanel);
        }
        return webComponentPanel;
    }

    private JPanel createLanguagePanel(List<Method> methods, String language, Map<String, LocalisedComponent> map){
        JPanel languagePanel = new JPanel(new BorderLayout());

        JPanel valuesPanel = new JPanel(new RiverLayout());
        String fieldName;
        JLabel label;
        JTextComponent component;
        LocalisedComponent localisedComponent;
        for (Method method : methods) {
            fieldName = getFieldName(method);
            label = new JLabel(fieldName);
            component = new JTextField();
            addToPanel(valuesPanel, label, component);

            localisedComponent = new LocalisedComponent(component, null, language);
            map.put(fieldName, localisedComponent);
        }

        languagePanel.add(new JLabel(language), BorderLayout.WEST);
        languagePanel.add(valuesPanel, BorderLayout.CENTER);
        return languagePanel;
    }

    private List<String> getLangauges() {
        return Arrays.stream(Language.values())
                .map(Language::getAbbreviation)
                .collect(Collectors.toList());
    }

    public void setEnabled(boolean enabled) {
        notLocalisedComponents.forEach((k, v) -> v.setEnabled(enabled));
        localisedComponentsMapIterate((language, fieldName, component) -> component.setComponentEnabled(enabled));
        saveButton.setEnabled(enabled);
    }

    private void localisedComponentsMapIterate(MapIteratorFunc func) {
        for (Map.Entry<String, Map<String, LocalisedComponent>> languageEntry : localisedComponents.entrySet()) {
            for (Map.Entry<String, LocalisedComponent> fieldEntry : languageEntry.getValue().entrySet()) {
                try {
                    func.run(languageEntry.getKey(), fieldEntry.getKey(), fieldEntry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load(Dictionary dictionary) {
        clearFields();
        if (dictionary == null) {
            clear();
            refreshView();
            return;
        }
        // if this dictionary class is equal old dictionary class, we not rebuild view
        if (editedDictionary == null || editedDictionary.getClass() != dictionary.getClass()) {
            editedDictionary = dictionary;
            clear();
            addValuesPanel(createValuesPanel(dictionary));
        }
        editedDictionary = dictionary;
        if (dictionary.getId() != null) {
            loadNotLocalisedFields(editedDictionary);
            loadLocalisedFields(editedDictionary);
            // TODO dorobić etykietę
            modeLabel.setText("Edycja");
        } else {
            // TODO dorobić etykietę
            modeLabel.setText("Dodawanie nowej wartosci");
        }
        validationManager = initValidation();
        refreshView();
        setEnabled(true);
    }

    private void refreshView() {
        validate();
        repaint();
    }

    private void loadNotLocalisedFields(Dictionary dictionary){
        Object value;
        for (Map.Entry<String, JComponent> entry : notLocalisedComponents.entrySet()) {
            value = ReflectionUtil.getValue(dictionary, entry.getKey());
            setValue(entry.getValue(), value);
        }
    }

    private void setValue(JComponent component, Object value){
        if (value instanceof String){
            ((JTextField)component).setText((String)value);
        } else if(value instanceof Boolean){
            ((JCheckBox)component).setSelected((Boolean)value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private String getText(JComponent component){
        if (component instanceof JTextField){
            return ((JTextField)component).getText();
        }
        if(component instanceof JCheckBox){
            return ((JCheckBox)component).getText();
        }
        throw new IllegalArgumentException();
    }

    private void loadLocalisedFields(Dictionary dictionary) {
        localisedComponentsMapIterate((language, fieldName, component) -> {
            Long id = ReflectionUtil.getLongValue(dictionary, fieldName);
            LocalisedString localisedString = getLocalisedString(id, language);
            component.setLocalisedString(localisedString);
        });
    }

    private LocalisedString getLocalisedString(Long id, String language) {
        if (id == null) {
            return null;
        }
        LocalisedKey key = new LocalisedKey(id, language);
        return RemoteService.localisedStringServiceRemote.findStringsByKey(key);
    }

    private void addValuesPanel(JPanel panel) {
        valuesPanel = panel;
        add(valuesPanel, BorderLayout.CENTER);
    }

    private void clear() {
        if (valuesPanel != null) {
            remove(valuesPanel);
        }
        notLocalisedComponents.clear();
        localisedComponents.forEach((k, v) -> v.clear());
        localisedComponents.clear();
    }

    private void clearFields() {
        modeLabel.setText("");
        localisedComponentsMapIterate((language, fieldName, component) -> {
            component.getComponent().setText("");
        });
    }

    private interface MapIteratorFunc {
        void run(String language, String fieldName, LocalisedComponent component);
    }

    private class LocalisedComponent {
        private String language;
        private JTextComponent component;
        private LocalisedString localisedString;

        public LocalisedComponent(JTextComponent component, LocalisedString localisedString, String language) {
            this.component = component;
            this.language = language;
            this.localisedString = localisedString;
            if (this.localisedString == null) {
                this.localisedString = new LocalisedString();
                this.localisedString.addKey(null, language);
            }
        }

        public LocalisedString getLocalisedString() {
            updateLocalisedString();
            return localisedString;
        }

        public void setLocalisedString(LocalisedString localisedString) {
            this.localisedString = localisedString;
            if (this.localisedString == null) {
                this.localisedString = new LocalisedString();
                this.localisedString.addKey(null, language);
            }
            updateComponentValue();
        }

        private void updateLocalisedString() {
            localisedString.setValue(component.getText());
        }

        private void updateComponentValue() {
            component.setText(localisedString.getValue());
        }

        public void setComponentEnabled(boolean enabled) {
            component.setEnabled(enabled);
        }

        public JTextComponent getComponent() {
            return component;
        }
    }
}
