package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import com.alee.extended.panel.WebComponentPanel;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.errors.ValidationManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class EditDictionaryPanel extends JPanel {

    private JLabel modeLabel;
    private JPanel valuesPanel;
    private JButton saveButton;

    private Dictionary editedDictionary;
    private ValidationManager validationManager;
    private Map<String, JTextComponent> notLocalisedComponents;
    private Map<String, Map<String, LocalisedComponent>> localisedComponents;
    public EditDictionaryPanel(int width) {
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
                    , "Pole nie może być puste, jeżeli jest wypełnione w inych językach",
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
        if (validationManager.validate()) {
            updateNotLocalisedValues(dictionary);
            Map<String, Long> localisedIds = saveLocalisedStrings();
            updateLocalisedValues(dictionary, localisedIds);
            editedDictionary = saveDictionary(dictionary);
        }
    }

    private void updateLocalisedValues(Dictionary dictionary, Map<String, Long> localisedIds) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String fieldName;
        Long id;
        for (Map.Entry<String, Long> entry : localisedIds.entrySet()) {
            fieldName = entry.getKey();
            id = entry.getValue();
            setValue(dictionary, fieldName, Long.class, id);
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
        // get ant value
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

    private void updateNotLocalisedValues(Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String fieldName;
        String value;
        for (Map.Entry<String, JTextComponent> entry : notLocalisedComponents.entrySet()) {
            fieldName = entry.getKey();
            value = entry.getValue().getText();
            setValue(dictionary, fieldName, String.class, value);
        }
    }

    private void setValue(Dictionary dictionary, String fieldName, Class parameterClass, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = dictionary.getClass().getMethod("set" + fieldName, parameterClass);
        method.invoke(dictionary, value);
    }

    private JPanel createValuesPanel(Dictionary dictionary) {
        List<Method> methods = getMethods(dictionary);
        List<String> notLocalisedFields = new ArrayList<>();
        List<String> localisedFields = new ArrayList<>();

        for (Method method : methods) {
            if (method.getReturnType() == Long.class) {
                localisedFields.add(getFieldName(method));
            } else if (method.getReturnType() == String.class) {
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

    private JPanel createNotLocalisedPanel(List<String> fieldsNames) {
        final int LABEL_WIDTH = 50;
        final String COLOR_FIELD = "Color";
        JPanel panel = new JPanel(new RiverLayout());
        for (String field : fieldsNames) {
            JLabel label = new JLabel(field);
            label.setSize(LABEL_WIDTH, label.getHeight());
            if(field.contains(COLOR_FIELD)){
                ColorPanel colorPanel = new ColorPanel();
                addToPanel(panel,label, colorPanel);
                addNotLocalisedComponent(field, colorPanel.getColorField());
            } else {
                JTextComponent component = new JTextField();
                addToPanel(panel, label, component);
                addNotLocalisedComponent(field, component);
            }
        }
        return panel;
    }

    private class ColorPanel extends JPanel{
        final int PREVIEW_SIZE = 20;

        private JTextField colorField;
        private JButton colorButton;
        private JPanel colorPreview;
        private Color currentColor;

        public ColorPanel() {
            initComponents();
            initView();
        }

        private void initComponents() {
            colorField = new JTextField();
            colorField.setEditable(false);
            colorField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    trySetColor(colorField.getText());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    trySetColor(colorField.getText());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    trySetColor(colorField.getText());
                }
            });

            colorPreview = new JPanel();
            colorPreview.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));

            colorButton = new MButton();
            colorButton.addActionListener(e-> {
                Color col = showColorPicker();
                setColor(col);
            });
        }

        private void trySetColor(String text){
            if(text.isEmpty()){
                return;
            }
            Color color = Color.decode(text);
            if(color != null){
                setColor(color);
            }
        }

        public void setEditable(boolean editable) {
            colorField.setEditable(editable);
        }

        private void initView() {
            setLayout(new BorderLayout());

            add(colorPreview, BorderLayout.WEST);
            add(colorField, BorderLayout.CENTER);
            add(colorButton, BorderLayout.EAST);
        }

        public JTextField getColorField() {
            return colorField;
        }

        public Color getColor() {
            return currentColor;
        }

        private Color showColorPicker() {
             return JColorChooser.showDialog(null, Labels.COLOR_COLON, currentColor);
        }

        private void setColor(String color) {
            setColor(Color.decode(color));
        }

        private void setColor(Color color){
            currentColor = color;
            colorPreview.setBackground(color);
            String colorText = getHex(color);
            if(!colorField.getText().equals(colorText)) {
                colorField.setText(colorText);
            }
        }

        private String getHex(Color color) {
            return "#" + Integer.toHexString(color.getRGB()).substring(2);
        }
    }

    private void addToPanel(JPanel panel, JLabel label, JComponent component) {
        panel.add(RiverLayout.LINE_BREAK, label);
        panel.add(RiverLayout.HFILL, component);
    }

    private void addNotLocalisedComponent(String fieldName, JTextComponent component) {
        notLocalisedComponents.put(fieldName, component);
    }

    private List<Method> getMethods(Dictionary dictionary) {
        Method[] methods = dictionary.getClass().getMethods();
        List<Method> methodList = Arrays.stream(methods).filter(x ->
                x.getName().startsWith("get") && !x.getName().equals("getClass") && !x.getName().equals("getId"))
                .collect(Collectors.toList());
        return methodList;
    }

    private String getFieldName(Method method) {
        return method.getName().substring(3, method.getName().length());
    }

    // TODO refactor
    private JPanel createLocalisedPanel(List<String> fieldsNames) {
        final int ELEMENT_MARGIN = 10;
        final boolean REORDERING_ALLOWED = true;

        WebComponentPanel webComponentPanel = new WebComponentPanel();
        webComponentPanel.setElementMargin(ELEMENT_MARGIN);
        webComponentPanel.setReorderingAllowed(REORDERING_ALLOWED);
        for (String language : getLangauges()) {
            Map<String, LocalisedComponent> map = new HashMap<>();
            localisedComponents.put(language, map);

            JPanel valuesPanel = new JPanel(new RiverLayout());
            for (String fieldName : fieldsNames) {
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

    public void load(Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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

    private void loadNotLocalisedFields(Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String value;
        for (Map.Entry<String, JTextComponent> entry : notLocalisedComponents.entrySet()) {
            value = (String) getValue(dictionary, entry.getKey());
            entry.getValue().setText(value);
        }
    }

    private void loadLocalisedFields(Dictionary dictionary) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        localisedComponentsMapIterate((language, fieldName, component) -> {
            Long id = (Long) getValue(dictionary, fieldName);
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

    private Object getValue(Dictionary dictionary, String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = dictionary.getClass().getMethod("get" + fieldName);
        return method.invoke(dictionary);
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
        void run(String language, String fieldName, LocalisedComponent component) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
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
