package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.radiobutton.WebRadioButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public final class SynsetCriteria extends CriteriaPanel implements ActionListener {

    private MTextField definition;
    private MTextField comment;
    private String isArtificial;
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
        isArtificial = "";
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
        addLimit();
    }

    public MTextField getDefinition() {
        return definition;
    }

    public MTextField getComment() {
        return comment;
    }

    protected void addDefinition() {
        add("br", new LabelExt(Labels.DEFINITION_COLON, 'd', definition));
        add("br hfill", definition);
    }

    protected void addComment() {
        add("br", new LabelExt(Labels.COMMENT_COLON, 'm', comment));
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
            isArtificial = "";
        }
        if (event.getSource() == artificial) {
            isArtificial = "1";
        }
        if (event.getSource() == notArtificial) {
            isArtificial = "0";
        }
    }

    @Override
    public void resetFields() {
        super.resetFields();
        definition.setText("");
        comment.setText("");
        all.setSelected(true);
    }

    public String getIsArtificial() {
        return isArtificial;
    }

    @Override
    public CriteriaDTO getCriteria() {
        crit.setLemma(getSearchTextField().getText());
        crit.setLexicon(getLexiconComboBox().getSelectedIndex());
        crit.setPartOfSpeech(getPartsOfSpeachComboBox().getSelectedIndex());
        crit.setDomain(getDomainComboBox().getSelectedIndex());
        crit.setRelation(getSynsetRelationTypeComboBox().getSelectedIndex());
        crit.setDefinition(getDefinition().getText());
        crit.setComment(getComment().getText());
        crit.setSynsetType(getIsArtificial());
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
        getSynsetRelationTypeComboBox().setSelectedIndex(criteria.getRelation());
        getDefinition().setText(criteria.getDefinition());
        getComment().setText(criteria.getComment());
        crit.setSense(criteria.getSense());
        if ("".equals(criteria.getSynsetType())) {
            all.setSelected(true);
        }
        if ("1".equals(criteria.getSynsetType())) {
            artificial.setSelected(true);
        }
        if ("0".equals(criteria.getSynsetType())) {
            notArtificial.setSelected(true);
        }
    }
}
