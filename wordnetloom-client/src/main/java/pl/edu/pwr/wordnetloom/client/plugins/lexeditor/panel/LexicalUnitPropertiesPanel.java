package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.ExampleFrame;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.errors.ValidationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.*;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.renderers.*;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.utils.PermissionHelper;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;
import pl.edu.pwr.wordnetloom.word.model.Word;
import se.datadosen.component.RiverLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;


public class LexicalUnitPropertiesPanel extends JPanel {

    private final String DEFAULT_VARIANT = "1";

    private Sense unit;

    private JTextField lemmaTextField;
    private JTextField variantTextField;
    private JComboBox lexiconComboBox;
    private JComboBox partOfSpeechComboBox;
    private JComboBox domainComboBox;
    private JComboBox registerComboBox;
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
    private SenseAttributes senseAttributes;

    private ValidationManager validationManager;

    private int width;
    private int height;

    public LexicalUnitPropertiesPanel(WebFrame frame/*, int width, int height*/) {
//        this.width = width;
//        this.height = height;
        this.frame = frame;
        initComponents();
        insertComponents();
        validationManager = initValidationManager();
    }

    private ValidationManager initValidationManager() {
        ValidationManager validationManager = new ValidationManager();
        // TODO dorobić etykiety
        validationManager.registerError(lemmaTextField, "To pole nie może być puste", ()->lemmaTextField.getText().isEmpty());
        validationManager.registerError(partOfSpeechComboBox, "Wybierz część mowy", ()->partOfSpeechComboBox.getSelectedItem() == null);
        validationManager.registerError(domainComboBox, "Wybierz domenę", ()->domainComboBox.getSelectedItem() == null);
        validationManager.registerError(registerComboBox, "Wybierz rejestr", ()->registerComboBox.getSelectedItem()==null);

        return validationManager;
    }

    private void initComponents() {
        final int AREA_WIDTH = 300;
        final int AREA_HEIGHT = 75;
        lemmaTextField = new JTextField(Labels.VALUE_UNKNOWN);
        variantTextField = new JTextField(DEFAULT_VARIANT);
        lexiconComboBox = createLexiconComboBox();
        partOfSpeechComboBox = createPartOfSpeechComboBox();
        domainComboBox = createDomainComboBox();
        registerComboBox = createRegisterComboBox();
        definitionArea = new JTextArea();
        definitionScroll = new JScrollPane(definitionArea);
        definitionScroll.setPreferredSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        commentArea = new JTextArea();
        commentScroll = new JScrollPane(commentArea);
        commentScroll.setPreferredSize(new Dimension(AREA_WIDTH, AREA_HEIGHT));
        examplesList = createExamplesList();
        linkTextField = new JTextField(Labels.LINK_COLON);

        addExampleButton = MButton.buildAddButton()
                .withActionListener(e->addExample());
        removeExampleButton = MButton.buildDeleteButton()
                .withActionListener(e->removeExample());
        editExampleButton = MButton.buildEditButton()
                .withActionListener(e->editExample());
        goToLinkButton = new MButton().withIcon(FontAwesome.INTERNET_EXPLORER)
                .withActionListener(e->goToLink());
    }

    private void insertComponents() {
        // TODO zmienić na wynmiary okna
        final int WIDTH = 560;
        final int HEIGHT = 520;
        final float LABEL_RATIO = 0.1f;
        final float COMPONENT_RATIO = 0.9f;
        Map<String, Component> componentsMap = new LinkedHashMap<>();
        componentsMap.put(Labels.LEMMA_COLON, lemmaTextField);
        componentsMap.put(Labels.NUMBER_COLON, variantTextField);
        componentsMap.put(Labels.LEXICON_COLON, lexiconComboBox);
        componentsMap.put(Labels.PARTS_OF_SPEECH_COLON, partOfSpeechComboBox);
        componentsMap.put(Labels.DOMAIN_COLON, domainComboBox);
        componentsMap.put(Labels.REGISTER_COLON, registerComboBox);
        componentsMap.put(Labels.DEFINITION_COLON, definitionScroll);
        componentsMap.put(Labels.COMMENT_COLON, commentScroll);
        componentsMap.put(Labels.EXAMPLES, createExamplesPanel());
        componentsMap.put(Labels.LINK_COLON, createLinkPanel());

        Component views = GroupView.createGroupView(componentsMap, new Dimension(WIDTH, HEIGHT),LABEL_RATIO, COMPONENT_RATIO);
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
        String example = ExampleFrame.showModal(frame, Labels.NEW_EXAMPLE, "", false);
        if(example != null && !example.isEmpty()) {
            SenseExample senseExample = new SenseExample();
            senseExample.setExample(example);
            senseExample.setType("W");
            examplesModel.addElement(senseExample);
            // TODO enableSaveButton
            examplesList.updateUI();
        }
    }

