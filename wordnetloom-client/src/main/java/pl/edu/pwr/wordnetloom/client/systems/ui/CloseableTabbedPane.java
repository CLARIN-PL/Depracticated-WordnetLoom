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
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import pl.edu.pwr.wordnetloom.client.systems.listeners.CloseableTabbedPaneListener;

/**
 * License
 *
 * Copyright 1994-2009 Sun Microsystems, Inc. All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *
 * Redistribution of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistribution in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS
 * LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility.
 *
 */
/**
 * A JTabbedPane which has a close ('X') icon on each tab.
 *
 * To add a tab, use the method addTab(String, Component)
 *
 * To have an extra icon on each tab (e.g. like in JBuilder, showing the file
 * type) use the method addTab(String, Component, Icon). Only clicking the 'X'
 * closes the tab.
 *
 * @author fast_
 *
 * Tab-button is a feature which gives you an ability to open new tabs in
 * firefox style. Button fires an event, you could do what you want opening a
 * new tab is only a suggestion
 *
 * @author amusial
 *
 */
public class CloseableTabbedPane extends JTabbedPane implements MouseListener,
        MouseMotionListener {

    /**
     *
     */
    private static final long serialVersionUID = 2472490265282611077L;

    /**
     * The <code>EventListenerList</code>.
     */
    private EventListenerList listenerList = null;

    /**
     * The viewport of the scrolled tabs.
     */
    private JViewport headerViewport = null;

    /**
     * The normal closeicon.
     */
    private Icon normalCloseIcon = null;

    /**
     * The closeicon when the mouse is over.
     */
    private Icon hooverCloseIcon = null;

    /**
     * The closeicon when the mouse is pressed.
     */
    private Icon pressedCloseIcon = null;

    /**
     * value true means that all tabs may be closed value false means that after
     * opening more than one tab, at least one must stay opened, default value
     * is false
     *
     */
    private boolean allowAllTabsClosing = false;

    /**
     * specific component which fills tab added to this panel only to provide
     * adding new panels tab-button
     *
     */
    private JLabel addTabLabel = null;

    /**
     * Creates a new instance of <code>CloseableTabbedPane</code>
     */
    public CloseableTabbedPane() {
        super();
        init(SwingUtilities.LEFT);
    }

    /**
     * Create new instance of <code>CloseableTabbedPane</code> with or without
     * special tab-button in firefox tabs style, which could be useful for
     * example for adding new tabs
     *
     * @param showTabButton <code>boolean</code> when true, new instance will
     * have a tab-button added, with false works as no parameters constructor
     *
     */
    public CloseableTabbedPane(boolean showTabButton) {
        super();
        init(SwingUtilities.LEFT);
        if (showTabButton) {
            addAddTab();
        }
    }

    /**
     * Create new instance of <code>CloseableTabbedPane</code> with or without
     * special tab-button in firefox tabs style, which could be useful for
     * example for adding new tabs
     *
     * @param showTabButton <code>boolean</code> when true, new instance will
     * have a tab-button added, with false works as no parameters constructor
     * @param allowAllTabsClosing all tabs could be closed when true, last tab
     * close button will be disabled when false
     *
     */
    public CloseableTabbedPane(boolean showTabButton, boolean allowAllTabsClosing) {
        super();
        init(SwingUtilities.LEFT);
        if (showTabButton) {
            addAddTab();
        }
        this.allowAllTabsClosing = allowAllTabsClosing;
    }

    /**
     * Creates a new instance of <code>CloseableTabbedPane</code>
     *
     * @param horizontalTextPosition the horizontal position of the text (e.g.
     * SwingUtilities.TRAILING or SwingUtilities.LEFT)
     */
    public CloseableTabbedPane(int horizontalTextPosition) {
        super();
        init(horizontalTextPosition);
    }

    /**
     * Initializes the <code>CloseableTabbedPane</code>
     *
     * @param horizontalTextPosition the horizontal position of the text (e.g.
     * SwingUtilities.TRAILING or SwingUtilities.LEFT)
     */
    private void init(int horizontalTextPosition) {
        listenerList = new EventListenerList();
        addMouseListener(this);
        addMouseMotionListener(this);

        if (getUI() instanceof MetalTabbedPaneUI) {
            setUI(new CloseableMetalTabbedPaneUI(horizontalTextPosition));
        } else {
            setUI(new CloseableTabbedPaneUI(horizontalTextPosition));
        }
    }

    /**
     * Allows setting own closeicons.
     *
     * @param normal the normal closeicon
     * @param hoover the closeicon when the mouse is over
     * @param pressed the closeicon when the mouse is pressed
     */
    public void setCloseIcons(Icon normal, Icon hoover, Icon pressed) {
        normalCloseIcon = normal;
        hooverCloseIcon = hoover;
        pressedCloseIcon = pressed;
    }

    /**
     * Adds a <code>Component</code> represented by a title and no icon.
     *
     * @param title the title to be displayed in this tab
     * @param component the component to be displayed when this tab is clicked
     */
    public void addTab(String title, Component component) {
        addTab(title, component, null);
    }

    /**
     * Adds a <code>Component</code> represented by a title and an icon.
     *
     * @param title the title to be displayed in this tab
     * @param component the component to be displayed when this tab is clicked
     * @param extraIcon the icon to be displayed in this tab
     */
    public void addTab(String title, Component component, Icon extraIcon) {
        boolean doPaintCloseIcon = true;
        try {
            Object prop = null;
            if ((prop = ((JComponent) component)
                    .getClientProperty("isClosable")) != null) {
                doPaintCloseIcon = (Boolean) prop;
            }
        } catch (Exception ignored) {/* Could probably be a ClassCastException */
        }

        super.addTab(title, doPaintCloseIcon ? new CloseTabIcon(extraIcon)
                : null, component);

        if (headerViewport == null) {
            for (Component c : getComponents()) {
                if ("TabbedPane.scrollableViewport".equals(c.getName())) {
                    headerViewport = (JViewport) c;
                }
            }
        }
    }

    /**
     * adds add tab-button add tab
     *
     *
     */
    private void addAddTab() {
        addTabLabel = new JLabel("ADD_TAB");
        super.addTab("", new AddTabIcon(null), addTabLabel);
        setToolTipTextAt(0, "Dodaj zakładkę");
    }

    /**
     * Override setSelectedIndex method allow to use tab-button in firefox
     * style. The tab-button could be use to add new tabs. Tab-button selection
     * is disabled, thats only difference.
     *
     */
    @Override
    public void setSelectedIndex(int index) {
        if (addTabLabel == null || indexOfComponent(addTabLabel) != index) {
            super.setSelectedIndex(index);
        }
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on
     * a component.
     *
     * @param e the <code>MouseEvent</code>
     */
    public void mouseClicked(MouseEvent e) {
        processMouseEvents(e);
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the <code>MouseEvent</code>
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the <code>MouseEvent</code>
     */
    public void mouseExited(MouseEvent e) {
        for (int i = 0; i < getTabCount(); i++) {
            CloseTabIcon icon = (CloseTabIcon) getIconAt(i);
            if (icon != null) {
                icon.mouseover = false;
            }
        }
        repaint();
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the <code>MouseEvent</code>
     */
    public void mousePressed(MouseEvent e) {
        processMouseEvents(e);
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the <code>MouseEvent</code>
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Invoked when a mouse button is pressed on a component and then dragged.
     * <code>MOUSE_DRAGGED</code> events will continue to be delivered to the
     * component where the drag originated until the mouse button is released
     * (regardless of whether the mouse position is within the bounds of the
     * component).<br/>
     * <br/>
     * Due to platform-dependent Drag&Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&Drop operation.
     *
     * @param e the <code>MouseEvent</code>
     */
    public void mouseDragged(MouseEvent e) {
        processMouseEvents(e);
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component but no
     * buttons have been pushed.
     *
     * @param e the <code>MouseEvent</code>
     */
    public void mouseMoved(MouseEvent e) {
        processMouseEvents(e);
    }

    /**
     * Processes all caught <code>MouseEvent</code>s.
     *
     * @param e the <code>MouseEvent</code>
     */
    private void processMouseEvents(MouseEvent e) {

        int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());

        if (tabNumber < 0) {
            return;
        }

        CloseTabIcon icon = (CloseTabIcon) getIconAt(tabNumber);

        if (icon != null) {
            // TODO: draw states of button '+'
            if (icon instanceof AddTabIcon && (e.getID() == MouseEvent.MOUSE_CLICKED)) {
//				System.out.println("dodaj!");
                tabButtonPressed();
                return;
            }

            /* allow all tabs closing feature */
            if (this.getTabCount() <= (allowAllTabsClosing ? (addTabLabel != null ? 1 : 0)
                    : addTabLabel != null ? 2 : 1)) {
                return;
            }

            Rectangle rect = icon.getBounds();
            Point pos = headerViewport == null ? new Point() : headerViewport
                    .getViewPosition();
            Rectangle drawRect = new Rectangle(rect.x - pos.x, rect.y - pos.y,
                    rect.width, rect.height);

            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                icon.mousepressed = e.getModifiers() == MouseEvent.BUTTON1_MASK;
                repaint(drawRect);
            } else if (e.getID() == MouseEvent.MOUSE_MOVED
                    || e.getID() == MouseEvent.MOUSE_DRAGGED
                    || e.getID() == MouseEvent.MOUSE_CLICKED) {
                pos.x += e.getX();
                pos.y += e.getY();
                if (rect.contains(pos)) {
                    if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                        int selIndex = getSelectedIndex();
                        if (fireCloseTab(selIndex)) {
                            if (selIndex > 0) {
                                // to prevent uncatchable null-pointers
                                Rectangle rec = getUI().getTabBounds(this,
                                        selIndex - 1);

                                MouseEvent event = new MouseEvent((Component) e
                                        .getSource(), e.getID() + 1, System
                                        .currentTimeMillis(), e.getModifiers(),
                                        rec.x, rec.y, e.getClickCount(), e
                                        .isPopupTrigger(), e
                                        .getButton());
                                dispatchEvent(event);
                            }
                            // the tab is being closed
                            // removeTabAt(tabNumber);
                            //remove(selIndex);
                            removeTabAt(selIndex);
                        } else {
                            icon.mouseover = false;
                            icon.mousepressed = false;
                            repaint(drawRect);
                        }
                    } else {
                        icon.mouseover = true;
                        icon.mousepressed = e.getModifiers() == MouseEvent.BUTTON1_MASK;
                    }
                } else {
                    icon.mouseover = false;
                }
                repaint(drawRect);
            }
        }
    }

    /**
     * Adds an <code>CloseableTabbedPaneListener</code> to the tabbedpane.
     *
     * @param l the <code>CloseableTabbedPaneListener</code> to be added
     */
    public void addCloseableTabbedPaneListener(CloseableTabbedPaneListener l) {
        listenerList.add(CloseableTabbedPaneListener.class, l);
    }

    /**
     * Removes an <code>CloseableTabbedPaneListener</code> from the tabbedpane.
     *
     * @param l the listener to be removed
     */
    public void removeCloseableTabbedPaneListener(CloseableTabbedPaneListener l) {
        listenerList.remove(CloseableTabbedPaneListener.class, l);
    }

    /**
     * Returns an array of all the <code>SearchListener</code>s added to this
     * <code>SearchPane</code> with addSearchListener().
     *
     * @return all of the <code>SearchListener</code>s added or an empty array
     * if no listeners have been added
     */
    public CloseableTabbedPaneListener[] getCloseableTabbedPaneListener() {
        return listenerList.getListeners(CloseableTabbedPaneListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type.
     *
     * @param tabIndexToClose the index of the tab which should be closed
     * @return true if the tab can be closed, false otherwise
     */
    protected boolean fireCloseTab(int tabIndexToClose) {
        boolean closeit = true;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        for (Object i : listeners) {
            if (i instanceof CloseableTabbedPaneListener) {
                if (!((CloseableTabbedPaneListener) i)
                        .closeTab(tabIndexToClose)) {
                    closeit = false;
                    break;
                }
            }
        }
        return closeit;
    }

    /**
     * @param b value true means that all tabs may be closed, value false means
     * that after opening more than one tab, at least one must stay opened
     *
     */
    public void setAllowAllTabsClosing(boolean b) {
        this.allowAllTabsClosing = b;
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. When tab-button is active, and has been clicked, fire
     * it.
     *
     */
    protected void tabButtonPressed() {
        Object[] listeners = listenerList.getListenerList();
        for (Object i : listeners) {
            if (i instanceof CloseableTabbedPaneListener) {
                ((CloseableTabbedPaneListener) i).tabButtonClicked();
            }
        }
    }

    /**
     * The class which generates the 'X' icon for the tabs. The constructor
     * accepts an icon which is extra to the 'X' icon, so you can have tabs like
     * in JBuilder. This value is null if no extra icon is required.
     */
    class CloseTabIcon implements Icon {

        /**
         * the x position of the icon
         */
        private int x_pos;

        /**
         * the y position of the icon
         */
        private int y_pos;

        /**
         * the width the icon
         */
        private int width;

        /**
         * the height the icon
         */
        private int height;

        /**
         * the additional fileicon
         */
        private Icon fileIcon;

        /**
         * true whether the mouse is over this icon, false otherwise
         */
        private boolean mouseover = false;

        /**
         * true whether the mouse is pressed on this icon, false otherwise
         */
        private boolean mousepressed = false;

        /**
         * Creates a new instance of <code>CloseTabIcon</code>
         *
         * @param fileIcon the additional fileicon, if there is one set
         */
        public CloseTabIcon(Icon fileIcon) {
            this.fileIcon = fileIcon;
            width = 16;
            height = 16;
        }

        /**
         * Draw the icon at the specified location. Icon implementations may use
         * the Component argument to get properties useful for painting, e.g.
         * the foreground or background color.
         *
         * @param c the component which the icon belongs to
         * @param g the graphic object to draw on
         * @param x the upper left point of the icon in the x direction
         * @param y the upper left point of the icon in the y direction
         */
        public void paintIcon(Component c, Graphics g, int x, int y) {
            boolean doPaintCloseIcon = true;
            try {
                // JComponent.putClientProperty("isClosable", new
                // Boolean(false));
                JTabbedPane tabbedpane = (JTabbedPane) c;
                int tabNumber = tabbedpane.getUI().tabForCoordinate(tabbedpane,
                        x, y);
                JComponent curPanel = (JComponent) tabbedpane
                        .getComponentAt(tabNumber);
                Object prop = null;
                if ((prop = curPanel.getClientProperty("isClosable")) != null) {
                    doPaintCloseIcon = (Boolean) prop;
                }
            } catch (Exception ignored) {
                /* Could probably be a ClassCastException */
            }
            if (doPaintCloseIcon) {
                x_pos = x;
                y_pos = y;
                int y_p = y + 1;

                if (normalCloseIcon != null && !mouseover) {
                    normalCloseIcon.paintIcon(c, g, x, y_p);
                } else if (hooverCloseIcon != null && mouseover
                        && !mousepressed) {
                    hooverCloseIcon.paintIcon(c, g, x, y_p);
                } else if (pressedCloseIcon != null && mousepressed) {
                    pressedCloseIcon.paintIcon(c, g, x, y_p);
                } else {
                    y_p++;

                    Color col = g.getColor();

                    if (mousepressed && mouseover) {
                        g.setColor(Color.WHITE);
                        g.fillRect(x + 1, y_p, 12, 13);
                    }

                    g.setColor(Color.GRAY);
                    g.drawLine(x + 1, y_p, x + 12, y_p);
                    g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);
                    g.drawLine(x, y_p + 1, x, y_p + 12);
                    g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);
                    g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);
                    if (mouseover) {
                        g.setColor(Color.BLACK);
                    }
                    g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);
                    g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);
                    g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);
                    g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);
                    g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);
                    g.setColor(col);
                    if (fileIcon != null) {
                        fileIcon.paintIcon(c, g, x + width, y_p);
                    }
                }
            }
        }

        /**
         * Returns the icon's width.
         *
         * @return an int specifying the fixed width of the icon.
         */
        public int getIconWidth() {
            return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);
        }

        /**
         * Returns the icon's height.
         *
         * @return an int specifying the fixed height of the icon.
         */
        public int getIconHeight() {
            return height;
        }

        /**
         * Gets the bounds of this icon in the form of a <code>Rectangle<code>
         * object. The bounds specify this icon's width, height, and location
         * relative to its parent.
         *
         * @return a rectangle indicating this icon's bounds
         */
        public Rectangle getBounds() {
            return new Rectangle(x_pos, y_pos, width, height);
        }
    } // CloseTabIcon

    /**
     * The class which generates the '+' icon for the tabs. The constructor
     * accepts an icon which is extra to the '+' icon, so you can have tabs like
     * in JBuilder. This value is null if no extra icon is required.
     */
    // TODO: implement '+' and use it at add tab
    class AddTabIcon extends CloseTabIcon {

        /**
         * the x position of the icon
         */
        private int x_pos;

        /**
         * the y position of the icon
         */
        private int y_pos;

        /**
         * the width the icon
         */
        private int width;

        /**
         * the height the icon
         */
        private int height;

        /**
         * the additional fileicon
         */
        private Icon fileIcon;

        /**
         * true whether the mouse is over this icon, false otherwise
         */
        private boolean mouseover = false;

        /**
         * true whether the mouse is pressed on this icon, false otherwise
         */
        private boolean mousepressed = false;

        /**
         * Creates a new instance of <code>CloseTabIcon</code>
         *
         * @param fileIcon the additional fileicon, if there is one set
         */
        public AddTabIcon(Icon fileIcon) {
            super(fileIcon);
            this.fileIcon = fileIcon;
            width = 16;
            height = 16;
        }

        /**
         * Draw the icon at the specified location. Icon implementations may use
         * the Component argument to get properties useful for painting, e.g.
         * the foreground or background color.
         *
         * @param c the component which the icon belongs to
         * @param g the graphic object to draw on
         * @param x the upper left point of the icon in the x direction
         * @param y the upper left point of the icon in the y direction
         */
        public void paintIcon(Component c, Graphics g, int x, int y) {
            boolean doPaintAddIcon = true;
            try {
                // JComponent.putClientProperty("isClosable", new
                // Boolean(false));
                JTabbedPane tabbedpane = (JTabbedPane) c;
                int tabNumber = tabbedpane.getUI().tabForCoordinate(tabbedpane,
                        x, y);
                JComponent curPanel = (JComponent) tabbedpane
                        .getComponentAt(tabNumber);
                Object prop = null;
                if ((prop = curPanel.getClientProperty("isAddTab")) != null) {
                    doPaintAddIcon = (Boolean) prop;
                }
            } catch (Exception ignored) {
                /* Could probably be a ClassCastException */
            }
            if (doPaintAddIcon) {
                x_pos = x;
                y_pos = y;
                int y_p = y + 1;

                if (normalCloseIcon != null && !mouseover) {
                    normalCloseIcon.paintIcon(c, g, x, y_p);
                } else if (hooverCloseIcon != null && mouseover
                        && !mousepressed) {
                    hooverCloseIcon.paintIcon(c, g, x, y_p);
                } else if (pressedCloseIcon != null && mousepressed) {
                    pressedCloseIcon.paintIcon(c, g, x, y_p);
                } else {
                    y_p++;

                    Color col = g.getColor();

                    if (mousepressed && mouseover) {
                        g.setColor(Color.WHITE);
                        g.fillRect(x + 1, y_p, 12, 13);
                    }

                    g.setColor(Color.BLACK);
                    g.drawLine(x + 1, y_p, x + 12, y_p);
                    g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);
                    g.drawLine(x, y_p + 1, x, y_p + 12);
                    g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);
                    if (mouseover) {
                        g.setColor(Color.GRAY);
                    }
                    g.drawLine(x + 3, y_p + 7, x + 10, y_p + 7);
                    g.drawLine(x + 3, y_p + 6, x + 10, y_p + 6);
                    g.drawLine(x + 7, y_p + 3, x + 7, y_p + 10);
                    g.drawLine(x + 6, y_p + 3, x + 6, y_p + 10);
                    g.setColor(col);
                    if (fileIcon != null) {
                        fileIcon.paintIcon(c, g, x + width, y_p);
                    }
                }
            }
        }

        /**
         * Returns the icon's width.
         *
         * @return an int specifying the fixed width of the icon.
         */
        public int getIconWidth() {
            return width + (fileIcon != null ? fileIcon.getIconWidth() : 0);
        }

        /**
         * Returns the icon's height.
         *
         * @return an int specifying the fixed height of the icon.
         */
        public int getIconHeight() {
            return height;
        }

        /**
         * Gets the bounds of this icon in the form of a <code>Rectangle<code>
         * object. The bounds specify this icon's width, height, and location
         * relative to its parent.
         *
         * @return a rectangle indicating this icon's bounds
         */
        public Rectangle getBounds() {
            return new Rectangle(x_pos, y_pos, width, height);
        }
    } // AddTabIcon

    /**
     * A specific <code>BasicTabbedPaneUI</code>.
     */
    class CloseableTabbedPaneUI extends BasicTabbedPaneUI {

        /**
         * the horizontal position of the text
         */
        private int horizontalTextPosition = SwingUtilities.LEFT;

        /**
         * Creates a new instance of <code>CloseableTabbedPaneUI</code>
         */
        public CloseableTabbedPaneUI() {
        }

        /**
         * Creates a new instance of <code>CloseableTabbedPaneUI</code>
         *
         * @param horizontalTextPosition the horizontal position of the text
         * (e.g. SwingUtilities.TRAILING or SwingUtilities.LEFT)
         */
        public CloseableTabbedPaneUI(int horizontalTextPosition) {
            this.horizontalTextPosition = horizontalTextPosition;
        }

        /**
         * Layouts the label
         *
         * @param tabPlacement the placement of the tabs
         * @param metrics the font metrics
         * @param tabIndex the index of the tab
         * @param title the title of the tab
         * @param icon the icon of the tab
         * @param tabRect the tab boundaries
         * @param iconRect the icon boundaries
         * @param textRect the text boundaries
         * @param isSelected true whether the tab is selected, false otherwise
         */
        protected void layoutLabel(int tabPlacement, FontMetrics metrics,
                int tabIndex, String title, Icon icon, Rectangle tabRect,
                Rectangle iconRect, Rectangle textRect, boolean isSelected) {

            textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

            javax.swing.text.View v = getTextViewForTab(tabIndex);
            if (v != null) {
                tabPane.putClientProperty("html", v);
            }

            SwingUtilities.layoutCompoundLabel((JComponent) tabPane, metrics,
                    title, icon, SwingUtilities.CENTER, SwingUtilities.CENTER,
                    SwingUtilities.CENTER,
                    // SwingUtilities.TRAILING,
                    horizontalTextPosition, tabRect, iconRect, textRect,
                    textIconGap + 2);

            tabPane.putClientProperty("html", null);

            int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
            int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
            iconRect.x += xNudge;
            iconRect.y += yNudge;
            textRect.x += xNudge;
            textRect.y += yNudge;
        }
    } // class CloseableTabbedPaneUI

    /**
     * A specific <code>MetalTabbedPaneUI</code>.
     */
    class CloseableMetalTabbedPaneUI extends MetalTabbedPaneUI {

        /**
         * the horizontal position of the text
         */
        private int horizontalTextPosition = SwingUtilities.LEFT;

        /**
         * Creates a new instance of <code>CloseableMetalTabbedPaneUI</code>
         */
        public CloseableMetalTabbedPaneUI() {
        }

        /**
         * Creates a new instance of <code>CloseableMetalTabbedPaneUI</code>
         *
         * @param horizontalTextPosition the horizontal position of the text
         * (e.g. SwingUtilities.TRAILING or SwingUtilities.LEFT)
         */
        public CloseableMetalTabbedPaneUI(int horizontalTextPosition) {
            this.horizontalTextPosition = horizontalTextPosition;
        }

        /**
         * Layouts the label
         *
         * @param tabPlacement the placement of the tabs
         * @param metrics the font metrics
         * @param tabIndex the index of the tab
         * @param title the title of the tab
         * @param icon the icon of the tab
         * @param tabRect the tab boundaries
         * @param iconRect the icon boundaries
         * @param textRect the text boundaries
         * @param isSelected true whether the tab is selected, false otherwise
         */
        protected void layoutLabel(int tabPlacement, FontMetrics metrics,
                int tabIndex, String title, Icon icon, Rectangle tabRect,
                Rectangle iconRect, Rectangle textRect, boolean isSelected) {

            textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

            javax.swing.text.View v = getTextViewForTab(tabIndex);
            if (v != null) {
                tabPane.putClientProperty("html", v);
            }

            SwingUtilities.layoutCompoundLabel((JComponent) tabPane, metrics,
                    title, icon, SwingUtilities.CENTER, SwingUtilities.CENTER,
                    SwingUtilities.CENTER,
                    // SwingUtilities.TRAILING,
                    horizontalTextPosition, tabRect, iconRect, textRect,
                    textIconGap + 2);

            tabPane.putClientProperty("html", null);

            int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
            int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
            iconRect.x += xNudge;
            iconRect.y += yNudge;
            textRect.x += xNudge;
            textRect.y += yNudge;
        }
    } // class CloseableMetalTabbedPaneUI

}
