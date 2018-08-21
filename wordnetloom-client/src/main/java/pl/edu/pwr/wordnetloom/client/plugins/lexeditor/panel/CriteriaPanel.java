package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.google.common.eventbus.Subscribe;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.events.SetLexiconsEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.SetCriteriaEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateCriteriaEvent;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.dictionary.model.Emotion;
import pl.edu.pwr.wordnetloom.dictionary.model.Markedness;
import pl.edu.pwr.wordnetloom.dictionary.model.Valuation;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.dto.CriteriaDTO;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public abstract class CriteriaPanel extends WebPanel {

    private static final long serialVersionUID = 4649824763750406980L;
    public static final String STANDARD_VALUE_FILTER = "";
    private final int DEFAULT_WIDTH = 150;
    private final int DEFAULT_HEIGHT = 20;
    protected final Dimension DEFAULT_DIMENSION_COMBO = new Dimension(DEFAULT_WIDTH, 25);

    protected WebTextField searchTextField;
    protected LexiconComboBox lexiconComboBox;
    protected DomainMComboBox domainComboBox;
    protected JTextComponent commentArea;
    protected JComboBox partsOfSpeechComboBox;
    protected LocalisedComboBox relationTypeComboBox;
    protected ComboCheckBox emotionsComboBox;
    protected ComboCheckBox valuationsComboBox;
    protected JComboBox<Markedness> markednessComboBox;

    private RelationArgument relationTypeArgument;

    private List<DictionaryCheckComboStore> emotionsItems = new ArrayList<>();
    private List<DictionaryCheckComboStore> valuationsItems = new ArrayList<>();

    private class DictionaryCheckComboStore implements pl.edu.pwr.wordnetloom.client.systems.ui.ComboCheckBox.CheckComboStore{

        private boolean selected;
        private Dictionary dictionary;

        public DictionaryCheckComboStore(Dictionary object, boolean selected) {
            this.dictionary = object;
            this.selected = selected;
        }

        @Override
        public boolean isSelected() {return selected;}

        @Override
        public void setSelected(boolean selected){
            this.selected = selected;
        }

        @Override
        public String getName() {
            return LocalisationManager.getInstance().getLocalisedString(dictionary.getName());
        }

        @Override
        public Long getId() {
            return dictionary.getId();
        }
    }


    public CriteriaPanel() {
        initialize();
        Application.eventBus.register(this);
    }

    @Subscribe
    public void setLexicons(SetLexiconsEvent event) {
        lexiconComboBox.refreshLexicons();
    }



    @Subscribe
    public void setCriteria(SetCriteriaEvent event){
        // TODO to powinno być raczej w SenseCriteria i SynsetCriteria
        if(event.getTabInfo().getSenseCriteriaDTO() != null){
            if(this instanceof SenseCriteria){
                restoreCriteria(event.getTabInfo().getSenseCriteriaDTO());
            } else {
                restoreCriteria(event.getTabInfo().getSynsetCriteriaDTO());
            }
        }
    }

    @Subscribe
    public void updateCriteria(UpdateCriteriaEvent event){
        CriteriaDTO dto = getCriteria();
        if(this instanceof SenseCriteria){
            event.getTabInfo().setSenseCriteriaDTO(dto);
        } else {
            event.getTabInfo().setSynsetCriteriaDTO(dto);
        }
        // TODO przenieść to do innych klas
    }

    private void initialize() {
        setLayout(new RiverLayout());

        lexiconComboBox = createLexiconComboBox();
        partsOfSpeechComboBox = createPartOfSpeechComboBox();
        searchTextField = new MTextField(STANDARD_VALUE_FILTER);
        domainComboBox = createDomainComboBox();
        commentArea = new MTextField(STANDARD_VALUE_FILTER);

        relationTypeComboBox = new LocalisedComboBox(Labels.VALUE_ALL);

        emotionsComboBox = createDictionariesComboBox(Emotion.class, emotionsItems);
        valuationsComboBox = createDictionariesComboBox(Valuation.class, valuationsItems);
        markednessComboBox = createMarkednessComboBox();
    }

    private ComboCheckBox createDictionariesComboBox(Class clazz, List<DictionaryCheckComboStore> list){
        ComboCheckBox comboCheckBox = new ComboCheckBox();
        List<Dictionary> dictionaries = RemoteService.dictionaryServiceRemote.findDictionaryByClass(clazz);
        List<DictionaryCheckComboStore> comboElements = new ArrayList<>();
        for(Dictionary dictionary : dictionaries) {
            comboElements.add(new DictionaryCheckComboStore(dictionary, false));
        }
        comboCheckBox.setElements(comboElements);
        return comboCheckBox;
    }

    private JComboBox createMarkednessComboBox() {
        return new LocalisedComboBox()
                .withItems(RemoteService.dictionaryServiceRemote.findDictionaryByClass(Markedness.class));
    }

    private LexiconComboBox createLexiconComboBox() {
        LexiconComboBox resultComboBox = new LexiconComboBox(Labels.VALUE_ALL);
        resultComboBox.setPreferredSize(DEFAULT_DIMENSION_COMBO);
        resultComboBox.addActionListener((ActionEvent e) -> {
            Lexicon lex = resultComboBox.getEntity();
            if (lex != null) {
                domainComboBox.filterDomainsByLexicon(lex, true);
            } else {
                domainComboBox.allDomains(true);
            }
            refreshRelationTypes();
        });

        return resultComboBox;
    }

    private void refreshRelationTypes() {
        RelationType selectedRelation = (RelationType) relationTypeComboBox.getSelectedItem();
        Lexicon lexicon = lexiconComboBox.getSelectedLexicon();
        List<RelationType> allowedRelationTypes;
        if(lexicon == null){
            allowedRelationTypes = RelationTypeManager.getInstance().getRelationsWithoutProxyParent(relationTypeArgument);
        } else {
            allowedRelationTypes = RelationTypeManager.getInstance().getRelationsWithoutProxyParent(relationTypeArgument, lexicon);
        }
        relationTypeComboBox.setItems(allowedRelationTypes);
        relationTypeComboBox.setSelectedItem(selectedRelation);
    }

    private LocalisedComboBox createPartOfSpeechComboBox() {
        LocalisedComboBox resultComboBox = new LocalisedComboBox(Labels.VALUE_ALL);
        resultComboBox.setPreferredSize(DEFAULT_DIMENSION_COMBO);
        resultComboBox.addItemListener((ItemEvent e) -> {
            PartOfSpeech pos = (PartOfSpeech) resultComboBox.getSelectedItem();
            Lexicon lex = lexiconComboBox.getEntity();
            if (pos != null && lex != null) {
                domainComboBox.filterDomainByUbyPosAndLexcion(pos, lex, true);
            } else if (lex != null && pos == null) {
                domainComboBox.filterDomainsByLexicon(lex, true);
            } else {
                domainComboBox.allDomains(true);
            }

        });
        resultComboBox.setItems(RemoteService.partOfSpeechServiceRemote.findAll());
        return resultComboBox;
    }

    private DomainMComboBox createDomainComboBox() {
        DomainMComboBox resultComboBox = new DomainMComboBox(Labels.VALUE_ALL);
        resultComboBox.allDomains(true);
        resultComboBox.setPreferredSize(DEFAULT_DIMENSION_COMBO);
        return resultComboBox;
    }

    protected abstract void initializeFormPanel();

    public abstract CriteriaDTO getCriteria();

    protected CriteriaDTO getCriteriaDTO() {
        CriteriaDTO dto = new CriteriaDTO();
        dto.setLemma(searchTextField.getText());
        Lexicon lexicon = lexiconComboBox.getSelectedLexicon();
        if(lexicon == null){
            List<Long> lexiconsIds = LexiconManager.getInstance().getUserChosenLexiconsIds();
            dto.setLexicons(lexiconsIds);
        } else {
            dto.setLexiconId(lexicon.getId());
        }
        dto.setDomain(domainComboBox.getSelectedDomain());
        dto.setPartOfSpeech((PartOfSpeech) partsOfSpeechComboBox.getSelectedItem());
        dto.setComment(commentArea.getText());
        dto.setRelationType((RelationType) relationTypeComboBox.getSelectedItem());
        dto.setEmotions(emotionsComboBox.getSelectedItemsIds());
        dto.setValuations(valuationsComboBox.getSelectedItemsIds());
        dto.setMarkedness((Markedness) markednessComboBox.getSelectedItem());

        return dto;
    }

    public abstract void restoreCriteria(CriteriaDTO criteria);

    protected void restoreCriteriaDTO(CriteriaDTO criteriaDTO) {
        searchTextField.setText(criteriaDTO.getLemma());
        lexiconComboBox.setSelectedLexicon(criteriaDTO.getLexicons());
        domainComboBox.setSelectedDomain(criteriaDTO.getDomain());
        partsOfSpeechComboBox.setSelectedItem(criteriaDTO.getPartOfSpeech());
        commentArea.setText(criteriaDTO.getComment());
        relationTypeComboBox.setSelectedItem(criteriaDTO.getRelationType());
        emotionsComboBox.setSelectedIds(criteriaDTO.getEmotions());
        valuationsComboBox.setSelectedIds(criteriaDTO.getValuations());
        markednessComboBox.setSelectedItem(criteriaDTO.getMarkedness());
        // TODO uzupełnić
    }

    protected void addRelationType(RelationArgument type){
        relationTypeArgument = type;
        relationTypeComboBox.setItems(RemoteService.relationTypeRemote.findLeafs(type));

        add("br", new MLabel(Labels.RELATION_COLON, 'r' ,relationTypeComboBox));
        add("br hfill", relationTypeComboBox);
    }

    protected void addDomain() {
        add("br", new MLabel(Labels.DOMAIN_COLON, 'd', domainComboBox));
        add("br hfill", domainComboBox);
    }

    protected void addPartsOfSpeach() {
        add("br", new MLabel(Labels.PARTS_OF_SPEECH_COLON, 'm', partsOfSpeechComboBox));
        add("br hfill", partsOfSpeechComboBox);
    }

    protected void addLexicon() {
        add("br", new MLabel(Labels.LEXICON_COLON, 'l', lexiconComboBox));
        add("br hfill", lexiconComboBox);
    }

    protected void addSearch() {
        add("", new MLabel(Labels.SEARCH_COLON, 'w', searchTextField));
        add("br hfill", searchTextField);
    }

    protected void addEmotions() {
        // TODO dorobić etykiet
        add("br", new JLabel(Labels.EMOTIONS));
        add("br hfill", emotionsComboBox);
        add("br", new JLabel("Wartościowanie"));
        add("br hfill", valuationsComboBox);
        add("br", new JLabel("Nacechowanie"));
        add("br hfill",markednessComboBox);
    }

    protected void addComment() {
        add("br", new MLabel(Labels.COMMENT_COLON, 'm', commentArea));
        add("br hfill", commentArea);
    }

    public void refreshPartOfSpeech() {
        int selected = partsOfSpeechComboBox.getSelectedIndex();
        if (selected != -1) {
            partsOfSpeechComboBox.setSelectedIndex(selected);
        }
    }

    public void refreshDomain() {
        int selected = domainComboBox.getSelectedIndex();
        if (selected != -1) {
            domainComboBox.setSelectedIndex(selected);
        }
    }

    public void resetFields() {
        System.out.println("CriteriaPanel - resetFields");
        searchTextField.setText("");
        domainComboBox.setSelectedIndex(0);
        partsOfSpeechComboBox.setSelectedIndex(0);
        commentArea.setText("");
        relationTypeComboBox.setSelectedIndex(0);
        lexiconComboBox.setSelectedIndex(0);
    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    public DomainMComboBox getDomainComboBox() {
        return domainComboBox;
    }

    public LexiconComboBox getLexiconComboBox() {
        return lexiconComboBox;
    }

    public JComboBox getPartsOfSpeechComboBox() {
        return partsOfSpeechComboBox;
    }

}
