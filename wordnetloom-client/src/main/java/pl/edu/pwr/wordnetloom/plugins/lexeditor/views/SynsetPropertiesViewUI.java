package pl.edu.pwr.wordnetloom.plugins.lexeditor.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.systems.ui.ComboBoxPlain;
import pl.edu.pwr.wordnetloom.systems.ui.TextAreaPlain;
import pl.edu.pwr.wordnetloom.utils.Common;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.model.Synset;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.systems.enums.WorkState;
import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.systems.ui.TextFieldPlain;
import pl.edu.pwr.wordnetloom.utils.Hints;
import pl.edu.pwr.wordnetloom.utils.Messages;
import se.datadosen.component.RiverLayout;


/**
 * klasa opisujacy wyglada okienka z wlasciwoscami synsetu
 * @author Max
 */
public class SynsetPropertiesViewUI extends AbstractViewUI implements ActionListener,CaretListener {


	private static final String SUPER_MODE_VALUE = "1";
	private static final String SUPER_MODE = "SuperMode";

	private TextAreaPlain definitionValue;
	private TextAreaPlain commentValue;
	private ComboBoxPlain statusValue;
	private ButtonExt buttonSave;
	private JCheckBox abstractValue;

	private Synset lastSynset=null;
	private boolean quiteMode=false;
	private final ViwnGraphViewUI graphUI;
	
	public SynsetPropertiesViewUI(ViwnGraphViewUI graphUI){
		this.graphUI = graphUI;
	}

	/*
	 *  (non-Javadoc)
	 * @see pl.wroc.pwr.ci.plwordnet.workbench.implementation.AbstractViewUI#initialize(javax.swing.JPanel)
	 */
	@Override
	protected void initialize(JPanel content) {
		// ustawienie layoutu
		content.setLayout(new RiverLayout());


		definitionValue=new TextAreaPlain(Labels.VALUE_UNKNOWN);
		definitionValue.addCaretListener(this);
		definitionValue.setRows(3);

		statusValue=new ComboBoxPlain(WorkState.values(), null);
		statusValue.addActionListener(this);

		commentValue=new TextAreaPlain(Labels.VALUE_UNKNOWN);
		commentValue.addCaretListener(this);
		commentValue.setRows(3);

		buttonSave=new ButtonExt(Labels.SAVE,this);
		buttonSave.setEnabled(false);
		buttonSave.setToolTipText(Hints.SAVE_CHANGES_IN_SYNSET);

		abstractValue=new JCheckBox(Labels.ARTIFICIAL);
		abstractValue.setSelected(false);
		abstractValue.addActionListener(this);
		


		content.add("vtop",new JLabel(Labels.DEFINITION_COLON));
		content.add("tab hfill",new JScrollPane(definitionValue));
		content.add("br vtop",new JLabel(Labels.COMMENT_COLON));
		content.add("tab hfill",new JScrollPane(commentValue));
		content.add("br",abstractValue);
		content.add("br center",buttonSave);

		// ustawienie akywnosci
		statusValue.setEnabled(false); // czy combo jest aktywne
		commentValue.setEnabled(false);// czy dostepny jest komentarz
		definitionValue.setEnabled(false);// czy definicja jest dostepna
		abstractValue.setEnabled(false); // czy abstrakcyjny ma byc dostepny
	}

	/*
	 *  (non-Javadoc)
	 * @see AbstractViewUI#getRootComponent()
	 */
	@Override
	public JComponent getRootComponent() {
		return definitionValue;
	}

