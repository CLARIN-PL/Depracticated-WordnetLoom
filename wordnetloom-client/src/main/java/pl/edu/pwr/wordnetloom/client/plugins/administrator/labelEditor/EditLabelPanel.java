package pl.edu.pwr.wordnetloom.client.plugins.administrator.labelEditor;

import com.alee.extended.panel.WebComponentPanel;
import com.alee.laf.text.WebTextArea;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.localisation.model.ApplicationLabel;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class EditLabelPanel extends JPanel {

    public interface EditLabelListener {
        void onSave(String key);
    }

    private final int LANGUAGE_LABEL_WIDTH = 50;
    private JTextField labelName;
    private Map<String, JComponent> componentsMap;
    private List<ApplicationLabel> labels;
    private EditLabelListener listener;

    // TODO można dorobić zapisywanie kolejności języków
    EditLabelPanel(EditLabelListener listener) {
        super();
        this.listener = listener;
        labels = new ArrayList<>();
        setLayout(new BorderLayout());
        JPanel namePanel = createLabelNamePanel();
        componentsMap = new HashMap<>();
        JPanel labelsPanel = createLabelsPanel();
        JButton saveButton = MButton.buildSaveButton()
                .withToolTip(Labels.SAVE)
                .withActionListener(e -> saveLabel());
        JScrollPane labelsPanelScroll = new JScrollPane(labelsPanel);
        add(namePanel, BorderLayout.NORTH);
        add(labelsPanelScroll, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }


    private JPanel createLabelNamePanel() {
        labelName = new JTextField();
        labelName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        // TODO dorobić etykiety
        JLabel label = new JLabel("Nazwa");
        JPanel componentGroup = new JPanel(new BorderLayout());
        componentGroup.add(label, BorderLayout.WEST);
        componentGroup.add(labelName, BorderLayout.CENTER);
        return componentGroup;
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
        // TODO zrobić walidacje
        for(ApplicationLabel label : labels){
            label.setKey(labelName.getText());
            label.setValue(((JTextArea)componentsMap.get(label.getLanguage())).getText());
            RemoteService.localisedStringServiceRemote.save(label);
        }
        listener.onSave(labelName.getText());
    }

    void loadLabel(ApplicationLabel applicationLabel) {
        if(applicationLabel.getKey() == null){
            prepareCreatingLabel();
        } else {
            prepareEditingLabel(applicationLabel);
        }
    }

    private void prepareEditingLabel(ApplicationLabel applicationLabel) {
        labelName.setText(applicationLabel.getKey());
        labels = RemoteService.localisedStringServiceRemote.findStringInAllLanguages(applicationLabel.getKey());
        for (ApplicationLabel label : labels) {
            editLabelValue(label.getLanguage(), label.getValue());
        }
    }

    private void prepareCreatingLabel() {
        // clear fields
        clear();
        labels.clear();
        for(Map.Entry<String, JComponent> entry: componentsMap.entrySet()){
            ApplicationLabel label = new ApplicationLabel();
            label.setLanguage(entry.getKey());
            labels.add(label);
        }
    }

    private void editLabelValue(String language, String value) {
        JTextArea labelTextField = (JTextArea) componentsMap.get(language);
        labelTextField.setText(value!=null ? value : "");
    }

    private void clear(){
        labelName.setText("");
        for(Map.Entry<String, JComponent> entry : componentsMap.entrySet()) {
            ((JTextArea)entry.getValue()).setText("");
        }
    }
}
