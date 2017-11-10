package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;

public abstract class CriteriaPanel extends JPanel {

    private static final long serialVersionUID = 4649824763750406980L;
    public static final String STANDARD_VALUE_FILTER = "";
    private int SCROLL_PANE_HEIGHT = 400;
    public static final int MAX_ITEMS_COUNT = 500;

    private JTextField searchTextField;
    private LexiconComboBox lexiconComboBox;
    private DomainComboBox domainComboBox;
    private PartOfSpeechComboBox partsOfSpeachComboBox;
    private ComboBoxPlain<RelationType> synsetRelationsComboBox;
    private ComboBoxPlain<RelationType> senseRelationsComboBox;
    private JCheckBox limitResultCheckBox;

    public CriteriaPanel(int scrollHeight) {
        SCROLL_PANE_HEIGHT = scrollHeight;
        initialize();
    }

    private void initialize() {
        setLayout(new RiverLayout());
        setMaximumSize(new Dimension(0, SCROLL_PANE_HEIGHT));
        setMinimumSize(new Dimension(0, SCROLL_PANE_HEIGHT));
        setPreferredSize(new Dimension(0, SCROLL_PANE_HEIGHT));

        lexiconComboBox = new LexiconComboBox(Labels.VALUE_ALL);
        lexiconComboBox.setPreferredSize(new Dimension(150, 20));
        lexiconComboBox.addActionListener((ActionEvent e) -> {
            Lexicon lex = lexiconComboBox.retriveComboBoxItem();
            if (lex != null) {
                domainComboBox.filterDomainsByLexicon(lex, true);
            } else {
                domainComboBox.allDomains(true);
            }
            refreshSenseRelations();
        });

        searchTextField = new TextFieldPlain(STANDARD_VALUE_FILTER);

        partsOfSpeachComboBox = new PartOfSpeechComboBox(Labels.VALUE_ALL);
        partsOfSpeachComboBox.withoutFilter();
        partsOfSpeachComboBox.setPreferredSize(new Dimension(150, 20));
        partsOfSpeachComboBox.addItemListener((ItemEvent e) -> {
            PartOfSpeech pos = partsOfSpeachComboBox.retriveComboBoxItem();
            Lexicon lex = lexiconComboBox.retriveComboBoxItem();
            if (pos != null && lex != null) {
                domainComboBox.filterDomainByUbyPosAndLexcion(pos, lex, true);
            } else if (lex != null && pos == null) {
                domainComboBox.filterDomainsByLexicon(lex, true);
            } else if (pos != null && lex == null) {
                domainComboBox.filterDomainByUbyPos(pos, true);
            } else {
                domainComboBox.allDomains(true);
            }
        });

        domainComboBox = new DomainComboBox(Labels.VALUE_ALL);
        domainComboBox.allDomains(true);
        domainComboBox.setPreferredSize(new Dimension(150, 20));

        synsetRelationsComboBox = createSynsetRelationsComboBox();
        synsetRelationsComboBox.setPreferredSize(new Dimension(150, 20));

        senseRelationsComboBox = createSenseRelationsComboBox();
        senseRelationsComboBox.setPreferredSize(new Dimension(150, 20));

        limitResultCheckBox = createLimitResultSearch();

    }

    protected abstract void initializeFormPanel();

    public abstract CriteriaDTO getCriteria();

    public abstract void restoreCriteria(CriteriaDTO criteria);

    protected void addLimit() {
        add("br left", limitResultCheckBox);
    }

    protected void addSynsetRelationTypes() {
        add("br", new LabelExt(Labels.RELATIONS_COLON, 'r', synsetRelationsComboBox));
        add("br hfill", synsetRelationsComboBox);
        refreshSynsetRelations();
    }

    protected void addSenseRelationTypes() {
        add("br", new LabelExt(Labels.RELATIONS_COLON, 'r', senseRelationsComboBox));
        add("br hfill", senseRelationsComboBox);
        refreshSenseRelations();
    }

    protected void addDomain() {
        add("br", new LabelExt(Labels.DOMAIN_COLON, 'd', domainComboBox));
        add("br hfill", domainComboBox);
    }

    protected void addPartsOfSpeach() {
        add("br", new LabelExt(Labels.PARTS_OF_SPEECH_COLON, 'm', partsOfSpeachComboBox));
        add("br hfill", partsOfSpeachComboBox);
    }

