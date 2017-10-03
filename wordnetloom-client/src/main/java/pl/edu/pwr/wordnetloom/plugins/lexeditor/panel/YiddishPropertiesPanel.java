package pl.edu.pwr.wordnetloom.plugins.lexeditor.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DialectalDictionary;
import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.model.yiddish.VariantType;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.AgeDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.GrammaticalGenderDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.LexicalCharacteristicDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StatusDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StyleDictionary;
import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.systems.ui.TextPanePlain;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;

public class YiddishPropertiesPanel extends JPanel implements CaretListener, ActionListener, Serializable {

	private static final long serialVersionUID = 2718809063521938318L;

	private TextFieldPlain latinSpelling;
	private TextFieldPlain yiddishSpelling;
	private TextFieldPlain yivoSpelling;
	private TranscriptionPanel transcriptionPanel;
	private DomainPanel domainPanel;
	private InflectionPanel inflectionPanel;
	private ParticlesPanel particlesPanel;
	private TextFieldPlain etymology;
	private TextFieldPlain etymologicalRoot;
	private SourcePanel sourcePanel;
	private ComboBoxPlain<AgeDictionary> age;
	private ComboBoxPlain<GrammaticalGenderDictionary> grammaticalGender;
	private ComboBoxPlain<StyleDictionary> style;
	private ComboBoxPlain<StatusDictionary> status;
	private ComboBoxPlain<LexicalCharacteristicDictionary> lexiaclCharacteristic;
	private JTextArea meaning;
	private TextPanePlain comment;
	private TextPanePlain context;

	private ComboBoxPlain<VariantType> varianType;
	private ComboBoxPlain<DialectalDictionary> dialectal;

	private final JButton btnNewVariantButton;

	private final YiddishSenseExtension yiddish;

	private LexicalUnitPropertiesPanel parent;

	private JButton btnRemoveVariant;


	public void loadData() {
		domainPanel.setDomains(yiddish.getYiddishDomains());
		transcriptionPanel.setTranscritptions(yiddish.getTranscriptions());
		particlesPanel.setParticles(yiddish.getParticels());
		inflectionPanel.setInflection(yiddish.getInflection());
		sourcePanel.setSource(yiddish.getSource());

		yiddishSpelling.setText(yiddish.getYiddishSpelling());
		yivoSpelling.setText(yiddish.getYivoSpelling());
		latinSpelling.setText(yiddish.getLatinSpelling());
		etymology.setText(yiddish.getEtymology());
		etymologicalRoot.setText(yiddish.getEtymologicalRoot());
		meaning.setText(yiddish.getMeaning());
		comment.setText(yiddish.getComment());
		context.setText(yiddish.getContext());

		age.setSelectedItem(yiddish.getAge() != null ? yiddish.getAge().getName() : "", yiddish.getAge());
		lexiaclCharacteristic.setSelectedItem(
				yiddish.getLexicalCharcteristic() != null ? yiddish.getLexicalCharcteristic().getName() : "",
				yiddish.getLexicalCharcteristic());
		status.setSelectedItem(yiddish.getStatus() != null ? yiddish.getStatus().getName() : "", yiddish.getStatus());
		style.setSelectedItem(yiddish.getStyle() != null ? yiddish.getStyle().getName() : "", yiddish.getStyle());
		grammaticalGender.setSelectedItem(
				yiddish.getGrammaticalGender() != null ? yiddish.getGrammaticalGender().getName() : "",
				yiddish.getGrammaticalGender());
		dialectal.setSelectedItem(
				yiddish.getDialectalDictionary() != null ? yiddish.getDialectalDictionary().getName() : "",
				yiddish.getDialectalDictionary());
		varianType.setSelectedItem(yiddish.getVariant() != null ? yiddish.getVariant().name() : "",
				yiddish.getVariant());

		if (VariantType.Yiddish_Primary_Lemma == yiddish.getVariant()) {
			varianType.setEnabled(false);
			btnNewVariantButton.setEnabled(true);
			btnRemoveVariant.setEnabled(false);
		}
	}

