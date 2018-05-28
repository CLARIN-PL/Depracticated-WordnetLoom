package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.google.common.eventbus.Subscribe;
import jiconfont.icons.FontAwesome;
import pl.edu.pwr.wordnetloom.client.Application;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.events.SearchUnitsEvent;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.NewLexicalUnitFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.SynsetsFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.SenseCriteria;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.events.UpdateGraphEvent;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.SenseFormat;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.common.Pair;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.SenseTooltipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.*;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.synset.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Lexical unit search panel
 */
public class LexicalUnitsViewUI extends AbstractViewUI implements
        ActionListener, ListSelectionListener, KeyListener, MouseListener {

    private final int LIMIT = 50;
    private LazyScrollPane unitsListScrollPane;
    private SenseCriteria criteria;
    private ToolTipList unitsList;
    private WebLabel infoLabel;
    private SenseCriteriaDTO lastSenseCriteria;
    private int allUnitsCount;
    private MButton btnSearch, btnReset, btnNew, btnDelete, btnNewWithSyns, btnAddToSyns;
    private DefaultListModel<Sense> listModel = new DefaultListModel<>();
    private Sense lastSelectedValue = null;

    private SenseCriteria initSenseCriteria() {
        SenseCriteria senseCriteria = new SenseCriteria();
        senseCriteria.getDomainComboBox().addActionListener(this);
        senseCriteria.getPartsOfSpeechComboBox().addActionListener(this);
        return senseCriteria;
    }

    @Override
    protected void initialize(WebPanel content) {

        WebPanel criteriaPanel = new WebPanel();
        criteriaPanel.setLayout(new RiverLayout());

        criteria = initSenseCriteria();

        btnSearch = MButton.buildSearchButton()
                .withActionListener(this);

        btnReset = MButton.buildClearButton()
                .withActionListener(this);

        infoLabel = new WebLabel();
        infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "0"));

        btnNewWithSyns = new MButton(this)
                .withIcon(FontAwesome.PLUS_SQUARE)
                .withToolTip(Hints.CREATE_NEW_UNIT_AND_SYNSET);


        btnNew = new MButton(this)
                .withIcon(FontAwesome.PLUS)
                .withToolTip(Hints.CREATE_NEW_UNIT);

        installViewScopeShortCut(btnNew, 0, KeyEvent.VK_INSERT);

        btnDelete = MButton.buildDeleteButton()
                .withActionListener(this)
                .withEnabled(false)
                .withToolTip(Hints.REMOVE_UNIT);

        installViewScopeShortCut(btnDelete, 0, KeyEvent.VK_DELETE);

        btnAddToSyns = new MButton(this)
                .withIcon(FontAwesome.SIGN_IN)
                .withToolTip(Hints.ADD_TO_NEW_SYNSET)
                .withEnabled(false);

        WebPanel buttons = new MComponentGroup(btnNewWithSyns, btnNew, btnDelete, btnAddToSyns)
                .withHorizontalLayout();

        WebScrollPane scroll = new WebScrollPane(criteria);
        scroll.setMaximumSize(DEFAULT_SCROLL_DIMENSION);
        scroll.setMinimumSize(DEFAULT_SCROLL_DIMENSION);
        scroll.setPreferredSize(DEFAULT_SCROLL_DIMENSION);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        criteriaPanel.setLayout(new RiverLayout());
        criteriaPanel.add("hfill vfill", scroll);
        criteriaPanel.add("br center", btnSearch);
        criteriaPanel.add("center", btnReset);

        unitsList = new ToolTipList(workbench, listModel, new SenseTooltipGenerator());
        unitsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        unitsList.getSelectionModel().addListSelectionListener(this);
        unitsList.setCellRenderer(new UnitListCellRenderer());

        unitsListScrollPane = new LazyScrollPane(unitsList,listModel, LIMIT);
        unitsListScrollPane.setScrollListener((offset, limit) -> loadMoreUnits());

        WebPanel resultListPanel = new WebPanel();
        resultListPanel.setLayout(new RiverLayout());
        resultListPanel.add("br left", new MLabel(Labels.LEXICAL_UNITS_COLON, 'j', unitsList));
        resultListPanel.add("br hfill vfill", unitsListScrollPane);
        resultListPanel.add("br left", infoLabel);

        resultListPanel.add("br center", buttons);

        MSplitPane split = new MSplitPane(0, criteriaPanel, resultListPanel);

        content.setLayout(new RiverLayout(0, 0));
        content.add("hfill vfill", split);

        Application.eventBus.register(this);
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (unitsList == null) {
            return;
        }
        if (event == null || event.getValueIsAdjusting()) {
            return;
        }

        int returnValue = unitsList.getSelectedIndex();
        if (returnValue < 0) {
            return;
        }
        Sense unit = listModel.get(returnValue);

        btnDelete.setEnabled(unit != null);
        btnAddToSyns.setEnabled(!checkInSynset(unit));

        unitsList.setEnabled(false);
