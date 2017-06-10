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

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;

/**
 * Class works as manager of parent and kids for view UI that can be cloned to
 * separate window.
 *
 * @author Max
 *
 * @param <T>
 */
public class FamilyManager<T extends AbstractViewUI> {

    private static final int DEFAULT_HEIGHT = 400;
    private static final int DEFAULT_WIDTH = 500;
    private JFrame parentFrame = null;
    private final List<T> children = new ArrayList<>();

    final void clearParentFrame() {
        this.parentFrame = null;
    }

    /**
     * Add new child to the family. It crate new window wrapper for view and
     * shows it.
     *
     * @param workbench
     * @param title
     * @param childObject
     * @param childManager
     */
    public final void addChild(final Workbench workbench, final String title, final T childObject, final FamilyManager<T> childManager) {
        childManager.parentFrame = new IconFrame(workbench.getFrame(), title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        childObject.init(workbench);

        childManager.parentFrame.setLayout(new BorderLayout());
        childManager.parentFrame.setDefaultCloseOperation(IconFrame.DISPOSE_ON_CLOSE);
        childManager.parentFrame.add(childObject.getContent(), BorderLayout.CENTER);
        childManager.parentFrame.setVisible(true);
        childManager.parentFrame.addWindowListener(new ChildViewUIWindowListener(this, childManager, childObject));

        synchronized (children) {
            children.add(childObject);
        }
    }

    final void removeChild(T child) {
        synchronized (children) {
            children.remove(child);
        }
    }

    /**
     * Get list of children of the view
     *
     * @return list of children
     */
    final public List<T> getChildren() {
        return children;
    }

    /**
     * Windows activity listener. When the windows is closed, the child is
     * unregistered from parent view.
     */
    class ChildViewUIWindowListener implements WindowListener {

        private final T childObject;
        private final FamilyManager<T> parentManager;
        private final FamilyManager<T> childManager;

        /**
         * @param parentManager
         * @param childManager
         * @param childObject
         */
        public ChildViewUIWindowListener(FamilyManager<T> parentManager, FamilyManager<T> childManager, T childObject) {
            this.childObject = childObject;
            this.parentManager = parentManager;
            this.childManager = childManager;
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowClosed(WindowEvent e) {
            childManager.clearParentFrame();
            parentManager.removeChild(childObject);
        }

        @Override
        public void windowClosing(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }
    }
}
