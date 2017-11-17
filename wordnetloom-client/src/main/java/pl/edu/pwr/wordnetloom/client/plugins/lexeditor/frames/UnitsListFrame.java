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
package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.frames;

import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.da.LexicalDA;
import pl.edu.pwr.wordnetloom.client.systems.common.ValueContainer;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.DialogBox;
import pl.edu.pwr.wordnetloom.client.utils.Labels;
import pl.edu.pwr.wordnetloom.client.utils.Messages;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

import javax.swing.*;
import java.util.Collection;

/**
 * klasa dostarczajace okno z lista jednostek leksykalnych
 *
 * @author Max
 */
public class UnitsListFrame extends AbstractListFrame<Sense, PartOfSpeech> {

    private static final long serialVersionUID = 1L;

    private JCheckBox filterUnitsInAnySynset;
    private PartOfSpeech filterObject = null;

    private boolean unitWasCreated;

    /**
     * konsuktor
     *
     * @param workbench - srodowisko
     * @param x         - położenie X
     * @param y         - położenie Y
     */
    protected UnitsListFrame(Workbench workbench, int x, int y) {
        super(workbench, Labels.LEXICAL_UNITS, Labels.LEXICAL_UNITS_COLON, x, y, true);
        unitWasCreated = false;
    }

    @Override
    protected void addExtraControlsInSearchBox() {
        filterUnitsInAnySynset = new JCheckBox(Labels.HIDE_UNITS_ASSIGNED_TO_SYNSETS, true);
        add("br", filterUnitsInAnySynset);
    }

    @Override
    protected boolean verifySelectedElements() {
        if (filterObject != null) {
            for (Sense elem : selectedElements) {
                if (LexicalDA.checkIfInAnySynset(elem)) {
                    DialogBox.showError(String.format(Messages.INFO_UNIT_ALREADY_ASSIGNED_TO_SYNSET, (elem).getWord()));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * wypełnienie kolekcji danymi
     *
     * @param filter - filtr tekstowy
     * @param pos    - czesc mowy
     * @return kolekcja z danymi
     */
    @Override
    protected Collection<Sense> fillCollection(String filter, PartOfSpeech pos) {
        if (filterObject != null && filterUnitsInAnySynset.isSelected()) {
            return LexicalDA.getLexicalUnitsNotInAnySynset(filter, pos);
        } else {
            return LexicalDA.getLexicalUnits(filter, pos, LexiconManager.getInstance().getUserChosenLexiconsIds());
        }
    }

    /**
     * uruchomienie dodania nowego elementu
     */
    @Override
    protected void invokeNew() {
        // wyswiętlanie okienka z parametrami
        Sense newUnit = NewLexicalUnitFrame.showModal(workbench, filterObject);
        if (newUnit != null) {
            LexicalDA.saveUnit(newUnit);
            filterEdit.setText(newUnit.getWord().getWord());
            unitWasCreated = true;
            // odswieżenie listy
            refreshListModel();
        }
    }

    protected void setFilterObject(PartOfSpeech filterObject) {
        this.filterObject = filterObject;
        if (this.filterObject == null) {
            filterUnitsInAnySynset.setVisible(false);
        }
    }

    /**
     * wyswietlenie okienka
     *
     * @param workbench      - srodowisko
     * @param x              - położenie X
     * @param y              - położenie Y
     * @param multiSelect    - pozwala zaznaczyc wiecej niz jeden obiekt na liscie
     * @param filterObject   - obiekt filtrujący
     * @param unitWasCreated - parametr wyjsciowy, przechowuje informacje o tym
     *                       czy nowa jednostka zostala stworzona
     * @return zaznaczone elemty albo null gdy anulowano
     */
    static public Collection<Sense> showModal(Workbench workbench,
                                              int x, int y,
                                              boolean multiSelect,
                                              PartOfSpeech filterObject,
                                              ValueContainer<Boolean> unitWasCreated) {
        UnitsListFrame frame = new UnitsListFrame(workbench, x, y);
        frame.setFilterObject(filterObject);
        frame.setMultSelect(multiSelect);
        frame.execute(filterObject);
        unitWasCreated.setValue(frame.unitWasCreated);
        return frame.selectedElements;
    }

}
