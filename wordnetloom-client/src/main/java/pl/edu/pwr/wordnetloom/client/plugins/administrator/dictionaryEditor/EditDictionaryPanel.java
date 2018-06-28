package pl.edu.pwr.wordnetloom.client.plugins.administrator.dictionaryEditor;

import com.alee.extended.panel.WebComponentPanel;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.enums.Language;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EditDictionaryPanel extends JPanel {
    private JLabel dictionaryType;
    private Map<String, DictionaryComponent> componentsMap;
    private JButton saveButton;

    public EditDictionaryPanel() {
        super();
        componentsMap = new HashMap<>();
        initView();
    }

    private void initView(){
        setLayout(new BorderLayout());

        dictionaryType = new JLabel();
        JPanel valuesPanel = createValuesPanel();

        add(valuesPanel);
    }

    private JPanel createValuesPanel() {
        final int ELEMENT_MARGIN = 10;
        WebComponentPanel panel = new WebComponentPanel();
        panel.setElementMargin(ELEMENT_MARGIN);
        panel.setReorderingAllowed(true);

        for(Language language : Language.values()){
            panel.addElement(createDictionaryPanel(language.getAbbreviation()));
        }

        return panel;
    }

    private JPanel createDictionaryPanel(String language){
        final int DESCRIPTION_ROWS_NUM = 2;
        
        JLabel languageLabel = new JLabel(language);

        JTextField nameField = new JTextField();

        JTextArea descriptionField = new JTextArea();
        descriptionField.setRows(DESCRIPTION_ROWS_NUM);
        descriptionField.setLineWrap(true);

        DictionaryComponent component = new DictionaryComponent(language);
        component.setNameField(nameField);
        component.setDescriptionField(descriptionField);
        componentsMap.put(language, component);

        JPanel resultPanel = new JPanel(new BorderLayout());

        JPanel valuesPanel = new JPanel(new RiverLayout());
        JLabel nameLabel = new JLabel("Nazwa");
        JLabel descriptionLabel = new JLabel("Opis");
        valuesPanel.add(nameLabel);
        valuesPanel.add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL, nameField);
        valuesPanel.add(RiverLayout.LINE_BREAK, descriptionLabel);
        valuesPanel.add(RiverLayout.LINE_BREAK + " " + RiverLayout.HFILL, descriptionField);

        resultPanel.add(languageLabel, BorderLayout.WEST);
        resultPanel.add(valuesPanel, BorderLayout.CENTER);

        return resultPanel;
    }

    public void load(Dictionary dictionary, String dictionaryType) {
        System.out.println("HAHAH");
        this.dictionaryType.setText(dictionaryType);
        componentsMap.forEach((k,v)-> v.setValues(dictionary));
    }

    private class DictionaryComponent{
        private String language;
        private JTextField nameField;
        private JTextArea descriptionField;

        private LocalisedString localisedStringName;
        private LocalisedString localisedStringDescription;

        DictionaryComponent(String language){
            this.language = language;
        }

        void setNameField(JTextField nameField){
            this.nameField = nameField;
        }

        void setDescriptionField(JTextArea descriptionField){
            this.descriptionField = descriptionField;
        }

        public void setValues(Dictionary dictionary){
            if(dictionary != null){
                setName(dictionary.getName());
                setDescription(dictionary.getDescription());
            }
        }

        public void setName(Long nameId){
            LocalisedKey key = new LocalisedKey(nameId, language);
            localisedStringName = RemoteService.localisedStringServiceRemote.findStringsByKey(key);
            nameField.setText(localisedStringName.getValue());
        }

        void setDescription(Long descriptionId){
            if(descriptionId == null){
                descriptionField.setText("");
                return;
            }
            LocalisedKey key = new LocalisedKey(descriptionId, language);
            localisedStringDescription = RemoteService.localisedStringServiceRemote.findStringsByKey(key);
            descriptionField.setText(localisedStringDescription.getValue());
        }

        public String getName() {
            return nameField.getText();
        }

        public String getDescription(){
            return descriptionField.getText();
        }

    }

}
