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

import com.alee.laf.menu.WebMenuItem;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MMenuItem extends WebMenuItem {

    private static final long serialVersionUID = 1L;
    private Object tag = null;

    public MMenuItem(String text) {
        super(text);
    }

    public MMenuItem(ActionListener listener) {
        addActionListener(actionListener);
    }

    public MMenuItem withCaption(String text) {
        super.setText(text);
        return this;
    }

    public MMenuItem withIcon(FontAwesome icon) {
        Icon i = IconFontSwing.buildIcon(icon, 12);
        setIcon(i);
        return this;
    }

    public MMenuItem withMnemonic(int mnemonic) {
        setMnemonic(mnemonic);
        return this;
    }

    public MMenuItem withActionListener(ActionListener listener) {
        addActionListener(listener);
        return this;
    }

    public MMenuItem withKeyStroke(KeyStroke keyStroke) {
        setAccelerator(keyStroke);
        return this;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