    private void removeExample() {
        int index = examplesList.getSelectedIndex();
        if(index >= 0) {
            examplesModel.remove(index);
            // TODO enableSaveButton
        }
    }

    private void editExample() {
        SenseExample example = (SenseExample) examplesList.getSelectedValue();
        if (example != null) {
            String value = ExampleFrame.showModal(frame, Labels.EDIT_EXAMPLE, example.getExample(), true);
            String old = example.getExample();
            if(value != null && !value.equals(old)) {
                example.setExample(value);
                examplesList.updateUI();
                // TODO enableSaveButton
            }
        }
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
        return createComboBox(lexiconsList.iterator(), new LexiconRenderer());
    }

    private JComboBox createPartOfSpeechComboBox() {
        List<PartOfSpeech> partOfSpeechList = PartOfSpeechManager.getInstance().getAll();
        return createComboBox(partOfSpeechList.iterator(), new PartOfSpeechRenderer());
    }

    private JComboBox createDomainComboBox() {
        List<Domain> domains = DomainManager.getInstance().getAllDomains();
        return createComboBox(domains.iterator(), new LocalisedRenderer());
    }

    private JComboBox createRegisterComboBox() {
        List<Register> registerList = (List<Register>) RemoteService.dictionaryServiceRemote.findDictionaryByClass(Register.class);
        return createComboBox(registerList.iterator(), new LocalisedRenderer());
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
        if(unit == null) {
            return;
        }
        senseAttributes = RemoteService.senseRemote.fetchSenseAttribute(unit.getId());
        assert senseAttributes != null;
        lemmaTextField.setText(unit.getWord().getWord());
        variantTextField.setText(String.valueOf(unit.getVariant()));
        lexiconComboBox.setSelectedItem(unit.getLexicon());
        partOfSpeechComboBox.setSelectedItem(unit.getPartOfSpeech());
        domainComboBox.setSelectedItem(unit.getDomain());
        registerComboBox.setSelectedItem(senseAttributes.getRegister());
        definitionArea.setText(senseAttributes.getDefinition());
        commentScroll.setToolTipText(senseAttributes.getComment());
        for(SenseExample example : senseAttributes.getExamples()) {
            examplesModel.addElement(example);
        }
        linkTextField.setText(senseAttributes.getLink());
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
        // TODO chyba nie powiniśmy zmieniać numerów wariantu
        sense.setVariant(Integer.valueOf(variantTextField.getText()));
        sense.setLexicon((Lexicon) lexiconComboBox.getSelectedItem());
        sense.setPartOfSpeech((PartOfSpeech)partOfSpeechComboBox.getSelectedItem());
        sense.setDomain((Domain)domainComboBox.getSelectedItem());
        attributes.setRegister((Register)registerComboBox.getSelectedItem());

        String definition = !definitionArea.getText().isEmpty() ? definitionArea.getText() : null;
        String comment = !commentArea.getText().isEmpty() ? commentArea.getText() : null;
        String link = !linkTextField.getText().isEmpty() ? linkTextField.getText() : null;
        attributes.setDefinition(definition);
        attributes.setComment(comment);
        attributes.setLink(link);

        attributes.setSense(sense);
        return attributes;
    }

    public Sense save() {
        if(validateComponents()){
            if(!checkUnitExists()) {
                return saveSense(getSense());
            }
        }
        return null;
    }

    private Sense saveSense(SenseAttributes attributes) {
        // TODO sprawdzić, czy będzie poprawnie zapisywało, zwłaszcza nowe wartości
        Sense savedSense = RemoteService.senseRemote.save(attributes.getSense());
        attributes.setSense(savedSense);
        attributes = RemoteService.senseRemote.save(attributes);
        return attributes.getSense();
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
                .filter(u->u.getSense().getWord().getWord().equals(lemma)
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