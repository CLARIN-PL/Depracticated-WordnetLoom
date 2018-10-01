package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import com.alee.laf.radiobutton.WebRadioButton;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.SetCriteriaEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateCriteriaEvent;
import pl.edu.pwr.wordnetloom.client.security.UserSessionContext;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Markedness;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.synset.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import pl.edu.pwr.wordnetloom.user.model.Role;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

public final class SynsetCriteria extends CriteriaPanel implements ActionListener {

    private JTextField id;
    private MTextField definition;
//    private MTextField comment;
    private Boolean isArtificial;
    private WebRadioButton all;
    private WebRadioButton artificial;
    private WebRadioButton notArtificial;
    private CriteriaDTO crit;

    public SynsetCriteria() {
        super();
        init();
        initializeFormPanel();
    }

    @Override
    public void setCriteria(SetCriteriaEvent event){
        if(event.getTabInfo().getSynsetCriteriaDTO()!=null){
            restoreCriteriaDTO(event.getTabInfo().getSynsetCriteriaDTO());
        }
    }

    @Override
    public void updateCriteria(UpdateCriteriaEvent event){
        event.getTabInfo().setSynsetCriteriaDTO(getCriteria());
    }

    private void init() {
        crit = new CriteriaDTO();
        isArtificial = null;

        id = createIdField();
        definition = new MTextField(STANDARD_VALUE_FILTER);
        all = new WebRadioButton(Labels.VALUE_ALL, true);
        all.addActionListener(this);
        artificial = new WebRadioButton(Labels.ARTIFICIAL, false);
        artificial.addActionListener(this);
        notArtificial = new WebRadioButton(Labels.NORMAL, false);
        notArtificial.addActionListener(this);
    }

    private JTextField createIdField() {
        JTextField id = new MTextField(STANDARD_VALUE_FILTER);
        id.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c= e.getKeyChar();
                if(!Character.isDigit(c)|| c== KeyEvent.VK_BACK_SPACE || c==KeyEvent.VK_DELETE){
                    e.consume();
                }
            }
        });

        return id;
    }

    @Override
    protected void initializeFormPanel() {
        addSearch();
        if(UserSessionContext.getInstance().hasRole(Role.ADMIN)){
            addId();
        }
        addLexicon();
        addPartsOfSpeach();
        addDomain();
        addStatus();
        addRelationType(RelationArgument.SYNSET_RELATION);
        addDefinition();
        addComment();
        addArificial();
        addEmotions();
    }

    public MTextField getDefinition() {
        return definition;
    }

    private void addId() {
        add("br", new MLabel("id", 'i', id));
        add("br hfill", id);
    }

    protected void addDefinition() {
        add("br", new MLabel(Labels.DEFINITION_COLON, 'd', definition));
        add("br hfill", definition);
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
        all.setSelected(true);
    }

    public Boolean getIsArtificial() {
        return isArtificial;
    }

    @Override
    public CriteriaDTO getCriteria() {
        return getSynsetCriteria();
    }

    public SynsetCriteriaDTO getSynsetCriteria() {
        SynsetCriteriaDTO dto = new SynsetCriteriaDTO(getCriteriaDTO());
        if (!id.getText().isEmpty()){
            dto.setSynsetId(Long.parseLong(id.getText()));
        }
        dto.setDefinition(definition.getText());
        dto.setAbstract(artificial.isSelected());

        return dto;
    }

    @Override
    public void restoreCriteria(CriteriaDTO criteria) {
        assert criteria instanceof SynsetCriteriaDTO;
        SynsetCriteriaDTO dto = (SynsetCriteriaDTO) criteria;
        restoreCriteriaDTO(dto);
        id.setText(String.valueOf(dto.getSynsetId()));
        definition.setText(dto.getDefinition());
        artificial.setSelected(dto.isAbstract());
        notArtificial.setSelected(!dto.isAbstract());
    }
}
