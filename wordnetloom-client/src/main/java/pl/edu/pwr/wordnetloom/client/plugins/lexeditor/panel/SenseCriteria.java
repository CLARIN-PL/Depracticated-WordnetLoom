package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import pl.edu.pwr.wordnetloom.client.systems.enums.RegisterTypes;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.client.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

public final class SenseCriteria extends CriteriaPanel {

    private ComboBoxPlain<RegisterTypes> registerComboBox;
    private TextFieldPlain comment;
    private TextFieldPlain example;
    private CriteriaDTO crit;

    public SenseCriteria() {
        super(420);
        init();
        initializeFormPanel();
    }

    private void init() {
        crit = new CriteriaDTO();
        registerComboBox = new ComboBoxPlain<>();
        registerComboBox.addItem(new CustomDescription<>(Labels.VALUE_ALL, null));
        for (RegisterTypes reg : RegisterTypes.values()) {
            registerComboBox.addItem(reg);
        }
        registerComboBox.setPreferredSize(new Dimension(150, 20));

        comment = new TextFieldPlain(STANDARD_VALUE_FILTER);
        example = new TextFieldPlain(STANDARD_VALUE_FILTER);
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
        addLimit();
    }

    protected void addRegister() {
        add("br", new LabelExt(Labels.REGISTER_COLON, 'd', registerComboBox));
        add("br hfill", registerComboBox);
    }

    protected void addComment() {
        add("br", new LabelExt(Labels.COMMENT_COLON, 'd', comment));
        add("br hfill", comment);
    }

    protected void addExample() {
        add("br", new LabelExt(Labels.USE_CASE_COLON, 'd', example));
        add("br hfill", example);
    }

    public ComboBoxPlain<RegisterTypes> getRegisterComboBox() {
        return registerComboBox;
    }

    public TextFieldPlain getComment() {
        return comment;
    }

    public TextFieldPlain getExample() {
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
        crit.setLemma(getSearchTextField().getText());
        crit.setLexicon(getLexiconComboBox().getSelectedIndex());
        crit.setPartOfSpeech(getPartsOfSpeachComboBox().getSelectedIndex());
        crit.setDomain(getDomainComboBox().getSelectedIndex());
        crit.setRelation(getSenseRelationTypeComboBox().getSelectedIndex());
        crit.setRegister(getRegisterComboBox().getSelectedIndex());
        crit.setComment(getComment().getText());
        crit.setExample(getExample().getText());
        return crit;
    }

    public void setSensesToHold(List<Sense> sense) {
        crit.setSense(new ArrayList<>(sense));
    }

    @Override
    public void restoreCriteria(CriteriaDTO criteria) {
        getSearchTextField().setText(criteria.getLemma());
        getLexiconComboBox().setSelectedIndex(criteria.getLexicon());
        getPartsOfSpeachComboBox().setSelectedIndex(criteria.getPartOfSpeech());
        getDomainComboBox().setSelectedIndex(criteria.getDomain());
        getSenseRelationTypeComboBox().setSelectedIndex(criteria.getRelation());
        getRegisterComboBox().setSelectedIndex(criteria.getRegister());
        getComment().setText(criteria.getComment());
        getExample().setText(criteria.getExample());
        crit.setSense(criteria.getSense());
    }
}
