package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.rootpane.WebFrame;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.ExampleFrame;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.errors.ValidationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.*;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.renderers.*;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.utils.PermissionHelper;
import pl.edu.pwr.wordnetloom.common.model.Example;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.dictionary.model.Status;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;
import pl.edu.pwr.wordnetloom.word.model.Word;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class LexicalUnitPropertiesPanel extends JPanel {

    private final String DEFAULT_VARIANT = "1";

    private Sense unit;

    private JTextField lemmaTextField;
    private JTextField variantTextField;
    private JComboBox lexiconComboBox;
    private JComboBox partOfSpeechComboBox;
    private JComboBox domainComboBox;
    private JComboBox registerComboBox;
    private JComboBox statusComboBox;
    private JTextArea definitionArea;
    private JScrollPane definitionScroll;
    private JTextArea commentArea;
    private JScrollPane commentScroll;
    private JList examplesList;
    private DefaultListModel examplesModel;
    private JTextField linkTextField;

    private JButton addExampleButton;
    private JButton removeExampleButton;
    private JButton editExampleButton;
    private JButton goToLinkButton;

    private boolean permissionToEdit = false;

    private WebFrame frame;
    private Sense editedSense;
    private String editedWord;
    private SenseAttributes senseAttributes;

    private ValidationManager validationManager;

    private int width;
    private int height;


    public LexicalUnitPropertiesPanel(WebFrame frame, int width, int height) {
        this.width = width;
        this.height = height;
        this.frame = frame;
        initComponents();
        insertComponents();
        validationManager = initValidationManager();
    }

    private ValidationManager initValidationManager() {
        ValidationManager validationManager = new ValidationManager();
        // TODO dorobić etykiety
        validationManager.registerError(lemmaTextField, "To pole nie może być puste", ()->lemmaTextField.getText().isEmpty());
        validationManager.registerError(lexiconComboBox, "To pole nie może być puste", ()->lexiconComboBox.getSelectedItem() == null);
        validationManager.registerError(partOfSpeechComboBox, "Wybierz część mowy", ()->partOfSpeechComboBox.getSelectedItem() == null);
        validationManager.registerError(domainComboBox, "Wybierz domenę", ()->domainComboBox.getSelectedItem() == null);
        validationManager.registerError(statusComboBox, "Wybierz status", ()->statusComboBox.getSelectedItem() == null);

        return validationManager;
    }

    private void initComponents() {
        final int AREA_WIDTH = 300;
        final int AREA_HEIGHT = 75;
        lemmaTextField = new JTextField();
        variantTextField = new JTextField(DEFAULT_VARIANT);
        variantTextField.setEnabled(false);
        lexiconComboBox = createLexiconComboBox();
        partOfSpeechComboBox = createPartOfSpeechComboBox();
        domainComboBox = createDomainComboBox();
        registerComboBox = createRegisterComboBox();
        statusComboBox = createStatusComboBox();
        definitionArea = new JTextArea();
        definitionScroll = new JScrollPane(definitionArea);
        definitionScroll.setPreferredSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        commentArea = new JTextArea();
        commentScroll = new JScrollPane(commentArea);
        commentScroll.setPreferredSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        examplesList = createExamplesList();
        linkTextField = new JTextField();
        linkTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableGoToLinkButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableGoToLinkButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableGoToLinkButton();
            }
        });

        addExampleButton = MButton.buildAddButton()
                .withActionListener(e->addExample());
        removeExampleButton = MButton.buildDeleteButton()
                .withActionListener(e->removeExample());
        editExampleButton = MButton.buildEditButton()
                .withActionListener(e->editExample());
        goToLinkButton = new MButton().withIcon(FontAwesome.INTERNET_EXPLORER)
                .withActionListener(e->goToLink());
    }

    private void enableGoToLinkButton(){
        // TODO can add link validation
        goToLinkButton.setEnabled(!linkTextField.getText().isEmpty());
    }

    private void insertComponents() {
        final float LABEL_RATIO = 0.1f;
        final float COMPONENT_RATIO = 0.9f;
        Map<String, Component> componentsMap = new LinkedHashMap<>();
        componentsMap.put(Labels.LEMMA_COLON, lemmaTextField);
        componentsMap.put(Labels.NUMBER_COLON, variantTextField);
        componentsMap.put(Labels.LEXICON_COLON, lexiconComboBox);
        componentsMap.put(Labels.PARTS_OF_SPEECH_COLON, partOfSpeechComboBox);
        componentsMap.put(Labels.DOMAIN_COLON, domainComboBox);
        componentsMap.put(Labels.REGISTER_COLON, registerComboBox);
        componentsMap.put(Labels.STATUS_COLON, statusComboBox);
        componentsMap.put(Labels.DEFINITION_COLON, definitionScroll);
        componentsMap.put(Labels.COMMENT_COLON, commentScroll);
        componentsMap.put(Labels.EXAMPLES, createExamplesPanel());
        componentsMap.put(Labels.LINK_COLON, createLinkPanel());

        Component views = GroupView.createGroupView(componentsMap, new Dimension(width, height),LABEL_RATIO, COMPONENT_RATIO);
        add(views);
    }

    private JPanel createExamplesPanel() {
        JPanel buttonsPanel = new MComponentGroup(addExampleButton, editExampleButton, removeExampleButton)
                .withVerticalLayout();
        JPanel examplesPanel = new JPanel(new BorderLayout());
        examplesPanel.add(examplesList, BorderLayout.CENTER);
        examplesPanel.add(buttonsPanel, BorderLayout.EAST);

        return examplesPanel;
    }

    private JPanel createLinkPanel() {
        JPanel linkPanel = new JPanel(new BorderLayout());
        linkPanel.add(linkTextField, BorderLayout.CENTER);
        linkPanel.add(goToLinkButton, BorderLayout.EAST);

        return linkPanel;
    }

    private void addExample() {
        Example example = ExampleFrame.showModal(frame, Labels.NEW_EXAMPLE, new SenseExample(), false);
        examplesModel.addElement(example);
        examplesList.updateUI();
    }

    private void removeExample() {
        int index = examplesList.getSelectedIndex();
        if(index >= 0) {
            examplesModel.remove(index);
        }
    }

    private void editExample() {
        Example example = (Example) examplesList.getSelectedValue();
        if (example != null) {
            Example value = ExampleFrame.showModal(frame, Labels.EDIT_EXAMPLE, example, true);
            updateExample(example, value);
            examplesList.updateUI();
        }
    }

    private void updateExample(Example oldExample, Example newExample){
        oldExample.setExample(newExample.getExample());
        oldExample.setType(newExample.getType());
    }

    private void goToLink() {
        try{
            URI uri = new URI(linkTextField.getText());
            LinkRunner linkRunner = new LinkRunner(uri);
            linkRunner.execute();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private JComboBox createLexiconComboBox() {
        List<Lexicon> lexiconsList = LexiconManager.getInstance().getLexicons();
        // when user is not an administrator, we delete all read-only lexicons
        if(!PermissionHelper.isAdministrator()) {
            lexiconsList = lexiconsList.stream().filter(lexicon->!lexicon.isOnlyToRead()).collect(Collectors.toList());
        }
        return createComboBox(lexiconsList.iterator(), new LexiconRenderer());
    }

    private JComboBox createPartOfSpeechComboBox() {
        List<PartOfSpeech> partOfSpeechList = PartOfSpeechManager.getInstance().getAll();
        return createComboBox(partOfSpeechList.iterator(), new LocalisedRenderer());
    }

    private JComboBox createDomainComboBox() {
        List<Domain> domains = DomainManager.getInstance().getAllDomains();
        return createComboBox(domains.iterator(), new LocalisedRenderer());
    }

    private JComboBox createRegisterComboBox() {
        List<Register> registerList = (List<Register>) RemoteService.dictionaryServiceRemote.findDictionaryByClass(Register.class);
        return createComboBox(registerList.iterator(), new LocalisedRenderer());
    }

    private JComboBox createStatusComboBox() {
        List<Status> statusList = (List<Status>) RemoteService.dictionaryServiceRemote.findDictionaryByClass(Status.class);
        return createComboBox(statusList.iterator(), new LocalisedRenderer());
    }

    private JComboBox createComboBox(Iterator iterator, ListCellRenderer renderer) {
        JComboBox comboBox = new MComboBox();
        comboBox.setRenderer(renderer);

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(null);
        while(iterator.hasNext()) {
            model.addElement(iterator.next());
        }
        comboBox.setModel(model);
        return comboBox;
    }

    private JList createExamplesList() {
        JList list = new JList();
        list.setCellRenderer(new ExampleCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        examplesModel = new DefaultListModel();
        list.setModel(examplesModel);
        return list;
    }

    public void setSense(Sense unit) {
        this.editedSense = unit;
        this.editedWord = editedSense.getWord().getWord();
        examplesModel.clear();
        if(unit == null) {
            return;
        }
        // when user isn't administrator, all only-read lexicons has been filtered, and current lexicon will not be shown
        // then, we must add current lexicon to lexiconComboBox
        if(!PermissionHelper.isAdministrator() && unit.getLexicon().isOnlyToRead()){
            lexiconComboBox.addItem(unit.getLexicon());
        }
        senseAttributes = RemoteService.senseRemote.fetchSenseAttribute(unit.getId());
        assert senseAttributes != null;
        lemmaTextField.setText(unit.getWord().getWord());
        variantTextField.setText(String.valueOf(unit.getVariant()));
        lexiconComboBox.setSelectedItem(unit.getLexicon());
        partOfSpeechComboBox.setSelectedItem(unit.getPartOfSpeech());
        domainComboBox.setSelectedItem(unit.getDomain());
        registerComboBox.setSelectedItem(senseAttributes.getRegister());
        statusComboBox.setSelectedItem(unit.getStatus());
        definitionArea.setText(senseAttributes.getDefinition());
        commentScroll.setToolTipText(senseAttributes.getComment());
        for(SenseExample example : senseAttributes.getExamples()) {
            examplesModel.addElement(example);
        }
        linkTextField.setText(senseAttributes.getLink());
        setEnableEditing(unit);
        enableGoToLinkButton();
    }

    private void setEnableEditing(Sense unit){
        PermissionHelper.checkPermissionToEditAndSetComponents(unit,
                lemmaTextField, variantTextField, lexiconComboBox, partOfSpeechComboBox, domainComboBox,
                registerComboBox, definitionArea, commentArea, examplesList, linkTextField,
                addExampleButton, removeExampleButton, editExampleButton);
    }

    private SenseAttributes getSense(){
        Sense sense = editedSense;
        SenseAttributes attributes = senseAttributes;
        if(sense == null) {
            sense = new Sense();
            attributes = new SenseAttributes();
            attributes.setSense(sense);
        }
        Word word = RemoteService.wordServiceRemote.findByWord(lemmaTextField.getText());
        if(word == null){
            word = new Word(lemmaTextField.getText());
        }
        sense.setWord(word);

//        sense.setVariant(Integer.valueOf(variantTextField.getText()));
        sense.setLexicon((Lexicon) lexiconComboBox.getSelectedItem());
        sense.setPartOfSpeech((PartOfSpeech)partOfSpeechComboBox.getSelectedItem());
        sense.setDomain((Domain)domainComboBox.getSelectedItem());
        attributes.setRegister((Register)registerComboBox.getSelectedItem());
        sense.setStatus((Status)statusComboBox.getSelectedItem());

        String definition = !definitionArea.getText().isEmpty() ? definitionArea.getText() : null;
        String comment = !commentArea.getText().isEmpty() ? commentArea.getText() : null;
        String link = !linkTextField.getText().isEmpty() ? linkTextField.getText() : null;

        Set<SenseExample> examples = new HashSet<>();
        for(int i=0; i<examplesModel.getSize(); i++) {
            SenseExample example = (SenseExample)examplesModel.getElementAt(i);
            example.setSenseAttributes(senseAttributes);
            examples.add(example);
        }

        attributes.setDefinition(definition);
        attributes.setComment(comment);
        attributes.setLink(link);
        attributes.setExamples(examples);

        attributes.setSense(sense);
        return attributes;
    }

    public Sense save() {
        if(validateComponents()){
            if(editedSense == null || !checkUnitExists()) {
                return RemoteService.senseRemote.saveSense(getSense(), editedWord);
            }
        }
        return null;
    }

    private boolean validateComponents() {
        return validationManager.validate();
    }

    private boolean checkUnitExists() {
        assert !lemmaTextField.getText().isEmpty();
        String lemma = lemmaTextField.getText();
        List<SenseAttributes> units = RemoteService.senseRemote.findByLemmaWithSense(lemma, LexiconManager.getInstance().getUserChosenLexiconsIds());
        if(units == null || units.isEmpty()) {
            return false;
        }
        Lexicon lexicon = (Lexicon) lexiconComboBox.getSelectedItem();
        Domain domain = (Domain) domainComboBox.getSelectedItem();
        PartOfSpeech partOfSpeech = (PartOfSpeech) partOfSpeechComboBox.getSelectedItem();
        String definition = !definitionArea.getText().isEmpty() ? definitionArea.getText() : null;

        long count = units.stream()
                .filter(u-> !u.getId().equals(editedSense.getId())
                        && u.getSense().getWord().getWord().equals(lemma)
                && u.getSense().getLexicon().equals(lexicon)
                && u.getSense().getDomain().equals(domain)
                && u.getSense().getPartOfSpeech().equals(partOfSpeech)
                && u.getDefinition() != null
                && u.getDefinition().equals(definition))
                .count();
        if(count > 0){
            DialogBox.showError(Messages.FAILURE_UNIT_EXISTS);
            return true;
        }
        return false;
    }
}