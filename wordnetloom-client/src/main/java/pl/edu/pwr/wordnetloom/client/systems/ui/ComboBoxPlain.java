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
package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;

/**
 * nakladka na jcombobox wylaczajaca pogrubiona czcionke
 *
 * @author Max
 * @param <T> stored item type
 *
 */
public class ComboBoxPlain<T> extends JComboBox {

    private static final long serialVersionUID = 1L;
    private static Font newFont = new JLabel().getFont().deriveFont(Font.PLAIN);
    private static Color backgroundColor = new JTextField().getBackground();
    private int lastSelectedIndex = 0;

    /**
     * konstruktor
     *
     * @param values - wartosci
     */
    public ComboBoxPlain(Object[] values) {
        super(values);
        setFont(newFont);
        setBackground(backgroundColor);
    }

    /**
     * konstruktor
     *
     */
    public ComboBoxPlain() {
        super();
        setFont(newFont);
        setBackground(backgroundColor);
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.JComboBox#setSelectedIndex(int)
     */
    @Override
    public void setSelectedIndex(int index) {
        if (super.getItemCount() > index) {
            super.setSelectedIndex(index);
        }
        lastSelectedIndex = getSelectedIndex();
    }

    /*
	 *  (non-Javadoc)
	 * @see javax.swing.JComboBox#setSelectedItem(java.lang.Object)
     */
    @Override
    public void setSelectedItem(Object item) {
        super.setSelectedItem(item);
        lastSelectedIndex = getSelectedIndex();
    }

    /**
     * czy zmienil sie wybrany elementy
     *
     * @return true jesli sie zmienil
     */
    public boolean wasItemChanged() {
        return lastSelectedIndex != getSelectedIndex();
    }

    public T retriveComboBoxItem() {
        if (getSelectedIndex() > 0) {
            @SuppressWarnings("unchecked")
            CustomDescription<T> item = (CustomDescription<T>) getItemAt(getSelectedIndex());
            return item.getObject();
        }
        return null;
    }
}
