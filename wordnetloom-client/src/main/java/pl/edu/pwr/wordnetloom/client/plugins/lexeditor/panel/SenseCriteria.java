package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import pl.edu.pwr.wordnetloom.client.systems.managers.DictionaryManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComboBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.RegisterDictionary;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.dto.CriteriaDTO;

import java.util.ArrayList;
import java.util.List;

public final class SenseCriteria extends CriteriaPanel {

    private MComboBox<RegisterDictionary> registerComboBox;
    private MTextField comment;
    private MTextField example;
    private CriteriaDTO criteria;

    public SenseCriteria() {
        super(420);
        init();
        initializeFormPanel();
    }

    private void init() {
        criteria = new CriteriaDTO();
        registerComboBox = initRegisterComboBox();
        comment = new MTextField(STANDARD_VALUE_FILTER);
        example = new MTextField(STANDARD_VALUE_FILTER);
    }

    private MComboBox initRegisterComboBox() {
        List<RegisterDictionary> list = DictionaryManager.getInstance().getDictionaryByClassName(RegisterDictionary.class);
        MComboBox<RegisterDictionary> resultComboBox = new MComboBox<>()
                .withDictionaryItems(list, Labels.VALUE_ALL);

        resultComboBox.setPreferredSize(DEFAULT_DIMENSION);

        return resultComboBox;
    }

    public SenseCriteriaDTO getSenseCriteriaDTO() {

        String lemma = getSearchTextField().getText();
        String comment = getComment().getText();
        String example = getExample().getText();
        Long partOfSpeechId = getPartsOfSpeechComboBox().getEntity() == null ? null : getPartsOfSpeechComboBox().getEntity().getId();
        Long domainId = getDomainComboBox().getEntity() == null ? null : getDomainComboBox().getEntity().getId();
        List<Long> lexicons = new ArrayList<>();
        Lexicon lexicon = getLexiconComboBox().getEntity();
        Long relationType = getSenseRelationTypeComboBox().getEntity() == null ? null : getSenseRelationTypeComboBox().getEntity().getId();

        if (lexicon != null) {
            lexicons.add(lexicon.getId());
        } else {
            lexicons.addAll(LexiconManager.getInstance().getUserChosenLexiconsIds());
        }

        SenseCriteriaDTO senseCriteria = new SenseCriteriaDTO(partOfSpeechId, domainId, lemma, lexicons);
        senseCriteria.setRelationTypeId(relationType);
        senseCriteria.setComment(comment);
        senseCriteria.setExample(example);
        //senseCriteria.setRegisterId(relationTypeId);

        return senseCriteria;
    }

    @Override
    protected void initializeFormPanel() {
        addSearch();
        addLexicon();
        addPartsOfSpeach();
        addDomain();
        addSenseRelationTypes();
        addRegister();
        addComment();
        addExample();
    }

    protected void addRegister() {
        add("br", new MLabel(Labels.REGISTER_COLON, 'd', registerComboBox));
        add("br hfill", registerComboBox);
    }

    protected void addComment() {
        add("br", new MLabel(Labels.COMMENT_COLON, 'd', comment));
        add("br hfill", comment);
    }

    protected void addExample() {
        add("br", new MLabel(Labels.USE_CASE_COLON, 'd', example));
        add("br hfill", example);
    }

    public MComboBox<RegisterDictionary> getRegisterComboBox() {
        return registerComboBox;
    }

    public MTextField getComment() {
        return comment;
    }

    public MTextField getExample() {
        return example;
    }

    @Override
    public void resetFields() {
        super.resetFields();
        registerComboBox.setSelectedIndex(0);
        comment.setText("");
        example.setText("");
    }

    @Override
    public CriteriaDTO getCriteria() {
        criteria.setLemma(getSearchTextField().getText());
        criteria.setLexicon(getLexiconComboBox().getSelectedIndex());
        criteria.setPartOfSpeech(getPartsOfSpeechComboBox().getSelectedIndex());
        criteria.setDomain(getDomainComboBox().getSelectedIndex());
        criteria.setRelation(getSenseRelationTypeComboBox().getSelectedIndex());
        criteria.setRegister(getRegisterComboBox().getSelectedIndex());
        criteria.setComment(getComment().getText());
        criteria.setExample(getExample().getText());
        return criteria;
    }

    public void setSensesToHold(List<Sense> sense) {
        criteria.setSense(new ArrayList<>(sense));
    }

    @Override
    public void restoreCriteria(CriteriaDTO criteria) {
        getSearchTextField().setText(criteria.getLemma());
        getLexiconComboBox().setSelectedIndex(criteria.getLexicon());
        getPartsOfSpeechComboBox().setSelectedIndex(criteria.getPartOfSpeech());
        getDomainComboBox().setSelectedIndex(criteria.getDomain());
        getSenseRelationTypeComboBox().setSelectedIndex(criteria.getRelation());
        getRegisterComboBox().setSelectedIndex(criteria.getRegister());
        getComment().setText(criteria.getComment());
        getExample().setText(criteria.getExample());
        this.criteria.setSense(criteria.getSense());
    }
}
