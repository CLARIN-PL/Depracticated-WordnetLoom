/*
    Copyright (C) 2011 Łukasz Jastrzębski, Paweł Koczan, Michał Marcińczuk,
                       Bartosz Broda, Maciej Piasecki, Adam Musiał,
                       Radosław Ramocki, Michał Stanek
    Part of the WordnetLoom

    This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 3 of the License, or (at your option)
any later version.

    This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.

    See the LICENSE and COPYING files for more details.
 */
package pl.edu.pwr.wordnetloom.client.plugins.relations.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames.RelationTypeFrame;
import pl.edu.pwr.wordnetloom.client.plugins.relations.RelationsIM;
import pl.edu.pwr.wordnetloom.client.plugins.relations.da.RelationsDA;
import pl.edu.pwr.wordnetloom.client.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenersContainer;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.systems.misc.Tools;
import pl.edu.pwr.wordnetloom.client.systems.progress.AbstractProgressThread;
import pl.edu.pwr.wordnetloom.client.systems.ui.ButtonExt;
import pl.edu.pwr.wordnetloom.client.utils.RemoteUtils;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationArgument;
import pl.edu.pwr.wordnetloom.model.wordnet.RelationType;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;
import se.datadosen.component.RiverLayout;

/**
 * klasa opisujacy wyglada paska narzedzi
 *
 * @author Max
 */
public class ToolbarViewUI extends AbstractViewUI implements ActionListener {

    /**
     * odswiezenie relacji synsetu
     */
    public static final int REFRESH_RELATION = 0;
    /**
     * odswiezenie tylko zaznaczenia
     */
    public static final int REFRESH_MARKS = 1;

    private static String QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION = "Czy utworzyć powiązanie dla relacji odwrotnej (%s)?";
    private static String ERROR_YOU_CANNOT_MOVE_ALL_UNITS_FROM_SYNSET = "Nie można przenieść WSZYSTKICH jednostek z synsetu.";
    static String PROGRESS_MOVE_UNITS = "Przenoszenie jednostek";
    static String PROGRESS_MAKE_CONNECTION = "Tworzenie powiązania";
    private static String PROGRESS_WINDOW_TITLE = "Relacje synsetów";
    private static String BUTTON_SWITCH_LABEL = "Ustawienie docelowego jako źródłowy (Ctrl+\")";
    private static String BUTTON_SWITCH_BOTH_LABEL = "Zamienienie źrodłowego z docelowym (Ctrl+Shift+\")";
    private static String BUTTON_ONLY_RELATION_LABEL = "Utworzenie relacji pomiędzy źródłowym i docelowym synsetem (Ctrl+Shift+R)";
    private static String BUTTON_TO_EXISTEN_LABEL = "Przeniesienie wybranych jednostek do docelowego synsetu i utworzenie relacji z aktualnie wybranym (źródłowym) synstem (Ctrl+Shift+S)";
    private static String BUTTON_TO_NEW_LABEL = "Utworzenie nowego synsetu, przeniesienie do niego wybranych jednostek i utworzenie relacji z aktualnie wybranym (źródłowym) synstem (Ctrl+Shift+N)";
    private static String BUTTON_TO_MERGE = "Połącz otwarte synsety";
    private static String RELATION_ALREADY_EXISTS = "<html>Relacja typu <font color=\"blue\">%s</font> pomiędzy<br><font color=\"blue\">%s</font><br>i<br><font color=\"blue\">%s</font><br>już istnieje.</html>";
    public static String MERGE_SYNSETS = "<html>Czy na pewno chcesz połączyć synsety:<br>"
            + "1. <font color=\"blue\">%s</font><br>"
            + "2. <font color=\"blue\">%s</font> ?</html>";
    public static String EMPTY_SYNSET_RELATION = "Nie można utworzyć relacji pomiędzy pustymi synsetami.";

    private JButton buttonToNew;
    private JButton buttonToExisten;
    private JButton buttonToMerge;
    private JButton buttonOnlyRelation;
    private JButton buttonSwitchBoth;
    private JButton buttonSwitch;

    private SimpleListenersContainer refreshUnitsInSynsetListeners = new SimpleListenersContainer();
    private SimpleListenersContainer showSynsetListeners = new SimpleListenersContainer();
    private SimpleListenersContainer refreshRelationListeners = new SimpleListenersContainer();
    private SimpleListenersContainer synsetChangedListeners = new SimpleListenersContainer();

