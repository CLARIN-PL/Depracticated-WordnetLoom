package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
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
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.LexicalIM;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.NewLexicalUnitFrame;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.CriteriaDTO;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.CriteriaPanel;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.SenseCriteria;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.client.utils.Hints;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.model.wordnet.Domain;
import pl.edu.pwr.wordnetloom.model.wordnet.Lexicon;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationArgument;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import se.datadosen.component.RiverLayout;

/**
 * klasa opisujacy wyglada okienka z jedn. leksykalnymi
 *
 * @author Max
 */
public class LexicalUnitsViewUI extends AbstractViewUI implements
        ActionListener, ListSelectionListener, KeyListener, MouseListener {

    private static final String SUPER_MODE_VALUE = "1";
    private static final String SUPER_MODE = "SuperMode";

    private SenseCriteria criteria;
    private ToolTipList unitsList;
    private JLabel infoLabel;
    private ButtonExt btnSearch, btnReset, btnNew, btnDelete, btnNewWithSyns, btnAddToSyns;

    private final boolean quietMode = false;

    private final GenericListModel<Sense> listModel = new GenericListModel<>();
    private Sense lastSelectedValue = null;

    @Override
    protected void initialize(JPanel content) {
        // ustawienie layoutu
        content.setLayout(new RiverLayout());
        criteria = new SenseCriteria(RelationArgument.LEXICAL);
        criteria.getDomainComboBox().addActionListener(this);
        criteria.getPartsOfSpeachComboBox().addActionListener(this);

        btnSearch = new ButtonExt(Labels.SEARCH_NO_COLON, this, KeyEvent.VK_K);
        btnReset = new ButtonExt(Labels.CLEAR, this, KeyEvent.VK_C);
        unitsList = new ToolTipList(workbench, listModel, ToolTipGenerator.getGenerator());
        unitsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        unitsList.getSelectionModel().addListSelectionListener(this);

        infoLabel = new JLabel();
        infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "0"));

        btnNewWithSyns = new ButtonExt(LexicalIM.getNew(), this);
        btnNewWithSyns.setToolTipText(Hints.CREATE_NEW_UNIT_AND_SYNSET);

        btnNew = new ButtonExt(LexicalIM.getNew2(), this);
        btnNew.setToolTipText(Hints.CREATE_NEW_UNIT);
        installViewScopeShortCut(btnNew, 0, KeyEvent.VK_INSERT);

        btnDelete = new ButtonExt(LexicalIM.getDelete(), this);
        btnDelete.setEnabled(false);
        btnDelete.setToolTipText(Hints.REMOVE_UNIT);
        installViewScopeShortCut(btnDelete, 0, KeyEvent.VK_DELETE);

        btnAddToSyns = new ButtonExt(LexicalIM.getNew(), this);
        btnAddToSyns.setEnabled(false);
        btnAddToSyns.setToolTipText(Hints.ADD_TO_NEW_SYNSET);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(btnNewWithSyns);
        buttons.add(btnNew);
        buttons.add(btnDelete);
        buttons.add(btnAddToSyns);

        final int scrollHeight = 220;
        JScrollPane scroll = new JScrollPane(criteria);
        scroll.setMaximumSize(new Dimension(0, scrollHeight));
        scroll.setMinimumSize(new Dimension(0, scrollHeight));
        scroll.setPreferredSize(new Dimension(0, scrollHeight));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        content.setLayout(new RiverLayout());
        content.add("hfill", scroll);
        content.add("br center", btnSearch);
        content.add("center", btnReset);
        content.add("br left", new LabelExt(Labels.LEXICAL_UNITS_COLON, 'j', unitsList));
        content.add("br hfill vfill", new JScrollPane(unitsList));
        content.add("br left", infoLabel);

        content.add("br center", buttons);

    }

    /**
     * zaznaczenie w tabeli zostalo zmienione
     *
     * @param event
     */
    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (unitsList == null) {
            return;
        }
        if (event != null && event.getValueIsAdjusting()) {
            return;
        }
        if (event == null) {
            return;
        }

        final int returnValue = unitsList.getSelectedIndex();
        Sense unit = listModel.getObjectAt(returnValue);
        boolean superMode = workbench.getParam(SUPER_MODE) != null
                && workbench.getParam(SUPER_MODE).equals(SUPER_MODE_VALUE);
        btnDelete.setEnabled(unit != null ? superMode : false);
        btnAddToSyns.setEnabled(unit != null ? (superMode)
                && !LexicalDA.checkIfInAnySynset(unit) : false);

        // powiadomienie zainteresowanych
        unitsList.setEnabled(false);
        listeners.notifyAllListeners(unitsList.getSelectedIndices().length == 1 ? unit : null);
        unitsList.setEnabled(true);
        unitsList.grabFocus();

        SwingUtilities.invokeLater(() -> {
            unitsList.grabFocus();
        });
    }

    public void refreshLexiocn() {
        criteria.getLexiconComboBox().refreshLexicons();
    }

    /**
     * odświeżenie listy jednostek
     */
    public void refreshData() {

        final int limitSize = criteria.getLimitResultCheckBox().isSelected() ? CriteriaPanel.MAX_ITEMS_COUNT : 0;
        final String oldFilter = criteria.getSearchTextField().getText();
        final Domain oldDomain = (Domain) criteria.getDomainComboBox().retriveComboBoxItem();
        final RelationType oldRelation = (RelationType) criteria.getRelationsComboBox().retriveComboBoxItem();
        final String register = criteria.getRegisterComboBox().getSelectedIndex() == 0 ? null : criteria.getRegisterComboBox().getSelectedItem().toString();
        final String comment = criteria.getComment().getText();
        final String example = criteria.getExample().getText();

        final List<Long> lexicons = new ArrayList<>();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                workbench.setBusy(true);
                List<Sense> units = new ArrayList<>();
                Lexicon lex = criteria.getLexiconComboBox().retriveComboBoxItem();
                if (lex != null) {
                    lexicons.clear();
                    lexicons.add(lex.getId());
                } else {
                    lexicons.addAll(LexiconManager.getInstance().getLexicons());
                }

                units = LexicalDA.getLexicalUnits(oldFilter,
                        oldDomain, criteria.getPartsOfSpeachComboBox().retriveComboBoxItem() == null ? null : criteria.getPartsOfSpeachComboBox().retriveComboBoxItem().getUbyType(),
                        oldRelation, register, comment, example, limitSize, lexicons);

                // odczytanie zaznaczonej jednostki
                if (lastSelectedValue == null && unitsList != null
                        && !unitsList.isSelectionEmpty()) {
                    lastSelectedValue = listModel.getObjectAt(unitsList.getSelectedIndex());
                }
                if (units.isEmpty()) {
                    workbench.setBusy(false);
                }
                criteria.setSensesToHold(units);
                listModel.setCollection(units);
                return null;
            }

            @Override
            protected void done() {
                if (unitsList != null) {
                    SwingUtilities.invokeLater(() -> {
                        unitsList.clearSelection();
                        if (listModel.getSize() != 0) {
                            unitsList.grabFocus();
                            unitsList.setSelectedIndex(0);
                            unitsList.ensureIndexIsVisible(0);
                        }
                        infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "" + listModel.getSize()));
                    });
                }
                lastSelectedValue = null;
            }
        };
        worker.execute();
    }

    /**
     * wciśnięto przycisk szukaj
     *
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (quietMode) {
            return;
        }

        // wywolanie search
        if (event.getSource() == btnSearch) {
            refreshData();
        } else if (event.getSource() == btnReset) {
            criteria.resetFields();
        } else if (event.getSource() == btnDelete) {
            int returnValues[] = unitsList.getSelectedIndices();
            if (returnValues == null || returnValues.length == 0) {
                return;
            }
            // warto sie zapytac
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
                Sense unit = listModel.getObjectAt(i);

                // spradzenie czy ma jakies relacje
                int result = DialogBox.YES;
                if (RemoteUtils.lexicalRelationRemote
                        .dbGetRelationCountOfUnit(unit) > 0) {
                    result = DialogBox.showYesNoCancel(String.format(
                            Messages.QUESTION_UNIT_HAS_RELATIONS,
                            unit.toString()));
                    if (result == DialogBox.CANCEL) {
                        continue;
                    }
                }

                if (result == DialogBox.YES) {
                    LexicalDA.delete(unit);
                    ViWordNetService s = (ViWordNetService) workbench
                            .getService("pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService");
                    s.getActiveGraphView().getUI().releaseDataSetCache();
                    s.getActiveGraphView().getUI().clear();
                    listeners.notifyAllListeners(null);
                }
            }
            refreshData();

        } else if (event.getSource() == btnAddToSyns) {
            int i = unitsList.getSelectedIndex();
            Sense unit = listModel.getObjectAt(i);
            LexicalDA.addToNewSynset(unit);

            lastSelectedValue = null;

            if (lastSelectedValue == null && unitsList != null
                    && !unitsList.isSelectionEmpty()) {
                lastSelectedValue = listModel.getObjectAt(unitsList
                        .getSelectedIndex());
            }

            // przywrocenie zaznaczenia
            if (unitsList != null) {
                SwingUtilities.invokeLater(() -> {
                    unitsList.clearSelection();
                    if (listModel.getSize() != 0) {
                        unitsList.grabFocus();
                        unitsList.setSelectedIndex(0);
                        unitsList.ensureIndexIsVisible(0);
                    }
                    infoLabel.setText(String.format(
                            Labels.VALUE_COUNT_SIMPLE,
                            "" + listModel.getSize()));
                });
            }
            // dodanie nowej jednostki
        } else if (event.getSource() == btnNew) {
            // wyswietlenie okienka
            Sense newUnit = NewLexicalUnitFrame.showModal(workbench, null);
            if (newUnit != null) {
                ArrayList<Sense> col = new ArrayList<>();
                col.add(newUnit);
                listModel.setCollection(col);
                unitsList.setSelectedIndex(0);
                valueChanged(new ListSelectionEvent(btnNew, 0, 0, false));
                lastSelectedValue = null;
            }
        } else if (event.getSource() == btnNewWithSyns) {
            Sense newUnit = NewLexicalUnitFrame.showModal(workbench, null);
            if (newUnit != null) {
                newUnit = LexicalDA.saveUnitWithReturn(newUnit);
                ArrayList<Sense> col = new ArrayList<>();
                col.add(newUnit);
                listModel.setCollection(col);
                unitsList.setSelectedIndex(0);
                LexicalDA.addToNewSynset(newUnit);
                valueChanged(new ListSelectionEvent(btnNewWithSyns, 0, 0, false));
                lastSelectedValue = null;
            }
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
            refreshData();
        }
    }

    public void setSelectedUnit(Sense unit) {
        this.lastSelectedValue = unit;
    }

    @Override
    public JComponent getRootComponent() {
        return unitsList;
    }

    /**
     * Odczytanie zaznaczonej jednostki
     *
     * @return zaznaczona jednostka lub NULL
     */
    public Sense getSelectedUnit() {
        final int returnValue = unitsList.getSelectedIndex();
        return listModel.getObjectAt(returnValue);
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
        return criteria.getDomainComboBox().retriveComboBoxItem();
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
        listModel.setCollection(crit.getSense());
    }
}
