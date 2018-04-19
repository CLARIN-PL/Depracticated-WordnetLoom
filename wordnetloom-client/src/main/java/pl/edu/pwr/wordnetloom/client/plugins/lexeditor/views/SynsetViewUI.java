package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.google.common.eventbus.Subscribe;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.SynsetCriteria;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.event.SearchRandomSynsetEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.SynsetFormat;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.SynsetTooltipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.LazyScrollPane;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
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
    private WebLabel infoLabel;
    private int allMatchedSynsetCount = -1;
    private DefaultListModel<Synset> synsetListModel = new DefaultListModel<>();

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
        Application.eventBus.register(this);
        initializeComponents();
        content.setLayout(new RiverLayout());

        int scrollHeight = 220;
        WebScrollPane scroll = new WebScrollPane(criteria);
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

    private void initializeComponents() {
        criteria = new SynsetCriteria();
        criteria.getDomainComboBox().addActionListener(this);
        criteria.getPartsOfSpeechComboBox().addActionListener(this);
        synsetList = createSynsetList(synsetListModel);
        scrollPane = new LazyScrollPane(synsetList, LIMIT);
        scrollPane.setHorizontalScrolling(false);
        scrollPane.setScrollListener((offset, limit) -> loadMoreSynsets());

        infoLabel = new WebLabel();
        infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "0"));
    }

    private ToolTipList createSynsetList(DefaultListModel model) {
        ToolTipList list = new ToolTipList(workbench, model, new SynsetTooltipGenerator());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.getSelectionModel().addListSelectionListener(this);
        list.setCellRenderer(new SynsetListCellRenderer());
        return list;
    }

    @Subscribe
    public void onSearchRandomSynset(SearchRandomSynsetEvent event){
        new SynsetDownloaderWorker(true, true).execute();
    }

    public void refreshLexicons() {
        criteria.getLexiconComboBox().refreshLexicons();
    }

    public void refreshData() {
        new SynsetDownloaderWorker(true, false).execute();
    }

    public void loadMoreSynsets() {
        new SynsetDownloaderWorker(false, false).execute();
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (event == null || event.getValueIsAdjusting()) {
            return;
        }
        int selectedIndex = synsetList.getSelectedIndex();
        if(isCorrectSelectedIndex(selectedIndex)){
            Synset synset = synsetListModel.getElementAt(selectedIndex);
            listeners.notifyAllListeners(synsetList.getSelectedIndices().length == 1 ? synset : null);
        }
    }

    private boolean isCorrectSelectedIndex(int selectedIndex) {
        return selectedIndex >= 0 && selectedIndex < synsetListModel.size();
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

    private class SynsetDownloaderWorker extends SwingWorker<Void, Void> {

        private boolean isNewCriteria;
        private boolean randomSearch;

        SynsetDownloaderWorker(boolean isNewCriteria, boolean randomSearch) {
            this.isNewCriteria = isNewCriteria;
            this.randomSearch = randomSearch;
        }

        @Override
        protected Void doInBackground() throws Exception {
            workbench.setBusy(true);

            if(isNewCriteria){
                resetSynsetList();
                lastCriteriaDTO = criteria.getSynsetCriteria();
                lastCriteriaDTO.setLimit(LIMIT);
                // get number of all synsets matched to criteria
                if(randomSearch){
                    lastCriteriaDTO.setLimit(1);
                    allMatchedSynsetCount = RemoteService.synsetRemote.getCountSynsetRandomByCriteria(lastCriteriaDTO);
                }else {
                    allMatchedSynsetCount = RemoteService.synsetRemote.getCountSynsetsByCriteria(lastCriteriaDTO);
                }
            }
            lastCriteriaDTO.setOffset(synsetListModel.getSize());
            loadAndAddSynsets(lastCriteriaDTO);
            return null;
        }

        private void resetSynsetList() {
            synsetListModel.clear();
            scrollPane.reset();
        }

        private void loadAndAddSynsets(SynsetCriteriaDTO criteriaDTO) {
            lastCriteriaDTO = criteriaDTO;
            List<Synset> synsets;
            if(randomSearch){
               synsets = RemoteService.synsetRemote.findSynsetRandomByCriteria(criteriaDTO);
            } else {
               synsets = RemoteService.synsetRemote.findSynsetsByCriteria(criteriaDTO);
            }
            addElementsToList(synsets);
            scrollPane.setEnd(synsets.isEmpty() || synsets.size() < criteriaDTO.getLimit());
            if(randomSearch){
                scrollPane.hideLoadMoreButton();
                synsetList.setSelectedIndex(0);
            }
        }

        private void addElementsToList(List<Synset> synsets) {
            for (Synset synset : synsets) {
                synsetListModel.addElement(synset);
            }
        }

        @Override
        protected void done(){
            synsetList.updateUI();
            setInfoLabelText(synsetListModel.getSize(), allMatchedSynsetCount);
            workbench.setBusy(false);
        }
    }

    private class SynsetListCellRenderer extends WebLabel implements ListCellRenderer {
        private final String FONT_NAME = "Courier New";
        private final int FONT_SIZE = 14;
        private final Font FONT = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

        SynsetListCellRenderer() {
            this.setDrawShade(false);
            this.setFont(FONT);
            this.setMargin(1,2,1,2);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(SynsetFormat.getHtmlText((Synset) value, scrollPane.getWidth()));
            return this;
        }
    }
}