    Synset lastMainSynset = null;
    Synset lastDescSynset = null;
    Collection<Sense> lastSelectedUnits = null;
    private List<Long> lexicons;

    @Override
    protected void initialize(JPanel content) {

        this.lexicons = LexiconManager.getInstance().getLexicons();
        // ustawienie layoutu
        content.setLayout(new RiverLayout());

        buttonToNew = new ButtonExt(RelationsIM.getToNew(), this);
        buttonToNew.setToolTipText(BUTTON_TO_NEW_LABEL);
        buttonToNew.setEnabled(false);
        installPerspectiveScopeShortCut(buttonToNew, InputEvent.SHIFT_DOWN_MASK
                | InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_N);

        buttonToExisten = new ButtonExt(RelationsIM.getToExisten(), this);
        buttonToExisten.setToolTipText(BUTTON_TO_EXISTEN_LABEL);
        buttonToExisten.setEnabled(false);
        installPerspectiveScopeShortCut(buttonToExisten,
                InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK,
                KeyEvent.VK_S);

        buttonToMerge = new ButtonExt(RelationsIM.getToMerge(), this);
        buttonToMerge.setToolTipText(BUTTON_TO_MERGE);
        buttonToMerge.setEnabled(false);

        buttonOnlyRelation = new ButtonExt(RelationsIM.getOnlyRelation(), this);
        buttonOnlyRelation.setToolTipText(BUTTON_ONLY_RELATION_LABEL);
        buttonOnlyRelation.setEnabled(false);
        installPerspectiveScopeShortCut(buttonOnlyRelation,
                InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK,
                KeyEvent.VK_R);

        buttonSwitchBoth = new ButtonExt(RelationsIM.getSwitchBoth(), this);
        buttonSwitchBoth.setToolTipText(BUTTON_SWITCH_BOTH_LABEL);
        buttonSwitchBoth.setEnabled(false);
        installPerspectiveScopeShortCut(buttonSwitchBoth,
                InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK,
                KeyEvent.VK_QUOTE);

        buttonSwitch = new ButtonExt(RelationsIM.getSwitch(), this);
        buttonSwitch.setToolTipText(BUTTON_SWITCH_LABEL);
        buttonSwitch.setEnabled(false);
        installPerspectiveScopeShortCut(buttonSwitch,
                InputEvent.CTRL_DOWN_MASK, KeyEvent.VK_QUOTE);

        content.add("center", buttonToNew);
        content.add(buttonToExisten);
        content.add(new JLabel(" "));
        content.add(buttonOnlyRelation);
        content.add(new JLabel(" "));
        content.add(buttonSwitchBoth);
        content.add(buttonSwitch);
        content.add(buttonToMerge);
    }

