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

import com.alee.laf.list.WebList;
import com.alee.laf.panel.WebPanel;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.LockerChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.SynsetSelectionChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.VertexSelectionChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.ViwnVertexToolTipTransformer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.renderers.ViwnVertexFillColor;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.renderers.ViwnVertexRenderer;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import se.datadosen.component.RiverLayout;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author amusial
 */
public class ViwnLockerViewUI extends AbstractViewUI
        implements ListSelectionListener, VertexSelectionChangeListener<ViwnNode> {

    /**
     * Holds MarkedElements to avoid adding same element more than one time
     */
    private List<LockerElement> list = new ArrayList<>();

    /**
     * DefaultListModel give a possibility to add and remove elements to JList
     */
    private DefaultListModel dlm;
    private WebList jl;

    /**
     * This JPopupMenu is generated dynamically after clicking at the jl, its
     * JMenuItems depends on a type of MarkedElement
     */
    private JPopupMenu jpm;

    /**
     * Collection of object which listen for an event of synset selection
     * change.
     */
    protected Collection<SynsetSelectionChangeListener> synsetSelectionChangeListeners = new ArrayList<>();

    /**
     * Collection of object which listen for an event of locker focus change
     */
    protected Collection<LockerChangeListener> lockerSelectionChangeListeners = new ArrayList<>();

    @Override
    public JComponent getRootComponent() {
        return null;
    }

    @Override
    protected void initialize(WebPanel content) {

        content.setLayout(new RiverLayout());

        dlm = new DefaultListModel();

        jl = new WebList(dlm);
        jl.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        jl.setVisibleRowCount(1);
        jl.setCellRenderer(new CustomCellRenderer());
        jl.addMouseListener(new LockerMouseAdapter());
        jl.addListSelectionListener(this);

        content.add(new JScrollPane(jl), "hfill vfill");

    }

    /**
     * Adds new marked element to locker/clipboard new element cannot be added
     * to list if element with same node and ui is already on list element is
     * added to list and dlm view is refreshed after this action automatically
     *
     * @param vn       node - a synset to add
     * @param renderer
     */
    public void addToLocker(Object vn, LockerElementRenderer renderer) {

        LockerElement newME = new LockerElement(vn, renderer);
        for (LockerElement me : list) {
            if (me.equals(newME)) {
                jl.ensureIndexIsVisible(dlm.indexOf(newME));
                return;
            }
        }

        // ta cała klasa to jakaś kpina
        list.add(newME);
        dlm.addElement(newME);
        jl.ensureIndexIsVisible(dlm.indexOf(newME));
    }

    /**
     * Remove marked element from locker view is refreshed after this action
     * automatically
     *
     * @param me marked element to remove from locker
     */
    public void remFromLocker(LockerElement me) {
        list.remove(me);
        dlm.removeElement(me);
    }

    @Override
    public void vertexSelectionChange(ViwnNode vertex) {
        for (SynsetSelectionChangeListener l : synsetSelectionChangeListeners) {
            l.synsetSelectionChangeListener(vertex);
        }
    }

    /**
     * Inform all objects waiting for locker selection changes.
     *
     * @param me <code>MarkedElement</code> that get focus
     */
    private void fireLockerSelectionChanged(LockerElement me) {
        for (LockerChangeListener lcl : lockerSelectionChangeListeners) {
            if (me.val instanceof ViwnNode) {
                ViwnNode ret = lcl.lockerSelectionChanged((ViwnNode) me.val);
                if (ret != me.val) {
                    me.val = ret;
                }
            }
        }
    }

    /**
     * ListSelectionChangeListener
     *
     * @param lse
     */
    @Override
    public void valueChanged(ListSelectionEvent lse) {
        // When the user release the mouse button and completes the selection,
        // getValueIsAdjusting() becomes false
        if (!lse.getValueIsAdjusting()) {
            if (jl.getSelectedIndex() >= 0) {
                if (((LockerElement) jl.getSelectedValue()).val instanceof ViwnNode) {
                    vertexSelectionChange((ViwnNode) ((LockerElement) jl.getSelectedValue()).val);
                }
                jl.ensureIndexIsVisible(jl.getSelectedIndex());
            }
        }
    }

    /**
     * @param cursor
     */
    public void setCursor(Cursor cursor) {
        jl.setCursor(cursor);
    }

    private class LockerElement extends JPanel {

        Object val;
        LockerElementType type;

        private static final long serialVersionUID = 910828919149035565L;

        protected LockerElement() {
            super();
            set();
        }

        private void set() {
            setLayout(new RiverLayout());
        }

        /**
         * @param o        object to store in locker
         * @param renderer class providing ability to make an panel from object
         */
        public LockerElement(Object o, LockerElementRenderer renderer) {
            this();

            val = o;

            setType(o);

            renderer.renderElement(this, o);

        }

        /**
         * sets type of stored element
         *
         * @param o object stored in locker
         * @return type... no need, but...
         */
        private LockerElementType setType(Object o) {
            if (o instanceof ViwnNode) {
                type = LockerElementType.SYNSET;
                return LockerElementType.SYNSET;
            } else if (o instanceof Sense) {
                type = LockerElementType.LEXICAL_UNIT;
                return LockerElementType.LEXICAL_UNIT;
            }
            type = LockerElementType.OBJECT;
            return LockerElementType.OBJECT;
        }

        /* TODO: probably a comparator will be needed to avoid
         * adding same LockerElement two times
		 * */
        @Override
        public boolean equals(Object o) {
            return (o != null
                    && getClass().equals(o.getClass())
                    && val.equals(((LockerElement) o).val));
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 71 * hash + Objects.hashCode(val);
            return hash;
        }

    }

    private class CustomCellRenderer implements ListCellRenderer {

        @Override
        public java.awt.Component getListCellRendererComponent(JList list,
                                                               Object value, int index, boolean isSelected,
                                                               boolean cellHasFocus) {
            if (value instanceof Component) {
                Component c = (Component) value;
                c.setBackground(isSelected ? Color.lightGray : Color.white);
                return c;
            } else {
                // nie powinno się zdarzyć, cóż, szybki fix
                throw new RuntimeException("exception in CustomCellRenderer in ViwnLockerViewUI class, wrong value!");
            }
        }
    }

    private class LockerMouseAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent mE) {
            ViWordNetService s = (ViWordNetService) workbench.getService("pl.edu.pwr.wordnetloom.client.plwordnet.plugins.viwordnet.ViWordNetService");
            // if make relation mode is on
            if (s.isMakeRelationModeOn()
                    && jl.locationToIndex(mE.getPoint()) != -1) {
                if (SwingUtilities.isRightMouseButton(mE)) // right mouse button to simply back to normal mode
                {
                    s.switchMakeRelationMode();
                } else {
                    // left mouse button to make relation
                    s.makeRelation(list.get(jl.locationToIndex(mE.getPoint())).val);
                }
            } else if (s.isMergeSynsetsModeOn()
                    && jl.locationToIndex(mE.getPoint()) != -1) {
                if (SwingUtilities.isRightMouseButton(mE)) // right mouse button to simply back to normal mode
                {
                    s.switchMergeSynsetsMode();
                } else {
                    // left mouse button to make relation
                    s.mergeSynsets(list.get(jl.locationToIndex(mE.getPoint())).val);
                }
            } else // normal mode
            {
                if (SwingUtilities.isRightMouseButton(mE)
                        && jl.locationToIndex(mE.getPoint()) != -1) {
                    jpm = getPopup((LockerElement) dlm.elementAt(
                            jl.locationToIndex(mE.getPoint())));
                    jpm.show(jl, mE.getX(), mE.getY());
                } else if (mE.getClickCount() == 2
                        && jl.locationToIndex(mE.getPoint()) != -1) {
                    // TODO: checkme-
                    fireLockerSelectionChanged(list.get(jl.locationToIndex(mE.getPoint())));
                }
            }
        }

        /**
         * @param me clicked LockerElement
         * @return popup menu for this LockerElement
         */
        public JPopupMenu getPopup(final LockerElement me) {
            JPopupMenu jpm = new JPopupMenu();

            jpm.add(new AbstractAction("Usuń ze schowka") {

                private static final long serialVersionUID = -4830433852737055434L;

                @Override
                public void actionPerformed(ActionEvent ae) {
                    remFromLocker(me);
                }
            });

            switch (me.type) {
                case SYNSET:
                    /* TODO: probably this option should not be always visible */
                    jpm.add(new AbstractAction("Utwórz relację z ...") {
                        /**
                         *
                         */
                        private static final long serialVersionUID = -987233321164815L;

                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            /* TODO: implement me
                         * mark or something else,
						 * action should be handled at upper level
						 * for example by service
						 * */
                            ViWordNetService s = (ViWordNetService) workbench.getService("pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService");
                            s.setFirstMakeRelation(me.val);
                        }
                    });

                    jpm.add(new AbstractAction("Połącz synset z ...") {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            ViWordNetService s = (ViWordNetService) workbench.getService("pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService");
                            s.setFirstMergeSynsets(me.val);
                        }
                    });
                    break;
                case LEXICAL_UNIT:
                    /* TODO: implement me
                 * generate popup menu and it actions for lexical unit
				 * */
                    jpm.add(new AbstractAction("Utwórz relację z jednostką ...") {

                        private static final long serialVersionUID = -987233321164815L;

                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            /* TODO: implement me
                         * mark or something else,
						 * action should be handled at upper level
						 * for example by service
						 * */
                            ViWordNetService s = (ViWordNetService) workbench.getService("pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService");
                            s.setFirstMakeRelation(me.val);
                        }
                    });
                    break;
            }

            return jpm;
        }
    }

    public enum LockerElementType {
        SYNSET,
        LEXICAL_UNIT,
        OBJECT
    }

    public abstract class LockerElementRenderer {

        public abstract void renderElement(JPanel jp, Object elem);
    }

    public class ViwnNodeRenderer extends LockerElementRenderer {

        @Override
        public void renderElement(JPanel jp, Object elem) {
            if (elem instanceof ViwnNode) {
                ViwnNode vn = (ViwnNode) elem;

                // one node visualisation
                Graph<ViwnNode, ViwnEdge> g = new DirectedSparseGraph<>();
                g.addVertex(vn);
                VisualizationViewer<ViwnNode, ViwnEdge> vv = new VisualizationViewer<>(new StaticLayout<>(g));
                vv.getRenderer().setVertexRenderer(new ViwnVertexRenderer(vv.getRenderer().getVertexRenderer()));

                vv.getRenderContext().setVertexShapeTransformer((ViwnNode v) -> v.getShape());

                vv.getRenderContext().setVertexFillPaintTransformer(new ViwnVertexFillColor(vv.getPickedVertexState(), null));
                vv.setVertexToolTipTransformer(new ViwnVertexToolTipTransformer());
                vv.setPreferredSize(new Dimension(110, 50));
                Point2D q = vv.getGraphLayout().transform(vn);
                Point2D lvc = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(vv.getCenter());
                vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(lvc.getX() - q.getX(), lvc.getY() - q.getY());
                jp.add(vv, "vfill br");
            }
        }
    }

    public SenseRenderer getSenseRenderer(){
        return new SenseRenderer();
    }

    /**
     * @author amusial
     */
    public class SenseRenderer extends LockerElementRenderer {

        @Override
        public void renderElement(JPanel jp, Object unit) {
            if (unit instanceof Sense) {
                Sense lu = (Sense) unit;
                jp.setLayout(new GridBagLayout());
                jp.add(new JLabel(lu.toString()));
            }
        }
    }

    /**
     * @return VivnLockerViewUI... ugly hack to get access to those class above
     */
    public static ViwnLockerViewUI getInstance() {
        return new ViwnLockerViewUI();
    }

    public void refreshData() {
        list.stream().filter((l) -> (l.val instanceof ViwnNodeSynset)).map((l) -> (ViwnNodeSynset) l.val).forEach((s) -> {
            s.setLabel(null);
        });
        jl.repaint();
    }

}
