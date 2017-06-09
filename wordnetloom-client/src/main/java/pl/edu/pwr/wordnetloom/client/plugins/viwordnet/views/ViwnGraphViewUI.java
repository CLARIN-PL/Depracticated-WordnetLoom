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

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.LayoutScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ViewScalingControl;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.collections15.Transformer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.CriteriaDTO;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.GraphChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.SynsetSelectionChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.VertexSelectionChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.ViwnGraphMouseListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdgeCand;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdgeSet;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdgeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode.Direction;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeCand;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeCandExtension;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeRoot;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSet;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeWord;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.control.ViwnGraphViewModalGraphMouse;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.ViwnEdgeStrokeTransformer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.ViwnVertexToolTipTransformer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.layout.ViwnLayout2;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.renderers.AstrideLabelRenderer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.renderers.ViwnVertexFillColor;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.renderers.ViwnVertexRenderer;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.model.dto.DataEntry;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;
import pl.edu.pwr.wordnetloom.model.wordnet.SynsetRelation;
import se.datadosen.component.RiverLayout;

/**
 * @author boombel
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 */
public class ViwnGraphViewUI extends AbstractViewUI implements
        VertexSelectionChangeListener<ViwnNode> {

    /**
     * A graph of synset and relations between synsets.
     */
    private final DirectedGraph<ViwnNode, ViwnEdge> forest = new DirectedSparseMultigraph<>();

    private final HashMap<Long, ViwnNodeSynset> cache = new HashMap<>();

    private final ViwnLayout2 layout = new ViwnLayout2(forest);

    private VisualizationViewer<ViwnNode, ViwnEdge> vv = null;

    private ViwnNodeRoot rootNode = null;

    // Collection of object which listen for an event of synset selection
    // change.
    protected Collection<SynsetSelectionChangeListener> synsetSelectionChangeListeners = new ArrayList<>();

    // Collection of objects which listen for an event of graph changes
    protected Collection<GraphChangeListener> graphChangeListeners = new ArrayList<>();

    private ViwnNode selectedNode = null;

    private final CriteriaDTO criteria = new CriteriaDTO();
    private int openedFromTabIndex;

    private final ScalingControl scaler = new ViewScalingControl();

    // Graph mouse listener, handles mouse clicks at vertices
    private ViwnGraphMouseListener graphMouseListener = null;

    private static final float EDGE_PICK_SIZE = 10f;
    final int MAX_SYNSETS_SHOWN = 4;
    final int MIN_SYNSETS_IN_GROUP = 2;

    /* Transient cache for graph biulding */
    private HashMap<Long, DataEntry> entrySets = new HashMap<>();

    public void setEntrySets(HashMap<Long, DataEntry> entrySets) {
        this.entrySets = entrySets;
    }

    public DataEntry getEntrySetFor(Long id) {
        return entrySets.get(id); // if there is no cache returns null
    }

    public void addSynsetToCash(Long synsetId, ViwnNodeSynset node) {
        cache.put(synsetId, node);
    }

    public List<SynsetRelation> getRelationsFor(Long id) {
        DataEntry e = getEntrySetFor(id);
        if (e == null) {
            return new ArrayList<>();
        }

        ArrayList<SynsetRelation> rels = new ArrayList<>();
        rels.addAll(e.getRelsFrom());
        rels.addAll(e.getRelsTo());

        return rels;
    }

    public List<SynsetRelation> getUpperRelationsFor(Long id) {
        DataEntry e = getEntrySetFor(id);
        if (e == null) {
            return null;
        }

        return e.getRelsFrom();
    }

    public List<SynsetRelation> getSubRelationsFor(Long id) {
        DataEntry e = getEntrySetFor(id);
        if (e == null) {
            return null;
        }

        return e.getRelsTo();
    }

    public void releaseDataSetCache() {
        entrySets.clear();
    }

    /* End of transient cache for graph biulding */
    @Override
    public JComponent getRootComponent() {
        return null;
    }

    /**
     * @param content JPanel from workbench
     * @author amusial
     *
     */
    @Override
    protected void initialize(JPanel content) {
        content.removeAll();
        content.setLayout(new RiverLayout());

        // Create a panel for graph visualisation.
        JPanel graph;
        try {
            graph = this.getSampleGraphViewer();
            content.add(graph, "hfill vfill");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @author amusial
     * @return JPanel filled with jung's graph visualization
     * @throws IOException from inside methods
     *
     */
    private JPanel getSampleGraphViewer() throws IOException {
        vv = new VisualizationViewer<>(layout);

        vv.getRenderer().setVertexRenderer(
                new ViwnVertexRenderer(vv.getRenderer().getVertexRenderer()));
        HashMap<RenderingHints.Key, Object> hints = new HashMap<>();
        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        hints.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        hints.put(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
        hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        hints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);
        vv.setRenderingHints(hints);

        RenderContext<ViwnNode, ViwnEdge> rc = vv.getRenderContext();

        rc.setVertexShapeTransformer(new Transformer<ViwnNode, Shape>() {
            public Shape transform(ViwnNode v) {
                return v.getShape();
            }
        });

        rc.setVertexFillPaintTransformer(new ViwnVertexFillColor(vv
                .getPickedVertexState(), rootNode));

        rc.setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<>(
                0.5, 0.5));

        rc.setEdgeIncludePredicate((Context<Graph<ViwnNode, ViwnEdge>, ViwnEdge> context) -> {
            if (context.element instanceof ViwnEdgeCand) {
                ViwnEdgeCand cand = (ViwnEdgeCand) context.element;
                return !cand.isHidden();
            }
            return true;
        });

        Transformer<ViwnEdge, Paint> edgeDrawColor = (ViwnEdge e) -> e.getColor();
        rc.setEdgeDrawPaintTransformer(edgeDrawColor);
        rc.setArrowDrawPaintTransformer(edgeDrawColor);
        rc.setArrowFillPaintTransformer(edgeDrawColor);

        rc.setEdgeStrokeTransformer(new ViwnEdgeStrokeTransformer());

        this.graphMouseListener = new ViwnGraphMouseListener(this);
        vv.addGraphMouseListener(this.graphMouseListener);

        Transformer<ViwnEdge, String> stringer = (ViwnEdge rel) -> rel.toString();
        rc.setEdgeLabelTransformer(stringer);
        rc.setEdgeShapeTransformer(new EdgeShape.Line<>());

        ViwnGraphViewModalGraphMouse gm = new ViwnGraphViewModalGraphMouse(this);
        vv.addKeyListener(gm.getModeKeyListener());

        vv.getRenderer().setEdgeLabelRenderer(new AstrideLabelRenderer<>());

        vv.setGraphMouse(gm);

        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        panel.add(vv);

        ((ShapePickSupport<ViwnNode, ViwnEdge>) rc.getPickSupport())
                .setPickSize(EDGE_PICK_SIZE);

        vv.setVertexToolTipTransformer(new ViwnVertexToolTipTransformer());

        return panel;
    }

    /**
     * @return cache_
     */
    public HashMap<Long, ViwnNodeSynset> getCache() {
        return cache;
    }

    /**
     * clears cache
     */
    public void cleanCache() {
        cache.clear();
    }

    /**
     *
     * @param synset
     */
    public void refreshView(final Synset synset) {
        // Clear the graph.
        clear();

        selectedNode = rootNode = new ViwnNodeSynset(synset, this);
        cache.put(synset.getId(), (ViwnNodeSynset) rootNode);

        vv.getRenderContext().setVertexFillPaintTransformer(
                new ViwnVertexFillColor(vv.getPickedVertexState(), rootNode));

        final ViwnNodeSynset synsetNode = (ViwnNodeSynset) rootNode;

        if (!forest.containsVertex(rootNode)) {
            forest.addVertex(rootNode);
        }

        cache.values().stream().map((n) -> {
            n.setSpawner(null, null);
            return n;
        }).filter((n) -> (n instanceof ViwnNodeSynset)).forEachOrdered((n) -> {
            for (Direction rclass : Direction.values()) {
                ViwnNodeSynset nn = (ViwnNodeSynset) n;
                nn.setState(rclass, ViwnNodeSynset.State.NOT_EXPANDED);
            }
        });

        for (Direction rel_class : Direction.values()) {
            if (rootNode instanceof ViwnNodeSynset) {
                ((ViwnNodeSynset) rootNode).setState(rel_class,
                        ViwnNodeSynset.State.EXPANDED);
            }
        }

        for (Direction dir : Direction.values()) {
            showRelationGUI(synsetNode, dir);
        }

        recreateLayout();
        center();
        this.vv.setVisible(true);
        releaseDataSetCache();
    }

    /**
     * @param root
     * @param groups
     * @param roots
     * @param freeSyns
     * @author boombel
     * @param extensions
     */
    public void loadCandidate(ViwnNodeWord root,
            ArrayList<TreeSet<ViwnNodeSynset>> groups,
            ArrayList<ViwnNodeCand> roots, ArrayList<ViwnNodeSynset> freeSyns,
            ArrayList<ViwnNodeCandExtension> extensions) {
        clear();

        rootNode = root;
        forest.addVertex(root);
        HashSet<Long> indexes = new HashSet<>();
        // Dodanie rozszerzeń w górnej części grafu

        if (extensions != null) {
            for (ViwnNodeCandExtension node : extensions) {
                roots.remove(node);
                if (node.getSpawner() == null) {
                    node.setSpawner(root, Direction.BOTTOM);
                    ViwnEdgeCand e = new ViwnEdgeCand(node.getRelName());
                    indexes.add(node.getId());
                    e.setHidden(false);
                    if (node.getExtGraphExtension().getBase()) {
                        forest.addEdge(e, rootNode, node);
                    } else {
                        forest.addEdge(e, node, rootNode);
                    }
                    forest.addVertex(node);
                }
            }
        }

        freeSyns.stream().filter((node) -> (node.getSpawner() == null)).map((node) -> {
            node.setSpawner(root, Direction.BOTTOM);
            return node;
        }).map((node) -> {
            ViwnEdgeCand e = new ViwnEdgeCand();
            e.setHidden(true);
            forest.addEdge(e, rootNode, node);
            return node;
        }).forEachOrdered((node) -> {
            forest.addVertex(node);
        });

        HashMap<ViwnNodeRoot, ArrayList<ViwnNodeSynset>> map = new HashMap<>();

        roots.forEach((r) -> {
            map.put(r, new ArrayList<>());
        });

        for (int i = 0; i < groups.size(); ++i) {
            for (ViwnNodeSynset s : groups.get(i)) {
                ArrayList<ViwnNodeSynset> list = map.get((ViwnNodeRoot) s
                        .getSpawner());
                if (list == null) {
                    list = new ArrayList<>();
                    list.add(s);
                    map.put((ViwnNodeRoot) s.getSpawner(), list);
                } else {
                    list.add(s);
                }

            }
        }

        for (ViwnNodeRoot key : map.keySet()) {
            if (key == null) {
                continue;
            }

            ArrayList<ViwnNodeRoot> add_list = new ArrayList<>();

            TreeMap<String, ArrayList<ViwnNodeRoot>> all_sorted = new TreeMap<>(
                    (String o1, String o2) -> o2.compareTo(o1));

            MultiValueMap mult_all_sorted = MultiValueMap.decorate(all_sorted);
            map.get(key).forEach((r) -> {
                mult_all_sorted.put(r.getLabel(), r);
            });

            @SuppressWarnings("unchecked")
            // legacy
            Iterator<ViwnNodeSynset> it = mult_all_sorted.values().iterator();

            int to_add = map.get(key).size() - MAX_SYNSETS_SHOWN;

            while (to_add > 0 && it.hasNext()) {
                ViwnNodeSynset s = it.next();
                ViwnNodeSet set = key.getSynsetSet(s.getSpawnDir());

                if (!forest.containsVertex(s) && map.get(s) == null) {
                    s.setSet(set);
                    set.add(s);
                    to_add--;
                } else {
                    add_list.add(s);
                }
            }

            for (Direction dir : Direction.values()) {
                ViwnNodeSet set = key.getSynsetSet(dir);

                if (set.getSynsets().size() < MIN_SYNSETS_IN_GROUP) {
                    add_list.addAll(set.getSynsets());
                    set.removeAll();
                } else if (!set.getSynsets().isEmpty()) {
                    set.setSpawner(key, dir);
                    forest.addVertex(set);

                    addEdgeSynsSet(key, set, dir);
                }
            }

            while (it.hasNext()) {
                ViwnNodeSynset s = it.next();
                forest.addVertex(s);
                if (key instanceof ViwnNodeWord) {
                    ViwnNodeCand c = (ViwnNodeCand) s;
                    ViwnEdgeCand ec = new ViwnEdgeCand();
                    if (c.isEvaluated()
                            || s.getLabel().equals("! S.y.n.s.e.t p.u.s.t.y !")
                            || indexes.contains(s.getId())) {
                        ec.setHidden(true);
                    }
                    forest.addEdge(ec, rootNode, s);
                }
            }

            add_list.stream().map((s) -> {
                forest.addVertex(s);
                return s;
            }).filter((s) -> (key instanceof ViwnNodeWord)).forEachOrdered((s) -> {
                ViwnNodeCand c = (ViwnNodeCand) s;
                ViwnEdgeCand ec = new ViwnEdgeCand();
                if (c.isEvaluated()) {
                    ec.setHidden(true);
                }
                forest.addEdge(ec, rootNode, s);
            });
        }

        forest.getVertices().stream().filter((n) -> (n instanceof ViwnNodeSynset
                && !(n instanceof ViwnNodeCandExtension))).forEachOrdered((n) -> {
                    addMissingRelations((ViwnNodeSynset) n);
        });

        checkAllStates();

        recreateLayout();
        selectedNode = rootNode;
        center();
    }

    /**
     * Recreate a graph layout. Currently fires graphChanged event, this should
     * be done somewhere else
     *
     * @author amusial
     */
    public void recreateLayout() {
        layout.mapNodes2Points(rootNode);

        graphChanged();
        /*
		 * this event above (graphChanged) should be fired only when graph
		 * changes, and not in here where layout is recreated, it should be
		 * fired when new nodes shows or some nodes hides...
         */
    }

    /**
     * place selected node in the center of the screen
     *
     * @author amusial
     *
     */
    public void center() {
        ViwnNode central;

        if (selectedNode != null) {
            central = selectedNode;
        } else if (rootNode != null) {
            central = rootNode;
        } else {
            return;
        }

        Point2D q = layout.transform(central);
        Point2D lvc = vv.getRenderContext().getMultiLayerTransformer()
                .inverseTransform(vv.getCenter());
        vv.getRenderContext().getMultiLayerTransformer()
                .getTransformer(Layer.LAYOUT)
                .translate(lvc.getX() - q.getX(), lvc.getY() - q.getY());

    }

    /**
     * deselects all graph nodes
     */
    public void deselectAll() {
        vv.getPickedVertexState().clear();
    }

    /**
     * scale graph to fill full visualization viewer space
     *
     * @author amusial
     *
     */
    protected void fillVV() {
        // scale
        // if layout was scaled, scale it to it original size
        if (vv.getRenderContext().getMultiLayerTransformer()
                .getTransformer(Layer.LAYOUT).getScaleX() > 1D) {
            (new LayoutScalingControl()).scale(vv, (1f / (float) vv
                    .getRenderContext().getMultiLayerTransformer()
                    .getTransformer(Layer.LAYOUT).getScaleX()),
                    new Point2D.Double());
        }
        // get view bounds
        Dimension vd = vv.getPreferredSize();
        if (vv.isShowing()) {
            vd = vv.getSize();
        }
        // get graph layout size
        Dimension ld = vv.getGraphLayout().getSize();
        // finally scale it if view bounds are different than graph layer bounds
        if (vd.equals(ld) == false) {
            float heightRatio = (float) (vd.getWidth() / ld.getWidth()), widthRatio = (float) (vd
                    .getHeight() / ld.getHeight());

            scaler.scale(vv, (heightRatio < widthRatio ? heightRatio
                    : widthRatio), new Point2D.Double());
        }
    }

    /**
     * @param node
     * @param rel
     */
    public void checkState(ViwnNodeSynset node, ViwnNode.Direction rel) {
        node.setState(rel, ViwnNodeSynset.State.NOT_EXPANDED);
        boolean all_ok = true;

        for (ViwnEdgeSynset e : node.getRelation(rel)) {
            ViwnNodeSynset other = cache.get(e.getParent());
            if (other != null && other.equals(node)) {
                other = cache.get(e.getChild());
            }

            if (forest.getEdges().contains(e)) {
                node.setState(rel, ViwnNodeSynset.State.SEMI_EXPANDED);
            } else if (!node.getSynsetSet(rel).getSynsets().contains(other)) {
                all_ok = false;
            }
        }

        if (all_ok) {
            if (forest.containsVertex(node.getSynsetSet(rel))
                    || node.getSynsetSet(rel).getSynsets().isEmpty()) {
                node.setState(rel, ViwnNodeSynset.State.EXPANDED);
            }
        }
    }

    /**
     * @param node
     * @param dir
     */
    public void checkStateAllInRel(ViwnNodeSynset node, Direction dir) {
        for (ViwnEdgeSynset e : node.getRelation(dir)) {
            ViwnNodeSynset test = cache.get(e.getParent());
            if (test != null && test.equals(node)) {
                test = cache.get(e.getChild());
            }
            if (test != null) {
                checkState(test, dir.getOposite());
            }
        }
    }

    /**
     * @return true when synsets can be grouped
     */
    public boolean canGroupSynsets() {
        Set<ViwnNode> picked = vv.getPickedVertexState().getPicked();
        if (picked.size() < 1) {
            return false;
        } else if (picked.size() == 1
                && picked.iterator().next() instanceof ViwnNodeSynset) {
            ViwnNodeSynset syns = (ViwnNodeSynset) picked.iterator().next();
            if (syns.equals(rootNode)) {
                return false;
            }
            if (!forest.containsVertex(syns)) {
                return false;
            }
            ViwnNodeSet s = getSetFrom(syns);
            if (!forest.containsVertex(s)) {
                return false;
            }
        }

        boolean can_group = false;
        Iterator<ViwnNode> pick_itr = picked.iterator();
        if (pick_itr.hasNext()) {

            ViwnNode spawner = pick_itr.next().getSpawner();
            if (spawner != null) {
                while (pick_itr != null && pick_itr.hasNext()) {
                    if (spawner != pick_itr.next().getSpawner()) {
                        pick_itr = null;
                    }
                }
                if (pick_itr != null) {
                    can_group = true;
                }
            }
        }
        return can_group;
    }

    private ViwnEdgeSynset findRelation(ViwnNodeSynset s1, ViwnNodeSynset s2,
            Direction rel) {
        for (ViwnEdgeSynset s : s1.getRelation(rel)) {
            if ((cache.get(s.getChild()) != null && cache.get(s.getChild())
                    .equals(s2))
                    || (cache.get(s.getParent()) != null && cache.get(
                    s.getParent()).equals(s2))) {
                return s;
            }
        }

        return null;
    }

    private void addEdge(ViwnEdge e, Long first, Long second) {
        ViwnNode v1 = cache.get(first);
        ViwnNode v2 = cache.get(second);
        if (v1 != null && v2 != null) {
            addEdge(e, (ViwnNodeSynset) v1, (ViwnNodeSynset) v2);
        }
    }

    private void addEdge(ViwnEdge e, ViwnNodeSynset first, ViwnNodeSynset second) {
        ViwnEdgeSynset se = (ViwnEdgeSynset) e;
        se.setSynset1(first);
        se.setSynset2(second);
        forest.addEdge(e, first, second);
    }

    private Collection<ViwnNodeSynset> addMissingRelations(ViwnNodeSynset synset) {
        ArrayList<ViwnNodeSynset> changed = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            for (ViwnEdgeSynset e : synset.getRelation(dir)) {
                ViwnNodeSynset inner = null;
                if (!forest.containsEdge(e)) {
                    if (synset == cache.get(e.getChild())) {
                        inner = cache.get(e.getParent());
                    } else {
                        inner = cache.get(e.getChild());
                    }
                    if (forest.containsVertex(inner)) {
                        addEdge(e, e.getParent(), e.getChild());

                        for (Direction in_dir : Direction.values()) {
                            if (inner.getSynsetSet(in_dir).contains(synset)) {
                                inner.getSynsetSet(in_dir).remove(synset);
                            }
                        }
                        changed.add(inner);
                    }
                }
            }
        }
        return changed;
    }

    private ViwnNodeSet getSetFrom(ViwnNodeSynset synset) {
        ViwnNodeRoot spawner = (ViwnNodeRoot) synset.getSpawner();
        return spawner.getSynsetSet(synset.getSpawnDir());
    }

    private void addEdgeSynsSet(ViwnNodeRoot syns, ViwnNodeSet set,
            Direction dir) {
        ViwnEdgeSet e = new ViwnEdgeSet();
        switch (dir) {
            case TOP:
            case BOTTOM:
                forest.addEdge(e, syns, set);
                break;
            case LEFT:
            case RIGHT:
                forest.addEdge(e, set, syns);
                break;
        }
    }

    private void addSynsetFromSet_(ViwnNodeSynset synset) {
        synset.setSet(null);
        forest.addVertex(synset);

        ViwnNodeSynset spawner = (ViwnNodeSynset) synset.getSpawner();
        ViwnNodeSet set = getSetFrom(synset);

        set.remove(synset);

        ViwnEdgeSynset e = findRelation(spawner, synset, synset.getSpawnDir());

        addEdge(e, loadSynsetNode(e.getSynsetFrom()),
                loadSynsetNode(e.getSynsetTo()));

        if (set.getSynsets().isEmpty()) {
            forest.removeVertex(set);
        }

        Collection<ViwnNodeSynset> changed = new ArrayList<>();

        for (ViwnNode node : forest.getVertices()) {
            if (node instanceof ViwnNodeSynset) {
                changed.addAll(addMissingRelations((ViwnNodeSynset) node));
            }
        }

        {
            Iterator<ViwnNodeSynset> iter = changed.iterator();
            while (iter.hasNext()) {
                ViwnNodeSynset node = iter.next();
                for (Direction rclass : Direction.values()) {
                    checkState(node, rclass);
                }
            }
        }

        for (Direction dir : Direction.values()) {
            checkState(synset, dir);
        }
    }

    /**
     * @param synset
     */
    public void addSynsetFromSet(ViwnNodeSynset synset) {
        addSynsetFromSet_(synset);
        vv.getPickedVertexState().pick(synset, true);
        if (getSetFrom(synset).getSynsets().size() == 1) {
            ViwnNodeSynset last = getSetFrom(synset).getSynsets().iterator()
                    .next();
            addSynsetFromSet_(last);
            vv.getPickedVertexState().pick(last, true);
        }
        selectedNode = synset;

    }

    /**
     * @param synset
     * @return set
     */
    public ViwnNodeSet addSynsetToSet(ViwnNodeSynset synset) {

        for (Direction dir : Direction.values()) {
            hideRelation(synset, dir);
        }
        forest.removeVertex(synset);
        ViwnNodeSet set = getSetFrom(synset);
        set.add(synset);
        synset.setSet(set);

        // if this is first synset, add the set to the graph
        if (set.getSynsets().size() == 1) {
            forest.addVertex(set);
            addEdgeSynsSet((ViwnNodeSynset) synset.getSpawner(), set,
                    synset.getSpawnDir());
            set.setSpawner(synset.getSpawner(), synset.getSpawnDir());
        }
        selectedNode = set;

        checkAllStates();

        return set;
    }

    /**
     * @param synsetNode
     * @param hide_dir
     */
    public void hideRelation(ViwnNodeSynset synsetNode, Direction hide_dir) {
        synchronized (this.forest) {
            boolean semi = false;
            boolean changed = false;
            for (ViwnEdgeSynset r : synsetNode.getRelation(hide_dir)) {
                ViwnNodeSynset rem = cache.get(r.getParent());
                if (rem != null && rem.equals(synsetNode)) {
                    rem = cache.get(r.getChild());
                }

                if (rem != null && forest.containsVertex(rem)) {
                    if (rem.getSpawner() != null
                            && rem.getSpawner().equals(synsetNode)) {
                        for (Direction rel : Direction.values()) {
                            hideRelation(rem, rel);
                        }
                        forest.removeEdge(r);
                        forest.removeVertex(rem);
                        changed = true;
                    } else {
                        semi = true;
                    }
                }
            }
            if (semi && changed) {
                synsetNode.setState(hide_dir,
                        ViwnNodeSynset.State.SEMI_EXPANDED);
            } else if (changed) {
                synsetNode.setState(hide_dir, ViwnNodeSynset.State.NOT_EXPANDED);
            }
            synsetNode.getSynsetSet(hide_dir).removeAll();
            forest.removeVertex(synsetNode.getSynsetSet(hide_dir));
            checkAllStates();
        }
    }

    private void checkAllStates() {
        for (ViwnNode node : forest.getVertices()) {
            if (node instanceof ViwnNodeSynset) {
                for (Direction d : Direction.values()) {
                    checkState((ViwnNodeSynset) node, d);
                }
            }
        }
    }

    public void showRelation(final ViwnNodeSynset synsetNode,
            final Direction[] dirs) {

        SwingUtilities.invokeLater(() -> {
            workbench.setBusy(true);
        });

        setSelectedNode(synsetNode);
        for (Direction dir : dirs) {
            showRelationGUI(synsetNode, dir);
        }

        SwingUtilities.invokeLater(() -> {
            recreateLayoutWithFix(synsetNode, synsetNode);
            recreateLayout();
            vv.repaint();
            
            workbench.setBusy(false);
        });
    }

    /**
     * @param synsetNode node which relations will be shown
     * @param dir relation class which will be shown
     */
    @SuppressWarnings("unchecked")
    private void showRelationGUI(ViwnNodeSynset synsetNode, Direction dir) {
        Collection<ViwnEdgeSynset> to_show_edges = new ArrayList<>();

        TreeMap<String, ArrayList<ViwnEdgeSynset>> all_sorted = new TreeMap<>(
                (String o1, String o2) -> o2.compareTo(o1));

        MultiValueMap mult_all_sorted = MultiValueMap.decorate(all_sorted);

        for (ViwnEdgeSynset e : synsetNode.getRelation(dir)) {
            ViwnNodeSynset node = loadSynsetNode(e.getSynsetTo());
            if (node.equals(synsetNode)) {
                node = loadSynsetNode(e.getSynsetFrom());
            }
            if (!forest.containsEdge(e)) {
                mult_all_sorted.put(node.getLabel(), e);
            }
        }

        Iterator<ViwnEdgeSynset> it = mult_all_sorted.values().iterator();

        int to_add = all_sorted.size() - MAX_SYNSETS_SHOWN;
        if (to_add >= MIN_SYNSETS_IN_GROUP) {
            ViwnNodeSet set = synsetNode.getSynsetSet(dir);
            while (to_add > 0 && it.hasNext()) {
                ViwnEdgeSynset e = it.next();

                ViwnNodeSynset node = loadSynsetNode(e.getSynsetFrom());
                if (node.equals(synsetNode)) {
                    node = loadSynsetNode(e.getSynsetTo());
                }

                if (!forest.containsVertex(node)) {
                    if (set.contains(node)) {
                        continue;
                    }
                    node.setSpawner(synsetNode, dir);
                    node.setSet(set);
                    set.add(node);
                }
                to_add--;
            }

            if (!set.getSynsets().isEmpty()) {
                set.setSpawner(synsetNode, dir);
                forest.addVertex(set);
                addEdgeSynsSet(synsetNode, set, dir);
            }
        }

        while (it.hasNext()) {
            to_show_edges.add(it.next());
        }

        for (ViwnEdgeSynset rel : to_show_edges) {
            ViwnNodeSynset node = loadSynsetNode(rel.getSynsetFrom());
            if (node.equals(synsetNode)) {
                node = loadSynsetNode(rel.getSynsetTo());
            }

            if (!forest.containsVertex(node)) {
                if (node.getSet() != null) {
                    node.getSet().remove(node);
                    node.setSet(null);
                }
                if (!node.getLexiconLabel().equals("")) {
                    addEdge(rel, loadSynsetNode(rel.getSynsetFrom()),
                            loadSynsetNode(rel.getSynsetTo()));
                }
                node.setSpawner(synsetNode, dir);

            }
        }

        synsetNode.setState(dir, ViwnNodeSynset.State.EXPANDED);

        List<ViwnNode> nodes = new ArrayList<>(forest.getVertices());
        nodes.stream().filter((node) -> ((node instanceof ViwnNodeSynset))).forEachOrdered((node) -> {
            addMissingRelations((ViwnNodeSynset) node);
        });

        this.vv.setVisible(true);
        checkAllStates();
    }

    /**
     * Clear graph (removes all edges and nodes).
     */
    public void clear() {
        // Lock the graph object
        synchronized (this.forest) {
            
            new ArrayList<>(forest.getEdges()).forEach((o) -> {
                this.forest.removeEdge(o);
            });
            
            new ArrayList<>(forest.getVertices()).forEach((o) -> {
                this.forest.removeVertex(o);
            });
        }
    }

    public ViwnNodeSynset loadSynsetNode(Synset synset) {
        if (cache.containsKey(synset.getId())) {
            ViwnNodeSynset s = cache.get(synset.getId());
            if (s.isDirty()) {
                s.construct();
            }
            return s;
        }

        ViwnNodeSynset new_synset = new ViwnNodeSynset(synset, this);

        cache.put(synset.getId(), new_synset);
        return new_synset;
    }

    private Direction findCommonRelationDir(ViwnNodeSynset parent,
            ViwnNodeSynset child) {
        for (Direction dir : Direction.values()) {
            for (ViwnEdgeSynset e : parent.getRelation(dir)) {
                if (e.getChild().equals(child.getSynset().getId())
                        || e.getParent().equals(child.getSynset().getId())) {
                    return dir;
                }
            }
        }
        return null;
    }

    public void addConnectedSynsetsToGraph(ViwnNodeSynset first, List<Synset> synsets) {
        ViwnNodeSynset prev = first;
        for (Synset synset : synsets) {
            ViwnNodeSynset node = loadSynsetNode(synset);
            relationAdded(prev, node);
            prev = node;
        }
    }

    public void relationAdded(final ViwnNodeSynset from, final ViwnNodeSynset to) {

        ViwnNodeSynset first = new ViwnNodeSynset(from.getSynset(), this);
        ViwnNodeSynset second = new ViwnNodeSynset(to.getSynset(), this);

        if (!forest.containsVertex(first)) {

            Direction cdir = findCommonRelationDir(second, first);
            if (cdir != null) {
                first.setSpawner(second, cdir);
                forest.addVertex(first);
            }
            checkMissing();
            recreateLayoutWithFix(null, null);
        } else if (!forest.containsVertex(second)) {
            Direction cdir = findCommonRelationDir(first, second);
            if (cdir != null) {
                second.setSpawner(first, cdir);
                forest.addVertex(second);
            }
            checkMissing();
            recreateLayoutWithFix(null, null);
        }
    }

    protected void checkMissing() {
        List<ViwnNode> nodes = new ArrayList<>(forest.getVertices());
       
        nodes.stream().filter((node) -> ((node instanceof ViwnNodeSynset)
                && !(node instanceof ViwnNodeCandExtension))).forEachOrdered((node) -> {
                    addMissingRelations((ViwnNodeSynset) node);
        });

        nodes.stream().filter((node) -> (node instanceof ViwnNodeSynset)).forEachOrdered((node) -> {
            for (Direction d : Direction.values()) {
                checkState((ViwnNodeSynset) node, d);
            }
        });
    }

    private ViwnEdge getFirstOnPath(Graph<ViwnNode, ViwnEdge> g, ViwnNode v1,
            ViwnNode v2) {
        DijkstraShortestPath<ViwnNode, ViwnEdge> dsp = new DijkstraShortestPath<>(g);

        List<ViwnEdge> list = dsp.getPath(v1, v2);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    private void setSpawnerByEdge(ViwnNodeSynset node, ViwnEdgeSynset edge) {

        ViwnNodeSynset second = cache.get(edge.getChild());
        if (second.equals(node)) {
            second = cache.get(edge.getParent());
        }
        if (second == null) {
            Logger.getLogger(getClass()).log(Level.FATAL,
                    "node of edge not in cache");
        }

        Direction d = findCommonRelationDir(second, node);
        if (d == null) {
            d = findCommonRelationDir(node, second);
            if (d != null) {
                d = d.getOposite();
            }
        }

        if (d == null) {
            Logger.getLogger(getClass()).log(Level.FATAL,
                    "can't find relation in any direction");
        }

        node.setSpawner(second, d);
    }

    private Graph<ViwnNode, ViwnEdge> undirect(Graph<ViwnNode, ViwnEdge> g) {
        Graph<ViwnNode, ViwnEdge> new_g = new UndirectedSparseGraph<>();
        forest.getVertices().stream().filter((n) -> (n instanceof ViwnNodeRoot)).forEachOrdered((n) -> {
            new_g.addVertex(n);
        });

        forest.getEdges().stream().filter((e) -> (e instanceof ViwnEdgeSynset || e instanceof ViwnEdgeCand)).forEachOrdered((e) -> {
            new_g.addEdge(e, forest.getEndpoints(e));
        });
        return new_g;
    }

    public void removeSynset(ViwnNodeSynset syns) {
        if (syns.equals(rootNode)) {
            clear();
            return;
        }

        for (Direction dir : Direction.values()) {
            forest.removeVertex(syns.getSynsetSet(dir));
            for (ViwnEdge ve : syns.getRelation(dir)) {
                ViwnEdgeSynset ves = (ViwnEdgeSynset) ve;
                if (!ves.getChild().equals(syns.getId())) {
                    ViwnNodeSynset n = cache.get(ves.getChild());
                    if (n != null) {
                        n.rereadDB();
                    }
                }

                if (!ves.getParent().equals(syns.getId())) {
                    ViwnNodeSynset n = cache.get(ves.getParent());
                    if (n != null) {
                        n.rereadDB();
                    }
                }
            }
        }

        relationDeleted(forest.getIncidentEdges(syns));

        forest.removeVertex(syns);
        cache.remove(syns.getId());
    }

    public void updateSynset(ViwnNodeSynset syns) {
        if (!forest.containsVertex(syns)) {
            return;
        }

        syns.rereadDB();

        addMissingRelations(syns);

        checkAllStates();
        recreateLayoutWithFix(syns, syns);
    }

    /**
     * @param edges
     */
    public void relationDeleted(Collection<ViwnEdge> edges) {
        if (edges == null) {
            return;
        }

        ViwnNode center = null;

        edges.forEach((col_e) -> {
            forest.removeEdge(col_e);
        });

        for (ViwnEdge col_e : edges) {
            ViwnEdgeSynset edge;
            if (col_e instanceof ViwnEdgeSynset) {
                edge = (ViwnEdgeSynset) col_e;
            } else {
                continue;
            }

            ViwnNodeSynset first = cache.get(edge.getChild());
            ViwnNodeSynset second = cache.get(edge.getParent());

            for (ViwnNodeSynset node : new ViwnNodeSynset[]{first, second}) {
                if (node == null) {
                    continue;
                }

                node.rereadDB();

                if (!forest.containsVertex(node)) {
                    continue;
                }

                if (!node.equals(rootNode)) {
                    if (forest.getIncidentEdges(node) == null) {
                        forest.removeVertex(node);
                    } else {
                        ViwnEdge e = getFirstOnPath(undirect(forest), node, rootNode);
                        if (e != null) {
                            center = node;
                            if (e instanceof ViwnEdgeCand) {
                                node.setSpawner(rootNode, Direction.BOTTOM);
                            } else {
                                setSpawnerByEdge(node, (ViwnEdgeSynset) e);
                            }
                        } else if (node instanceof ViwnNodeCand) {
                            node.setSpawner(rootNode, Direction.BOTTOM);
                            forest.addEdge(new ViwnEdgeCand(), rootNode, node);
                            center = node;
                        } else {
                            for (Direction dir : Direction.values()) {
                                hideRelation(node, dir);
                            }
                            forest.removeVertex(node);
                            if (first.equals(node)) {
                                center = second;
                            } else {
                                center = first;
                            }
                        }
                    }
                }
            }
        }

        checkAllStates();
        recreateLayoutWithFix(center, center);
    }

    /**
     * Invoke the change of the synset selection.
     *
     * @author amusial
     * @param synset
     */
    @Override
    public void vertexSelectionChange(ViwnNode synset) {
        if (this.selectedNode != synset) {
            this.selectedNode = synset;
        }

        // TODO: check me : if we are in make relation mode
        ViWordNetService s = ((ViWordNetService) workbench
                .getService("pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService"));
        if (s.isMakeRelationModeOn()) {
            s.makeRelation(synset);
            return;
        }

        if (s.isMergeSynsetsModeOn()) {
            s.mergeSynsets(synset);
            return;
        }

        synsetSelectionChangeListeners.forEach((l) -> {
            l.synsetSelectionChangeListener(synset);
        });
    }

    /**
     * Invoke graph change events.
     *
     * @author amusial
     *
     */
    public void graphChanged() {
        graphChangeListeners.forEach((gcl) -> {
            gcl.graphChanged();
        });
    }

    /**
     * selected node setter
     *
     * @param selected_node actually clicked
     *
     */
    public void setSelectedNode(ViwnNode selected_node) {
        this.selectedNode = selected_node;
    }

    /**
     * @return visualization viewer
     */
    public VisualizationViewer<ViwnNode, ViwnEdge> getVisualizationViewer() {
        return this.vv;
    }

    /**
     * @return graph
     */
    public Graph<ViwnNode, ViwnEdge> getGraph() {
        return this.forest;
    }

    /**
     * @return layout
     */
    public Layout<ViwnNode, ViwnEdge> getLayout() {
        return this.layout;
    }

    /**
     * @return rootNode
     *
     */
    public ViwnNode getRootNode() {
        return rootNode;
    }

    /**
     * @return selectedNode
     */
    public ViwnNode getSelectedNode() {
        return selectedNode;
    }

    /**
     * @return workbench
     *
     */
    public Workbench getWorkbench() {
        return this.workbench;
    }

    /**
     * @param cursor new value of <code>VisualizationViewer</code> cursor
     * @author amusial
     *
     */
    public void setCursor(Cursor cursor) {
        this.vv.setCursor(cursor);
    }

    /**
     * v1 and v2 could be same node, then it will stay in same point of view
     *
     * @param v1 node in first point of transformation vector
     * @param v2 node in second point of transformation vector
     * @author amusial
     *
     */
    public void recreateLayoutWithFix(ViwnNode v1, ViwnNode v2) {
        /* remember location of group node */
        Point2D p1 = (Point2D) layout.transform(v1).clone();
        /* recreate layout */
        this.layout.mapNodes2Points(rootNode);
        Point2D p2 = (Point2D) layout.transform(v2).clone();
        /* transform layout */
        vv.getRenderContext().getMultiLayerTransformer()
                .getTransformer(Layer.LAYOUT)
                .translate(p1.getX() - p2.getX(), p1.getY() - p2.getY());
        /* fire graph changed event, do it somewhere else... */
        graphChanged();
    }

    public void saveToFile(String filename) {
        Dimension size = layout.getSize();

        VisualizationImageServer<ViwnNode, ViwnEdge> vv = new VisualizationImageServer<>(
                layout, size);

        vv.getRenderer().setVertexRenderer(
                new ViwnVertexRenderer(vv.getRenderer().getVertexRenderer()));

        RenderContext<ViwnNode, ViwnEdge> rc = vv.getRenderContext();

        rc.setVertexShapeTransformer((ViwnNode v) -> v.getShape());

        rc.setVertexFillPaintTransformer(new ViwnVertexFillColor(vv
                .getPickedVertexState(), rootNode));

        rc.setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<>(
                0.5, 0.5));

        rc.setEdgeIncludePredicate((Context<Graph<ViwnNode, ViwnEdge>, ViwnEdge> context) -> {
            if (context.element instanceof ViwnEdgeCand) {
                ViwnEdgeCand cand = (ViwnEdgeCand) context.element;
                return !cand.isHidden();
            }
            return true;
        });

        Transformer<ViwnEdge, Paint> edgeDrawColor = (ViwnEdge e) -> e.getColor();

        rc.setEdgeDrawPaintTransformer(edgeDrawColor);
        rc.setArrowDrawPaintTransformer(edgeDrawColor);
        rc.setArrowFillPaintTransformer(edgeDrawColor);

        rc.setEdgeStrokeTransformer(new ViwnEdgeStrokeTransformer());

        Transformer<ViwnEdge, String> stringer = (ViwnEdge rel) -> rel.toString();

        rc.setEdgeLabelTransformer(stringer);
        rc.setEdgeShapeTransformer(new EdgeShape.Line<>());

        ViwnGraphViewModalGraphMouse gm = new ViwnGraphViewModalGraphMouse(this);
        vv.addKeyListener(gm.getModeKeyListener());

        vv.getRenderer().setEdgeLabelRenderer(
                new AstrideLabelRenderer<>());

        BufferedImage myImage = new BufferedImage(size.width, size.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = myImage.createGraphics();

        Image img = vv.getImage(new Point2D.Float(0, 0), size);
        g2.drawImage(img, 0, 0, null);


        try (OutputStream out = new FileOutputStream(filename)) {
            ImageIO.write(myImage, "png", out);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViwnGraphViewUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ViwnGraphViewUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    public CriteriaDTO getCriteria() {
        return this.criteria;
    }

    public void setCriteria(CriteriaDTO criteria) {
        this.criteria.setLemma(criteria.getLemma());
        this.criteria.setLexicon(criteria.getLexicon());
        this.criteria.setPartOfSpeech(criteria.getPartOfSpeech());
        this.criteria.setDomain(criteria.getDomain());
        this.criteria.setRegister(criteria.getRegister());
        this.criteria.setDefinition(criteria.getDefinition());
        this.criteria.setComment(criteria.getComment());
        this.criteria.setExample(criteria.getExample());
        this.criteria.setRelation(criteria.getRelation());
        this.criteria.setSynsetType(criteria.getSynsetType());
        this.criteria.setSense(criteria.getSense());
    }

    public int getOpenedFromTabIndex() {
        return openedFromTabIndex;
    }

    public void setOpenedFromTabIndex(int openedFromTabIndex) {
        this.openedFromTabIndex = openedFromTabIndex;
    }

}