    /**
     * kliknieto przycisk
     */
    public void actionPerformed(ActionEvent arg0) {
        // dodanie do nowej
        if (arg0.getSource() == buttonToNew) {
            // czy nie ma przypadkiem do przeniesienia wszystkich jednostek
            List<Sense> lastMainSynsetSenses = RemoteUtils.lexicalUnitRemote
                    .dbFastGetUnits(lastMainSynset, LexiconManager.getInstance().getLexicons());
            if (lastSelectedUnits.size() == lastMainSynsetSenses.size()) {
                DialogBox
                        .showError(ERROR_YOU_CANNOT_MOVE_ALL_UNITS_FROM_SYNSET);
                return;
            }
            Collection<Sense> unitsOfMainSynset = RelationsDA.getUnits(
                    lastMainSynset, lastSelectedUnits, lexicons);
            final RelationType rel = RelationTypeFrame.showModal(workbench,
                    RelationArgument.SYNSET,
                    RelationsDA.getPos(lastMainSynset, lexicons),
                    unitsOfMainSynset, lastSelectedUnits);
            if (rel == null) {
                return;
            }

            // utworzenie nowego synsetu
            final Synset newSynset = RelationsDA.newSynset();

            // utworzenie powiazan
            new AbstractProgressThread(Tools.findFrame(getContent()),
                    PROGRESS_WINDOW_TITLE, null) {
                /**
                 * glowna procedura watku
                 */
                @Override
                protected void mainProcess() {
                    // ustawienie parametrow dla paska postepu
                    progress.setGlobalProgressParams(1, 2);
                    progress.setProgressParams(0, 0, PROGRESS_MOVE_UNITS);
                    RelationsDA.moveUnitsToExistenSynset(lastMainSynset,
                            lastSelectedUnits, newSynset);
                    progress.setGlobalProgressValue(2);
                    progress.setProgressParams(0, 0, PROGRESS_MAKE_CONNECTION);
                    RelationsDA.makeRelation(lastMainSynset, newSynset, rel);
                }
            };

            // czy istieje relacja odwrotna
            if (rel.getReverse() != null) {
                // testy dla relacji odwrotnej
                Collection<String> tests = LexicalDA.getTests(
                        RelationsDA.getReverseRelation(rel),
                        lastMainSynset.toString(), newSynset.toString(),
                        RelationsDA.getPos(lastMainSynset, lexicons));
                String test = "\n\n";
                for (String i : tests) {
                    test += i + "\n";
                }
                // czy utworzyc dla relacji odwrotnej
                RelationType reverse = RelationsDA.getReverseRelation(rel);
                String reverseName = RelationsDA.getRelationName(reverse);
                if (rel.isAutoReverse()
                        || DialogBox.showYesNoCancel(String.format(
                                QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION
                                + test, reverseName)) == DialogBox.YES) {
                    RelationsDA
                            .makeRelation(newSynset, lastMainSynset, reverse);
                }
            }

            // odswiezenie drzewa i jednostek
            refreshUnitsInSynsetListeners.notifyAllListeners(null, 1);
            refreshRelationListeners.notifyAllListeners(lastMainSynset,
                    REFRESH_RELATION);
            refreshRelationListeners.notifyAllListeners(newSynset,
                    REFRESH_MARKS);
            showSynsetListeners.notifyAllListeners(newSynset, 2);

            // dodanie do istniejacego
        } else if (arg0.getSource() == buttonToExisten) {
            // czy nie ma przypadkiem do przeniesienia wszystkich jednostek
            List<Sense> lastMainSynsetSenses = RemoteUtils.lexicalUnitRemote
                    .dbFastGetUnits(lastMainSynset, LexiconManager.getInstance().getLexicons());
            if (lastSelectedUnits.size() == lastMainSynsetSenses.size()) {
                DialogBox
                        .showError(ERROR_YOU_CANNOT_MOVE_ALL_UNITS_FROM_SYNSET);
                return;
            }
            // odczytanie jednostek
            Collection<Sense> unitsOfMainSynset = RelationsDA.getUnits(
                    lastMainSynset, lexicons);
            Collection<Sense> unitsOfDescSynset = RelationsDA.getUnits(
                    lastDescSynset, lexicons);
            // dodanie tych przenoszonych
            unitsOfDescSynset.addAll(lastSelectedUnits);
            final RelationType rel = RelationTypeFrame.showModal(workbench,
                    RelationArgument.SYNSET,
                    RelationsDA.getPos(lastMainSynset, lexicons),
                    unitsOfMainSynset, unitsOfDescSynset);
            if (rel == null) {
                return;
            }

            // sprawdzenie czy nie ma już przypadkiem takiej relacji
            if (RelationsDA.checkIfRelationExists(lastMainSynset,
                    lastDescSynset, rel)) {
                DialogBox.showError(String.format(RELATION_ALREADY_EXISTS,
                        RelationTypes.getFullNameFor(rel.getId()),
                        lastMainSynset.toString(), lastDescSynset.toString()));
                return;
            }

            // utworzenie powiazan
            new AbstractProgressThread(Tools.findFrame(getContent()),
                    PROGRESS_WINDOW_TITLE, null) {
                /**
                 * glowna procedura watku
                 */
                @Override
                protected void mainProcess() {
                    // ustawienie parametrow dla paska postepu
                    progress.setGlobalProgressParams(0, 1);
                    progress.setProgressParams(0, 0, PROGRESS_MOVE_UNITS);
                    RelationsDA.moveUnitsToExistenSynset(lastMainSynset,
                            lastSelectedUnits, lastDescSynset);
                    progress.setGlobalProgressValue(1);
                    progress.setProgressParams(0, 0, PROGRESS_MAKE_CONNECTION);
                    RelationsDA.makeRelation(lastMainSynset, lastDescSynset,
                            rel);
                }
            };
            // czy istieje relacja odwrotna
            if (rel.getReverse() != null) {
                // testy dla relacji odwrotnej
                Collection<String> tests = LexicalDA.getTests(
                        RelationsDA.getReverseRelation(rel),
                        lastMainSynset.toString(), lastDescSynset.toString(),
                        RelationsDA.getPos(lastMainSynset, lexicons));
                String test = "\n\n";
                for (String i : tests) {
                    test += i + "\n";
                }
                // czy utworzyc dla relacji odwrotnej
                RelationType reverse = RelationsDA.getReverseRelation(rel);
                String reverseName = RelationsDA.getRelationName(reverse);

                if (rel.isAutoReverse()
                        || DialogBox.showYesNoCancel(String.format(
                                QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION
                                + test, reverseName)) == DialogBox.YES) {
                    if (RelationsDA.checkIfRelationExists(lastDescSynset,
                            lastMainSynset, reverse)) {
                        DialogBox.showError(String.format(
                                RELATION_ALREADY_EXISTS, reverseName,
                                lastDescSynset.toString(),
                                lastMainSynset.toString()));
                    } else {
                        RelationsDA.makeRelation(lastDescSynset,
                                lastMainSynset, reverse);
                    }
                }
            }

            // odswiezenie drzewa i jednostek
            refreshUnitsInSynsetListeners.notifyAllListeners(null, 1);
            refreshUnitsInSynsetListeners.notifyAllListeners(null, 2);
            refreshRelationListeners.notifyAllListeners(lastMainSynset,
                    REFRESH_RELATION);
            refreshRelationListeners.notifyAllListeners(lastDescSynset,
                    REFRESH_MARKS);
            // }
            // else if (arg0.getSource() == buttonToMerge //&
            // // DialogBox.showYesNo(String.format(MERGE_SYNSETS,
            // // RemoteUtils.synsetRemote.dbRebuildUnitsStr(lastMainSynset),
            // //
            // RemoteUtils.synsetRemote.dbRebuildUnitsStr(lastDescSynset)))==DialogBox.YES)
            // {
            // RelationsDA.mergeSynsets(lastDescSynset, lastMainSynset);
            //
            // refreshUnitsInSynsetListeners.notifyAllListeners(null,1);
            // refreshUnitsInSynsetListeners.notifyAllListeners(null,2);
            // refreshRelationListeners.notifyAllListeners(lastMainSynset,REFRESH_RELATION);
            // refreshRelationListeners.notifyAllListeners(lastDescSynset,REFRESH_MARKS);
            // synsetChangedListeners.notifyAllListeners(lastDescSynset, 0);
            //
        } else if (arg0.getSource() == buttonOnlyRelation) {
            // odczytanie jednostek
            Collection<Sense> unitsOfMainSynset = RelationsDA.getUnits(
                    lastMainSynset, lexicons);
            Collection<Sense> unitsOfDescSynset = RelationsDA.getUnits(
                    lastDescSynset, lexicons);
            if (unitsOfMainSynset.isEmpty() || unitsOfDescSynset.isEmpty()) {
                DialogBox.showError(EMPTY_SYNSET_RELATION);
                return;
            }

            final RelationType rel = RelationTypeFrame.showModal(workbench,
                    RelationArgument.SYNSET,
                    RelationsDA.getPos(lastMainSynset, lexicons),
                    unitsOfMainSynset, unitsOfDescSynset);
            if (rel == null) {
                return;
            }

            // sprawdzenie czy nie ma już przypadkiem takiej relacji
            if (RelationsDA.checkIfRelationExists(lastMainSynset,
                    lastDescSynset, rel)) {
                DialogBox.showError(String.format(RELATION_ALREADY_EXISTS,
                        RelationTypes.getFullNameFor(rel.getId()),
                        lastMainSynset.toString(), lastDescSynset.toString()));
                return;
            }

            // utworzenie powiazan
            new AbstractProgressThread(Tools.findFrame(getContent()),
                    PROGRESS_WINDOW_TITLE, null) {
                /**
                 * glowna procedura watku
                 */
                @Override
                protected void mainProcess() {
                    // ustawienie parametrow dla paska postepu
                    progress.setGlobalProgressParams(0, 0);
                    progress.setProgressParams(0, 0, PROGRESS_MAKE_CONNECTION);
                    RelationsDA.makeRelation(lastMainSynset, lastDescSynset,
                            rel);
                }
            };

            // czy istieje relacja odwrotna
            if (rel.getReverse() != null) {
                // testy dla relacji odwrotnej
                Collection<String> tests = LexicalDA.getTests(
                        RelationsDA.getReverseRelation(rel),
                        lastMainSynset.toString(), lastDescSynset.toString(),
                        RelationsDA.getPos(lastMainSynset, lexicons));
                String test = "\n\n";
                for (String i : tests) {
                    test += i + "\n";
                }
                // czy utworzyc dla relacji odwrotnej
                RelationType reverse = RelationsDA.getReverseRelation(rel);
                String reverseName = RelationsDA.getRelationName(reverse);

                if (rel.isAutoReverse()
                        || DialogBox.showYesNoCancel(String.format(
                                QUESTION_CREATE_CONNECTION_FOR_REVERSE_RELATION
                                + test, reverseName)) == DialogBox.YES) {
                    if (RelationsDA.checkIfRelationExists(lastDescSynset,
                            lastMainSynset, reverse)) {
                        DialogBox.showError(String.format(
                                RELATION_ALREADY_EXISTS, reverseName,
                                lastDescSynset.toString(),
                                lastMainSynset.toString()));
                    } else {
                        RelationsDA.makeRelation(lastDescSynset,
                                lastMainSynset, reverse);
                    }
                }
            }
            refreshRelationListeners.notifyAllListeners(lastMainSynset,
                    REFRESH_RELATION);
            refreshRelationListeners.notifyAllListeners(lastDescSynset,
                    REFRESH_MARKS);

            // przelaczenie docelowego na zrodlowy
        } else if (arg0.getSource() == buttonSwitch) {
            showSynsetListeners.notifyAllListeners(lastDescSynset, 1);

            // przelaczenie docelowego na zrodlowy i zrodlowego na docelowy
        } else if (arg0.getSource() == buttonSwitchBoth) {
            Synset s1 = lastDescSynset;
            Synset s2 = lastMainSynset;

            showSynsetListeners.notifyAllListeners(s1, 1);
            showSynsetListeners.notifyAllListeners(s2, 2);
        }
    }

