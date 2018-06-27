package pl.edu.pwr.wordnetloom.client.plugins.administrator.labelEditor;

import com.alee.extended.panel.WebComponentPanel;
import com.alee.laf.text.WebTextArea;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.client.systems.errors.ValidationManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.localisation.model.ApplicationLabel;


import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.*;
import java.awt.*;
import java.util.List;

class EditLabelPanel extends JPanel{

    // TODO dorobić etykiety
    private final String ADDING_MODE = "Dodawanie";
    private final String EDITING_MODE = "Edytowanie";
    private final String EMPTY_MODE = " ";


    public interface EditLabelListener {
        void onSave(String key);
    }

    private final int LANGUAGE_LABEL_WIDTH = 50;
    private JTextField labelName;
    private Map<String, JComponent> componentsMap;
    private List<ApplicationLabel> labels;
    private EditLabelListener listener;
    private JLabel modeLabel;
    private JButton saveButton;

    private ValidationManager validationManager;

    // TODO można dorobić zapisywanie kolejności języków
    EditLabelPanel(EditLabelListener listener) {
        super();
        this.listener = listener;
        labels = new ArrayList<>();
        componentsMap = new HashMap<>();
        initView();
        setComponentsEnabled(false);
        validationManager = initValidation();
    }

    private void initView(){
        setLayout(new BorderLayout());

        JPanel namePanel = createInfoLabelPanel();
        JPanel labelsPanel = createLabelsPanel();
        saveButton = MButton.buildSaveButton()
                .withToolTip(Labels.SAVE)
                .withActionListener(e -> saveLabel());


        add(namePanel, BorderLayout.NORTH);
        add(new JScrollPane(labelsPanel), BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private ValidationManager initValidation(){
        ValidationManager manager = new ValidationManager();
        // TODO zrobić etykiety
        manager.registerError(labelName, "Pole nie może być puste",()->labelName.getText().isEmpty());
        manager.registerError(labelName, "Nazwa jest już zajęta", ()->{
            if(isEditingMode()) return false;
            return checkNameIsUsed(labelName.getText());
        });
        componentsMap.forEach((k,v)->manager.registerError(v,"Pole nie może być puste",()-> ((JTextArea)v).getText().isEmpty()));

        return manager;
    }

    private boolean checkNameIsUsed(String name) {
        return !RemoteService.localisedStringServiceRemote.findStringInAllLanguages(name).isEmpty();
    }

    private boolean isEditingMode() {
        return modeLabel.getText().equals(EDITING_MODE);
    }

    private JPanel createInfoLabelPanel() {
        // creating label name
        final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        labelName = new JTextField();
        labelName.setFont(FONT);
        // TODO dorobić etykiety
        JLabel label = new JLabel("Nazwa");
        JPanel componentGroup = new JPanel(new BorderLayout());
        // create mode name label (editing old label or creating new label)
        modeLabel = new JLabel(" ", SwingConstants.CENTER);
        modeLabel.setFont(FONT);

        componentGroup.add(modeLabel, BorderLayout.NORTH);
        componentGroup.add(label, BorderLayout.WEST);
        componentGroup.add(labelName, BorderLayout.CENTER);
        return componentGroup;
    }

    public void setComponentsEnabled(boolean enabled){
        labelName.setEnabled(enabled);
        componentsMap.forEach((k,v)->v.setEnabled(enabled));
        saveButton.setEnabled(enabled);
        if(!enabled){
            modeLabel.setText(EMPTY_MODE);
        }
    }

    private JPanel createLabelsPanel() {
        final int ELEMENT_MARGIN = 10;

        WebComponentPanel panel = new WebComponentPanel();
        panel.setElementMargin(ELEMENT_MARGIN);
        panel.setReorderingAllowed(true);

        for (Language language : Language.values()) {
            panel.addElement(createLanguagePanel(language.getAbbreviation()));
        }
        return panel;
    }

    private JPanel createLanguagePanel(String language) {
        final int ROWS_NUM = 2;

        JLabel label = new JLabel(language);
        label.setPreferredSize(new Dimension(LANGUAGE_LABEL_WIDTH, label.getHeight()));

        JTextArea textField = new WebTextArea();
        textField.setRows(ROWS_NUM);
        textField.setLineWrap(true);

        componentsMap.put(language, textField);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private void saveLabel() {
        if(validateFields()){
            for(ApplicationLabel label : labels){
                label.setKey(labelName.getText());
                label.setValue(((JTextArea)componentsMap.get(label.getLanguage())).getText());
                RemoteService.localisedStringServiceRemote.save(label);
            }
            listener.onSave(labelName.getText());
        }
    }

    private boolean validateFields(){
        return validationManager.validate();
    }

    void loadLabel(ApplicationLabel applicationLabel) {
        if(isNewLabel(applicationLabel)){
            prepareCreatingLabel();
        } else {
            prepareEditingLabel(applicationLabel);
        }
        setComponentsEnabled(true);
    }

    private boolean isNewLabel(ApplicationLabel applicationLabel) {
        return applicationLabel.getKey() == null;
    }

    private void prepareEditingLabel(ApplicationLabel applicationLabel) {
        labelName.setText(applicationLabel.getKey());
        labels = RemoteService.localisedStringServiceRemote.findStringInAllLanguages(applicationLabel.getKey());
        labels.forEach(l->editLabelValue(l.getLanguage(), l.getValue()));
        modeLabel.setText(EDITING_MODE);
    }

    private void prepareCreatingLabel() {
        clearFields();
        labels.clear();
        componentsMap.forEach((k,v)->{
            ApplicationLabel label = new ApplicationLabel();
            label.setLanguage(k);
            labels.add(label);
        });
        modeLabel.setText(ADDING_MODE);
    }

    private void editLabelValue(String language, String value) {
        JTextArea labelTextField = (JTextArea) componentsMap.get(language);
        labelTextField.setText(value!=null ? value : "");
    }

    private void clearFields(){
        labelName.setText("");
        componentsMap.forEach((k,v)->((JTextComponent)v).setText(""));
    }
}
