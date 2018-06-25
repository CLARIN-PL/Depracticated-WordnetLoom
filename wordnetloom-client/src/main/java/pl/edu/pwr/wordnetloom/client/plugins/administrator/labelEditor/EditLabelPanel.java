package pl.edu.pwr.wordnetloom.client.plugins.administrator.labelEditor;

import com.alee.extended.panel.WebComponentPanel;
import com.alee.laf.text.WebTextArea;
import com.alee.laf.text.WebTextField;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class EditLabelPanel extends JPanel {

    public interface EditLabelListener{
        void onSave(String labelName);
    }

    private final int LANGUAGE_LABEL_WIDTH = 50;
    private JTextField labelName;
    private Map<String, JComponent> componentsMap;

    private Long labelId;

    // TODO można dorobić zapisywanie kolejności języków
    EditLabelPanel() {
        super();
        setLayout(new BorderLayout());
        labelName = createLabelNameField();
        componentsMap = new HashMap<>();
        JPanel labelsPanel = createLabelsPanel();
        JButton saveButton = MButton.buildSaveButton()
                .withToolTip(Labels.SAVE)
                .withActionListener(e -> saveLabel());
        JScrollPane labelsPanelScroll = new JScrollPane(labelsPanel);
        add(labelName, BorderLayout.NORTH);
        add(labelsPanelScroll, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private JTextField createLabelNameField() {
        JTextField textField = new JTextField();
        textField.setEditable(false);
        textField.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        textField.setEnabled(false);
        return textField;
    }

    private JPanel createLabelsPanel() {
        WebComponentPanel panel = new WebComponentPanel();
        panel.setElementMargin(10);
        panel.setReorderingAllowed(true);
        Language[] languages = Language.values();
        for (Language language : languages) {
            panel.addElement(createLanguagePanel(language.getAbbreviation()));
        }
        return panel;
    }

    private JPanel createLanguagePanel(String language) {
        JLabel label = new JLabel(language);
        label.setPreferredSize(new Dimension(LANGUAGE_LABEL_WIDTH, label.getHeight()));
        JTextArea textField = new WebTextArea();
        textField.setRows(2);
        textField.setLineWrap(true);
        componentsMap.put(language, textField);
        JPanel componentGroup = new JPanel(new BorderLayout());
        componentGroup.add(label, BorderLayout.WEST);
        componentGroup.add(textField, BorderLayout.CENTER);
        return componentGroup;
    }

    private void saveLabel() {
        String language;
        String value;
        for(Map.Entry<String, JComponent> entry: componentsMap.entrySet()){
            language = entry.getKey();
            value = ((JTextField)entry.getValue()).getText();
            // TODO tutaj zrobić zapisywanie
            LocalisedKey key = new LocalisedKey();
            // TODO zrobić pobieranie numeru id

        }

        throw new NotImplementedException();
    }

    void loadLabel(String name) {
        // TODO zrobić pobieranie numeru id
        Map<String, String> labelsMap = RemoteService.localisedStringServiceRemote.findStringInAllLanguages(name);
        for (Map.Entry<String, String> entry : labelsMap.entrySet()) {
            editLabelValue(entry.getKey(), entry.getValue());
        }
    }

    private void editLabelValue(String language, String value) {
        JTextArea labelTextField = (JTextArea) componentsMap.get(language);
        labelTextField.setText(value);
    }
}
