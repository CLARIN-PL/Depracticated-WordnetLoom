package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.SynsetCriteria;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.SynsetFormat;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.SynsetTooltipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
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
    private LazyScrollList synsetList;
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

    private void setCountInfoText(int numAllObjects) {
        String infoText;
        if(numAllObjects > 0){
            infoText = String.format(Labels.VALUE_COUNT_SIMPLE, numAllObjects);
        } else {
            infoText = "";
        }
        infoLabel.setText(infoText);
    }

    @Override
    protected void initialize(WebPanel content) {
        initializeComponents();

        WebScrollPane scroll = new WebScrollPane(criteria);
        scroll.setMaximumSize(DEFAULT_SCROLL_DIMENSION);
        scroll.setMinimumSize(DEFAULT_SCROLL_DIMENSION);
        scroll.setPreferredSize(DEFAULT_SCROLL_DIMENSION);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        WebPanel top = new WebPanel();
        top.setLayout(new RiverLayout());
        top.setMargin(0);

        WebPanel bottom = new WebPanel();
        bottom.setLayout(new RiverLayout());
        bottom.setMargin(0);

        top.add("hfill vfill", scroll);
        top.add("br center", btnSearch);
        top.add("center", btnReset);

        bottom.add("br left", new MLabel(Labels.SYNSETS_COLON, 'j', synsetList));
//        bottom.add("br hfill vfill", scrollPane);
        bottom.add("br hfill vfill", synsetList);
        bottom.add("br left", infoLabel);

        MSplitPane split = new MSplitPane(0, top, bottom);
        split.setResizeWeight(0.0f);
        split.setMargin(0);

        content.setLayout(new RiverLayout(0,0));
        content.setMargin(0);
        content.add("hfill vfill", split);
    }

    private void initializeComponents() {
        criteria = new SynsetCriteria();
        criteria.getDomainComboBox().addActionListener(this);
        criteria.getPartsOfSpeechComboBox().addActionListener(this);

        WebList list = createSynsetList(synsetListModel);
        synsetList = new LazyScrollList(list, synsetListModel, new Synset(), LIMIT);
        synsetList.setScrollListener(((offset, limit) -> getSynsets(offset, limit)));

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

    public void refreshLexicons() {
        criteria.getLexiconComboBox().refreshLexicons();
    }

    public void refreshData() {
        new SynsetDownloaderWorker(true).execute();
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (event == null || event.getValueIsAdjusting()) {
            return;
        }
        Synset selectedSynset = (Synset) synsetList.getSelectedItem();
        listeners.notifyAllListeners(selectedSynset);
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

    private List<Synset> getSynsets(int offset, int limit) {
        SynsetCriteriaDTO synsetCriteriaDTO = criteria.getSynsetCriteria();
        synsetCriteriaDTO.setOffset(offset);
        synsetCriteriaDTO.setLimit(limit);
        lastCriteriaDTO = synsetCriteriaDTO;
        return RemoteService.synsetRemote.findSynsetsByCriteria(synsetCriteriaDTO);
    }

    private class SynsetDownloaderWorker extends SwingWorker<List<Synset>, Void> {

        private boolean isNewCriteria;

        SynsetDownloaderWorker(boolean isNewCriteria) {
            this.isNewCriteria = isNewCriteria;
        }

        @Override
        protected List<Synset> doInBackground() throws Exception {
            workbench.setBusy(true);
            if(isNewCriteria){
                resetSynsetList();
                lastCriteriaDTO = criteria.getSynsetCriteria();
                lastCriteriaDTO.setLimit(LIMIT);
                // get number of all synsets matched to criteria
                allMatchedSynsetCount = RemoteService.synsetRemote.getCountSynsetsByCriteria(lastCriteriaDTO);
            }
            lastCriteriaDTO.setOffset(synsetListModel.getSize());
            return loadAndAddSynsets(lastCriteriaDTO);
        }

        private void resetSynsetList() {
            synsetList.reset();
        }

        private List<Synset> loadAndAddSynsets(SynsetCriteriaDTO criteriaDTO) {
            lastCriteriaDTO = criteriaDTO;
            List<Synset> synsets = RemoteService.synsetRemote.findSynsetsByCriteria(criteriaDTO);
            int synsetsCount = RemoteService.synsetRemote.getCountSynsetsByCriteria(criteriaDTO);
            synsetList.setCollection(synsets, synsetsCount);
            return synsets;
        }

        @Override
        protected void done(){
            synsetList.updateUI();
            setCountInfoText(allMatchedSynsetCount);
            workbench.setBusy(false);
        }
    }

    private class SynsetListCellRenderer implements ListCellRenderer {

        private final String FONT_NAME = Font.SANS_SERIF;
        private final int FONT_SIZE = 14;
        private final Font FONT = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

        private WebLabel component;
        private Synset synset;

        public SynsetListCellRenderer() {
            component = new WebLabel();
            component.setFont(FONT);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            synset = (Synset) value;
            String text = SynsetFormat.getHtmlText(synset, synsetList.getWidth());
            component.setText(text);
            return component;
        }
    }
}
