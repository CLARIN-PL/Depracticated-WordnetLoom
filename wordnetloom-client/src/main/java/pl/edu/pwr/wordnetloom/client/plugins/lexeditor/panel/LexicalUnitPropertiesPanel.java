package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.util.LayoutStyle;
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
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.sense.model.SenseExample;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class LexicalUnitPropertiesPanel extends JPanel implements
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
    private final MButton btnCancel;
    private MButton btnSave;
    private JScrollPane commentScrollPane;
    private JScrollPane scrollPaneExamples;
    private final MButton btnGoToLink;
    private final MButton btnNewExample;
    private final MButton btnEditExample;
    private final MButton btnRemoveExample;
    private WebList examplesList;
    private DefaultListModel examplesModel;
    private WebLabel lblDefinition;
    private JScrollPane definitionScrollPane;
    private MTextPane definition;

    public LexicalUnitPropertiesPanel(final WebFrame frame) {
        setLayout(new BorderLayout(0, 0));
        WebPanel mainPanel = new WebPanel();
        mainPanel.setLayout(new FormLayout(new ColumnSpec[]{
                ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
                ColumnSpec.decode("max(57dlu;min)"),
                ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
                ColumnSpec.decode("max(48dlu;min)"),
                ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
                ColumnSpec.decode("max(49dlu;min)"),
                ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
                ColumnSpec.decode("max(28dlu;min):grow"),
                ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
                ColumnSpec.decode("max(17dlu;min)"),
                ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
                ColumnSpec.decode("max(44dlu;default)"),
                ColumnSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadX()),
                ColumnSpec.decode("max(5dlu;default):grow"),},
                new RowSpec[]{
                        RowSpec.decode("1dlu"),
                        RowSpec.decode("6dlu"),
                        RowSpec.decode("fill:max(14dlu;default)"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("fill:max(14dlu;default)"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("fill:default"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("fill:default"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("fill:max(14dlu;default)"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("max(36dlu;default):grow"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("fill:max(40dlu;pref):grow"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("max(14dlu;default)"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("max(14dlu;default)"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("max(14dlu;default)"),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        new RowSpec(Sizes.DEFAULT),
                        RowSpec.createGap(LayoutStyle.getCurrent().getRelatedComponentsPadY()),
                        RowSpec.decode("fill:max(14dlu;default)"),
                        RowSpec.decode("max(10dlu;default):grow"),}));

        JLabel lblLemma = new JLabel(Labels.LEMMA_COLON);
        lblLemma.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblLemma, "2, 3, left, default");

        lemma = new MTextField(Labels.VALUE_UNKNOWN);
        lemma.addCaretListener(this);
        mainPanel.add(lemma, "4, 3, 4, 1, fill, fill");
        lemma.setColumns(10);

        JLabel lblVariant = new JLabel(Labels.NUMBER_COLON);
        lblVariant.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblVariant, "8, 3, 3, 1, fill, fill");

        variant = new MTextField(DEFAULT_VARIANT);
        variant.addCaretListener(this);
        variant.setEditable(false);
        mainPanel.add(variant, "12, 3, left, fill");
        variant.setColumns(10);

        JLabel lblLexicon = new JLabel(Labels.LEXICON_COLON);
        lblLexicon.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblLexicon, "2, 5, left, fill");
        lexicon = new LexiconComboBox(Labels.NOT_CHOSEN);
        lexicon.addActionListener(this);
        lexicon.addItemListener((ItemEvent e) -> {
            Lexicon lex = lexicon.getEntity();
            if (lex != null) {
//                partOfSpeech.filterByLexicon(lex);
                domain.filterDomainsByLexicon(lex, false);
            }
        });
        mainPanel.add(lexicon, "4, 5, 3, 1, fill, fill");

        JLabel lblPoS = new JLabel(Labels.PARTS_OF_SPEECH_COLON);
        lblPoS.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblPoS, "2, 7, left, default");

        partOfSpeech = new PartOfSpeechComboBox(Labels.NOT_CHOSEN);
        java.util.List<PartOfSpeech> partOfSpeechList = PartOfSpeechManager.getInstance().getAll();
        java.util.List<CustomDescription> partOfSpeechDescriptorList = new ArrayList<>();
        partOfSpeechDescriptorList.add(new CustomDescription("Brak", null)); //TODO zmienić puste
        String posName;

        for(PartOfSpeech pos : partOfSpeechList){
            posName = LocalisationManager.getInstance().getLocalisedString(pos.getName());
            partOfSpeechDescriptorList.add(new CustomDescription(posName, pos));
        }

        partOfSpeech.setModel(new DefaultComboBoxModel(partOfSpeechDescriptorList.toArray()));
        partOfSpeech.addActionListener(this);
        partOfSpeech.addItemListener((ItemEvent e) -> {
            PartOfSpeech pos = partOfSpeech.getEntity();
            if (pos != null) {
                domain.filterDomainByPos(pos, false);
            }
        });
        mainPanel.add(partOfSpeech, "4, 7, 3, 1, fill, fill");

        JLabel lblDomain = new JLabel(Labels.DOMAIN_COLON);
        lblDomain.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblDomain, "2, 9, left, fill");

        domain = new DomainMComboBox(Labels.NOT_CHOSEN);
        domain.allDomains(false);
        domain.addActionListener(this);
        mainPanel.add(domain, "4, 9, 3, 1, fill, fill");

        JLabel lblRegister = new JLabel(Labels.REGISTER_COLON);
        lblRegister.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblRegister, "2, 11, left, fill");

        register = new MComboBox<>()
                .withDictionaryItems(
                        DictionaryManager.getInstance().getDictionaryByClassName(Register.class),
                        Labels.NOT_CHOSEN);

        register.addActionListener(this);
        mainPanel.add(register, "4, 11, 3, 1, fill, fill");

        lblDefinition = new WebLabel(Labels.DEFINITION_COLON);
        lblDefinition.setVerticalAlignment(SwingConstants.TOP);
        lblDefinition.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(lblDefinition, "2, 13, left, top");

        definitionScrollPane = new JScrollPane();
        mainPanel.add(definitionScrollPane, "4, 13, 9, 1, default, fill");

        definition = new MTextPane();
        definition.addCaretListener(this);
        definitionScrollPane.setViewportView(definition);

        JLabel lblComment = new JLabel(Labels.COMMENT_COLON);
        lblComment.setVerticalAlignment(SwingConstants.TOP);
        lblComment.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(lblComment, "2, 15, left, default");

        commentScrollPane = new JScrollPane();
        mainPanel.add(commentScrollPane, "4, 15, 9, 1, default, fill");

        comment = new MTextPane();
        comment.addCaretListener(this);
        commentScrollPane.setViewportView(comment);

        scrollPaneExamples = new JScrollPane();
        mainPanel.add(scrollPaneExamples, "4, 17, 7, 7, default, fill");

        examplesList = new WebList() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        examplesList.setCellRenderer(new ExampleCellRenderer());

        ComponentListener l = new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                examplesList.setFixedCellHeight(10);
                examplesList.setFixedCellHeight(-1);
            }

        };

        examplesList.addComponentListener(l);
        examplesModel = new DefaultListModel();
        scrollPaneExamples.setViewportView(examplesList);

        btnNewExample = MButton.buildAddButton();

        btnNewExample.addActionListener((ActionEvent e) -> {
            String example = ExampleFrame.showModal(frame, Labels.NEW_EXAMPLE, "", false);
            if (example != null && !"".equals(example)) {
                SenseExample senseExample = new SenseExample();
                senseExample.setExample(example);
                senseExample.setType("W");
                examplesModel.addElement(senseExample);
                btnSave.setEnabled(true);
                examplesList.updateUI();
            }
        });

        JLabel lblExample = new JLabel(Labels.USE_CASE_COLON);
        lblExample.setVerticalAlignment(SwingConstants.TOP);
        lblExample.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblExample, "2, 17, left, fill");
        mainPanel.add(btnNewExample, "12, 17, fill, fill");

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
                    btnSave.setEnabled(true);
                }
            }
        });
        mainPanel.add(btnEditExample, "12, 19, fill, fill");

        btnRemoveExample = MButton.buildDeleteButton();
        btnRemoveExample.addActionListener((ActionEvent e) -> {
            int idx = examplesList.getSelectedIndex();
            if (idx >= 0) {
                examplesModel.remove(idx);
                btnSave.setEnabled(true);
            }
        });
        mainPanel.add(btnRemoveExample, "12, 21, fill, fill");

        WebLabel lblLink = new WebLabel(Labels.LINK_COLON);
        lblLink.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(lblLink, "2, 25, left, fill");

        link = new MTextField("");
        link.addCaretListener(this);
        mainPanel.add(link, "4, 25, 7, 1, fill, fill");
        link.setColumns(10);

        btnGoToLink = new MButton().withIcon(FontAwesome.INTERNET_EXPLORER);
        btnGoToLink.addActionListener((ActionEvent e) -> {
            try {
                URI uri = new java.net.URI(link.getText());
                LinkRunner lr =  new LinkRunner(uri);
                lr.execute();
            } catch (URISyntaxException use) {
            }
        });
        mainPanel.add(btnGoToLink, "12, 25, fill, fill");

        WebTabbedPane tabs = new WebTabbedPane();
        tabs.setPreferredSize(new Dimension(620, 500));
        tabs.setLayout(new BorderLayout());
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        add(tabs, BorderLayout.CENTER);
        tabs.addTab("Main", mainPanel);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        btnCancel = MButton.buildCancelButton();
        buttons.add(btnCancel);

        btnSave = MButton.buildSaveButton();
        buttons.add(btnSave);
        btnSave.addActionListener(this);
        add(buttons, BorderLayout.SOUTH);
    }

    public SenseAttributes getSenseAttributes(Long senseId)
    {
        SenseAttributes attributes = RemoteService.senseRemote.fetchSenseAttribute(senseId);
        Register reg = register.getEntity();
        String definition = getDefinition().getText();
        String link = getLink().getToolTipText();
        String comment = getComment().getText();
        if(attributes == null && (definition != null || link != null || comment != null || register.getSelectedIndex() > 0))
        {
            attributes = new SenseAttributes();
        }
        attributes.setComment(comment);
        attributes.setLink(link);
        attributes.setDefinition(definition);
        attributes.setRegister(reg);

        java.util.Set<SenseExample> examples = attributes.getExamples();
        examples.clear();

        if(!examplesModel.isEmpty()) {
            for(int i = 0; i < examplesModel.size(); i++)
            {
                examples.add((SenseExample)examplesModel.getElementAt(i));
            }
        }
        return attributes;
    }

    public Sense updateAndGetSense()
    {
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
    }

    @Override
    public void caretUpdate(CaretEvent event) {
        if (event.getSource() instanceof MTextField) {
            MTextField field = (MTextField) event.getSource();
            btnSave.setEnabled(btnSave.isEnabled() | field.wasTextChanged());
        }
        if (event.getSource() instanceof MTextPane) {
            MTextPane field = (MTextPane) event.getSource();
            btnSave.setEnabled(btnSave.isEnabled() | field.wasTextChanged());
        }
    }

    public void refreshData() {

        lemma.setText(formatValue(unit != null ? unit.getWord().getWord() : null));
        variant.setText(unit != null ? "" + unit.getVariant() : null);
        lexicon.setSelectedItem(unit != null ? new CustomDescription<>(unit.getLexicon().toString(), unit.getLexicon()) : null);

        String partOfSpeechText = LocalisationManager.getInstance().getLocalisedString(unit.getPartOfSpeech().getId());
        partOfSpeech.setSelectedItem(unit != null ? new CustomDescription<>(
                partOfSpeechText, unit.getPartOfSpeech()): null);

        String domainText = LocalisationManager.getInstance().getLocalisedString(unit.getDomain().getName());
        domain.setSelectedItem(domainText == null ? null
                : new CustomDescription<>(domainText, unit.getDomain()));

        SenseAttributes attributes = RemoteService.senseRemote.fetchSenseAttribute(unit.getId());

        if(attributes != null){
            definition.setText(attributes.getDefinition());
            comment.setText(attributes.getComment());
            register.setSelectedItem(attributes.getRegister() == null ? null :
                    new CustomDescription<>(LocalisationManager.getInstance().getLocalisedString(attributes.getRegister().getName()), attributes.getRegister()));
            link.setText(attributes.getLink());
        }

        examplesModel.clear();
        if(attributes.getExamples() != null){
            for(SenseExample example : attributes.getExamples()){
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
            btnSave.setEnabled(true);
        } else if (event.getSource() == lexicon) {
            btnSave.setEnabled(true);
        } else if (event.getSource() == register) {
            btnSave.setEnabled(true);
        } else if (event.getSource() == link) {
            btnSave.setEnabled(true);
        } else if (event.getSource() == examplesList) {
            btnSave.setEnabled(true);
        } else if (event.getSource() == partOfSpeech) {
            btnSave.setEnabled(true);
        }
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

}
