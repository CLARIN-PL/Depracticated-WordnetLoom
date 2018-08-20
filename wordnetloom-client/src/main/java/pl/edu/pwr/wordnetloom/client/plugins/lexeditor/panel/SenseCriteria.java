package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel;

import pl.edu.pwr.wordnetloom.client.systems.managers.DictionaryManager;
import pl.edu.pwr.wordnetloom.client.systems.renderers.LocalisedRenderer;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.systems.ui.MTextField;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.dto.CriteriaDTO;

import javax.swing.*;
import java.util.List;

public final class SenseCriteria extends CriteriaPanel {

    private JComboBox<Register> registerComboBox;
    private MTextField example;
    private CriteriaDTO criteria;
    // TODO chyba powinno się tutaj dodać wariant

    public SenseCriteria() {
        init();
        initializeFormPanel();
    }

    private void init() {
        criteria = new CriteriaDTO();
        registerComboBox = createRegisterComboBox();
        example = new MTextField(STANDARD_VALUE_FILTER);
    }

    private JComboBox createRegisterComboBox() {
        JComboBox resultComboBox = new JComboBox();
        resultComboBox.setRenderer(new LocalisedRenderer(Labels.VALUE_ALL));
        List<Register> registerList = DictionaryManager.getInstance().getDictionaryByClassName(Register.class);
        resultComboBox.addItem(null);
        for(Register register : registerList){
            resultComboBox.addItem(register);
        }
        return resultComboBox;
    }

    public SenseCriteriaDTO getSenseCriteriaDTO() {
        // TODO to może będzie trzeba zrobić troszkę inaczej, getCriteriaDTO, powinna tylko ustawiać wartości, bez tworzenia nowego
        SenseCriteriaDTO dto = new SenseCriteriaDTO(getCriteriaDTO());
        // TODO ustawić wariant
        dto.setExample(example.getText());
        dto.setRegister((Register) registerComboBox.getSelectedItem());

        return dto;
    }

    @Override
    protected void initializeFormPanel() {
        addSearch();
        addLexicon();
        addPartsOfSpeach();
        addDomain();
        addRelationType(RelationArgument.SENSE_RELATION);
        addRegister();
        addComment();
        addExample();
        addEmotions();
    }

    protected void addRegister() {
        add("br", new MLabel(Labels.REGISTER_COLON, 'd', registerComboBox));
        add("br hfill", registerComboBox);
    }

    protected void addExample() {
        add("br", new MLabel(Labels.USE_CASE_COLON, 'd', example));
        add("br hfill", example);
    }

    public JComboBox<Register> getRegisterComboBox() {
        return registerComboBox;
    }

    public MTextField getExample() {
        return example;
    }

    @Override
    public void resetFields() {
        super.resetFields();
        registerComboBox.setSelectedIndex(0);
        example.setText("");
    }

    @Override
    public CriteriaDTO getCriteria() {
        criteria = getSenseCriteriaDTO();
        return criteria;
    }

    public void setSensesToHold(List<Sense> sense) {
        // TODO dokończyć
//        criteria.setSense(new ArrayList<>(sense));
    }

    @Override
    public void restoreCriteria(CriteriaDTO criteria) {
        if(criteria != null){
            SenseCriteriaDTO dto = (SenseCriteriaDTO) criteria;
            restoreCriteriaDTO(criteria);
            example.setText(dto.getExample());
            registerComboBox.setSelectedItem(dto.getRegister());
            // TODO może należy ustawić sense do criteria
        }
    }
}
