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

/**
 * uniwersalny model tabeli o dowolnej liczbie kolumn
 *
 * @author Max
 */
public class UniversalTableModel extends AbstractSimpleTableModel {

    private String[] columnNames = null;
    private Object types[] = new Object[0];
    private int values[][] = new int[0][0];

    /**
     * konstruktor
     *
     * @param columnNames - nazwy dla kolumn
     */
    public UniversalTableModel(String[] columnNames) {
        super();
        this.columnNames = columnNames;
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return types.length;
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
        if (col == 0) {
            return types[row].toString();
        }
        return "" + values[col - 1][row];
    }

    @Override
    public Object getRealValueAt(int row, int col) {
        return getValueAt(row, col);
    }

    /**
     * ustawienie danych
     *
     * @param types - typu relacji
     * @param values - tablica licznosci relacji
     */
    public void setCollection(Object[] types, int[][] values) {
        this.types = types;
        this.values = values;
        notifyAllListeners(); // powiadomienie słuchaczy o zmianie
    }
}
