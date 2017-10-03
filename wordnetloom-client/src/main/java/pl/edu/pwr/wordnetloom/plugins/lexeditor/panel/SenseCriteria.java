package pl.edu.pwr.wordnetloom.plugins.lexeditor.panel;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.edu.pwr.wordnetloom.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.model.RelationArgument;
import pl.edu.pwr.wordnetloom.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.dto.RegisterTypes;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.systems.ui.TextFieldPlain;

public class SenseCriteria extends CriteriaPanel implements Serializable{

	private static final long serialVersionUID = 1597186652521168461L;

	private ComboBoxPlain<RegisterTypes> registerComboBox;
	private TextFieldPlain comment;
	private TextFieldPlain example;
	private CriteriaDTO crit;

	public SenseCriteria(RelationArgument relationArgumentType) {
		super(relationArgumentType, 680);
		init();
		initializeFormPanel();
	}

	private void init() {
		crit = new CriteriaDTO();
		registerComboBox = new ComboBoxPlain<>();
		registerComboBox.addItem(Labels.VALUE_ALL, null);
		for (RegisterTypes reg : RegisterTypes.values()) {
			registerComboBox.addItem(reg.getAbbreviation(), reg);
		}
		registerComboBox.setPreferredSize(new Dimension(150, 20));

		comment = new TextFieldPlain(STANDARD_VALUE_FILTER);
		example = new TextFieldPlain(STANDARD_VALUE_FILTER);
	}

	@Override
	protected void initializeFormPanel() {
		addSearchType();
		addSearch();
		addLexicon();
		addPartsOfSpeach();
		addGrammaticalGenderDic();
		addStyleDic();
		addDomain();
		addDomainDic();
		addDomainModifDic();
		addStatusDic();
		addAgeDic();
		addSourceDic();
		addLexicalCharacteristicDic();
		addEthymology();
		addRelations();
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
		crit.setSearchType(getSearchTypeComboBox().retriveComboBoxItem());
		crit.setLemma(getSearchTextField().getText());
		crit.setLexicon(getLexiconComboBox().retriveComboBoxItem());
		crit.setPartOfSpeech(getPartsOfSpeachComboBox().retriveComboBoxItem());
		crit.setDomain(getDomainComboBox().retriveComboBoxItem());
		crit.setRelation(getRelationsComboBox().retriveComboBoxItem());
		crit.setRegister(getRegisterComboBox().retriveComboBoxItem());
		crit.setComment(getComment().getText());
		crit.setExample(getExample().getText());
		crit.setGrammaticalGenderDictionary(getGrammaticalGenderDictionaryComboBox().retriveComboBoxItem());
		crit.setSourceDictionary(getSourceDictionaryComboBox().retriveComboBoxItem());
		crit.setStatusDictionary(getStatusDictionaryComboBox().retriveComboBoxItem());
		crit.setStyleDictionary(getStyleDictionaryComboBox().retriveComboBoxItem());
		crit.setAgeDictionary(getAgeDictionaryComboBox().retriveComboBoxItem());
		crit.setEthymology(getEntymologyTextField().getText());
		crit.setYiddishDomain(getDomainDictionaryComboBox().retriveComboBoxItem());
		crit.setLexicalCharacteristicDictionary(getLexicalChracteristicDictionaryComboBox().retriveComboBoxItem());
		crit.setDomainModifierDictionary(getDomainModifierComboBox().retriveComboBoxItem());

		return crit;
	}

	public void setSensesToHold(List<Sense> sense) {
		crit.setSense(new ArrayList<Sense>(sense));
	}

	@Override
	public void restoreCriteria(CriteriaDTO criteria) {
		getSearchTypeComboBox().setSelectedItem(criteria.getSearchType() != null ? criteria.getSearchType().name(): "", criteria.getSearchType());

		getSearchTextField().setText(criteria.getLemma());
		getComment().setText(criteria.getComment());
		getExample().setText(criteria.getExample());
		getEntymologyTextField().setText(criteria.getEthymology());
		
		getLexiconComboBox().setSelectedItem(criteria.getLexicon() != null ? criteria.getLexicon().getName().getText() : "", criteria.getLexicon());
		getPartsOfSpeachComboBox().setSelectedItem(criteria.getPartOfSpeech() != null ? criteria.getPartOfSpeech().getName().getText() : "", criteria.getPartOfSpeech());
		getDomainComboBox().setSelectedItem(criteria.getDomain() != null ? criteria.getDomain().toString() : "", criteria.getDomain());
		getRelationsComboBox().setSelectedItem(criteria.getRelation() != null ? criteria.getRelation().toString() : "", criteria.getRelation());
		getRegisterComboBox().setSelectedItem(criteria.getRegister() != null ? criteria.getRegister().getAbbreviation() : "", criteria.getRegister());
		getAgeDictionaryComboBox().setSelectedItem(criteria.getAgeDictionary() != null ? criteria.getAgeDictionary().getName() : "", criteria.getAgeDictionary());
		getSourceDictionaryComboBox().setSelectedItem(criteria.getSourceDictionary() != null ? criteria.getSourceDictionary().getName() : "", criteria.getSourceDictionary());
		getStatusDictionaryComboBox().setSelectedItem(criteria.getStatusDictionary() != null ? criteria.getStatusDictionary().getName() : "", criteria.getStatusDictionary());
		getStyleDictionaryComboBox().setSelectedItem(criteria.getStyleDictionary() != null ? criteria.getStyleDictionary().getName() : "", criteria.getStyleDictionary());
		getDomainDictionaryComboBox().setSelectedItem(criteria.getYiddishDomain() != null ? criteria.getYiddishDomain().getName() : "", criteria.getYiddishDomain());
		getGrammaticalGenderDictionaryComboBox().setSelectedItem(criteria.getGrammaticalGenderDictionary() != null ? criteria.getGrammaticalGenderDictionary().getName() : "", criteria.getGrammaticalGenderDictionary());
		getDomainModifierComboBox().setSelectedItem(criteria.getDomainModifierDictionary() != null ? criteria.getDomainModifierDictionary().getName() : "", criteria.getDomainModifierDictionary());
		getLexicalChracteristicDictionaryComboBox().setSelectedItem(criteria.getLexicalCharacteristicDictionary()!= null ? criteria.getLexicalCharacteristicDictionary().getName() : "", criteria.getLexicalCharacteristicDictionary());

		crit.setSense(criteria.getSense());
	}
}