    /**
     * odswieżenie przyciksów
     *
     * @param mainSynset - głowny synset
     * @param selectedUnits - wybrane jednostki w głównym synsecie
     * @param descSynset - docelowy synset
     */
    public void refreshButtons(Synset mainSynset,
            Collection<Sense> selectedUnits, Synset descSynset) {
        buttonToNew.setEnabled(selectedUnits.size() > 0);
        buttonSwitch.setEnabled(descSynset != null);
        buttonToMerge.setEnabled(mainSynset != null && descSynset != null
                && !mainSynset.equals(descSynset));
        boolean enableRelation = mainSynset != null && descSynset != null
                && !mainSynset.equals(descSynset); // nie są takie same

        buttonToExisten.setEnabled(enableRelation && selectedUnits.size() > 0);
        buttonSwitchBoth.setEnabled(enableRelation);
        buttonOnlyRelation.setEnabled(enableRelation);
        // zapamietanie danych
        lastMainSynset = mainSynset;
        lastDescSynset = descSynset;
        lastSelectedUnits = selectedUnits;
    }

    @Override
    public JComponent getRootComponent() {
        return null;
    }

    /**
     * dodanie obiektu nasluchujacego gdy potrzeba odswiezenia jednostek w
     * synsecie
     *
     * @param newListener - sluchacz
     */
    public void addRefreshUnitsInSynsetListener(
            SimpleListenerInterface newListener) {
        refreshUnitsInSynsetListeners.add(newListener);
    }

    /**
     * dodanie obiektu nasluchujacego gdy potrzeba ponownego pokazania synsetu
     *
     * @param newListener - sluchacz
     */
    public void addShowSynsetListener(SimpleListenerInterface newListener) {
        showSynsetListeners.add(newListener);

    }

    /**
     * dodanie obiektu nasluchujacego gdy potrzeba odswiezenia relacji
     *
     * @param newListener - sluchacz
     */
    public void addRefreshRelationListener(SimpleListenerInterface newListener) {
        refreshRelationListeners.add(newListener);
    }

    /**
     * dodanie obiektu nasluchujacego gdy usunięto synset
     *
     * @param newListener - sluchacz
     */
    public void addSynsetChangedListener(SimpleListenerInterface newListener) {
        synsetChangedListeners.add(newListener);
    }
}
