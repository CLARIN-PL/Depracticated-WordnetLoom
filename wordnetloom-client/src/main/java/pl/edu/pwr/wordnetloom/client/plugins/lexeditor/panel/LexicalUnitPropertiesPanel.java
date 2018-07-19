package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.ExampleFrame;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.DictionaryManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.client.systems.renderers.ExampleCellRenderer;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.PermissionHelper;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class LexicalUnitPropertiesPanel extends WebPanel implements
        CaretListener, ActionListener {

    private static final long serialVersionUID = 8598891792812358941L;
    private final String DEFAULT_VARIANT = "1";
    private Sense unit;
    private LexiconComboBox lexicon;
    private MTextField lemma;
    private MTextField variant;
    private MTextField link;
    private MComboBox<Register> register;
    private MComboBox<PartOfSpeech> partOfSpeech;
    private DomainMComboBox domain;
    private MTextPane comment;
    private MButton btnCancel;
    private MButton btnSave;
    private WebScrollPane commentScrollPane;
    private WebScrollPane scrollPaneExamples;
    private MButton btnGoToLink;
    private MButton btnNewExample;
    private MButton btnEditExample;
    private MButton btnRemoveExample;
    private WebList examplesList;
    private DefaultListModel examplesModel;
    private WebScrollPane definitionScrollPane;
    private MTextPane definition;

    private boolean permissionToEdit = false;
    private EmotionsPropertiesPanel emotionsPanel;

    private void setupPermissionComponents() {
        permissionToEdit = PermissionHelper.checkPermissionToEditAndSetComponents(
                lexicon, lemma, variant, link, register, partOfSpeech,
                domain, comment, btnSave, btnNewExample, btnEditExample,
                btnRemoveExample, examplesList, definition
        );
    }

    public LexicalUnitPropertiesPanel(WebFrame frame) {
        setLayout(new RiverLayout());
        Map<String, Component> components = new LinkedHashMap<>();

        lemma = new MTextField(Labels.VALUE_UNKNOWN)
                .withSize(new Dimension(300, 25))
                .withCaretListener(this);
        components.put(Labels.LEMMA_COLON, new MComponentGroup(lemma)
                .withHorizontalLayout());

        variant = new MTextField(DEFAULT_VARIANT)
                .withSize(new Dimension(50, 25))
                .withCaretListener(this)
                .withEnabled(false);
        components.put(Labels.NUMBER_COLON, new MComponentGroup(variant).withHorizontalLayout());

        lexicon = new LexiconComboBox(Labels.NOT_CHOSEN);
        lexicon.addActionListener(this);
        lexicon.addItemListener((ItemEvent e) -> {
            Lexicon lex = lexicon.getEntity();
            if (lex != null) {
                // partOfSpeech.filterByLexicon(lex);
                // domain.filterDomainsByLexicon(lex, false);
            }
        });
        components.put(Labels.LEXICON_COLON, new MComponentGroup(lexicon)
                .withFirstElementSize(new Dimension(300, 25))
                .withHorizontalLayout());

        partOfSpeech = new PartOfSpeechComboBox(Labels.NOT_CHOSEN);
        List<PartOfSpeech> partOfSpeechList = PartOfSpeechManager.getInstance().getAll();
        List<CustomDescription> partOfSpeechDescriptorList = new ArrayList<>();
        partOfSpeechDescriptorList.add(new CustomDescription(Labels.NOT_CHOSEN, null)); //TODO zmienić puste
        String posName;

        for (PartOfSpeech pos : partOfSpeechList) {
            posName = LocalisationManager.getInstance().getLocalisedString(pos.getName());
            partOfSpeechDescriptorList.add(new CustomDescription(posName, pos));
        }

        partOfSpeech.setModel(new DefaultComboBoxModel(partOfSpeechDescriptorList.toArray()));
        partOfSpeech.addActionListener(this);
        partOfSpeech.addItemListener((ItemEvent e) -> {
            PartOfSpeech pos = partOfSpeech.getEntity();
            if (pos != null) {
                //  domain.filterDomainByPos(pos, false);
            }
        });
        components.put(Labels.PARTS_OF_SPEECH_COLON, new MComponentGroup(partOfSpeech)
                .withFirstElementSize(new Dimension(300, 25))
                .withHorizontalLayout());

        domain = new DomainMComboBox(Labels.NOT_CHOSEN);
        domain.allDomains(false);
        domain.addActionListener(this);
        components.put(Labels.DOMAIN_COLON, new MComponentGroup(domain)
                .withFirstElementSize(new Dimension(300,25))
                .withHorizontalLayout());

        register = new MComboBox<>()
                .withDictionaryItems(
                        DictionaryManager.getInstance().getDictionaryByClassName(Register.class),
                        Labels.NOT_CHOSEN);

        register.addActionListener(this);
        components.put(Labels.REGISTER_COLON, new MComponentGroup(register)
                .withFirstElementSize(new Dimension(300, 25))
                .withHorizontalLayout());

        definition = new MTextPane();
        definition.addCaretListener(this);
        definitionScrollPane = new WebScrollPane(definition);
        components.put(Labels.DEFINITION_COLON, new MComponentGroup(definitionScrollPane)
                .withFirstElementSize(new Dimension(300, 75))
                .withHorizontalLayout());

        comment = new MTextPane();
        comment.addCaretListener(this);
        commentScrollPane = new WebScrollPane(comment);

        components.put(Labels.COMMENT_COLON, new MComponentGroup(commentScrollPane)
                .withFirstElementSize(new Dimension(300, 50))
                .withHorizontalLayout());

        examplesList = new WebList() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        examplesList.setCellRenderer(new ExampleCellRenderer());

        examplesList.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                examplesList.setFixedCellHeight(10);
                examplesList.setFixedCellHeight(-1);
            }

        });

        examplesModel = new DefaultListModel();
        scrollPaneExamples = new WebScrollPane(examplesList);

        btnNewExample = MButton.buildAddButton();
        btnNewExample.addActionListener((ActionEvent e) -> {
            String example = ExampleFrame.showModal(frame, Labels.NEW_EXAMPLE, "", false);
            if (example != null && !"".equals(example)) {
                SenseExample senseExample = new SenseExample();
                senseExample.setExample(example);
                senseExample.setType("W");
                examplesModel.addElement(senseExample);
                enableSaveButton();
                examplesList.updateUI();
            }
        });

        btnEditExample = MButton.buildEditButton();
        btnEditExample.addActionListener((ActionEvent e) -> {
            int idx = examplesList.getSelectedIndex();
            SenseExample example = (SenseExample) examplesModel.get(idx);
            if (idx >= 0) {
                String modified = ExampleFrame.showModal(frame,
                        Labels.EDIT_EXAMPLE,
                        example.getExample(), true);
                String old = example.getExample();
                if (modified != null && !old.equals(modified)) {
                    example.setExample(modified);
                    examplesList.updateUI();
                    enableSaveButton();
                }
            }
        });

        btnRemoveExample = MButton.buildDeleteButton();
        btnRemoveExample.addActionListener((ActionEvent e) -> {
            int idx = examplesList.getSelectedIndex();
            if (idx >= 0) {
                examplesModel.remove(idx);
                enableSaveButton();
            }
        });

        components.put(Labels.USE_CASE_COLON, new MComponentGroup(scrollPaneExamples,
                new MComponentGroup(btnNewExample, btnEditExample, btnRemoveExample)
                        .withVerticalLayout())
                .withFirstElementSize(new Dimension(300, 110))
                .withHorizontalLayout());

        link = new MTextField("");
        link.addCaretListener(this);

        btnGoToLink = new MButton().withIcon(FontAwesome.INTERNET_EXPLORER);
        btnGoToLink.addActionListener((ActionEvent e) -> {
            try {
                URI uri = new java.net.URI(link.getText());
                LinkRunner lr = new LinkRunner(uri);
                lr.execute();
            } catch (URISyntaxException use) {
            }
        });
        components.put(Labels.LINK_COLON, new MComponentGroup(link, btnGoToLink)
                .withFirstElementSize(new Dimension(300, 25))
                .withHorizontalLayout());

        WebTabbedPane tabs = new WebTabbedPane();
        tabs.setLayout(new BorderLayout());
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.addTab("Main", GroupView.createGroupView(components, new Dimension(560, 520),0.1f, 0.85f));

        emotionsPanel = new EmotionsPropertiesPanel(frame);
        tabs.addTab(Labels.EMOTIONS, emotionsPanel);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        btnCancel = MButton.buildCancelButton();
        buttons.add(btnCancel);

        btnSave = MButton.buildSaveButton();
        buttons.add(btnSave);
        btnSave.addActionListener(this);

        add("", tabs);
        add("br center", buttons);

        setupPermissionComponents();
    }

    public SenseAttributes getSenseAttributes(Long senseId) {
        SenseAttributes attributes = RemoteService.senseRemote.fetchSenseAttribute(senseId);
        Register reg = register.getEntity();
        String definition = getDefinition().getText();
        String link = getLink().getToolTipText();
        String comment = getComment().getText();
        if (attributes == null && (definition != null || link != null || comment != null || register.getSelectedIndex() > 0)) {
            attributes = new SenseAttributes();
        }
        attributes.setComment(comment);
        attributes.setLink(link);
        attributes.setDefinition(definition);
        attributes.setRegister(reg);

        java.util.Set<SenseExample> examples = attributes.getExamples();
        examples.clear();

        if (!examplesModel.isEmpty()) {
            for (int i = 0; i < examplesModel.size(); i++) {
                examples.add((SenseExample) examplesModel.getElementAt(i));
            }
        }
        return attributes;
    }

    public Sense updateAndGetSense() {
        unit.getWord().setWord(getLemma().getText());
        unit.setPartOfSpeech(getPartOfSpeech().getEntity());
        unit.setDomain(getDomain().getEntity());
        int variant = Integer.parseInt(getVariant().getText());
        unit.setVariant(variant);

        return unit;
    }

    public Sense getSense() {
        return unit;
    }

    public void setSense(Sense unit) {
        this.unit = unit;
        refreshData();
        emotionsPanel.load(unit);
    }

    @Override
    public void caretUpdate(CaretEvent event) {
        if (event.getSource() instanceof MTextField) {
            MTextField field = (MTextField) event.getSource();
            btnSave.setEnabled(permissionToEdit && btnSave.isEnabled() | field.wasTextChanged());
        }
        if (event.getSource() instanceof MTextPane) {
            MTextPane field = (MTextPane) event.getSource();
            btnSave.setEnabled(permissionToEdit && btnSave.isEnabled() | field.wasTextChanged());
        }
    }

    public void refreshData() {

        lemma.setText(formatValue(unit != null ? unit.getWord().getWord() : null));
        variant.setText(unit != null ? "" + unit.getVariant() : null);
        lexicon.setSelectedItem(unit != null ? new CustomDescription<>(unit.getLexicon().toString(), unit.getLexicon()) : null);

        String partOfSpeechText = LocalisationManager.getInstance().getLocalisedString(unit.getPartOfSpeech().getId());
        partOfSpeech.setSelectedItem(unit != null ? new CustomDescription<>(
                partOfSpeechText, unit.getPartOfSpeech()) : null);

        String domainText = String.format("%s (%s)",
                LocalisationManager.getInstance().getLocalisedString(unit.getDomain().getDescription()),
                LocalisationManager.getInstance().getLocalisedString(unit.getDomain().getName()));
        domain.setSelectedItem(domainText == null ? null
                : new CustomDescription<>(domainText, unit.getDomain()));

        SenseAttributes attributes = RemoteService.senseRemote.fetchSenseAttribute(unit.getId());

        if (attributes != null) {
            definition.setText(attributes.getDefinition());
            comment.setText(attributes.getComment());
            register.setSelectedItem(attributes.getRegister() == null ? null :
                    new CustomDescription<>(LocalisationManager.getInstance().getLocalisedString(attributes.getRegister().getName()), attributes.getRegister()));
            link.setText(attributes.getLink());
        }

        examplesModel.clear();
        if (attributes.getExamples() != null) {
            for (SenseExample example : attributes.getExamples()) {
                examplesModel.addElement(example);
            }
            examplesList.setModel(examplesModel);
        }

        btnSave.setEnabled(false);
    }

    private String formatValue(String value) {
        return (value == null || value.length() == 0) ? null : value;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == domain) {
            enableSaveButton();
        } else if (event.getSource() == lexicon) {
            enableSaveButton();
        } else if (event.getSource() == register) {
            enableSaveButton();
        } else if (event.getSource() == link) {
            enableSaveButton();
        } else if (event.getSource() == examplesList) {
            enableSaveButton();
        } else if (event.getSource() == partOfSpeech) {
            enableSaveButton();
        }
    }

    private void enableSaveButton() {
        btnSave.setEnabled(permissionToEdit);
    }

    public MComboBox<Lexicon> getLexicon() {
        return lexicon;
    }

    public MTextField getLemma() {
        return lemma;
    }

    public MTextField getVariant() {
        return variant;
    }

    public MTextField getLink() {
        return link;
    }

    public MComboBox<Register> getRegister() {
        return register;
    }

    public MComboBox<PartOfSpeech> getPartOfSpeech() {
        return partOfSpeech;
    }

    public MComboBox<Domain> getDomain() {
        return domain;
    }

    public JTextPane getDefinition() {
        return definition;
    }

    public JTextPane getComment() {
        return comment;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public DefaultListModel getExamplesModel() {
        return examplesModel;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public boolean isPermissionToEdit(){
        return permissionToEdit;
    }

}
