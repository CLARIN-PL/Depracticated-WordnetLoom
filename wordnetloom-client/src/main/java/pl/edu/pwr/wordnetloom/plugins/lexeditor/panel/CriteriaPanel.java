package pl.edu.pwr.wordnetloom.plugins.lexeditor.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.edu.pwr.wordnetloom.dto.SearchType;
import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.RelationArgument;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainModifierDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SourceDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StatusDictionary;
import pl.edu.pwr.wordnetloom.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.AgeDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.GrammaticalGenderDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.LexicalCharacteristicDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StyleDictionary;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.systems.ui.DomainComboBox;
import pl.edu.pwr.wordnetloom.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.systems.ui.LexiconComboBox;
import pl.edu.pwr.wordnetloom.systems.ui.PartOfSpeechComboBox;
import pl.edu.pwr.wordnetloom.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;
import se.datadosen.component.RiverLayout;

/**
 * @author tnaskret
 *
 */
public abstract class CriteriaPanel extends JPanel {

	private static final long serialVersionUID = 4649824763750406980L;
	public static final String STANDARD_VALUE_FILTER = "";
	private int SCROLL_PANE_HEIGHT = 500;
	public static final int MAX_ITEMS_COUNT = 500;

	private ComboBoxPlain<SearchType> searchTypeComboBox;

	private ComboBoxPlain<StatusDictionary> statusDictionaryComboBox;
	private ComboBoxPlain<StyleDictionary> styleDictionaryComboBox;
	private ComboBoxPlain<DomainDictionary> domainDictionaryComboBox;
	private ComboBoxPlain<SourceDictionary> sourceDictionaryComboBox;
	private ComboBoxPlain<AgeDictionary> ageDictionaryComboBox;
	private ComboBoxPlain<GrammaticalGenderDictionary> grammaticalGenderDictionaryComboBox;
	private ComboBoxPlain<DomainModifierDictionary> domainModifierComboBox;
	private ComboBoxPlain<LexicalCharacteristicDictionary> lexicalChracteristicDictionaryComboBox;
	private JTextField entymologyTextField;

	private JTextField searchTextField;
	private LexiconComboBox lexiconComboBox;
	private DomainComboBox domainComboBox;
	private PartOfSpeechComboBox partsOfSpeachComboBox;
	private ComboBoxPlain<RelationType> relationsComboBox;
	private JCheckBox limitResultCheckBox;
	private RelationArgument relationArgument;

	public CriteriaPanel(RelationArgument relationArgumentType, int scrollHeight) {
		this.SCROLL_PANE_HEIGHT = scrollHeight;
		this.relationArgument = relationArgumentType;
		initialize();
	}

