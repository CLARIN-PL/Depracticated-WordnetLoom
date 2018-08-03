package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import pl.edu.pwr.wordnetloom.client.systems.managers.DictionaryManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.ui.MComboBox;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Markedness;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.dto.CriteriaDTO;

import java.util.ArrayList;
import java.util.List;

public final class SenseCriteria extends CriteriaPanel {

    private MComboBox<Register> registerComboBox;
    private MTextField comment;
    private MTextField example;
    private CriteriaDTO criteria;

    public SenseCriteria() {
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
        return  new MComboBox<Register>()
                .withSize(DEFAULT_DIMENSION_COMBO)
                .withDictionaryItems(
                        DictionaryManager.getInstance().getDictionaryByClassName(Register.class),
                        Labels.VALUE_ALL);
    }

    public SenseCriteriaDTO getSenseCriteriaDTO() {
        Long partOfSpeechId = getPartsOfSpeechComboBox().getEntity() != null ? getPartsOfSpeechComboBox().getEntity().getId() : null;
        Long domainId = getDomainComboBox().getEntity() != null ? getDomainComboBox().getEntity().getId() : null;
        String lemma = getSearchTextField().getText();
        List<Long> lexicons = new ArrayList<>();
        Lexicon lexicon = getLexiconComboBox().getEntity();
        if(lexicon != null){
            lexicons.add(lexicon.getId());
        } else {
            lexicons.addAll(LexiconManager.getInstance().getUserChosenLexiconsIds());
        }

        SenseCriteriaDTO senseCriteria = new SenseCriteriaDTO(partOfSpeechId, domainId, lemma, lexicons);
        senseCriteria.setRelationTypeId(getSenseRelationTypeComboBox().getEntity() != null ? getSenseRelationTypeComboBox().getEntity().getId() : null);
        senseCriteria.setComment(getComment().getText());
        senseCriteria.setExample(getExample().getText());
        senseCriteria.setRegisterId(registerComboBox.getEntity() != null ? registerComboBox.getEntity().getId() : null);
        senseCriteria.setEmotions(emotionsComboBox.getSelectedItemsIds());
        senseCriteria.setValuations(valuationsComboBox.getSelectedItemsIds());
        senseCriteria.setMarkedness(markednessComboBox.getSelectedItem() != null ? ((Markedness)markednessComboBox.getSelectedItem()).getId() : null);

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
        addEmotions();
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

    public MComboBox<Register> getRegisterComboBox() {
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
