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
package pl.edu.pwr.wordnetloom.client.systems.spantable;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipGeneratorInterface;
import pl.edu.pwr.wordnetloom.client.systems.tooltips.ToolTipTable;

/**
 * tabela obsługująca połączone komorki
 *
 * @author Max
 *
 */
public class SpanTable extends ToolTipTable implements ListSelectionListener {

    private static final long serialVersionUID = 1L;
    private SpanMapperInterface mapper;

    /**
     * konstruktor
     *
     * @param mapper - obiekt mapujacy �aczenia kom�rek
     * @param tableModel - model danych
     * @param generator - generator tooltipow
     */
    public SpanTable(SpanMapperInterface mapper, TableModel tableModel, ToolTipGeneratorInterface generator) {
        super(tableModel, generator);
        this.mapper = mapper;
        setUI(new SpanTableUI());
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.JTable#getCellRect(int, int, boolean)
     */
    @Override
    public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
        row = visibleRow(row, column); // korekta wiersza
        Rectangle cellRect = super.getCellRect(row, column, includeSpacing);
        int spanSize = rowSpan(row, column);
        for (int i = 1; i < spanSize; i++) {
            cellRect.height += getRowHeight(row + i);
        }
        return cellRect;
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.JTable#rowAtPoint(java.awt.Point)
     */
    @Override
    public int rowAtPoint(Point p) {
        int y = super.rowAtPoint(p);
        if (y < 0) {
            return y;
        }
        int x = super.columnAtPoint(p);
        if (x < 0) {
            return x;
        }
        return visibleRow(y, x);
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        super.valueChanged(e);
        this.repaint();
    }

    /**
     * odczytanie wlasciwy numer widocznej komorki
     *
     * @param row - wiersz
     * @param column - kolumna
     * @return numer widocznego wiersz
     */
    public int visibleRow(int row, int column) {
        if (mapper == null) {
            return row;
        }

        int lastIndex = 0, newIndex = 0;
        while (newIndex < row) {
            lastIndex = newIndex;
            newIndex += mapper.rowSpan(lastIndex, column);
        }
        if (newIndex > row) { // jest za du�y indeks, wi�c u�ywamy ostatni
            return newIndex = lastIndex;
        }
        return newIndex;
    }

    /**
     * odczytanie polaczonych wierszy
     *
     * @param row - wiersz
     * @param column - kolumna
     * @return ilosc polaczonych wierszy
     */
    public int rowSpan(int row, int column) {
        if (mapper == null) {
            return 1;
        }
        return mapper.rowSpan(row, column);
    }
}