    protected void addLexicon() {
        add("br", new LabelExt(Labels.LEXICON_COLON, 'l', lexiconComboBox));
        add("br hfill", lexiconComboBox);
    }

    protected void addSearch() {
        add("", new LabelExt(Labels.SEARCH_COLON, 'w', searchTextField));
        add("br hfill", searchTextField);
    }

    private ComboBoxPlain<RelationType> createSynsetRelationsComboBox() {
        ComboBoxPlain<RelationType> combo = new ComboBoxPlain<>();
        combo.addItem(new CustomDescription<>(Labels.VALUE_ALL, null));
        combo.setPreferredSize(new Dimension(150, 20));
        return combo;
    }

    private ComboBoxPlain<RelationType> createSenseRelationsComboBox() {
        ComboBoxPlain<RelationType> combo = new ComboBoxPlain<>();
        combo.addItem(new CustomDescription<>(Labels.VALUE_ALL, null));
        combo.setPreferredSize(new Dimension(150, 20));
        return combo;
    }

    private JCheckBox createLimitResultSearch() {
        JCheckBox limitResult = new JCheckBox(String.format(Labels.LIMIT_TO, "" + MAX_ITEMS_COUNT));
        limitResult.setSelected(true);
        return limitResult;
    }

    public void refreshPartOfSpeech() {
        int selected = partsOfSpeachComboBox.getSelectedIndex();
        if (selected != -1) {
            partsOfSpeachComboBox.setSelectedIndex(selected);
        }
    }

    public void refreshDomain() {
        int selected = domainComboBox.getSelectedIndex();
        if (selected != -1) {
            domainComboBox.setSelectedIndex(selected);
        }
    }

    public void refreshSynsetRelations() {
    }

    public void refreshSenseRelations() {
        RelationTypeManager.getInstance().refresh();
        List<RelationType> relations = RemoteService.relationTypeRemote.findLeafs(RelationArgument.SENSE_RELATION);
        int selected = senseRelationsComboBox.getSelectedIndex();

        senseRelationsComboBox.removeAllItems();
        senseRelationsComboBox.addItem(new CustomDescription<>(Labels.VALUE_ALL, null));

        if (lexiconComboBox.retriveComboBoxItem() != null) {
            for (RelationType relation : relations) {
                if (relation.getLexicons().contains(lexiconComboBox.retriveComboBoxItem())) {
                    RelationType currentRelation = relation;//RelationTypeManager.get(relation.getId()).getRelationType(), RelationTypeManager.getFullNameFor(currentRelation.getId();
                    //senseRelationsComboBox.addItem(new CustomDescription<>(relation.getName(RemoteConnectionProvider.getInstance().getLanguage()), currentRelation));
                }
            }
        } else {
            for (RelationType relation : relations) {
                RelationType currentRelation = relation; //RelationTypeManager.get(relation.getId()).getRelationType();
                // senseRelationsComboBox.addItem(new CustomDescription<>(relation.getName(RemoteConnectionProvider.getInstance().getLanguage()), currentRelation));
            }
        }

        if (selected != -1) {
            senseRelationsComboBox.setSelectedIndex(selected);
        }
    }

    public void resetFields() {
        searchTextField.setText("");
        domainComboBox.setSelectedIndex(0);
        partsOfSpeachComboBox.setSelectedIndex(0);
        synsetRelationsComboBox.setSelectedIndex(0);
        senseRelationsComboBox.setSelectedIndex(0);
        lexiconComboBox.setSelectedIndex(0);
    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    public ComboBoxPlain<Domain> getDomainComboBox() {
        return domainComboBox;
    }

    public ComboBoxPlain<RelationType> getSynsetRelationTypeComboBox() {
        return synsetRelationsComboBox;
    }

    public ComboBoxPlain<RelationType> getSenseRelationTypeComboBox() {
        return senseRelationsComboBox;
    }

    public JCheckBox getLimitResultCheckBox() {
        return limitResultCheckBox;
    }

    public LexiconComboBox getLexiconComboBox() {
        return lexiconComboBox;
    }

    public PartOfSpeechComboBox getPartsOfSpeachComboBox() {
        return partsOfSpeachComboBox;
    }

}
