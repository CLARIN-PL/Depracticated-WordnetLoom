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
package pl.edu.pwr.wordnetloom.client.systems.tooltips;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import pl.edu.pwr.wordnetloom.client.systems.models.AbstractSimpleTableModel;

/**
 * tabela z obsluga tooltipow
 *
 * @author Max
 *
 */
public class ToolTipTable extends JTable {

    private static final long serialVersionUID = 1L;
    private ToolTipGeneratorInterface toolTipsGenerator = null;

    /**
     * konstruktor
     *
     * @param tableModel - model danych
     * @param toolTipsGenerator - generator danych do tooltipa
     */
    public ToolTipTable(TableModel tableModel, ToolTipGeneratorInterface toolTipsGenerator) {
        super(tableModel);
        this.toolTipsGenerator = toolTipsGenerator;
    }

    @Override
    public String getToolTipText(MouseEvent event) {

        if (!toolTipsGenerator.hasEnabledTooltips()) {
            return null;
        }

        int col = this.columnAtPoint(event.getPoint());
        int row = this.rowAtPoint(event.getPoint());
        AbstractSimpleTableModel model = (AbstractSimpleTableModel) getModel();
        if (toolTipsGenerator != null && model != null && col != -1 && row != -1) {
            Object item = model.getRealValueAt(row, col);
            return toolTipsGenerator.getToolTipText(item);
        }
        return super.getToolTipText(event);
    }

}
