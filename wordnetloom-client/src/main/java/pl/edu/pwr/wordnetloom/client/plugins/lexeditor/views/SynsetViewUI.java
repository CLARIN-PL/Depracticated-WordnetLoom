package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.panel.WebPanel;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.CriteriaPanel;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.SynsetCriteria;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.systems.ui.MLabel;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetCriteriaDTO;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
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
//    private final GenericListModel<Sense> senseListModel = new GenericListModel<>(true);
    private DefaultListModel<Synset> synsetListModel = new DefaultListModel<>();
    private Sense lastSelectedValue;

    private class SynsetListCellRenderer extends JLabel implements ListCellRenderer {
        private final Font listFont = new Font("Courier New", Font.PLAIN, 14); //TODO można zrobić klasę nadrzędną, która będzie przechowywała części wspólne Z UnitsListCellRenderer
        private final String DESCRIPTION_FORMAT = "%s %s(%s) %s";

        private final static String HTML_HEADER = "<html><body style=\"font-weight:normal\">";
        private final static String HTML_FOOTER = "</body></html>";
        protected final static String DESC_STR = "<div style=\"text-align:left; margin-left:10px; width:250px;\">%s</div>";

        private String name;
        private String variant;
        private String domain;
        private String lexicon;

        private int width = 250;

        private StringBuilder synsetTextBuilder;

        public SynsetListCellRenderer(){
            synsetTextBuilder = new StringBuilder();

        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            this.setFont(listFont);
            synsetTextBuilder.setLength(0);
            Synset synset = (Synset) value;
            Sense sense;
            //TODO zrobić auto rozmiar
            synsetTextBuilder.append("<div style=\"text-align:left; margin-left:10px; width:"+synsetList.getWidth()+"px;\">");
            for(int i=0; i<synset.getSenses().size(); i++){
                sense = synset.getSenses().get(i);
                if(i == 0){ // head synset
                    synsetTextBuilder.append("<font color=\"blue\">");
                    synsetTextBuilder.append(getSynsetDescritpion(sense));
                    synsetTextBuilder.append("</font>");
                }
                else{
                    synsetTextBuilder.append(getSynsetDescritpion(sense));
                }

                if(i != synset.getSenses().size() -1) {
                    synsetTextBuilder.append(" | ");
                } else {
                    synsetTextBuilder.append(" ");
                }
            }
            synsetTextBuilder.append("</div>");
            setText(HTML_HEADER + synsetTextBuilder.toString() + HTML_FOOTER);
            return this;
        }



        private String getSynsetDescritpion(Sense sense) {
            name = sense.getWord().getWord();
            variant = String.valueOf(sense.getVariant());
            domain = LocalisationManager.getInstance().getLocalisedString(sense.getDomain().getName());
            lexicon = sense.getLexicon().getIdentifier();

            return String.format(DESCRIPTION_FORMAT, name, variant, domain, lexicon);
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
        content.add("br hfill vfill", new JScrollPane(synsetList));
        content.add("br left", infoLabel);
    }

    private void initilizeComponents() {
        criteria = new SynsetCriteria();
        criteria.getDomainComboBox().addActionListener(this);
        criteria.getPartsOfSpeechComboBox().addActionListener(this);
//        synsetList = createSynsetList(senseListModel);
        synsetList = createSynsetList(synsetListModel);
        synsetList.setCellRenderer(new SynsetListCellRenderer());
        //TODO można ustawić minimalny rozmiar listy

        infoLabel = new JLabel();
        infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "0"));
    }

//    private ToolTipList createSynsetList(GenericListModel<Sense> model) {
//        ToolTipList list = new ToolTipList(workbench, model, ToolTipGenerator.getGenerator());
//        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        list.getSelectionModel().addListSelectionListener(this);
//        return list;
//    }

    private ToolTipList createSynsetList(DefaultListModel model) {
        ToolTipList list = new ToolTipList(workbench, model, ToolTipGenerator.getGenerator());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.getSelectionModel().addListSelectionListener(this);
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
                Lexicon lex = criteria.getLexiconComboBox().getEntity();
                if (lex != null) {
                    lexicons.clear();
                    lexicons.add(lex.getId());
                } else {
                    lexicons.addAll(LexiconManager.getInstance().getUserChosenLexiconsIds());
                }

                SynsetCriteriaDTO dto = criteria.getSynsetCriteria();
                List<Synset> synsets = RemoteService.synsetRemote.findSynsetsByCriteria(dto);
//                List<Sense> sense = new ArrayList<>();
//                sense = LexicalDA.getSenseBySynsets(oldFilter, oldDomain, oldRelation,
//                        definition, comment, artificial, limitSize,
//                        criteria.getPartsOfSpeechComboBox().getEntity() == null ? null : criteria.getPartsOfSpeechComboBox().getEntity(), lexicons);

//                if (lastSelectedValue == null && synsetList != null && !synsetList.isSelectionEmpty()) {
//                    lastSelectedValue = senseListModel.getObjectAt(synsetList.getSelectedIndex());
//                }
                if(synsets.isEmpty()){
                    workbench.setBusy(false);
                }
                //TODO zrobić dodawnie synsetów do listy i ustalić sposób wyświetlania
//                senseListModel.setCollectionToSynsets(sense, oldFilter);
                for(Synset synset: synsets) {
                    synsetListModel.addElement(synset);
                }
//                criteria.setSensesToHold(new ArrayList<>(senseListModel.getCollection()));
                return null;
            }

            @Override
            protected void done() {
                workbench.setBusy(false);
                synsetList.updateUI();
                System.out.println("Koniec pobierania synsetów");
//                if (synsetList != null) {
//                    synsetList.clearSelection();
//                    if (senseListModel.getSize() != 0) {
//                        synsetList.grabFocus();
//                        synsetList.setSelectedIndex(0);
//                        synsetList.ensureIndexIsVisible(0);
//                    }
//                    infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "" + senseListModel.getSize()));
//                }
//                lastSelectedValue = null;
            }
        };
        worker.execute();
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        //TODO odkomencić to
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
}