	private YiddishSenseExtension bindData() {
		yiddish.setLatinSpelling(latinSpelling.getText());
		yiddish.setYiddishSpelling(yiddishSpelling.getText());
		yiddish.setYivoSpelling(yivoSpelling.getText());
		yiddish.setGrammaticalGender(grammaticalGender.retriveComboBoxItem());
		yiddish.setAge(age.retriveComboBoxItem());
		yiddish.setStatus(status.retriveComboBoxItem());
		yiddish.setStyle(style.retriveComboBoxItem());
		yiddish.setEtymologicalRoot(etymologicalRoot.getText());
		yiddish.setLexicalCharcteristic(lexiaclCharacteristic.retriveComboBoxItem());
		yiddish.setMeaning(meaning.getText());
		yiddish.setComment(comment.getText());
		yiddish.setContext(context.getText());
		yiddish.setEtymology(etymology.getText());
		yiddish.setYiddishDomains(domainPanel.getDomains());
		yiddish.setInflection(inflectionPanel.getInflection());
		yiddish.setTranscriptions(transcriptionPanel.getTranscritptions());
		yiddish.setSource(new HashSet<>());
		yiddish.getSource().addAll(sourcePanel.getSource());
		yiddish.setVariant(varianType.retriveWihtoutNullComboBoxItem());
		yiddish.setDialectalDictionary(dialectal.retriveComboBoxItem());

		return yiddish;
	}

