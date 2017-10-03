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

package pl.edu.pwr.wordnetloom.systems.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import pl.edu.pwr.wordnetloom.utils.Labels;
import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;

/**
 * nakladka na jcombobox wylaczajaca pogrubiona czcionke
 * 
 * @author Max
 * @param <T>
 *            stored item type
 *
 */

public class ComboBoxPlain<T> extends JComboBox {

	private static final long serialVersionUID = 1L;
	private static Font newFont = new JLabel().getFont().deriveFont(Font.PLAIN);
	private static Color backgroundColor = new JTextField().getBackground();
	private int lastSelectedIndex = 0;

	public ComboBoxPlain(Object[] values, CustomDescription<T> nullObject) {
		if (nullObject != null) {
			addItem(nullObject);
		} else {
			addItem(new CustomDescription<T>(Labels.VALUE_ALL, null));
		}
		Arrays.asList(values).stream().forEach(i -> addItem(new CustomDescription<>(i.toString(), i)));
		setFont(newFont);
		setBackground(backgroundColor);
	}

	public ComboBoxPlain(Object[] values) {
		Arrays.asList(values).stream().forEach(i -> addItem(new CustomDescription<>(i.toString(), i)));
		setFont(newFont);
		setBackground(backgroundColor);
	}

	public ComboBoxPlain(CustomDescription<T> nullObject) {
		super();
		addItem(nullObject);
		setFont(newFont);
		setBackground(backgroundColor);
	}

	public ComboBoxPlain(boolean withDeafultNullObject) {
		super();
		if (withDeafultNullObject)
			addItem(new CustomDescription<T>(Labels.VALUE_ALL, null));
		setFont(newFont);
		setBackground(backgroundColor);
	}

	public ComboBoxPlain() {
		super();
		setFont(newFont);
		setBackground(backgroundColor);
	}

	public void addItem(String desc, T item) {
		super.addItem(new CustomDescription<T>(desc, item));
	}

	@Override
	public void setSelectedIndex(int index) {
		if (super.getItemCount() > index)
			super.setSelectedIndex(index);
		lastSelectedIndex = getSelectedIndex();
	}

	@Override
	public void setSelectedItem(Object item) {
		super.setSelectedItem(item);
		lastSelectedIndex = getSelectedIndex();
	}

	public void setSelectedItem(String desc, T item) {
		if (item != null) {
			super.setSelectedItem(new CustomDescription<T>(desc, item));
		} else {
			super.setSelectedIndex(0);
		}

		lastSelectedIndex = getSelectedIndex();
	}

	public boolean wasItemChanged() {
		return lastSelectedIndex != getSelectedIndex();
	}

	@SuppressWarnings("unchecked")
	public T retriveComboBoxItem() {

		if (getSelectedIndex() > 0) {
			CustomDescription<T> item = (CustomDescription<T>) getItemAt(getSelectedIndex());
			return item.getObject();
		}

		return null;
	}

	public T retriveWihtoutNullComboBoxItem() {
		CustomDescription<T> item = (CustomDescription<T>) getItemAt(getSelectedIndex());
		return item.getObject();
	}
}
