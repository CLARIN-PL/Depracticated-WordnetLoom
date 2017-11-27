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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.control;

import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;

/**
 * @author amusial
 */
public final class ViwnGraphViewModalGraphMouse extends AbstractModalGraphMouse
        implements ModalGraphMouse, ItemSelectable {

    protected ViwnGraphViewPopupGraphMousePlugin popupPlugin;
    protected MultiLayerTransformer basicTransformer;
    protected RenderContext<ViwnNode, ViwnEdge> rc;

    protected ViwnGraphViewUI vgvui;

    /**
     * default constructor sets default scaling factor
     *
     * @param vgvui visualisation view
     */
    public ViwnGraphViewModalGraphMouse(ViwnGraphViewUI vgvui) {
        this(1.1f, 1 / 1.1f, vgvui);
    }

    /**
     * create an instance with passed values
     *
     * @param in    override value for scale in
     * @param out   override value for scale out
     * @param vgvui viwn visualisation view ui
     */
    protected ViwnGraphViewModalGraphMouse(float in, float out, ViwnGraphViewUI vgvui) {
        super(in, out);
        this.vgvui = vgvui;
        loadPlugins();
    }

    @Override
    protected void loadPlugins() {
        pickingPlugin = new ViwnPickingGraphMousePlugin();
        translatingPlugin = new ViwnTranslatingGraphMousePlugin();

        scalingPlugin = new ScalingGraphMousePlugin(new ViewScalingControl(), 0, in, out);
        popupPlugin = new ViwnGraphViewPopupGraphMousePlugin(vgvui);

        add(scalingPlugin);
        add(popupPlugin);

        setUniversalMode();
    }

    /**
     * setter for the Mode.
     *
     * @param mode
     */
    @Override
    public void setMode(Mode mode) {
        if (this.mode != mode) {
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
                    this.mode, ItemEvent.DESELECTED));
            this.mode = mode;
            switch (mode) {
                case TRANSFORMING:
                    setTransformingMode();
                    break;
                case PICKING:
                    setPickingMode();
                    break;
            }
            if (modeBox != null) {
                modeBox.setSelectedItem(mode);
            }
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, mode, ItemEvent.SELECTED));
        }
    }

    @Override
    protected void setPickingMode() {
        remove(translatingPlugin);
        add(pickingPlugin);
    }

    @Override
    protected void setTransformingMode() {
        remove(pickingPlugin);
        add(translatingPlugin);
    }

    /**
     * special mouse mode
     */
    public void setUniversalMode() {
        add(pickingPlugin);
        add(translatingPlugin);
    }

    /**
     * @return the modeBox.
     */
    @Override
    public JComboBox getModeComboBox() {
        if (modeBox == null) {
            modeBox = new JComboBox(new Mode[]{Mode.TRANSFORMING, Mode.PICKING, Mode.EDITING, Mode.ANNOTATING});
            modeBox.addItemListener(getModeListener());
        }
        modeBox.setSelectedItem(mode);
        return modeBox;
    }

    /**
     * ViwnPickingGraphMousePlugin is an extension of PickingGraphMousePlugin
     * Cursor changed... why JUNG do not allow this?
     */
    private class ViwnPickingGraphMousePlugin extends PickingGraphMousePlugin<ViwnNode, ViwnEdge> {

        public ViwnPickingGraphMousePlugin() {
            super(InputEvent.BUTTON1_MASK, InputEvent.BUTTON1_MASK | InputEvent.CTRL_MASK);
            cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        }

        public void mouseEntered(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            ViWordNetService s = (ViWordNetService) vgvui.getWorkbench().getService(Service.class.getName());
            if (s != null && !s.isMakeRelationModeOn()) {
                c.setCursor(cursor);
            }
        }

        public void mouseExited(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            ViWordNetService s = (ViWordNetService) vgvui.getWorkbench().getService(Service.class.getName());
            if (s != null && !s.isMakeRelationModeOn()) {
                c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    /**
     * ViwnTranslatingGraphMousePlugin is a combination of
     * TranslatingGraphMousePlugin and AnimatedPickingGraphMousePlugin when
     * vertex is clicked it is centered when mouse is dragged, screen translates
     */
    private class ViwnTranslatingGraphMousePlugin extends TranslatingGraphMousePlugin {

        public ViwnTranslatingGraphMousePlugin() {
            super(InputEvent.BUTTON2_MASK);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TRANSLATING PART
            VisualizationViewer<ViwnNode, ViwnEdge> vv = (VisualizationViewer<ViwnNode, ViwnEdge>) e.getSource();
            boolean accepted = checkModifiers(e);
            down = e.getPoint();
            if (accepted) {
                vv.setCursor(cursor);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TRANSLATING PART
            final VisualizationViewer<ViwnNode, ViwnEdge> vv = (VisualizationViewer<ViwnNode, ViwnEdge>) e.getSource();
            if (down != null) {
                vv.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            down = null;
        }

    }

}