	/**
	 * odswiezenie informacji o synsecie
	 * @param synset - synset
	 */
	public void refreshData(Synset synset) {
		if (synset!=null)
			synset = LexicalDA.refresh(synset);
		lastSynset=synset;
		quiteMode=true;

		// ustawienie wartosci elementow
		definitionValue.setText(synset!=null?formatValue(Common.getSynsetAttribute(synset, Synset.DEFINITION)):formatValue(null));
		statusValue.setSelectedItem(synset == null ? null : "");
		commentValue.setText(synset!=null?formatValue(Common.getSynsetAttribute(synset, Synset.COMMENT)):formatValue(null));
		abstractValue.setSelected(synset!=null && Synset.isAbstract(Common.getSynsetAttribute(synset, Synset.ISABSTRACT)));
		statusValue.setEnabled(synset!=null); // czy combo jest aktywne
		commentValue.setEnabled(synset!=null); // czy komentarz mozna dodac
		definitionValue.setEnabled(synset!=null);// czy definicja jest dostepna
		abstractValue.setEnabled(synset!=null);// czy abstrakcyjny jest dostpeny
		buttonSave.setEnabled(false);
		quiteMode=false;
	}

	/**
	 * formatowanie wartości, tak aby nie bylo nulla
	 * @param value - wartość wejsciowa
	 * @return wartośc wjesciowa lub "brak danych" gdy był null
	 */
	private static String formatValue(String value) {
		return (value==null || value.length()==0)?Labels.VALUE_UNKNOWN:value;
	}

	/**
	 * została wywołana jakaś akcja
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (quiteMode==true || lastSynset==null) return; // nie ma co aktualizować

		// zmiana zaznaczenia w checkboxie
		if (arg0.getSource()==abstractValue) {
			buttonSave.setEnabled(true);

		// proba zmiany statusu
		} else if (arg0.getSource()==statusValue) {
			if (statusValue.getSelectedIndex()>2 && (workbench.getParam(SUPER_MODE)==null || !workbench.getParam(SUPER_MODE).equals(SUPER_MODE_VALUE))) {
				DialogBox.showError(Messages.ERROR_CANNOT_CHANGE_STATUS_RESERVED_FOR_ADMIN);
				statusValue.setSelectedItem("");
			} else {
				buttonSave.setEnabled(true);
			}

		// zapisanie zmian
		} else if (arg0.getSource()==buttonSave) { // zapisanie zmian
			String definition=definitionValue.getText();
			int statusIndex=statusValue.getSelectedIndex();
			String comment=commentValue.getText();
			boolean isAbstract=abstractValue.isSelected();

			if (!LexicalDA.updateSynset(lastSynset,definition,statusIndex,comment,isAbstract)) {
				refreshData(lastSynset); // nieudana zmiana statusu
				DialogBox.showError(Messages.ERROR_NO_STATUS_CHANGE_BECAUSE_OF_RELATIONS_IN_SYNSETS);
			}

			buttonSave.setEnabled(false);
			if (definitionValue.isEnabled()) { // zwrocenie focusu
				definitionValue.grabFocus();
			} else {
				commentValue.grabFocus();
			}
			// poinformowanie o zmianie parametrow
			listeners.notifyAllListeners(lastSynset);
			
			ViwnNode node = graphUI.getSelectedNode();
			if(node != null && node instanceof ViwnNodeSynset){
				ViwnNodeSynset s = (ViwnNodeSynset) node;
				s.setLabel(null);
				graphUI.graphChanged();
			} else {
				// dodano nowy synset, nie istnieje on nigdzie w grafie
				graphUI.graphChanged();
			}

		}
	}

	/**
	 * zmienilo sie cos w polach
	 */
	public void caretUpdate(CaretEvent arg0) {
		if (quiteMode==true || lastSynset==null) return; // nie ma co aktualizować

		if (arg0.getSource() instanceof TextFieldPlain) {
			TextFieldPlain field = (TextFieldPlain) arg0.getSource();
			buttonSave.setEnabled(buttonSave.isEnabled() | field.wasTextChanged());
		}
		if (arg0.getSource() instanceof TextAreaPlain) {
			TextAreaPlain field = (TextAreaPlain) arg0.getSource();
			buttonSave.setEnabled(buttonSave.isEnabled() | field.wasTextChanged());
		}
	}
}
