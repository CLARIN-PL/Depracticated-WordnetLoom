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
package pl.edu.pwr.wordnetloom.client.systems.models;

import java.util.ArrayList;
import java.util.Collection;
import pl.edu.pwr.wordnetloom.client.systems.common.Container;

/**
 * uniwersalny model tabeli o dowolnej liczbie kolumn wykorzystujacy obiekty
 * implementujace interfejs Container
 *
 * @author Max
 * @param <T>
 */
public class ContainersTableModel<T extends Container> extends AbstractSimpleTableModel {

    private String[] columnNames = null;
    protected Collection<T> values = new ArrayList<T>();

    /**
     * konstruktor
     *
     * @param columnNames - nazwy dla kolumn
     */
    public ContainersTableModel(String[] columnNames) {
        super();
        this.columnNames = columnNames;
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        if (values != null) {
            return values.size();
        }
        return 0;
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        for (T item : values) {
            if (row-- == 0) {
                return item.getValue(col);
            }
        }
        return null;
    }

    @Override
    public Object getRealValueAt(int row, int col) {
        return getValueAt(row, col);
    }

    /**
     * ustawienie danych
     *
     * @param values - kolekcja z danymi
     */
    public void setData(Collection<T> values) {
        if (values != null) {
            this.values = values;
        } else {
            this.values = new ArrayList<T>();
        }
        notifyAllListeners(); // powiadomienie słuchaczy o zmianie
    }
}
