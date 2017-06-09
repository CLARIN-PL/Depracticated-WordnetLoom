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

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * abstrakcyjny model danych dla tabel uproszczajacy implementacje
 *
 * @author Max
 *
 */
public abstract class AbstractSimpleTableModel implements TableModel {

    private EventListenerList listeners = new EventListenerList();

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object arg0, int arg1, int arg2) {
        /**
         *
         */
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class<?> getColumnClass(int arg0) {
        return String.class;
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
     */
    public void addTableModelListener(TableModelListener listener) {
        listeners.add(TableModelListener.class, listener);
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
     */
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(TableModelListener.class, listener);
    }

    /**
     * powiadomienie wszystkich słuchaczy o zmianie
     */
    protected void notifyAllListeners() {
        for (TableModelListener dataListener : listeners.getListeners(TableModelListener.class)) {
            dataListener.tableChanged(new TableModelEvent(this));
        }
    }

    /**
     * odczytanie prawdziwego obiektu, a nie tylko tekstowej reprezentacji
     *
     * @param row - rzad
     * @param col - kolumna
     * @return obiekt
     */
    abstract public Object getRealValueAt(int row, int col);
}
