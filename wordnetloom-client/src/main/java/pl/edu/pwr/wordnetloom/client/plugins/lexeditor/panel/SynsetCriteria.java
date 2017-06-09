package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationArgument;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import se.datadosen.component.RiverLayout;

@SuppressWarnings("serial")
public class SynsetCriteria extends CriteriaPanel implements ActionListener {

    private TextFieldPlain definition;
    private TextFieldPlain comment;
    private String isArtificial;
    private JRadioButton all;
    private JRadioButton artificial;
    private JRadioButton notArtificial;
    private CriteriaDTO crit;

    public SynsetCriteria(RelationArgument relationArgumentType) {
        super(relationArgumentType, 480);
        init();
        initializeFormPanel();
    }

    private void init() {
        crit = new CriteriaDTO();
        isArtificial = "";
        definition = new TextFieldPlain(STANDARD_VALUE_FILTER);
        comment = new TextFieldPlain(STANDARD_VALUE_FILTER);
        all = new JRadioButton(Labels.VALUE_ALL, true);
        all.addActionListener(this);
        artificial = new JRadioButton(Labels.ARTIFICIAL, false);
        artificial.addActionListener(this);
        notArtificial = new JRadioButton(Labels.NORMAL, false);
        notArtificial.addActionListener(this);
    }

    @Override
    protected void initializeFormPanel() {
        addSearch();
        addLexicon();
        addPartsOfSpeach();
        addDomain();
        addRelations();
        addDefinition();
        addComment();
        addArificial();
        addLimit();
    }

    public TextFieldPlain getDefinition() {
        return definition;
    }

    public TextFieldPlain getComment() {
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
        crit.setRelation(getRelationsComboBox().getSelectedIndex());
        crit.setDefinition(getDefinition().getText());
        crit.setComment(getComment().getText());
        crit.setSynsetType(getIsArtificial());
        return crit;
    }

    public void setSensesToHold(List<Sense> sense) {
        crit.setSense(new ArrayList<Sense>(sense));
    }

    @Override
    public void restoreCriteria(CriteriaDTO criteria) {
        getSearchTextField().setText(criteria.getLemma());
        getLexiconComboBox().setSelectedIndex(criteria.getLexicon());
        getPartsOfSpeachComboBox().setSelectedIndex(criteria.getPartOfSpeech());
        getDomainComboBox().setSelectedIndex(criteria.getDomain());
        getRelationsComboBox().setSelectedIndex(criteria.getRelation());
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
