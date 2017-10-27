package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.CriteriaPanel;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.SynsetCriteria;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class SynsetViewUI extends AbstractViewUI implements ActionListener, ListSelectionListener, KeyListener {

    private SynsetCriteria criteria;

    private final MButton btnSearch = MButton.buildSearchButton()
            .withActionListener(this)
            .withMnemonic(KeyEvent.VK_K);

    private final MButton btnReset = MButton.buildCancelButton()
            .withCaption(Labels.CLEAR)
            .withActionListener(this)
            .withMnemonic(KeyEvent.VK_C);

    private ToolTipList synsetList;
    private JLabel infoLabel;
    private final GenericListModel<Sense> senseListModel = new GenericListModel<>(true);
    private Sense lastSelectedValue;

    @Override
    protected void initialize(JPanel content) {
        initilizeComponents();
        content.setLayout(new RiverLayout());

        int scrollHeight = 220;
        JScrollPane scroll = new JScrollPane(criteria);
        scroll.setMaximumSize(new Dimension(0, scrollHeight));
        scroll.setMinimumSize(new Dimension(0, scrollHeight));
        scroll.setPreferredSize(new Dimension(0, scrollHeight));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        content.add("hfill", scroll);
        content.add("br center", btnSearch);
        content.add("center", btnReset);
        content.add("br left", new LabelExt(Labels.SYNSETS_COLON, 'j', synsetList));
        content.add("br hfill vfill", new JScrollPane(synsetList));
        content.add("br left", infoLabel);
    }

    private void initilizeComponents() {
        criteria = new SynsetCriteria();
        criteria.getDomainComboBox().addActionListener(this);
        criteria.getPartsOfSpeachComboBox().addActionListener(this);
        synsetList = createSynsetList(senseListModel);

        infoLabel = new JLabel();
        infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "0"));
    }

    private ToolTipList createSynsetList(GenericListModel<Sense> model) {
        ToolTipList list = new ToolTipList(workbench, model, ToolTipGenerator.getGenerator());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.getSelectionModel().addListSelectionListener(this);
        return list;
    }

    public void refreshLexicons() {
        criteria.getLexiconComboBox().refreshLexicons();
    }

    public void refreshData() {

        int limitSize = criteria.getLimitResultCheckBox().isSelected() ? CriteriaPanel.MAX_ITEMS_COUNT : 0;
        String oldFilter = criteria.getSearchTextField().getText();
        Domain oldDomain = criteria.getDomainComboBox().retriveComboBoxItem();
        RelationType oldRelation = criteria.getSynsetRelationTypeComboBox().retriveComboBoxItem();
        String definition = criteria.getDefinition().getText();
        String comment = criteria.getComment().getText();
        String artificial = criteria.getIsArtificial();
        List<Long> lexicons = new ArrayList<>();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                workbench.setBusy(true);
                Lexicon lex = (Lexicon) criteria.getLexiconComboBox().retriveComboBoxItem();
                if (lex != null) {
                    lexicons.clear();
                    lexicons.add(lex.getId());
                } else {
                    lexicons.addAll(LexiconManager.getInstance().getLexicons());
                }
                List<Sense> sense = new ArrayList<>();
                sense = LexicalDA.getSenseBySynsets(oldFilter, oldDomain, oldRelation,
                        definition, comment, artificial, limitSize,
                        criteria.getPartsOfSpeachComboBox().retriveComboBoxItem() == null ? null : criteria.getPartsOfSpeachComboBox().retriveComboBoxItem(), lexicons);
                if (lastSelectedValue == null && synsetList != null && !synsetList.isSelectionEmpty()) {
                    lastSelectedValue = senseListModel.getObjectAt(synsetList.getSelectedIndex());
                }
                if (sense.isEmpty()) {
                    workbench.setBusy(false);
                }
                senseListModel.setCollectionToSynsets(sense, oldFilter);
                criteria.setSensesToHold(new ArrayList<>(senseListModel.getCollection()));
                return null;
            }

            @Override
            protected void done() {
                if (synsetList != null) {
                    synsetList.clearSelection();
                    if (senseListModel.getSize() != 0) {
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
    public void valueChanged(ListSelectionEvent event) {
        if (event != null && event.getValueIsAdjusting()) {
            return;
        }
        if (event == null) {
            return;
        }

        int returnValue = synsetList.getSelectedIndex();
        Sense unit = senseListModel.getObjectAt(returnValue);
        synsetList.setEnabled(false);
        listeners.notifyAllListeners(synsetList.getSelectedIndices().length == 1 ? unit : null);
        synsetList.setEnabled(true);

        SwingUtilities.invokeLater(() -> {
            synsetList.grabFocus();
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
        } else if (event.getSource() == btnReset) {
            criteria.resetFields();
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        super.keyPressed(event);
        if (!event.isConsumed() && event.getSource() == criteria.getSearchTextField() && event.getKeyChar() == KeyEvent.VK_ENTER) {
            event.consume();
            refreshData();
        }
    }
}
