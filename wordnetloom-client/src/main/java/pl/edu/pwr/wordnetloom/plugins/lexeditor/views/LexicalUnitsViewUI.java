package pl.edu.pwr.wordnetloom.plugins.lexeditor.views;

import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import pl.edu.pwr.wordnetloom.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.model.RelationArgument;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.frames.NewLexicalUnitFrame;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.panel.CriteriaPanel;
import pl.edu.pwr.wordnetloom.plugins.lexeditor.panel.SenseCriteria;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.systems.filechooser.FileChooser;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.systems.tooltips.ToolTipGenerator;
import pl.edu.pwr.wordnetloom.systems.tooltips.ToolTipList;
import pl.edu.pwr.wordnetloom.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.systems.ui.LabelExt;
import pl.edu.pwr.wordnetloom.utils.Hints;
import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.utils.Messages;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractViewUI;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LexicalUnitsViewUI extends AbstractViewUI
        implements ActionListener, ListSelectionListener, KeyListener, MouseListener {

    private static final String SUPER_MODE_VALUE = "1";
    private static final String SUPER_MODE = "SuperMode";

    private SenseCriteria criteria;
    private ToolTipList unitsList;
    private JLabel infoLabel;
    private ButtonExt btnSearch, btnReset, btnNew, btnDelete, btnNewWithSyns, btnAddToSyns, btnDownload;

    private boolean quietMode = false;

    private GenericListModel<Sense> listModel = new GenericListModel<>();
    private Sense lastSelectedValue = null;

    @Override
    protected void initialize(JPanel content) {
        // ustawienie layoutu
        content.setLayout(new RiverLayout());
        criteria = new SenseCriteria(RelationArgument.LEXICAL);
        criteria.getDomainComboBox().addActionListener(this);
        criteria.getPartsOfSpeachComboBox().addActionListener(this);

        Icon searchIcon = IconFontSwing.buildIcon(FontAwesome.SEARCH, 12);
        btnSearch = new ButtonExt(Labels.SEARCH_NO_COLON, this, KeyEvent.VK_K);
        btnSearch.setFont(new Font("Dialog", Font.BOLD, 12));
        btnSearch.setIcon(searchIcon);

        Icon resetIcon = IconFontSwing.buildIcon(FontAwesome.TIMES, 13);
        btnReset = new ButtonExt("", this, KeyEvent.VK_C);
        btnReset.setIcon(resetIcon);
        btnReset.setToolTipText(Labels.CLEAR);

        unitsList = new ToolTipList(workbench, listModel, ToolTipGenerator.getGenerator());
        unitsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        unitsList.getSelectionModel().addListSelectionListener(this);

        infoLabel = new JLabel();
        infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "0"));

        // dodatkowe przyciski
        Icon newSynsetBtn = IconFontSwing.buildIcon(FontAwesome.PLUS_SQUARE, 12);
        btnNewWithSyns = new ButtonExt(this);
        btnNewWithSyns.setIcon(newSynsetBtn);
        btnNewWithSyns.setToolTipText(Hints.CREATE_NEW_UNIT_AND_SYNSET);

        Icon newBtn = IconFontSwing.buildIcon(FontAwesome.PLUS, 12);
        btnNew = new ButtonExt(this);
        btnNew.setIcon(newBtn);

        btnNew.setToolTipText(Hints.CREATE_NEW_UNIT);
        installViewScopeShortCut(btnNew, 0, KeyEvent.VK_INSERT);

        Icon deleteBtn = IconFontSwing.buildIcon(FontAwesome.TRASH, 12);
        btnDelete = new ButtonExt(this);
        btnDelete.setIcon(deleteBtn);
        btnDelete.setEnabled(false);
        btnDelete.setToolTipText(Hints.REMOVE_UNIT);
        installViewScopeShortCut(btnDelete, 0, KeyEvent.VK_DELETE);

        Icon addBtn = IconFontSwing.buildIcon(FontAwesome.SIGN_IN, 12);
        btnAddToSyns = new ButtonExt(this);
        btnAddToSyns.setIcon(addBtn);

        btnAddToSyns.setEnabled(false);
        btnAddToSyns.setToolTipText(Hints.ADD_TO_NEW_SYNSET);

        Icon download = IconFontSwing.buildIcon(FontAwesome.DOWNLOAD, 12);
        btnDownload = new ButtonExt(this);
        btnDownload.setIcon(download);
        btnDownload.setToolTipText("Save unit list to file");

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(btnNewWithSyns);
        buttons.add(btnNew);
        buttons.add(btnDelete);
        buttons.add(btnAddToSyns);
        buttons.add(btnDownload);

        final int scrollHeight = 330;
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
     */
    public void valueChanged(ListSelectionEvent arg0) {
        if (unitsList == null)
            return;
        if (arg0 != null && arg0.getValueIsAdjusting())
            return;
        if (arg0 == null)
            return;

        final int returnValue = unitsList.getSelectedIndex();
        Sense unit = listModel.getObjectAt(returnValue);
        boolean superMode = workbench.getParam(SUPER_MODE) != null
                && workbench.getParam(SUPER_MODE).equals(SUPER_MODE_VALUE);
        btnDelete.setEnabled(unit != null ? superMode : false);
        btnAddToSyns.setEnabled(unit != null ? (superMode) && !LexicalDA.checkIfInAnySynset(unit) : false);

        // powiadomienie zainteresowanych
        unitsList.setEnabled(false);
        listeners.notifyAllListeners(unitsList.getSelectedIndices().length == 1 ? unit : null);
        unitsList.setEnabled(true);
        unitsList.grabFocus();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                unitsList.grabFocus();
            }
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
        final pl.edu.pwr.wordnetloom.model.PartOfSpeech pos = criteria.getPartsOfSpeachComboBox().retriveComboBoxItem();
        final List<Long> lexicons = new ArrayList<>();
        CriteriaDTO dto = criteria.getCriteria();

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

                PartOfSpeech uby = pos != null ? pos.getUbyType() : null;
                units = LexicalDA.getLexicalUnits(dto, uby, limitSize, lexicons);

                // odczytanie zaznaczonej jednostki
                if (lastSelectedValue == null && unitsList != null && !unitsList.isSelectionEmpty()) {
                    lastSelectedValue = listModel.getObjectAt(unitsList.getSelectedIndex());
                }
                if (units.size() == 0) {
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
     */
    public void actionPerformed(ActionEvent event) {
        if (quietMode)
            return;

        // wywolanie search
        if (event.getSource() == btnSearch) {
            refreshData();
            return;
        } else if (event.getSource() == btnReset) {
            criteria.resetFields();
            // wywolanie usun
        } else if (event.getSource() == btnDelete) {
            int returnValues[] = unitsList.getSelectedIndices();
            if (returnValues == null || returnValues.length == 0)
                return;
            // warto sie zapytac
            if (returnValues.length == 1 && DialogBox.showYesNoCancel(Messages.QUESTION_REMOVE_UNIT) != DialogBox.YES)
                return;
            if (returnValues.length != 1 && DialogBox.showYesNoCancel(Messages.QUESTION_REMOVE_UNITS) != DialogBox.YES)
                return;

            // usuuniecie zaznaczonych jednostek
            for (int i : returnValues) {
                Sense unit = listModel.getObjectAt(i);

                // spradzenie czy ma jakies relacje
                int result = DialogBox.YES;
                if (RemoteUtils.lexicalRelationRemote.dbGetRelationCountOfUnit(unit) > 0) {
                    result = DialogBox
                            .showYesNoCancel(String.format(Messages.QUESTION_UNIT_HAS_RELATIONS, unit.toString()));
                    if (result == DialogBox.CANCEL) {
                        continue;
                    }
                }

                // usuniecie jednostki
                if (result == DialogBox.YES) {
                    LexicalDA.delete(unit);
                    ViWordNetService s = (ViWordNetService) workbench
                            .getService("pl.edu.pwr.wordnetloom.plugins.viwordnet.ViWordNetService");
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

            // refreshData();
            lastSelectedValue = null;

            if (lastSelectedValue == null && unitsList != null && !unitsList.isSelectionEmpty()) {
                lastSelectedValue = listModel.getObjectAt(unitsList.getSelectedIndex());
            }

            // przywrocenie zaznaczenia
            if (unitsList != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        unitsList.clearSelection();
                        if (listModel.getSize() != 0) {
                            unitsList.grabFocus();
                            unitsList.setSelectedIndex(0);
                            unitsList.ensureIndexIsVisible(0);
                        }
                        infoLabel.setText(String.format(Labels.VALUE_COUNT_SIMPLE, "" + listModel.getSize()));
                    }
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
        } else if (event.getSource() == btnDownload) {
            new Thread(new Runnable() {
                public void run() {
                    FileChooser fileChooser = FileChooser.getInstance();

                    File newFile = fileChooser.saveCSVFile(workbench.getFrame());

                    if (newFile == null)
                        return;

                    BufferedWriter output = null;
                    try {

                        output = new BufferedWriter(new FileWriter(newFile));

                        Collection<Sense> items = listModel.getCollection();

                        for (Sense item : items) {

                            StringBuilder sb = new StringBuilder();
                            sb.append(item.toString());
                            sb.append("\n");
                            output.write(sb.toString());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (output != null) {
                            try {
                                output.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(workbench.getFrame(),
                            "File saved successfully " + newFile.getAbsolutePath());
                }
            }).start();
        }
    }

    /**
     * zdarzenie wciśnięcia klawisza w oknie z filtrem
     */

    @Override
    public void keyPressed(KeyEvent arg0) {
    }

    /**
     * użytko myszi na liście
     */
    public void mouseReleased(MouseEvent arg0) {
    }

    public void mouseClicked(MouseEvent arg0) {
        /***/
    }

    public void mousePressed(MouseEvent arg0) {
        /***/
    }

    public void mouseEntered(MouseEvent arg0) {
        /***/
    }

    public void mouseExited(MouseEvent arg0) {
        /***/
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        /***/
    }

    @Override
    public void keyReleased(KeyEvent event) {
        super.keyPressed(event);
        if (!event.isConsumed() && event.getSource() == criteria.getSearchTextField()
                && event.getKeyChar() == KeyEvent.VK_ENTER) {
            event.consume();
            refreshData();
        }
    }

    /**
     * ustawienie ostatnio uzywnej jednostki
     *
     * @param unit
     */
    public void setSelectedUnit(Sense unit) {
        lastSelectedValue = unit;
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
        if (criteria.getSearchTextField() != null)
            criteria.getSearchTextField().setText(filter);
    }

    /**
     * Odczytanie aktualnie ustawionej dziedziny
     *
     * @return dziedzina
     */
    public Domain getDomain() {
        if (criteria.getSearchTextField() == null)
            return null;
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
