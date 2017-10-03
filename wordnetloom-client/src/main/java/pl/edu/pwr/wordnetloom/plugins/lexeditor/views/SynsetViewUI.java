package pl.edu.pwr.wordnetloom.plugins.lexeditor.views;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.model.RelationArgument;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.panel.CriteriaPanel;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.panel.SynsetCriteria;
import pl.edu.pwr.wordnetloom.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.systems.tooltips.ToolTipGenerator;
import se.datadosen.component.RiverLayout;

public class SynsetViewUI extends AbstractViewUI implements ActionListener, ListSelectionListener, KeyListener{

	private SynsetCriteria criteria;
	private ButtonExt btnSearch, btnReset;
	private ToolTipList synsetList;
	private JLabel infoLabel;
	private GenericListModel<Sense> senseListModel = new GenericListModel<Sense>(true);
	private Sense lastSelectedValue;

	@Override
	protected void initialize(JPanel content) {
		initilizeComponents();
		content.setLayout(new RiverLayout());

		final int scrollHeight = 220;
		JScrollPane scroll = new JScrollPane(criteria);
		scroll.setMaximumSize(new Dimension(0, scrollHeight));
		scroll.setMinimumSize(new Dimension(0, scrollHeight));
		scroll.setPreferredSize(new Dimension(0, scrollHeight));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		content.add("hfill", scroll);
		content.add("br center", btnSearch);
		content.add("center", btnReset);
		content.add("br left", new LabelExt(Labels.SYNSETS_COLON, 'j',synsetList));
		content.add("br hfill vfill", new JScrollPane(synsetList));
		content.add("br left", infoLabel);
	}

	private void initilizeComponents() {
		criteria = new SynsetCriteria(RelationArgument.SYNSET);
		criteria.getDomainComboBox().addActionListener(this);
		criteria.getPartsOfSpeachComboBox().addActionListener(this);
		btnSearch = new ButtonExt(Labels.SEARCH_NO_COLON, this, KeyEvent.VK_K);
		btnReset = new ButtonExt(Labels.CLEAR, this, KeyEvent.VK_C);
		synsetList = createSynsetList(senseListModel);
		infoLabel = new JLabel();
		infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "0"));
	}

	private ToolTipList createSynsetList(GenericListModel<Sense> model){
		ToolTipList list = new ToolTipList(workbench,model, ToolTipGenerator.getGenerator());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.getSelectionModel().addListSelectionListener(this);
		return list;
	}
	public void refreshLexicons(){
		criteria.getLexiconComboBox().refreshLexicons();
	}

	public void refreshData() {

		final int limitSize = criteria.getLimitResultCheckBox().isSelected() ? CriteriaPanel.MAX_ITEMS_COUNT : 0;
		final String oldFilter = criteria.getSearchTextField().getText();
		final Domain oldDomain = (Domain) criteria.getDomainComboBox().retriveComboBoxItem();
		final RelationType oldRelation =(RelationType) criteria.getRelationsComboBox().retriveComboBoxItem();
		final String definition = criteria.getDefinition().getText();
		final String comment = criteria.getComment().getText();
		final String artificial = criteria.getIsArtificial();
		final List<Long> lexicons = new ArrayList<Long>();

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				workbench.setBusy(true);
				Lexicon lex = (Lexicon) criteria.getLexiconComboBox().retriveComboBoxItem();
				if(lex != null){
					lexicons.clear();
					lexicons.add(lex.getId());
				} else{
					lexicons.addAll(LexiconManager.getInstance().getLexicons());
				}
				List<Sense> sense = new ArrayList<Sense>();
				sense = LexicalDA.getSenseBySynsets(oldFilter, oldDomain, oldRelation,
						definition,comment,artificial, limitSize,
						criteria.getPartsOfSpeachComboBox().retriveComboBoxItem() == null ? null : criteria.getPartsOfSpeachComboBox().retriveComboBoxItem().getUbyType(), lexicons);
				if (lastSelectedValue == null && synsetList != null && !synsetList.isSelectionEmpty()) {
					lastSelectedValue = senseListModel.getObjectAt(synsetList.getSelectedIndex());
				}
				if(sense.size() == 0){
					workbench.setBusy(false);
				}
				senseListModel.setCollectionToSynsets(sense, oldFilter);
				criteria.setSensesToHold(new ArrayList<Sense>(senseListModel.getCollection()));
				return null;
			}

			@Override
			protected void done() {
				if (synsetList != null) {
					synsetList.clearSelection();
					if(senseListModel.getSize()!=0){
						synsetList.grabFocus();
						synsetList.setSelectedIndex(0);
						synsetList.ensureIndexIsVisible(0);
					}
					infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "" + senseListModel.getSize()));
				}
				lastSelectedValue = null;
			}
		};
		worker.execute();
	}
	@Override
	public void valueChanged(ListSelectionEvent event){
		if (event != null && event.getValueIsAdjusting())
			return;
		if(event == null)
			return;
		
		final int returnValue = synsetList.getSelectedIndex();
		Sense unit = senseListModel.getObjectAt(returnValue);
		synsetList.setEnabled(false);
		listeners.notifyAllListeners(synsetList.getSelectedIndices().length == 1 ? unit : null);
		synsetList.setEnabled(true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				synsetList.grabFocus();
			}
		});
	}

	@Override
	public JComponent getRootComponent() {
		return synsetList;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == btnSearch) {
			refreshData();
			return;
		} else if (event.getSource() == btnReset) {
			criteria.resetFields();
			return;
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		super.keyPressed(event);
		if (!event.isConsumed() && event.getSource() ==  criteria.getSearchTextField() && event.getKeyChar() == KeyEvent.VK_ENTER) {
			event.consume();
			refreshData();
		}
	}
}
