package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.radiobutton.WebRadioButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.synset.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class SynsetCriteria extends CriteriaPanel implements ActionListener {

    private MTextField definition;
    private MTextField comment;
    private Boolean isArtificial;
    private WebRadioButton all;
    private WebRadioButton artificial;
    private WebRadioButton notArtificial;
    private CriteriaDTO crit;

    public SynsetCriteria() {
        super(480);
        init();
        initializeFormPanel();
    }

    private void init() {
        crit = new CriteriaDTO();
        isArtificial = null;
        definition = new MTextField(STANDARD_VALUE_FILTER);
        comment = new MTextField(STANDARD_VALUE_FILTER);
        all = new WebRadioButton(Labels.VALUE_ALL, true);
        all.addActionListener(this);
        artificial = new WebRadioButton(Labels.ARTIFICIAL, false);
        artificial.addActionListener(this);
        notArtificial = new WebRadioButton(Labels.NORMAL, false);
        notArtificial.addActionListener(this);
    }

    @Override
    protected void initializeFormPanel() {
        addSearch();
        addLexicon();
        addPartsOfSpeach();
        addDomain();
        addSynsetRelationTypes();
        addDefinition();
        addComment();
        addArificial();
    }

    public MTextField getDefinition() {
        return definition;
    }

    public MTextField getComment() {
        return comment;
    }

    protected void addDefinition() {
        add("br", new MLabel(Labels.DEFINITION_COLON, 'd', definition));
        add("br hfill", definition);
    }

    protected void addComment() {
        add("br", new MLabel(Labels.COMMENT_COLON, 'm', comment));
        add("br hfill", comment);
    }

    protected void addArificial() {

        ButtonGroup aritificialGroup = new ButtonGroup();
        aritificialGroup.add(all);
        aritificialGroup.add(artificial);
        aritificialGroup.add(notArtificial);

        JPanel panel = new JPanel();
        panel.setLayout(new RiverLayout());
        panel.add("hfill", all);
        panel.add("br hfill", notArtificial);
        panel.add("br hfill", artificial);

        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Labels.SYNSET_TYPE));
        add("br hfill", panel);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == all) {
            isArtificial = null;
        } else if (event.getSource() == artificial) {
            isArtificial = true;
        } else if (event.getSource() == notArtificial) {
            isArtificial = false;
        }
    }

    @Override
    public void resetFields() {
        super.resetFields();
        definition.setText("");
        comment.setText("");
        all.setSelected(true);
    }

    public Boolean getIsArtificial() {
        return isArtificial;
    }

    @Override
    public CriteriaDTO getCriteria() {
        crit.setLemma(getSearchTextField().getText());
        crit.setLexicon(getLexiconComboBox().getSelectedIndex());
        crit.setPartOfSpeech(getPartsOfSpeechComboBox().getSelectedIndex());
        crit.setDomain(getDomainComboBox().getSelectedIndex());
        crit.setRelation(getSynsetRelationTypeComboBox().getSelectedIndex());
        crit.setDefinition(getDefinition().getText());
        crit.setComment(getComment().getText());
        crit.setAbstract(isArtificial);
        return crit;
    }

    public SynsetCriteriaDTO getSynsetCriteria() {
        SynsetCriteriaDTO dto = new SynsetCriteriaDTO();
        dto.setLemma(getSearchTextField().getText());
        if (getLexiconComboBox().getSelectedLexicon() != null) {
            dto.setLexiconId(getLexiconComboBox().getSelectedLexicon().getId());
        }

        if (getPartsOfSpeechComboBox().getSelectedPartOfSpeech() != null) {
            dto.setPartOfSpeechId(getPartsOfSpeechComboBox().getSelectedPartOfSpeech().getId());
        }

        if (getDomainComboBox().getSelectedDomain() != null) {
            dto.setDomainId(getDomainComboBox().getSelectedDomain().getId());
        }

        Long relationType = getSynsetRelationTypeComboBox().getEntity() == null ? null : getSynsetRelationTypeComboBox().getEntity().getId();

        dto.setDefinition(getDefinition().getText());
        dto.setComment(getComment().getText());
        dto.setAbstract(getIsArtificial());
        dto.setRelationTypeId(relationType);

        return dto;
    }

    @Override
    public void restoreCriteria(CriteriaDTO criteria) {
        getSearchTextField().setText(criteria.getLemma());
        getLexiconComboBox().setSelectedIndex(criteria.getLexicon());
        getPartsOfSpeechComboBox().setSelectedIndex(criteria.getPartOfSpeech());
        getDomainComboBox().setSelectedIndex(criteria.getDomain());
        getSynsetRelationTypeComboBox().setSelectedIndex(criteria.getRelation());
        getDefinition().setText(criteria.getDefinition());
        getComment().setText(criteria.getComment());
        if (criteria.isAbstract() == null) {
            all.setSelected(true);
        } else if (criteria.isAbstract()) {
            artificial.setSelected(true);
        } else {
            notArtificial.setSelected(true);
        }
        crit.setSense(criteria.getSense());
    }
}
