package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames;

import com.alee.laf.rootpane.WebFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.SynsetCriteria;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.renderers.SynsetListCellRenderer;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.SynsetTooltipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.DialogWindow;
import pl.edu.pwr.wordnetloom.client.systems.ui.LazyScrollPane;
import pl.edu.pwr.wordnetloom.client.systems.ui.MButton;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SynsetsFrame extends DialogWindow implements ActionListener {

    private static Sense sense;
    private final SynsetCriteria criteriaPanel;
    private final LazyScrollPane synsetsScrollPane;
    private final ToolTipList synsetsList;

    //TODO zrobić listę synsetów jako odzielny obiekt
    private final DefaultListModel<Synset> listModel;
    private final MButton searchButton;
    private final MButton addToNewSynsetButton;
    private final MButton addToSelectedButton;
    private final MButton cancelButton;
    private Synset selectedSynset;


    public SynsetsFrame(Workbench workbench, WebFrame webFrame, Sense sense) {
        super(webFrame, "Wybieranie synsetu", 600, 500); //TODO dorobić etykietę
        this.sense = sense;

        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        criteriaPanel = new SynsetCriteria();

        criteriaPanel.getDomainComboBox().addActionListener(this); //TODO sprawdzić, czy ten listener nie może zostać przeniesiony niżej
        criteriaPanel.getPartsOfSpeechComboBox().addActionListener(this);

        listModel = new DefaultListModel<>();
        synsetsList = new ToolTipList(workbench, listModel, new SynsetTooltipGenerator());
        synsetsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        synsetsScrollPane = new LazyScrollPane(synsetsList, 15); //TODO dorobić limit jako stałą, albo zmienić
        synsetsList.setCellRenderer(new SynsetListCellRenderer(synsetsScrollPane));

        addToNewSynsetButton = MButton.buildOkButton()
                .withCaption("Dodaj do nowego synsetu")
                .withActionListener(this);
        addToSelectedButton = MButton.buildOkButton()
                .withCaption("Dodaj do wybranego synsetu")
                .withActionListener(this);
        cancelButton = MButton.buildCancelButton()
                .withActionListener(this);

        searchButton = MButton.buildSearchButton()
                .withActionListener(this);

        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane criteriaScrollPane = new JScrollPane(criteriaPanel);
        criteriaScrollPane.setPreferredSize(new Dimension(250, 0));
        panel.add(criteriaScrollPane, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.SOUTH);
        add(panel, BorderLayout.WEST);
        add(synsetsScrollPane, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(addToSelectedButton);
        buttonsPanel.add(addToNewSynsetButton);
        add(buttonsPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public static Synset showModal(Workbench workbench, Sense sense) {
        SynsetsFrame modalFrame = new SynsetsFrame(workbench, workbench.getFrame(), sense);
        return modalFrame.selectedSynset;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addToNewSynsetButton) {
            addSenseToNewSynset();
        } else if (e.getSource() == addToSelectedButton) {
            addSenseToSelectedSynset();
        } else if (e.getSource() == cancelButton) {
            setVisible(false);
        } else if (e.getSource() == searchButton) {
            search();
        }
    }

    private void addSenseToNewSynset() {
        assert sense != null;
        Synset synset = new Synset();
        selectedSynset = RemoteService.synsetRemote.updateSynset(synset);
        RemoteService.synsetRemote.addSenseToSynset(sense, selectedSynset);
        setVisible(false);
    }

    private void addSenseToSelectedSynset() {
        int selectedIndex = synsetsList.getSelectedIndex();
        selectedSynset = listModel.getElementAt(selectedIndex);
        RemoteService.synsetRemote.addSenseToSynset(sense, selectedSynset);

        setVisible(false);
    }

    private void search() {
        //TODO przenieść częśc wspólną z SynsetViewUI
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                listModel.clear();
                SynsetCriteriaDTO dto = criteriaPanel.getSynsetCriteria();
                java.util.List<Synset> synsets = RemoteService.synsetRemote.findSynsetsByCriteria(dto);
                for (Synset synset : synsets) {
                    listModel.addElement(synset);
                }
                synsetsList.updateUI();
                return null;
            }
        };
        worker.execute();
    }
}