	public YiddishPropertiesPanel(final LexicalUnitPropertiesPanel parent, final YiddishSenseExtension extension) {
		this.parent = parent;
		this.yiddish = extension;

		setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						RowSpec.decode("max(25dlu;default)"), FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Variant", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, "2, 2, fill, fill");
		panel.setLayout(new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("max(47dlu;default)"), FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("max(94dlu;default):grow"), FormSpecs.RELATED_GAP_COLSPEC,
						FormSpecs.DEFAULT_COLSPEC, },
				new RowSpec[] { FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, }));

		JLabel lblNewLabel_4 = new JLabel("  Variant type:");
		panel.add(lblNewLabel_4, "1, 1, 5, 1, fill, default");

		varianType = new ComboBoxPlain(VariantType.values());

		varianType.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				VariantType t = ((CustomDescription<VariantType>) e.getItem()).getObject();
				if (t != null) {
					if (t == VariantType.Dialectal) {
						dialectal.setEnabled(true);
						varianType.setEnabled(true);
						btnNewVariantButton.setEnabled(false);
						btnRemoveVariant.setEnabled(true);
					} else {
						dialectal.setEnabled(false);
					}
					if (t == VariantType.Yiddish_Primary_Lemma) {
						varianType.setEnabled(false);
						btnNewVariantButton.setEnabled(true);
						btnRemoveVariant.setEnabled(false);
					}
					if (t == VariantType.Etymological) {
						varianType.setEnabled(true);
						btnNewVariantButton.setEnabled(false);
						btnRemoveVariant.setEnabled(true);
					}
					if (t == VariantType.Graphical) {
						varianType.setEnabled(true);
						btnNewVariantButton.setEnabled(false);
						btnRemoveVariant.setEnabled(true);
					}
					if (t == VariantType.Morphological) {
						varianType.setEnabled(true);
						btnNewVariantButton.setEnabled(false);
						btnRemoveVariant.setEnabled(true);
					}
					if (t == VariantType.Phonological) {
						varianType.setEnabled(true);
						btnNewVariantButton.setEnabled(false);
						btnRemoveVariant.setEnabled(true);
					}
				} else {
					btnNewVariantButton.setEnabled(true);
					btnRemoveVariant.setEnabled(false);
				}
			}
		});
		panel.add(varianType, "3, 1, fill, default");

		btnNewVariantButton = new JButton("New variant");
		btnNewVariantButton.addActionListener(this);
		panel.add(btnNewVariantButton, "5, 1");

		JLabel lblNewLabel_5 = new JLabel("Dialect:");
		panel.add(lblNewLabel_5, "1, 3, right, default");

		dialectal = new ComboBoxPlain(new CustomDescription<DialectalDictionary>("Select", null));
		dialectal.setEnabled(false);
		for (DialectalDictionary d : RemoteUtils.dictionaryRemote.findAllDialecticalDictionary()) {
			dialectal.addItem(d.getName(), d);
		}
		panel.add(dialectal, "3, 3, fill, default");

		btnRemoveVariant = new JButton("Remove variant");
		panel.add(btnRemoveVariant, "5, 3");
		btnRemoveVariant.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int result = DialogBox.showYesNoCancel(String.format("This Variant will be removed. Are you sure ?"));
				if (result == DialogBox.YES) {
					removeFromTab();
					parent.getSense().getYiddishSenseExtension().remove(yiddish); //usunięcie YiddishSenseExtension z modelu
					RemoteUtils.lexicalUnitRemote.dbRemoveYiddishSenseExtension(yiddish);
				}
			}
		});

		JPanel formPanel = new JPanel();
		formPanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Form", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(formPanel, "2, 4, fill, fill");
		formPanel.setLayout(new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("max(71dlu;default)"), FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), },
				new RowSpec[] { FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("max(40dlu;default):grow"), }));

		JLabel lblYivoSpelling = new JLabel("  YIVO spelling:");
		formPanel.add(lblYivoSpelling, "1, 1");

		JLabel lblLatinSpelling = new JLabel("  Latin spelling:");
		formPanel.add(lblLatinSpelling, "1, 3");

		yivoSpelling = new TextFieldPlain("");
		yivoSpelling.addCaretListener(this);
		formPanel.add(yivoSpelling, "3, 1, fill, default");
		yivoSpelling.setColumns(10);

		latinSpelling = new TextFieldPlain("");
		latinSpelling.addCaretListener(this);
		formPanel.add(latinSpelling, "3, 3, fill, default");
		latinSpelling.setColumns(10);

		JLabel lblJidyszSpelling = new JLabel("  Yiddish spelling:");
		formPanel.add(lblJidyszSpelling, "1, 5");

		yiddishSpelling = new TextFieldPlain("");
		yiddishSpelling.addCaretListener(this);
		formPanel.add(yiddishSpelling, "3, 5, fill, default");
		yiddishSpelling.setColumns(10);

		transcriptionPanel = new TranscriptionPanel();
		transcriptionPanel.getBtnNew().addActionListener(this);
		transcriptionPanel.getBtnRemove().addActionListener(this);
		formPanel.add(transcriptionPanel, "1, 7, 3, 1, fill, fill");

		domainPanel = new DomainPanel();
		formPanel.add(domainPanel, "1, 9, 3, 1, fill, fill");
		domainPanel.getBtnNewButton().addActionListener(this);
		domainPanel.getBtnRemove().addActionListener(this);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(
				new TitledBorder(null, "Meaning", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		formPanel.add(scrollPane, "1, 11, 3, 1, fill, fill");

		meaning = new JTextArea();
		meaning.setWrapStyleWord(true);
		meaning.setLineWrap(true);
		meaning.addCaretListener(this);
		scrollPane.setViewportView(meaning);

		JPanel grammarPanel = new JPanel();
		grammarPanel
				.setBorder(new TitledBorder(null, "Qualifiers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(grammarPanel, "2, 6, fill, fill");
		grammarPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(71dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(187dlu;default)"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(78dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(40dlu;default):grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));

		JLabel lblGrammaticalGender = new JLabel("  Grammatical gender:");
		grammarPanel.add(lblGrammaticalGender, "1, 3, left, default");

		grammaticalGender = new ComboBoxPlain<>(new CustomDescription<GrammaticalGenderDictionary>("Select", null));
		for (GrammaticalGenderDictionary d : RemoteUtils.dictionaryRemote.findAllGrammaticalGenderDictionary()) {
			grammaticalGender.addItem(d.getName(), d);
		}
		grammaticalGender.addActionListener(this);

		grammarPanel.add(grammaticalGender, "3, 3, fill, default");

		JLabel lblNewLabel_1 = new JLabel("  Style:");
		grammarPanel.add(lblNewLabel_1, "1, 5, left, default");

		style = new ComboBoxPlain<>(new CustomDescription<StyleDictionary>("General", null));
		for (StyleDictionary d : RemoteUtils.dictionaryRemote.findAllStyleDictionary()) {
			style.addItem(d.getName(), d);
		}
		style.addActionListener(this);
		grammarPanel.add(style, "3, 5, fill, default");

		JLabel lblLexicalCharacteristic = new JLabel("  Lexical Characteristic :");
		grammarPanel.add(lblLexicalCharacteristic, "1, 7, left, default");

		lexiaclCharacteristic = new ComboBoxPlain<>(
				new CustomDescription<LexicalCharacteristicDictionary>("Select", null));
		for (LexicalCharacteristicDictionary d : RemoteUtils.dictionaryRemote
				.findAllLexicalCharacteristicDictionary()) {
			lexiaclCharacteristic.addItem(d.getName(), d);
		}
		lexiaclCharacteristic.addActionListener(this);
		grammarPanel.add(lexiaclCharacteristic, "3, 7, fill, default");

		JLabel lblNewLabel_2 = new JLabel("  Status:");

		grammarPanel.add(lblNewLabel_2, "1, 9, left, default");

		status = new ComboBoxPlain<>(new CustomDescription<StatusDictionary>("Select", null));
		for (StatusDictionary d : RemoteUtils.dictionaryRemote.findAllStatusDictionary()) {
			status.addItem(d.getName(), d);
		}
		status.addActionListener(this);
		grammarPanel.add(status, "3, 9, fill, default");

		JLabel lblEtymology = new JLabel("  Etymology:");
		grammarPanel.add(lblEtymology, "1, 11, left, default");

		etymology = new TextFieldPlain("");
		etymology.addCaretListener(this);
		grammarPanel.add(etymology, "3, 11, default, fill");

		JLabel lblDate = new JLabel("  Age:");
		grammarPanel.add(lblDate, "1, 13, left, default");

		age = new ComboBoxPlain<>(new CustomDescription<AgeDictionary>("Select", null));
		for (AgeDictionary d : RemoteUtils.dictionaryRemote.findAllAgeDictionary()) {
			age.addItem(d.getName(), d);
		}
		age.addActionListener(this);
		grammarPanel.add(age, "3, 13, fill, default");

		sourcePanel = new SourcePanel();
		sourcePanel.getBtnNewButton().addActionListener(this);
		grammarPanel.add(sourcePanel, "1, 15, 3, 1, fill, default");

		inflectionPanel = new InflectionPanel();
		inflectionPanel.getBtnNewButton().addActionListener(this);
		inflectionPanel.getBtnRemove().addActionListener(this);
		grammarPanel.add(inflectionPanel, "1, 17, 3, 1, fill, default");

		JLabel etymologicalRootLbl = new JLabel(" Etymological root:");
		grammarPanel.add(etymologicalRootLbl, "1, 19, left, default");

		etymologicalRoot = new TextFieldPlain("");
		etymologicalRoot.addCaretListener(this);
		grammarPanel.add(etymologicalRoot, "3, 19, fill, default");
		etymologicalRoot.setColumns(10);

		JPanel commentPanel = new JPanel();
		commentPanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Comment",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		grammarPanel.add(commentPanel, "1, 21, 3, 1, default, fill");
		commentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("max(86dlu;default):grow"), FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), },
				new RowSpec[] { RowSpec.decode("max(40dlu;default):grow"), }));

		JScrollPane commentScrollPane = new JScrollPane();
		
		commentScrollPane.setViewportView(comment);
		commentPanel.add(commentScrollPane, "1, 1, 3, 1, default, fill");
		
		comment = new TextPanePlain();
		comment.addCaretListener(this);
		commentScrollPane.setViewportView(comment);

		JPanel contextPanel = new JPanel();
		contextPanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Context",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		grammarPanel.add(contextPanel, "1, 23, 3, 1, default, fill");
		contextPanel.setLayout(new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("max(86dlu;default):grow"), FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), },
				new RowSpec[] { RowSpec.decode("max(40dlu;default):grow"), }));

		JScrollPane contextScrollPane = new JScrollPane();
		contextPanel.add(contextScrollPane, "1, 1, 3, 1, default, fill");
		
		context = new TextPanePlain();
		context.addCaretListener(this);
		contextScrollPane.setViewportView(context);
		
		particlesPanel = new ParticlesPanel(extension);
		particlesPanel.getBtnNewButton().addActionListener(this);
		add(particlesPanel, "2, 8, fill, fill");

		loadData();
	}
	
	public void withoutPrimaryLemma() {
		varianType.removeItem(VariantType.Yiddish_Primary_Lemma);
	}

	private void removeFromTab() {
		parent.removeTab(this);
	}

	@Override
	public void caretUpdate(CaretEvent event) {
		if (event.getSource() instanceof TextFieldPlain) {
			TextFieldPlain field = (TextFieldPlain) event.getSource();
			parent.getBtnSave().setEnabled(parent.getBtnSave().isEnabled() | field.wasTextChanged());
		}
		if (event.getSource() instanceof TextPanePlain) {
			TextPanePlain field = (TextPanePlain) event.getSource();
			parent.getBtnSave().setEnabled(parent.getBtnSave().isEnabled() | field.wasTextChanged());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComboBox) {
			parent.getBtnSave().setEnabled(true);
		}
		if (e.getSource() == btnNewVariantButton) {
			if (VariantType.Yiddish_Primary_Lemma == yiddish.getVariant()) {
				bindData();
				YiddishSenseExtension yse = new YiddishSenseExtension(yiddish);
				yse.setVariant(VariantType.Phonological);
				yse = RemoteUtils.lexicalUnitRemote.save(yse);
				parent.addTab(yse);
			}
			parent.getBtnSave().setEnabled(true);
		}
		if (e.getSource() instanceof JButton) {
			parent.getBtnSave().setEnabled(true);
		}

	}

	public void save() {
		YiddishSenseExtension yse = bindData();
		yse = RemoteUtils.lexicalUnitRemote.save(yse);
		if (yiddish.getId() == null) {
			yiddish.setId(yse.getId());
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((varianType == null) ? 0 : varianType.hashCode());
		result = prime * result + ((yiddish == null) ? 0 : yiddish.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		YiddishPropertiesPanel other = (YiddishPropertiesPanel) obj;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (varianType == null) {
			if (other.varianType != null)
				return false;
		} else if (!varianType.equals(other.varianType))
			return false;
		if (yiddish == null) {
			if (other.yiddish != null)
				return false;
		} else if (!yiddish.equals(other.yiddish))
			return false;
		return true;
	}

}
