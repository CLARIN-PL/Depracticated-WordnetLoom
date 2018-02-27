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

import com.alee.laf.combobox.WebComboBox;
import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MComboBox<T> extends WebComboBox {

    private static Font newFont = new JLabel().getFont().deriveFont(Font.PLAIN);
    private static Color backgroundColor = new JTextField().getBackground();
    private int lastSelectedIndex = 0;


    public MComboBox(Object[] values) {
        super(values);
        setFont(newFont);
        setBackground(backgroundColor);
    }

    public MComboBox() {
        super();
        setFont(newFont);
        setBackground(backgroundColor);
    }

    public MComboBox withSize(Dimension dimension) {
        setPreferredSize(dimension);
        return this;
    }

    public <T> MComboBox withDictionaryItems(List<T> items, String nullRepresentation){
        removeAllItems();
        addItem(new CustomDescription<>(nullRepresentation, null));
        items.forEach(i -> {
            if(i instanceof Dictionary) {
                addItem(new CustomDescription<>(LocalisationManager.getInstance().getLocalisedString(((Dictionary)i).getName()), i));
            }
        });
        return this;
    }

    @Override
    public void setSelectedIndex(int index) {
        if (super.getItemCount() > index) {
            super.setSelectedIndex(index);
        }
        lastSelectedIndex = getSelectedIndex();
    }

    @Override
    public void setSelectedItem(Object item) {
        super.setSelectedItem(item);
        lastSelectedIndex = getSelectedIndex();
    }

    public boolean wasItemChanged() {
        return lastSelectedIndex != getSelectedIndex();
    }

    public T getEntity() {
        if (getSelectedIndex() >= 0) {
            if( getItemAt(getSelectedIndex()) instanceof  CustomDescription) {
                CustomDescription<T> item = (CustomDescription<T>) getItemAt(getSelectedIndex());
                return item.getObject();
            } else {
                return (T) getItemAt(getSelectedIndex());
            }

        }
        return null;
    }
}
