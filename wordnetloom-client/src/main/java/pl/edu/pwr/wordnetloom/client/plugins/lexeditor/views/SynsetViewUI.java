package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.panel.WebPanel;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.SynsetCriteria;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.SynsetFormat;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.SynsetTooltipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.LazyScrollPane;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SynsetViewUI extends AbstractViewUI implements ActionListener, ListSelectionListener, KeyListener {

    private final int LIMIT = 50;
    private final MButton btnSearch = MButton.buildSearchButton()
            .withActionListener(this)
            .withMnemonic(KeyEvent.VK_K);
    private final MButton btnReset = MButton.buildCancelButton()
            .withCaption(Labels.CLEAR)
            .withActionListener(this)
            .withMnemonic(KeyEvent.VK_C);
    private SynsetCriteria criteria;
    private SynsetCriteriaDTO lastCriteriaDTO;
    private ToolTipList synsetList;
    private LazyScrollPane scrollPane;
    private JLabel infoLabel;
    private int numMatchedSynsets = -1;
    private DefaultListModel<Synset> synsetListModel = new DefaultListModel<>();
    private Sense lastSelectedValue;

    private void setInfoLabelText(int numLoadObjects, int numAllObjects) {
        final String INFO_LABEL_FORMAT = "%s %d/%d";
        if (numAllObjects > 0) {
            infoLabel.setText(String.format(INFO_LABEL_FORMAT, Labels.NUMBER_COLON, numLoadObjects, numAllObjects));
        } else {
            infoLabel.setText("");
        }
    }

    @Override
    protected void initialize(WebPanel content) {
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
        content.add("br left", new MLabel(Labels.SYNSETS_COLON, 'j', synsetList));
        content.add("br hfill vfill", scrollPane);
        content.add("br left", infoLabel);
    }

    private void initilizeComponents() {
        criteria = new SynsetCriteria();
        criteria.getDomainComboBox().addActionListener(this);
        criteria.getPartsOfSpeechComboBox().addActionListener(this);
        synsetList = createSynsetList(synsetListModel);
        scrollPane = new LazyScrollPane(synsetList, LIMIT);
        scrollPane.setHorizontalScrolling(false);
        scrollPane.setScrollListener((offset, limit) -> loadMoreSynsets());

        infoLabel = new JLabel();
        infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "0"));
    }

    private ToolTipList createSynsetList(DefaultListModel model) {
        ToolTipList list = new ToolTipList(workbench, model, new SynsetTooltipGenerator());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.getSelectionModel().addListSelectionListener(this);
        list.setCellRenderer(new SynsetListCellRenderer());
        return list;
    }

    public void refreshLexicons() {
        criteria.getLexiconComboBox().refreshLexicons();
    }

    public void refreshData() {
        List<Long> lexicons = new ArrayList<>();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                workbench.setBusy(true);
                synsetListModel.clear();
                scrollPane.reset();
                SynsetCriteriaDTO dto = criteria.getSynsetCriteria();
                dto.setLimit(LIMIT);
                dto.setOffset(synsetListModel.getSize());
                numMatchedSynsets = RemoteService.synsetRemote.getCountSynsetsByCriteria(dto);
                lastCriteriaDTO = dto;
                loadAndAddSynsets(dto);
                if (!synsetListModel.isEmpty()) {
                    scrollPane.setEnd(false);
                }
                return null;
            }

            @Override
            protected void done() {
                workbench.setBusy(false);
                synsetList.updateUI();
            }
        };
        worker.execute();
    }

    private void loadAndAddSynsets(SynsetCriteriaDTO criteriaDTO) {
        lastCriteriaDTO = criteriaDTO;
        List<Synset> synsets = RemoteService.synsetRemote.findSynsetsByCriteria(criteriaDTO);
        for (Synset synset : synsets) {
            synsetListModel.addElement(synset);
        }
        if (!synsets.isEmpty()) {
            synsetList.updateUI();
        }
        setInfoLabelText(synsetListModel.getSize(), numMatchedSynsets); //TODO wstawić tutaj liczbę wszystkich synsetów
    }

    public void loadMoreSynsets() {
        //TODO zrobić pobieranie synsetów i dodawanie
        lastCriteriaDTO.setOffset(synsetListModel.getSize());
        loadAndAddSynsets(lastCriteriaDTO);
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        //TODO odkomencić to
        System.out.println("kliknięto synset");
        if (event == null || event.getValueIsAdjusting()) {
            return;
        }
        int selectedIndex = synsetList.getSelectedIndex();
        Synset synset = synsetListModel.getElementAt(selectedIndex);
        listeners.notifyAllListeners(synsetList.getSelectedIndices().length == 1 ? synset : null);
//        if (event != null && event.getValueIsAdjusting()) {
//            return;
//        }
//        if (event == null) {
//            return;
//        }
//
//        int returnValue = synsetList.getSelectedIndex();
//        Sense unit = senseListModel.getObjectAt(returnValue);
//        synsetList.setEnabled(false);
//        listeners.notifyAllListeners(synsetList.getSelectedIndices().length == 1 ? unit : null);
//        synsetList.setEnabled(true);
//
//        SwingUtilities.invokeLater(() -> {
//            synsetList.grabFocus();
//        });
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

    private class SynsetListCellRenderer extends JLabel implements ListCellRenderer {
        private final String FONT_NAME = "Courier New";
        private final int FONT_SIZE = 14;
        private final Font FONT = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

        SynsetListCellRenderer() {
            this.setFont(FONT);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(SynsetFormat.getHtmlText((Synset) value, scrollPane.getWidth()));
            return this;
        }
    }
}
