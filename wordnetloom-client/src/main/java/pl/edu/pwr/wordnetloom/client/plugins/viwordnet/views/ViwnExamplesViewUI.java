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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextPane;
import pl.edu.pwr.wordnetloom.client.utils.GUIUtils;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class ViwnExamplesViewUI extends AbstractViewUI {

    private WebScrollPane scroll;
    private WebTextPane ta;

    @Override
    public JComponent getRootComponent() {
        return null;
    }

    @Override
    protected void initialize(WebPanel content) {
        getContent().setLayout(new RiverLayout());
        ta = new WebTextPane();
        ta.setContentType("text/html");
        ta.setEditable(false);
        scroll = new WebScrollPane(ta);
        getContent().add(scroll, "hfill vfill");
    }

    public void load(final String text) {
        Runnable run = () -> {
            ta.setText(text);
            ta.setCaretPosition(0);
        };

        try {
            GUIUtils.delegateToEDT(run);
        } catch (InterruptedException | InvocationTargetException e) {
        }
    }
}