//        listeners.notifyAllListeners(unitsList.getSelectedIndices().length == 1 ? unit : null);
        Sense sense = unitsList.getSelectedIndices().length == 1 ? unit : null;
        Application.eventBus.post(new UpdateGraphEvent(sense));
        unitsList.setEnabled(true);
        unitsList.grabFocus();

        SwingUtilities.invokeLater(() -> unitsList.grabFocus());
    }

    private boolean checkInSynset(Sense unit) {
        return unit != null && unit.getSynset() != null;
    }

    public void refreshLexiocn() {
        criteria.getLexiconComboBox().refreshLexicons();
    }

    /**
     * Metoda ładująca nową listę jednostek
     * 1. wyczyszczenie listy
     * 2. pobranie jednostek i umieszczenie ich na liście
     */
    public void loadUnits() {
        //wyczyszczenie listy jednostek
        clearUnitsList();

        SenseCriteriaDTO dto = criteria.getSenseCriteriaDTO();
        List<Sense> units = getSenses(dto, LIMIT, 0);
        allUnitsCount = RemoteService.senseRemote.getCountUnitsByCriteria(dto);
        setInfoText(units.size(), allUnitsCount);
        addUnitsToList(units);
//        unitsListScrollPane.setEnd(units.size() < LIMIT);
    }

    private void addUnitsToList(List<Sense> units) {
        for (Sense sense : units) {
            listModel.addElement(sense);
        }
    }

    private void clearUnitsList() {
        unitsListScrollPane.reset();
        listModel.clear();
    }

    public void setInfoText(int loadedUnits, int allUnitsCount) {
        String labelText;
        if (allUnitsCount != 0) {
            labelText = String.format(Labels.VALUE_COUNT_SIMPLE, loadedUnits + "/" + allUnitsCount);
        } else {
            labelText = "";
        }
        infoLabel.setText(labelText);
    }

    private List<Sense> getSenses(SenseCriteriaDTO dto, int limit, int offset) {
        SenseCriteriaDTO senseCriteriaDTO = dto;
        senseCriteriaDTO.setLimit(limit);
        senseCriteriaDTO.setOffset(offset);
        lastSenseCriteria = senseCriteriaDTO;

        return RemoteService.senseRemote.findByCriteria(senseCriteriaDTO);
    }

    /**
     * Ładuje kolejne jednostki do listy
     */
    public List<Sense> loadMoreUnits() {
        List<Sense> units = getSenses(lastSenseCriteria, lastSenseCriteria.getLimit(), unitsListScrollPane.getModelSize());
        setInfoText(unitsListScrollPane.getModelSize() + units.size(), allUnitsCount);
        return units;
    }

    /**
     * odświeżenie listy jednostek
     */
    public void refreshData(int limit, int offset) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                workbench.setBusy(true);

                if (offset == 0) {
                    clearUnitsList();
                }

                SenseCriteriaDTO dto = criteria.getSenseCriteriaDTO();
                dto.setLimit(limit);
                dto.setOffset(offset);

                List<Sense> units = RemoteService.senseRemote.findByCriteria(dto);

                // odczytanie zaznaczonej jednostki
                if (lastSelectedValue == null && unitsList != null
                        && !unitsList.isSelectionEmpty()) {
                    lastSelectedValue = listModel.get(unitsList.getSelectedIndex());
                }
                if (units.isEmpty()) {
                    workbench.setBusy(false);
                }
                criteria.setSensesToHold(units);
                return null;
            }

            @Override
            protected void done() {
                if (unitsList != null) {
                    SwingUtilities.invokeLater(() -> {
                        if (listModel.getSize() != 0) {
                            if (listModel.getSize() == unitsListScrollPane.getLimit()) {
                                unitsList.clearSelection();
                                unitsList.grabFocus();
                                unitsList.ensureIndexIsVisible(0);
                            } else {
                                unitsList.updateUI();
                            }
                            workbench.setBusy(false);
                        }
                        setInfoText(0, 0); //TODO to może będzie można wyrzucić
                    });
                }
                lastSelectedValue = null;
            }
        };
        worker.execute();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        boolean quietMode = false;
        if (quietMode) {
            return;
        }
        if (event.getSource() == btnSearch) {
            loadUnits();
        } else if (event.getSource() == btnReset) {
            criteria.resetFields();
        } else if (event.getSource() == btnDelete) {
            deleteSense();
        } else if (event.getSource() == btnAddToSyns) {
            addToSynset();
        } else if (event.getSource() == btnNew) {
            addNewSense();
        } else if (event.getSource() == btnNewWithSyns) {
            addNewSenseWithSynset();
        }
    }

    @Subscribe
    public void loadUnits(SearchUnitsEvent event){
        loadUnits();
        System.out.println(" Szukanie jednostki ");
    }

    private void deleteSense() {
        int[] returnValues = unitsList.getSelectedIndices();
        if (returnValues == null || returnValues.length == 0) {
            return;
        }

        if (returnValues.length == 1
                && DialogBox.showYesNoCancel(Messages.QUESTION_REMOVE_UNIT) != DialogBox.YES) {
            return;
        }
        if (returnValues.length != 1
                && DialogBox
                .showYesNoCancel(Messages.QUESTION_REMOVE_UNITS) != DialogBox.YES) {
            return;
        }

        // usuuniecie zaznaczonych jednostek
        for (int i : returnValues) {
//                Sense unit = listModel.getObjectAt(i);
            Sense unit = listModel.get(i);

            // TODOspradzenie czy ma jakies relacje
//            int result = DialogBox.YES;
//                if (RemoteUtils.lexicalRelationRemote
//                        .dbGetRelationCountOfUnit(unit) > 0) {
//                    result = DialogBox.showYesNoCancel(String.format(
//                            Messages.QUESTION_UNIT_HAS_RELATIONS,
//                            unit.toString()));
//                    if (result == DialogBox.CANCEL) {
//                        continue;
//                    }
//                }

//            if (result == DialogBox.YES) {


            ViWordNetService s = ServiceManager.getViWordNetService(workbench);
            s.getActiveGraphView().getUI().clear();
            listeners.notifyAllListeners(null);
            listModel.remove(i);
            RemoteService.senseRemote.delete(unit);
            allUnitsCount--; // jeżeli usunięto
            setInfoText(listModel.getSize(), allUnitsCount);
//            }
        }
    }

    private void addToSynset() {
        int i = unitsList.getSelectedIndex();
//        Sense unit = listModel.get(i);
//        //TODO dodać sprawdzanie, czy jednosta zawiera już synset
//        Synset savedSynset = createNewSynsetAndAddSense(unit);
//        unit.setSynset(savedSynset);
//        lastSelectedValue = null;
//        if (lastSelectedValue == null && unitsList != null
//                && !unitsList.isSelectionEmpty()) {
////                lastSelectedValue = listModel.getObjectAt(unitsList
////                        .getSelectedIndex());
//            lastSelectedValue = listModel.get(unitsList.getSelectedIndex());
//        }
//
//        // przywrocenie zaznaczenia
//        if (unitsList != null) {
//            SwingUtilities.invokeLater(() -> {
//                unitsList.clearSelection();
//                if (listModel.getSize() != 0) {
//                    unitsList.grabFocus();
//                    unitsList.clearSelection();
//                    unitsList.ensureIndexIsVisible(0);
//                }
//                infoLabel.setText(String.format(
//                        Labels.VALUE_COUNT_SIMPLE,
//                        "" + listModel.getSize()));
//            });
//        }

        Sense unit = listModel.get(i);
        assert unit.getSynset() == null;
        Synset synset = SynsetsFrame.showModal(workbench, unit);
        if (synset != null) { // unit was move to other synset
            listeners.notifyAllListeners(unit);
        }
    }

    private Synset createNewSynsetAndAddSense(Sense sense) {

        Synset synset = new Synset();
        synset.setLexicon(sense.getLexicon());
        synset.setSplit(1);

        return RemoteService.synsetRemote.addSenseToSynset(sense, synset);
    }

    private void addNewSense() {
        Pair<Sense, SenseAttributes> newUnit = NewLexicalUnitFrame.showModal(workbench, null);
        if (newUnit != null) {
            Sense savedUnit = save(newUnit);
            insertSenseToList(savedUnit);
        }
    }

    private Sense save(Pair<Sense, SenseAttributes> swa) {
        Sense savedUnit = RemoteService.senseRemote.save(swa.getA());
        RemoteService.senseRemote.addSenseAttribute(savedUnit.getId(), swa.getB());
        return savedUnit;
    }

    private void insertSenseToList(Sense sense) {
        if (sense == null) {
            return;
        }
        Sense fetchedSense = RemoteService.senseRemote.fetchSense(sense.getId());
        clearUnitsList();
        listModel.addElement(fetchedSense);
        valueChanged(new ListSelectionEvent(btnNew, 0, 0, false));
        lastSelectedValue = null;
    }

    private void addNewSenseWithSynset() {
        Pair<Sense, SenseAttributes> newUnit = NewLexicalUnitFrame.showModal(workbench, null);
        if (newUnit != null) {
            Sense savedUnit = save(newUnit);
            createNewSynsetAndAddSense(savedUnit);
            insertSenseToList(savedUnit);
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent event) {
        super.keyPressed(event);
        if (!event.isConsumed() && event.getSource() == criteria.getSearchTextField() && event.getKeyChar() == KeyEvent.VK_ENTER) {
            event.consume();
            loadUnits();
        }
    }

    @Override
    public JComponent getRootComponent() {
        return unitsList;
    }

    public Sense getSelectedUnit() {
        int returnValue = unitsList.getSelectedIndex();
        return listModel.get(returnValue);
    }

    public void setSelectedUnit(Sense unit) {
        lastSelectedValue = unit;
    }

    /**
     * Odczytanie aktualnie ustawionego filtra
     *
     * @return filter
     */
    public String getFilter() {
        return criteria.getSearchTextField() != null ? criteria.getSearchTextField().getText() : "";
    }

    /**
     * Ustawienie nowej wartosci dla filtra
     *
     * @param filter - nowa wartosc dla filtra
     */
    public void setFilter(String filter) {
        if (criteria.getSearchTextField() != null) {
            criteria.getSearchTextField().setText(filter);
        }
    }

    /**
     * Odczytanie aktualnie ustawionej dziedziny
     *
     * @return dziedzina
     */
    public Domain getDomain() {
        if (criteria.getSearchTextField() == null) {
            return null;
        }
        return criteria.getDomainComboBox().getEntity();
    }

    /**
     * Ustawienie nowej wartosci dla dziedziny
     *
     * @param domain - wartosc dziedziny
     */
    public void setDomain(Domain domain) {
    }

    /**
     * odrysowanie zawartości listy bez ponownego odczytu danych
     */
    public void redrawList() {
        unitsList.repaint();
    }

    public void reselectData() {
        valueChanged(new ListSelectionEvent(new Object(), 0, 0, false));
    }

    public CriteriaDTO getCriteria() {
        return criteria.getCriteria();
    }

    public void setCriteria(CriteriaDTO crit) {
        criteria.restoreCriteria(crit);
        if (crit != null && crit.getSense() != null) {
            addUnitsToList(crit.getSense());
        }
    }

    private class UnitListCellRenderer extends WebLabel implements ListCellRenderer {
        private final String FONT_NAME = "Courier New";
        private final int FONT_SIZE = 14;
        final Font FONT = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);

        UnitListCellRenderer() {
            setDrawShade(false);
            setFont(FONT);
            setMargin(1, 2, 1, 2);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(SenseFormat.getTextWithLexicon((Sense) value));
            return this;
        }
    }
}