	private void initialize() {
		setLayout(new RiverLayout());
		setMaximumSize(new Dimension(0, SCROLL_PANE_HEIGHT));
		setMinimumSize(new Dimension(0, SCROLL_PANE_HEIGHT));
		setPreferredSize(new Dimension(0, SCROLL_PANE_HEIGHT));

		searchTypeComboBox = createSearchTypeComboBox();

		lexiconComboBox = new LexiconComboBox(Labels.VALUE_ALL);
		lexiconComboBox.setPreferredSize(new Dimension(150, 20));
		lexiconComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Lexicon lex = lexiconComboBox.retriveComboBoxItem();
				if (lex != null) {
					domainComboBox.filterDomainsByLexicon(lex, true);
				} else {
					domainComboBox.allDomains(true);
				}
				refreshRelations();
			}
		});

		searchTextField = new TextFieldPlain(STANDARD_VALUE_FILTER);

		partsOfSpeachComboBox = new PartOfSpeechComboBox(Labels.VALUE_ALL);
		partsOfSpeachComboBox.showUbyItems();
		partsOfSpeachComboBox.setPreferredSize(new Dimension(150, 20));
		partsOfSpeachComboBox.addItemListener(e -> {
            PartOfSpeech pos = partsOfSpeachComboBox.retriveComboBoxItem();
            Lexicon lex = lexiconComboBox.retriveComboBoxItem();
            if (pos != null && lex != null) {
                domainComboBox.filterDomainByUbyPosAndLexcion(pos, lex, true);
            } else if (lex != null && pos == null) {
                domainComboBox.filterDomainsByLexicon(lex, true);
            } else if (pos != null && lex == null) {
                domainComboBox.filterDomainByUbyPos(pos, true);
            } else {
                domainComboBox.allDomains(true);
            }
        });

		domainComboBox = new DomainComboBox(Labels.VALUE_ALL);
		domainComboBox.allDomains(true);
		domainComboBox.setPreferredSize(new Dimension(150, 20));

		relationsComboBox = createRelationsComboBox();
		relationsComboBox.setPreferredSize(new Dimension(150, 20));
		limitResultCheckBox = createLimitResultSearch();

		statusDictionaryComboBox = createStatusDictionaryComboBox();
		styleDictionaryComboBox = createStyleDictionaryComboBox();
		ageDictionaryComboBox = createAgeDictionaryComboBox();
		grammaticalGenderDictionaryComboBox = createGrammaticalGenderDictionaryComboBox();
		domainDictionaryComboBox = createDomainDictionaryComboBox();
		sourceDictionaryComboBox = createSourceDictionaryComboBox();
		lexicalChracteristicDictionaryComboBox = createLexicalCharacteristicDictionaryComboBox();
		domainModifierComboBox = createDomainModifierDictionaryComboBox();

		entymologyTextField = new TextFieldPlain(STANDARD_VALUE_FILTER);
	}

	private ComboBoxPlain<GrammaticalGenderDictionary> createGrammaticalGenderDictionaryComboBox() {
		ComboBoxPlain<GrammaticalGenderDictionary> combo = new ComboBoxPlain<>(true);
		for (GrammaticalGenderDictionary d : RemoteUtils.dictionaryRemote.findAllGrammaticalGenderDictionary()) {
			combo.addItem(d.getName(), d);
		}
		combo.setPreferredSize(new Dimension(150, 20));
		return combo;
	}

	private ComboBoxPlain<AgeDictionary> createAgeDictionaryComboBox() {
		ComboBoxPlain<AgeDictionary> combo = new ComboBoxPlain<>(true);
		for (AgeDictionary d : RemoteUtils.dictionaryRemote.findAllAgeDictionary()) {
			combo.addItem(d.getName(), d);
		}
		combo.setPreferredSize(new Dimension(150, 20));
		return combo;
	}

	private ComboBoxPlain<StatusDictionary> createStatusDictionaryComboBox() {
		ComboBoxPlain<StatusDictionary> combo = new ComboBoxPlain<>(true);
		for (StatusDictionary d : RemoteUtils.dictionaryRemote.findAllStatusDictionary()) {
			combo.addItem(d.getName(), d);
		}
		combo.setPreferredSize(new Dimension(150, 20));
		return combo;
	}

	private ComboBoxPlain<LexicalCharacteristicDictionary> createLexicalCharacteristicDictionaryComboBox() {
		ComboBoxPlain<LexicalCharacteristicDictionary> combo = new ComboBoxPlain<>(true);

		for (LexicalCharacteristicDictionary d : RemoteUtils.dictionaryRemote
				.findAllLexicalCharacteristicDictionary()) {
			combo.addItem(d.getName(), d);
		}
		combo.setPreferredSize(new Dimension(150, 20));
		return combo;
	}

	private ComboBoxPlain<DomainModifierDictionary> createDomainModifierDictionaryComboBox() {
		ComboBoxPlain<DomainModifierDictionary> combo = new ComboBoxPlain<>(true);
		for (DomainModifierDictionary d : RemoteUtils.dictionaryRemote.findAllDomainModifiersDictionary()) {
			combo.addItem(d.getName(), d);
		}
		combo.setPreferredSize(new Dimension(150, 20));
		return combo;
	}

	private ComboBoxPlain<SourceDictionary> createSourceDictionaryComboBox() {
		ComboBoxPlain<SourceDictionary> combo = new ComboBoxPlain<>(true);
		for (SourceDictionary d : RemoteUtils.dictionaryRemote.findAllSourceDictionary()) {
			combo.addItem(d.getName(), d);
		}
		combo.setPreferredSize(new Dimension(150, 20));
		return combo;
	}

	private ComboBoxPlain<DomainDictionary> createDomainDictionaryComboBox() {
		ComboBoxPlain<DomainDictionary> combo = new ComboBoxPlain<>(true);
		for (DomainDictionary d : RemoteUtils.dictionaryRemote.findAllDomainDictionary()) {
			combo.addItem(d.getName(), d);
		}
		combo.setPreferredSize(new Dimension(150, 20));
		return combo;
	}

	private ComboBoxPlain<StyleDictionary> createStyleDictionaryComboBox() {
		ComboBoxPlain<StyleDictionary> combo = new ComboBoxPlain<>(true);
		for (StyleDictionary d : RemoteUtils.dictionaryRemote.findAllStyleDictionary()) {
			combo.addItem(d.getName(), d);
		}
		combo.setPreferredSize(new Dimension(150, 20));
		return combo;
	}

	protected abstract void initializeFormPanel();

	public abstract CriteriaDTO getCriteria();

	public abstract void restoreCriteria(CriteriaDTO criteria);

	void addLimit() {
		add("br left", limitResultCheckBox);
	}

	protected void addRelations() {
		add("br", new LabelExt(Labels.RELATIONS_COLON, 'r', relationsComboBox));
		add("br hfill", relationsComboBox);
		refreshRelations();
	}

	protected void addDomain() {
		add("br", new LabelExt(Labels.DOMAIN_COLON, 'd', domainComboBox));
		add("br hfill", domainComboBox);
	}

	protected void addPartsOfSpeach() {
		add("br", new LabelExt(Labels.PARTS_OF_SPEECH_COLON, 'm', partsOfSpeachComboBox));
		add("br hfill", partsOfSpeachComboBox);
	}

	protected void addLexicon() {
		add("br", new LabelExt(Labels.LEXICON_COLON, 'l', lexiconComboBox));
		add("br hfill", lexiconComboBox);
	}

	protected void addSearch() {
		add("br", new LabelExt(Labels.SEARCH_COLON, 'w', searchTextField));
		add("br hfill", searchTextField);
	}

	protected void addSearchType() {
		add("", new LabelExt("Search type:", 's', searchTypeComboBox));
		add("br hfill", searchTypeComboBox);
	}

	protected void addStatusDic() {
		add("br", new LabelExt("Status:", 'u', statusDictionaryComboBox));
		add("br hfill", statusDictionaryComboBox);
	}

	protected void addDomainDic() {
		add("br", new LabelExt("Semantic Field:", 'o', domainDictionaryComboBox));
		add("br hfill", domainDictionaryComboBox);
	}

	protected void addDomainModifDic() {
		add("br", new LabelExt("S. Field Modifier:", 'g', domainModifierComboBox));
		add("br hfill", domainModifierComboBox);
	}

	protected void addLexicalCharacteristicDic() {
		add("br", new LabelExt("Lexical Characteristic:", 'h', lexicalChracteristicDictionaryComboBox));
		add("br hfill", lexicalChracteristicDictionaryComboBox);
	}

	protected void addSourceDic() {
		add("br", new LabelExt("Source:", 'u', sourceDictionaryComboBox));
		add("br hfill", sourceDictionaryComboBox);
	}

	protected void addStyleDic() {
		add("br", new LabelExt("Style:", 'y', styleDictionaryComboBox));
		add("br hfill", styleDictionaryComboBox);
	}

	protected void addAgeDic() {
		add("br", new LabelExt("Age:", 'a', ageDictionaryComboBox));
		add("br hfill", ageDictionaryComboBox);
	}

	protected void addGrammaticalGenderDic() {
		add("br", new LabelExt("Grammatical gender:", 'g', grammaticalGenderDictionaryComboBox));
		add("br hfill", grammaticalGenderDictionaryComboBox);
	}

	protected void addEthymology() {
		add("br", new LabelExt("Etymology:", 'e', entymologyTextField));
		add("br hfill", entymologyTextField);
	}

	private ComboBoxPlain<RelationType> createRelationsComboBox() {
		ComboBoxPlain<RelationType> combo = new ComboBoxPlain<>();
		combo.setPreferredSize(new Dimension(150, 20));
		return combo;
	}

	private ComboBoxPlain<SearchType> createSearchTypeComboBox() {
		ComboBoxPlain<SearchType> combo = new ComboBoxPlain<>(new CustomDescription<SearchType>("Lemma", null));
		combo.addItem(SearchType.Yiddish_Spelling.name(), SearchType.Yiddish_Spelling);
		combo.addItem(SearchType.YIVO_Spelling.name(), SearchType.YIVO_Spelling);
		combo.addItem(SearchType.Latin_Philological_Transcription.name(),
				SearchType.Latin_Philological_Transcription);
		combo.addItem(SearchType.Latin_YIVO_Transcription.name(), SearchType.Latin_YIVO_Transcription);
		combo.addItem(SearchType.Phonetic_Transcription.name(), SearchType.Phonetic_Transcription);
		combo.addItem(SearchType.Etymological_root.name(), SearchType.Etymological_root);
		combo.addItem(SearchType.Prefix.name(), SearchType.Prefix);
		combo.addItem(SearchType.Interfix.name(), SearchType.Interfix);
		combo.addItem(SearchType.Suffix.name(), SearchType.Suffix);
		combo.addItem(SearchType.Root.name(), SearchType.Root);
		combo.addItem(SearchType.Sense_number.name(), SearchType.Sense_number);
		combo.setPreferredSize(new Dimension(150, 20));
		return combo;
	}

	public JTextField getEntymologyTextField() {
		return entymologyTextField;
	}

	private JCheckBox createLimitResultSearch() {
		JCheckBox limitResult = new JCheckBox(String.format(Labels.LIMIT_TO, "" + MAX_ITEMS_COUNT));
		limitResult.setSelected(true);
		return limitResult;
	}

	public void refreshPartOfSpeech() {
		int selected = partsOfSpeachComboBox.getSelectedIndex();
		if (selected != -1)
			partsOfSpeachComboBox.setSelectedIndex(selected);
	}

	public void refreshDomain() {
		int selected = domainComboBox.getSelectedIndex();
		if (selected != -1)
			domainComboBox.setSelectedIndex(selected);
	}

	public void refreshRelations() {
		RelationTypes.refresh();
		List<RelationType> relations = RemoteUtils.relationTypeRemote.dbGetLeafs(relationArgument,
				LexiconManager.getInstance().getLexicons());
		int selected = relationsComboBox.getSelectedIndex();

		relationsComboBox.removeAllItems();
		relationsComboBox.addItem(Labels.VALUE_ALL, null);

		if (lexiconComboBox.retriveComboBoxItem() != null) {
			for (RelationType relation : relations) {
				if (relation.getLexicon().getId() == lexiconComboBox.retriveComboBoxItem().getId()) {
					RelationType currentRelation = RelationTypes.get(relation.getId()).getRelationType();
					relationsComboBox.addItem(RelationTypes.getFullNameFor(currentRelation.getId()), currentRelation);
				}
			}
		} else {
			for (RelationType relation : relations) {
				RelationType currentRelation = RelationTypes.get(relation.getId()).getRelationType();
				relationsComboBox.addItem(RelationTypes.getFullNameFor(currentRelation.getId()), currentRelation);
			}
		}

		if (selected != -1)
			relationsComboBox.setSelectedIndex(selected);
	}

	public ComboBoxPlain<StatusDictionary> getStatusDictionaryComboBox() {
		return statusDictionaryComboBox;
	}

	public ComboBoxPlain<StyleDictionary> getStyleDictionaryComboBox() {
		return styleDictionaryComboBox;
	}

	public ComboBoxPlain<DomainDictionary> getDomainDictionaryComboBox() {
		return domainDictionaryComboBox;
	}

	public ComboBoxPlain<SourceDictionary> getSourceDictionaryComboBox() {
		return sourceDictionaryComboBox;
	}

	public ComboBoxPlain<AgeDictionary> getAgeDictionaryComboBox() {
		return ageDictionaryComboBox;
	}

	public ComboBoxPlain<GrammaticalGenderDictionary> getGrammaticalGenderDictionaryComboBox() {
		return grammaticalGenderDictionaryComboBox;
	}

	public ComboBoxPlain<DomainModifierDictionary> getDomainModifierComboBox() {
		return domainModifierComboBox;
	}

	public ComboBoxPlain<LexicalCharacteristicDictionary> getLexicalChracteristicDictionaryComboBox() {
		return lexicalChracteristicDictionaryComboBox;
	}

	public RelationArgument getRelationArgument() {
		return relationArgument;
	}

	public ComboBoxPlain<SearchType> getSearchTypeComboBox() {
		return searchTypeComboBox;
	}

	public ComboBoxPlain<RelationType> getRelationsComboBox() {
		return relationsComboBox;
	}

	public void resetFields() {
		searchTextField.setText("");
		searchTypeComboBox.setSelectedIndex(0);
		domainComboBox.setSelectedIndex(0);
		partsOfSpeachComboBox.setSelectedIndex(0);
		relationsComboBox.setSelectedIndex(0);
		lexiconComboBox.setSelectedIndex(0);
		domainDictionaryComboBox.setSelectedIndex(0);
		statusDictionaryComboBox.setSelectedIndex(0);
		sourceDictionaryComboBox.setSelectedIndex(0);
		ageDictionaryComboBox.setSelectedIndex(0);
		grammaticalGenderDictionaryComboBox.setSelectedIndex(0);
		styleDictionaryComboBox.setSelectedIndex(0);
		lexicalChracteristicDictionaryComboBox.setSelectedIndex(0);
		domainModifierComboBox.setSelectedIndex(0);
	}

	public JTextField getSearchTextField() {
		return searchTextField;
	}

	public ComboBoxPlain<Domain> getDomainComboBox() {
		return domainComboBox;
	}

	public JCheckBox getLimitResultCheckBox() {
		return limitResultCheckBox;
	}

	public LexiconComboBox getLexiconComboBox() {
		return lexiconComboBox;
	}

	public PartOfSpeechComboBox getPartsOfSpeachComboBox() {
		return partsOfSpeachComboBox;
	}

}
