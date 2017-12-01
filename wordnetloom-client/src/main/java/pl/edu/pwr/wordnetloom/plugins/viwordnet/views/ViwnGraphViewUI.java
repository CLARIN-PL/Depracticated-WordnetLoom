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

package pl.edu.pwr.wordnetloom.plugins.viwordnet.views;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.control.LayoutScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ViewScalingControl;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.edu.pwr.wordnetloom.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.dto.SenseDataEntry;
import pl.edu.pwr.wordnetloom.dto.SynsetDataEntry;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseRelation;
import pl.edu.pwr.wordnetloom.model.Synset;
import pl.edu.pwr.wordnetloom.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.listeners.GraphChangeListener;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.listeners.SynsetSelectionChangeListener;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.listeners.VertexSelectionChangeListener;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.listeners.ViwnGraphMouseListener;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.*;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNode.Direction;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.control.ViwnGraphViewModalGraphMouse;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.decorators.ViwnEdgeStrokeTransformer;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.decorators.ViwnVertexToolTipTransformer;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.layout.ViwnLayout2;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.renderers.AstrideLabelRenderer;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.renderers.ViwnVertexFillColor;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.visualization.renderers.ViwnVertexRenderer;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import se.datadosen.component.RiverLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;

/**
 * @author boombel
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 */
public class ViwnGraphViewUI extends AbstractViewUI implements VertexSelectionChangeListener<ViwnNode> {
    /**
     * A graph of synset and relations between synsets.
     */
    private DirectedGraph<ViwnNode, ViwnEdge> forest = new DirectedSparseMultigraph<>();

    private HashMap<Long, ViwnNodeSynset> synsetCache = new HashMap<>();
    private HashMap<Long, ViwnNodeSense> senseCache = new HashMap<>();

    private ViwnLayout2 layout = new ViwnLayout2(forest);

    private VisualizationViewer<ViwnNode, ViwnEdge> vv = null;

    private ViwnNodeRoot rootNode = null;

    // Collection of object which listen for an event of synset selection
    // change.
    protected Collection<SynsetSelectionChangeListener> synsetSelectionChangeListeners = new ArrayList<>();

    // Collection of objects which listen for an event of graph changes
    protected Collection<GraphChangeListener> graphChangeListeners = new ArrayList<>();

    private ViwnNode selectedNode = null;

    private CriteriaDTO criteria = new CriteriaDTO();
    private int openedFromTabIndex;

    private ScalingControl scaler = new ViewScalingControl();

    // Graph mouse listener, handles mouse clicks at vertices
    private ViwnGraphMouseListener graphMouseListener = null;

    private static final float EDGE_PICK_SIZE = 10f;
    final int MAX_SYNSETS_SHOWN = 4;
    final int MIN_SYNSETS_IN_GROUP = 2;

    /* Transient cache for graph biulding */
    private HashMap<Long, SynsetDataEntry> synsetEntrySets = new HashMap<>();
    private HashMap<Long, SenseDataEntry> senseEntrySets = new HashMap<>();

    public void setSynsetEntrySets(HashMap<Long, SynsetDataEntry> synsetEntrySets) {
        this.synsetEntrySets = synsetEntrySets;
    }

    public void setSenseEntrySets(HashMap<Long, SenseDataEntry> senseEntrySets) {
        this.senseEntrySets = senseEntrySets;
    }

    public SynsetDataEntry getEntrySetForSynset(Long id) {
        return synsetEntrySets.get(id);
    }

    public SenseDataEntry getEntrySetForSense(Long id) {
        return senseEntrySets.get(id);
    }

    public void addSynsetToCash(Long synsetId, ViwnNodeSynset node) {
        synsetCache.put(synsetId, node);
    }

    public void addSenseToCash(Long senseId, ViwnNodeSense node) {
        senseCache.put(senseId, node);
    }

    public List<SynsetRelation> getRelationsForSynset(Long id) {
        SynsetDataEntry e = getEntrySetForSynset(id);
        if (e == null)
            return new ArrayList<>();

        ArrayList<SynsetRelation> rels = new ArrayList<>();
        rels.addAll(e.getRelsFrom());
        rels.addAll(e.getRelsTo());

        return rels;
    }

    public List<SenseRelation> getUpperRelationsForSense(Long id) {
        SenseDataEntry e = getEntrySetForSense(id);
        if (e == null)
            return null;

        return e.getRelsFrom();
    }

    public List<SenseRelation> getSenseSubRelationsForSense(Long id) {
        SenseDataEntry e = getEntrySetForSense(id);
        if (e == null)
            return null;

        return e.getRelsTo();
    }

    public List<SynsetRelation> getUpperRelationsForSynset(Long id) {
        SynsetDataEntry e = getEntrySetForSynset(id);
        if (e == null)
            return null;

        return e.getRelsFrom();
    }

    public List<SynsetRelation> getSubRelationsForSynset(Long id) {
        SynsetDataEntry e = getEntrySetForSynset(id);
        if (e == null)
            return null;

        return e.getRelsTo();
    }

    public void releaseDataSetCache() {
        synsetEntrySets.clear();
        senseEntrySets.clear();
    }

	/* End of transient synsetCache for graph biulding */

    @Override
    public JComponent getRootComponent() {
        return null;
    }

    /**
     * @param content JPanel from workbench
     * @author amusial
     */
    @Override
    protected void initialize(JPanel content) {
        content.removeAll();
        content.setLayout(new RiverLayout());

        // Create a panel for graph visualisation.
        JPanel graph;
        try {
            graph = getSampleGraphViewer();
            content.add(graph, "hfill vfill");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return JPanel filled with jung's graph visualization
     * @throws IOException from inside methods
     * @author amusial
     */
    private JPanel getSampleGraphViewer() throws IOException {
        vv = new VisualizationViewer<>(layout);

        vv.getRenderer().setVertexRenderer(new ViwnVertexRenderer(vv.getRenderer().getVertexRenderer()));
        HashMap<RenderingHints.Key, Object> hints = new HashMap<>();
        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        vv.setRenderingHints(hints);

        RenderContext<ViwnNode, ViwnEdge> rc = vv.getRenderContext();

        rc.setVertexShapeTransformer(v -> v.getShape());

        rc.setVertexFillPaintTransformer(new ViwnVertexFillColor(vv.getPickedVertexState(), rootNode));

        rc.setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<>(0.5, 0.5));

        rc.setEdgeIncludePredicate(context -> {
            if (context.element instanceof ViwnEdgeCand) {
                ViwnEdgeCand cand = (ViwnEdgeCand) context.element;
                return !cand.isHidden();
            }
            return true;
        });

        Transformer<ViwnEdge, Paint> edgeDrawColor = new Transformer<ViwnEdge, Paint>() {
            public Paint transform(ViwnEdge e) {
                return e.getColor();
            }
        };
        rc.setEdgeDrawPaintTransformer(edgeDrawColor);
        rc.setArrowDrawPaintTransformer(edgeDrawColor);
        rc.setArrowFillPaintTransformer(edgeDrawColor);

        rc.setEdgeStrokeTransformer(new ViwnEdgeStrokeTransformer());

        graphMouseListener = new ViwnGraphMouseListener(this);
        vv.addGraphMouseListener(graphMouseListener);

        Transformer<ViwnEdge, String> stringer = ViwnEdge::toString;
        rc.setEdgeLabelTransformer(stringer);
        rc.setEdgeShapeTransformer(new EdgeShape.Line<>());

        ViwnGraphViewModalGraphMouse gm = new ViwnGraphViewModalGraphMouse(this);
        vv.addKeyListener(gm.getModeKeyListener());

        vv.getRenderer().setEdgeLabelRenderer(new AstrideLabelRenderer<>());

        vv.setGraphMouse(gm);

        GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        panel.add(vv);

        ((ShapePickSupport<ViwnNode, ViwnEdge>) rc.getPickSupport()).setPickSize(EDGE_PICK_SIZE);

        vv.setVertexToolTipTransformer(new ViwnVertexToolTipTransformer());

        return panel;
    }

    /**
     * @return cache_
     */
    public HashMap<Long, ViwnNodeSynset> getSynsetCache() {
        return synsetCache;
    }

    public HashMap<Long, ViwnNodeSense> getSenseCache() {
        return senseCache;
    }

    /**
     * clears synsetCache
     */
    public void cleanCache() {
        synsetCache.clear();
    }

    /**
     * @param synset
     */
    public void refreshView(Synset synset) {
        // Clear the graph.
        clear();

        selectedNode = rootNode = new ViwnNodeSynset(synset, this);
        synsetCache.put(synset.getId(), (ViwnNodeSynset) rootNode);

        vv.getRenderContext()
                .setVertexFillPaintTransformer(new ViwnVertexFillColor(vv.getPickedVertexState(), rootNode));

        ViwnNodeSynset synsetNode = (ViwnNodeSynset) rootNode;

        if (!forest.containsVertex(rootNode))
            forest.addVertex(rootNode);

        for (ViwnNode n : synsetCache.values()) {
            n.setSpawner(null, null);
            if (n instanceof ViwnNodeSynset) {
                for (Direction rclass : Direction.values()) {
                    ViwnNodeSynset nn = (ViwnNodeSynset) n;
                    nn.setState(rclass, State.NOT_EXPANDED);
                }
            }
        }

        for (Direction rel_class : Direction.values()) {
            if (rootNode instanceof ViwnNodeSynset)
                ((ViwnNodeSynset) rootNode).setState(rel_class, State.EXPANDED);
        }

        for (Direction dir : Direction.values()) {
            showRelationGUI(synsetNode, dir);
        }

        recreateLayout();
        center();
        vv.setVisible(true);
        releaseDataSetCache();
    }

    public void refreshView(Sense sense) {
        // Clear the graph.
        clear();

        selectedNode = rootNode = new ViwnNodeSense(sense, this);
        senseCache.put(sense.getId(), (ViwnNodeSense) rootNode);

        vv.getRenderContext()
                .setVertexFillPaintTransformer(new ViwnVertexFillColor(vv.getPickedVertexState(), rootNode));

        ViwnNodeSense senseNode = (ViwnNodeSense) rootNode;

        if (!forest.containsVertex(rootNode)) {
            forest.addVertex(rootNode);
        }

        for (ViwnNode n : senseCache.values()) {
            n.setSpawner(null, null);
            if (n instanceof ViwnNodeSense) {
                for (Direction rclass : Direction.values()) {
                    ViwnNodeSense nn = (ViwnNodeSense) n;
                    nn.setState(rclass, State.NOT_EXPANDED);
                }
            }
        }

        for (Direction rel_class : Direction.values()) {
            if (rootNode instanceof ViwnNodeSense)
                ((ViwnNodeSense) rootNode).setState(rel_class, State.EXPANDED);
        }

        for (Direction dir : Direction.values()) {
            showRelationGUI(senseNode, dir);
        }

        recreateLayout();
        center();
        vv.setVisible(true);

        releaseDataSetCache();

    }

    public void loadCandidate(ViwnNodeWord root, ArrayList<TreeSet<ViwnNodeSynset>> groups,
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
                    if (node.getExtGraphExtension().getBase())
                        forest.addEdge(e, rootNode, node);
                    else
                        forest.addEdge(e, node, rootNode);
                    forest.addVertex(node);
                }
            }
        }

        for (ViwnNodeSynset node : freeSyns) {
            if (node.getSpawner() == null) {
                node.setSpawner(root, Direction.BOTTOM);
                ViwnEdgeCand e = new ViwnEdgeCand();
                e.setHidden(true);
                forest.addEdge(e, rootNode, node);
                forest.addVertex(node);
            }
        }

        HashMap<ViwnNodeRoot, ArrayList<ViwnNodeSynset>> map = new HashMap<>();

        for (ViwnNodeSynset r : roots) {
            map.put(r, new ArrayList<>());
        }

        for (int i = 0; i < groups.size(); ++i) {
            for (ViwnNodeSynset s : groups.get(i)) {
                ArrayList<ViwnNodeSynset> list = map.get((ViwnNodeRoot) s.getSpawner());
                if (list == null) {
                    list = new ArrayList<>();
                    list.add(s);
                    map.put((ViwnNodeRoot) s.getSpawner(), list);
                } else
                    list.add(s);

            }
        }

        for (ViwnNodeRoot key : map.keySet()) {
            if (key == null)
                continue;

            ArrayList<ViwnNodeRoot> add_list = new ArrayList<>();

            TreeMap<String, ArrayList<ViwnNodeRoot>> all_sorted = new TreeMap<>(
                    new Comparator<String>() {
                        public int compare(String o1, String o2) {
                            return o2.compareTo(o1);
                        }
                    });

            MultiValueMap mult_all_sorted = MultiValueMap.decorate(all_sorted);
            for (ViwnNodeRoot r : map.get(key)) {
                mult_all_sorted.put(r.getLabel(), r);
            }

            // legacy
            Iterator<ViwnNodeSynset> it = mult_all_sorted.values().iterator();

            int to_add = map.get(key).size() - MAX_SYNSETS_SHOWN;

            while (to_add > 0 && it.hasNext()) {
                ViwnNodeSynset s = it.next();
                ViwnNodeSynsetSet set = key.getSynsetSet(s.getSpawnDir());

                if (!forest.containsVertex(s) && map.get(s) == null) {
                    s.setSet(set);
                    set.add(s);
                    to_add--;
                } else
                    add_list.add(s);
            }

            for (Direction dir : Direction.values()) {
                ViwnNodeSynsetSet set = key.getSynsetSet(dir);

                if (set.getSynsets().size() < MIN_SYNSETS_IN_GROUP) {
                    add_list.addAll(set.getSynsets());
                    set.removeAll();
                } else if (!set.getSynsets().isEmpty()) {
                    set.setSpawner(key, dir);
                    forest.addVertex(set);
                    addEdgeToSet(key, set, dir);
                }
            }

            while (it.hasNext()) {
                ViwnNodeSynset s = it.next();
                forest.addVertex(s);
                if (key instanceof ViwnNodeWord) {
                    ViwnNodeCand c = (ViwnNodeCand) s;
                    ViwnEdgeCand ec = new ViwnEdgeCand();
                    if (c.isEvaluated() || s.getLabel().equals("! S.y.n.s.e.t p.u.s.t.y !")
                            || indexes.contains(s.getId()))
                        ec.setHidden(true);
                    forest.addEdge(ec, rootNode, s);
                }
            }

            for (ViwnNodeRoot s : add_list) {
                forest.addVertex(s);
                if (key instanceof ViwnNodeWord) {
                    ViwnNodeCand c = (ViwnNodeCand) s;
                    ViwnEdgeCand ec = new ViwnEdgeCand();
                    if (c.isEvaluated())
                        ec.setHidden(true);
                    forest.addEdge(ec, rootNode, s);
                }
            }
        }

        for (ViwnNode n : forest.getVertices()) {
            if (n instanceof ViwnNodeSynset && !(n instanceof ViwnNodeCandExtension))
                addMissingRelations((ViwnNodeSynset) n);
        }

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
     */
    public void center() {
        ViwnNode central;

        if (selectedNode != null)
            central = selectedNode;
        else if (rootNode != null)
            central = rootNode;
        else
            return;

        Point2D q = layout.transform(central);
        Point2D lvc = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(vv.getCenter());
        vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(lvc.getX() - q.getX(),
                lvc.getY() - q.getY());

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
     */
    protected void fillVV() {
        // scale
        // if layout was scaled, scale it to it original size
        if (vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).getScaleX() > 1D) {
            (new LayoutScalingControl()).scale(vv, (1f / (float) vv.getRenderContext().getMultiLayerTransformer()
                    .getTransformer(Layer.LAYOUT).getScaleX()), new Point2D.Double());
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
            float heightRatio = (float) (vd.getWidth() / ld.getWidth()),
                    widthRatio = (float) (vd.getHeight() / ld.getHeight());

            scaler.scale(vv, (heightRatio < widthRatio ? heightRatio : widthRatio), new Point2D.Double());
        }
    }

    /**
     * @param node
     * @param rel
     */
    public void checkState(ViwnNodeSynset node, ViwnNode.Direction rel) {
        node.setState(rel, State.NOT_EXPANDED);
        boolean all_ok = true;

        for (ViwnEdgeSynset e : node.getRelation(rel)) {
            ViwnNodeSynset other = synsetCache.get(e.getParent());
            if (other != null && other.equals(node))
                other = synsetCache.get(e.getChild());

            if (forest.getEdges().contains(e)) {
                node.setState(rel, State.SEMI_EXPANDED);
            } else if (!node.getSynsetSet(rel).getSynsets().contains(other))
                all_ok = false;
        }

        if (all_ok)
            if (forest.containsVertex(node.getSynsetSet(rel)) || node.getSynsetSet(rel).getSynsets().isEmpty())
                node.setState(rel, State.EXPANDED);
    }

    public void checkState(ViwnNodeSense node, ViwnNode.Direction rel) {
        node.setState(rel, State.NOT_EXPANDED);
        boolean all_ok = true;

        for (ViwnEdgeSense e : node.getRelation(rel)) {
            ViwnNodeSense other = senseCache.get(e.getParent());
            if (other != null && other.equals(node))
                other = senseCache.get(e.getChild());

            if (forest.getEdges().contains(e)) {
                node.setState(rel, State.SEMI_EXPANDED);
            } else if (!node.getSenseSet(rel).getSenses().contains(other))
                all_ok = false;
        }

        if (all_ok)
            if (forest.containsVertex(node.getSenseSet(rel)) || node.getSenseSet(rel).getSenses().isEmpty())
                node.setState(rel, State.EXPANDED);
    }

    /**
     * @param node
     * @param dir
     */
    public void checkStateAllInRel(ViwnNodeSynset node, Direction dir) {
        for (ViwnEdgeSynset e : node.getRelation(dir)) {
            ViwnNodeSynset test = synsetCache.get(e.getParent());
            if (test != null && test.equals(node))
                test = synsetCache.get(e.getChild());
            if (test != null)
                checkState(test, dir.getOposite());
        }
    }

    /**
     * @return true when synsets can be grouped
     */
    public boolean canGroupSynsets() {
        Set<ViwnNode> picked = vv.getPickedVertexState().getPicked();
        if (picked.size() < 1) {
            return false;
        } else if (picked.size() == 1 && picked.iterator().next() instanceof ViwnNodeSynset) {
            ViwnNodeSynset syns = (ViwnNodeSynset) picked.iterator().next();
            if (syns.equals(rootNode))
                return false;
            if (!forest.containsVertex(syns))
                return false;
            ViwnNodeSynsetSet s = getSetFrom(syns);
            if (!forest.containsVertex(s))
                return false;
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
                if (pick_itr != null)
                    can_group = true;
            }
        }
        return can_group;
    }

    private ViwnEdgeSynset findRelation(ViwnNodeSynset s1, ViwnNodeSynset s2, Direction rel) {
        for (ViwnEdgeSynset s : s1.getRelation(rel)) {
            if ((synsetCache.get(s.getChild()) != null && synsetCache.get(s.getChild()).equals(s2))
                    || (synsetCache.get(s.getParent()) != null && synsetCache.get(s.getParent()).equals(s2)))
                return s;
        }

        return null;
    }

    private ViwnEdgeSense findRelation(ViwnNodeSense s1, ViwnNodeSense s2, Direction rel) {
        for (ViwnEdgeSense s : s1.getRelation(rel)) {
            if ((senseCache.get(s.getChild()) != null && senseCache.get(s.getChild()).equals(s2))
                    || (senseCache.get(s.getParent()) != null && senseCache.get(s.getParent()).equals(s2)))
                return s;
        }

        return null;
    }

    private void addEdge(ViwnEdge e, Long first, Long second) {
        ViwnNode v1 = synsetCache.get(first);
        ViwnNode v2 = synsetCache.get(second);
        if (v1 != null && v2 != null)
            addEdge(e, (ViwnNodeSynset) v1, (ViwnNodeSynset) v2);
    }

    private void addEdge(ViwnEdge e, ViwnNodeSynset first, ViwnNodeSynset second) {
        ViwnEdgeSynset se = (ViwnEdgeSynset) e;
        se.setSynset1(first);
        se.setSynset2(second);
        forest.addEdge(e, first, second);
    }

    private void addEdge(ViwnEdge e, ViwnNodeSense first, ViwnNodeSense second) {
        ViwnEdgeSense se = (ViwnEdgeSense) e;
        se.setFirstSense(first);
        se.setSecondSense(second);
        forest.addEdge(e, first, second);
    }

    private Collection<ViwnNodeSynset> addMissingRelations(ViwnNodeSynset synset) {
        ArrayList<ViwnNodeSynset> changed = new ArrayList<>();
        for (Direction dir : Direction.values())
            for (ViwnEdgeSynset e : synset.getRelation(dir)) {
                ViwnNodeSynset inner = null;
                if (!forest.containsEdge(e)) {
                    if (synset == synsetCache.get(e.getChild()))
                        inner = synsetCache.get(e.getParent());
                    else
                        inner = synsetCache.get(e.getChild());
                    if (forest.containsVertex(inner)) {
                        addEdge(e, e.getParent(), e.getChild());

                        for (Direction in_dir : Direction.values()) {
                            if (inner.getSynsetSet(in_dir).contains(synset))
                                inner.getSynsetSet(in_dir).remove(synset);
                        }
                        changed.add(inner);
                    }
                }
            }
        return changed;
    }

    private Collection<ViwnNodeSense> addMissingRelations(ViwnNodeSense sense) {
        ArrayList<ViwnNodeSense> changed = new ArrayList<>();
        for (Direction dir : Direction.values())
            for (ViwnEdgeSense e : sense.getRelation(dir)) {
                ViwnNodeSense inner;
                if (!forest.containsEdge(e)) {
                    if (sense == senseCache.get(e.getChild()))
                        inner = senseCache.get(e.getParent());
                    else
                        inner = senseCache.get(e.getChild());
                    if (forest.containsVertex(inner)) {
                        addEdge(e, e.getParent(), e.getChild());

                        for (Direction in_dir : Direction.values()) {
                            if (inner.getSenseSet(in_dir).contains(sense))
                                inner.getSenseSet(in_dir).remove(sense);
                        }
                        changed.add(inner);
                    }
                }
            }
        return changed;
    }

    private ViwnNodeSynsetSet getSetFrom(ViwnNodeSynset synset) {
        ViwnNodeRoot spawner = (ViwnNodeRoot) synset.getSpawner();
        return spawner.getSynsetSet(synset.getSpawnDir());
    }

    private ViwnNodeSenseSet getSetFrom(ViwnNodeSense sense) {
        ViwnNodeRoot spawner = (ViwnNodeRoot) sense.getSpawner();
        return spawner.getSenseSet(sense.getSpawnDir());
    }

    private void addEdgeToSet(ViwnNodeRoot syns, ViwnNode set, Direction dir) {
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
        ViwnNodeSynsetSet set = getSetFrom(synset);

        set.remove(synset);

        ViwnEdgeSynset e = findRelation(spawner, synset, synset.getSpawnDir());

        addEdge(e, loadSynsetNode(e.getSynsetFrom()), loadSynsetNode(e.getSynsetTo()));

        if (set.getSynsets().isEmpty())
            forest.removeVertex(set);

        Collection<ViwnNodeSynset> changed = new ArrayList<>();

        for (ViwnNode node : forest.getVertices()) {
            if (node instanceof ViwnNodeSynset)
                changed.addAll(addMissingRelations((ViwnNodeSynset) node));
        }

        {
            Iterator<ViwnNodeSynset> iter = changed.iterator();
            while (iter.hasNext()) {
                ViwnNodeSynset node = iter.next();
                for (Direction rclass : Direction.values())
                    checkState(node, rclass);
            }
        }

        for (Direction dir : Direction.values())
            checkState(synset, dir);
    }

    /**
     * @param synset
     */
    public void addSynsetFromSet(ViwnNodeSynset synset) {
        addSynsetFromSet_(synset);
        vv.getPickedVertexState().pick(synset, true);
        if (getSetFrom(synset).getSynsets().size() == 1) {
            ViwnNodeSynset last = getSetFrom(synset).getSynsets().iterator().next();
            addSynsetFromSet_(last);
            vv.getPickedVertexState().pick(last, true);
        }
        selectedNode = synset;

    }

    public void addSenseFromSet(ViwnNodeSense sense) {
        addSenseFromSet_(sense);
        vv.getPickedVertexState().pick(sense, true);
        if (getSetFrom(sense).getSenses().size() == 1) {
            ViwnNodeSense last = getSetFrom(sense).getSenses().iterator().next();
            addSenseFromSet_(last);
            vv.getPickedVertexState().pick(last, true);
        }
        selectedNode = sense;

    }
    private void addSenseFromSet_(ViwnNodeSense sense) {
        sense.setSet(null);
        forest.addVertex(sense);

        ViwnNodeSense spawner = (ViwnNodeSense) sense.getSpawner();
        ViwnNodeSenseSet set = getSetFrom(sense);

        set.remove(sense);

        ViwnEdgeSense e = findRelation(spawner, sense, sense.getSpawnDir());

        addEdge(e, loadSenseNode(e.getSenseFrom()), loadSenseNode(e.getSenseTo()));

        if (set.getSenses().isEmpty())
            forest.removeVertex(set);

        Collection<ViwnNodeSense> changed = new ArrayList<>();

        for (ViwnNode node : forest.getVertices()) {
            if (node instanceof ViwnNodeSense)
                changed.addAll(addMissingRelations((ViwnNodeSense) node));
        }

        {
            Iterator<ViwnNodeSense> iter = changed.iterator();
            while (iter.hasNext()) {
                ViwnNodeSense node = iter.next();
                for (Direction rclass : Direction.values())
                    checkState(node, rclass);
            }
        }

        for (Direction dir : Direction.values())
            checkState(sense, dir);
    }

    /**
     * @param synset
     * @return set
     */
    public ViwnNodeSynsetSet addSynsetToSet(ViwnNodeSynset synset) {

        for (Direction dir : Direction.values())
            hideRelation(synset, dir);
        forest.removeVertex(synset);
        ViwnNodeSynsetSet set = getSetFrom(synset);
        set.add(synset);
        synset.setSet(set);

        // if this is first synset, add the set to the graph
        if (set.getSynsets().size() == 1) {
            forest.addVertex(set);
            addEdgeToSet((ViwnNodeSynset) synset.getSpawner(), set, synset.getSpawnDir());
            set.setSpawner(synset.getSpawner(), synset.getSpawnDir());
        }
        selectedNode = set;

        checkAllStates();

        return set;
    }

    public void hideRelation(ViwnNodeSynset synsetNode, Direction hide_dir) {
        synchronized (forest) {
            boolean semi = false;
            boolean changed = false;
            for (ViwnEdgeSynset r : synsetNode.getRelation(hide_dir)) {
                ViwnNodeSynset rem = synsetCache.get(r.getParent());
                if (rem != null && rem.equals(synsetNode))
                    rem = synsetCache.get(r.getChild());

                if (rem != null && forest.containsVertex(rem)) {
                    if (rem.getSpawner() != null && rem.getSpawner().equals(synsetNode)) {
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
            if (semi && changed)
                synsetNode.setState(hide_dir, State.SEMI_EXPANDED);
            else if (changed)
                synsetNode.setState(hide_dir, State.NOT_EXPANDED);
            synsetNode.getSynsetSet(hide_dir).removeAll();
            forest.removeVertex(synsetNode.getSynsetSet(hide_dir));
            checkAllStates();
        }
    }

    public void hideRelation(ViwnNodeSense senseNode, Direction hide_dir) {
        synchronized (forest) {
            boolean semi = false;
            boolean changed = false;
            for (ViwnEdgeSense r : senseNode.getRelation(hide_dir)) {
                ViwnNodeSense rem = senseCache.get(r.getParent());
                if (rem != null && rem.equals(senseNode))
                    rem = senseCache.get(r.getChild());

                if (rem != null && forest.containsVertex(rem)) {
                    if (rem.getSpawner() != null && rem.getSpawner().equals(senseNode)) {
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
            if (semi && changed)
                senseNode.setState(hide_dir, State.SEMI_EXPANDED);
            else if (changed)
                senseNode.setState(hide_dir, State.NOT_EXPANDED);
            senseNode.getSynsetSet(hide_dir).removeAll();
            forest.removeVertex(senseNode.getSynsetSet(hide_dir));
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

    public void showRelation(ViwnNodeSynset synsetNode, Direction[] dirs) {

        SwingUtilities.invokeLater(() -> workbench.setBusy(true));

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

    public void showRelation(ViwnNodeSense senseNode, Direction[] dirs) {

        SwingUtilities.invokeLater(() -> workbench.setBusy(true));

        setSelectedNode(senseNode);
        for (Direction dir : dirs) {
            showRelationGUI(senseNode, dir);
        }

        SwingUtilities.invokeLater(() -> {
            recreateLayoutWithFix(senseNode, senseNode);
            recreateLayout();
            vv.repaint();

            workbench.setBusy(false);
        });
    }

    /**
     * @param synsetNode node which relations will be shown
     * @param dir        relation class which will be shown
     */
    private void showRelationGUI(ViwnNodeSynset synsetNode, Direction dir) {
        Collection<ViwnEdgeSynset> to_show_edges = new ArrayList<>();

        TreeMap<String, ArrayList<ViwnEdgeSynset>> all_sorted = new TreeMap<>(
                Comparator.reverseOrder());

        MultiValueMap mult_all_sorted = MultiValueMap.decorate(all_sorted);

        for (ViwnEdgeSynset e : synsetNode.getRelation(dir)) {
            ViwnNodeSynset node = loadSynsetNode(e.getSynsetTo());
            if (node.equals(synsetNode))
                node = loadSynsetNode(e.getSynsetFrom());
            if (!forest.containsEdge(e)) {
                mult_all_sorted.put(node.getLabel(), e);
            }
        }

        Iterator<ViwnEdgeSynset> it = mult_all_sorted.values().iterator();

        int to_add = all_sorted.size() - MAX_SYNSETS_SHOWN;
        if (to_add >= MIN_SYNSETS_IN_GROUP) {
            ViwnNodeSynsetSet set = synsetNode.getSynsetSet(dir);
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
                addEdgeToSet(synsetNode, set, dir);
            }
        }

        while (it.hasNext()) {
            to_show_edges.add(it.next());
        }

        for (ViwnEdgeSynset rel : to_show_edges) {
            ViwnNodeSynset node = loadSynsetNode(rel.getSynsetFrom());
            if (node.equals(synsetNode))
                node = loadSynsetNode(rel.getSynsetTo());

            if (!forest.containsVertex(node)) {
                if (node.getSet() != null) {
                    node.getSet().remove(node);
                    node.setSet(null);
                }
                if (!node.getLexiconLabel().equals("")) {
                    addEdge(rel, loadSynsetNode(rel.getSynsetFrom()), loadSynsetNode(rel.getSynsetTo()));
                }
                node.setSpawner(synsetNode, dir);

            }
        }

        synsetNode.setState(dir, State.EXPANDED);

        List<ViwnNode> nodes = new ArrayList<>(forest.getVertices());
        for (ViwnNode node : nodes) {
            if ((node instanceof ViwnNodeSynset))
                addMissingRelations((ViwnNodeSynset) node);
        }

        vv.setVisible(true);
        checkAllStates();
    }

    private void showRelationGUI(ViwnNodeSense senseNode, Direction dir) {
        Collection<ViwnEdgeSense> to_show_edges = new ArrayList<>();

        TreeMap<String, ArrayList<ViwnEdgeSense>> all_sorted = new TreeMap<>(
                Comparator.reverseOrder());

        MultiValueMap mult_all_sorted = MultiValueMap.decorate(all_sorted);

        for (ViwnEdgeSense e : senseNode.getRelation(dir)) {
            ViwnNodeSense node = loadSenseNode(e.getSenseTo());
            if (node.equals(senseNode))
                node = loadSenseNode(e.getSenseFrom());
            if (!forest.containsEdge(e)) {
                mult_all_sorted.put(node.getLabel(), e);
            }
        }

        Iterator it = mult_all_sorted.values().iterator();

        int to_add = all_sorted.size() - MAX_SYNSETS_SHOWN;
        if (to_add >= MIN_SYNSETS_IN_GROUP) {
            ViwnNodeSenseSet set = senseNode.getSenseSet(dir);
            while (to_add > 0 && it.hasNext()) {
                ViwnEdgeSense e = (ViwnEdgeSense) it.next();

                ViwnNodeSense node = loadSenseNode(e.getSenseFrom());
                if (node.equals(senseNode)) {
                    node = loadSenseNode(e.getSenseTo());
                }

                if (!forest.containsVertex(node)) {
                    if (set.contains(node)) {
                        continue;
                    }
                    node.setSpawner(senseNode, dir);
                    node.setSet(set);
                    set.add(node);
                }
                to_add--;
            }

            if (!set.getSenses().isEmpty()) {
                set.setSpawner(senseNode, dir);
                forest.addVertex(set);
                addEdgeToSet(senseNode, set, dir);
            }
        }

        while (it.hasNext()) {
            to_show_edges.add((ViwnEdgeSense) it.next());
        }

        for (ViwnEdgeSense rel : to_show_edges) {
            ViwnNodeSense node = loadSenseNode(rel.getSenseFrom());
            if (node.equals(senseNode))
                node = loadSenseNode(rel.getSenseTo());

            if (!forest.containsVertex(node)) {
                if (node.getSet() != null) {
                    node.getSet().remove(node);
                    node.setSet(null);
                }
                if (!node.getLexiconLabel().equals("")) {
                    addEdge(rel, loadSenseNode(rel.getSenseFrom()), loadSenseNode(rel.getSenseTo()));
                }
                node.setSpawner(senseNode, dir);

            }
        }

        senseNode.setState(dir, State.EXPANDED);

        List<ViwnNode> nodes = new ArrayList<>(forest.getVertices());
        for (ViwnNode node : nodes) {
            if ((node instanceof ViwnNodeSense))
                addMissingRelations((ViwnNodeSense) node);
        }

        vv.setVisible(true);
        checkAllStates();
    }

    /**
     * Clear graph (removes all edges and nodes).
     */
    public void clear() {
        // Lock the graph object
        synchronized (forest) {
            for (ViwnEdge o : new ArrayList<>(forest.getEdges()))
                forest.removeEdge(o);
            for (ViwnNode o : new ArrayList<>(forest.getVertices()))
                forest.removeVertex(o);
        }
    }

    public ViwnNodeSynset loadSynsetNode(Synset synset) {
        if (synsetCache.containsKey(synset.getId())) {
            ViwnNodeSynset s = synsetCache.get(synset.getId());
            if (s.isDirty())
                s.construct();
            return s;
        }

        ViwnNodeSynset new_synset = new ViwnNodeSynset(synset, this);

        synsetCache.put(synset.getId(), new_synset);
        return new_synset;
    }

    public ViwnNodeSense loadSenseNode(Sense sense) {
        if (senseCache.containsKey(sense.getId())) {
            ViwnNodeSense s = senseCache.get(sense.getId());
            if (s.isDirty())
                s.construct();
            return s;
        }

        ViwnNodeSense newSense = new ViwnNodeSense(sense, this);

        senseCache.put(sense.getId(), newSense);
        return newSense;
    }

    private Direction findCommonRelationDir(ViwnNodeSynset parent, ViwnNodeSynset child) {
        for (Direction dir : Direction.values()) {
            for (ViwnEdgeSynset e : parent.getRelation(dir)) {
                if (e.getChild().equals(child.getSynset().getId()) || e.getParent().equals(child.getSynset().getId())) {
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

    public void relationAdded(ViwnNodeSynset from, ViwnNodeSynset to) {

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
            return;
        } else if (!forest.containsVertex(second)) {
            Direction cdir = findCommonRelationDir(first, second);
            if (cdir != null) {
                second.setSpawner(first, cdir);
                forest.addVertex(second);
            }
            checkMissing();
            recreateLayoutWithFix(null, null);
            return;
        }
    }

    protected void checkMissing() {
        List<ViwnNode> nodes = new ArrayList<>(forest.getVertices());
        for (ViwnNode node : nodes) {
            if ((node instanceof ViwnNodeSynset) && !(node instanceof ViwnNodeCandExtension))
                addMissingRelations((ViwnNodeSynset) node);
        }

        for (ViwnNode node : nodes) {
            if (node instanceof ViwnNodeSynset) {
                for (Direction d : Direction.values()) {
                    checkState((ViwnNodeSynset) node, d);
                }
            }
        }
    }

    private ViwnEdge getFirstOnPath(Graph<ViwnNode, ViwnEdge> g, ViwnNode v1, ViwnNode v2) {
        DijkstraShortestPath<ViwnNode, ViwnEdge> dsp = new DijkstraShortestPath<>(g);

        List<ViwnEdge> list = dsp.getPath(v1, v2);

        if (list.isEmpty())
            return null;

        return list.get(0);
    }

    private void setSpawnerByEdge(ViwnNodeSynset node, ViwnEdgeSynset edge) {

        ViwnNodeSynset second = synsetCache.get(edge.getChild());
        if (second.equals(node))
            second = synsetCache.get(edge.getParent());
        if (second == null)
            Logger.getLogger(getClass()).log(Level.FATAL, "node of edge not in synsetCache");

        Direction d = findCommonRelationDir(second, node);
        if (d == null) {
            d = findCommonRelationDir(node, second);
            if (d != null)
                d = d.getOposite();
        }

        if (d == null)
            Logger.getLogger(getClass()).log(Level.FATAL, "can't find relation in any direction");

        node.setSpawner(second, d);
    }

    private Graph<ViwnNode, ViwnEdge> undirect(Graph<ViwnNode, ViwnEdge> g) {
        Graph<ViwnNode, ViwnEdge> new_g = new UndirectedSparseGraph<>();
        for (ViwnNode n : forest.getVertices()) {
            if (n instanceof ViwnNodeRoot)
                new_g.addVertex(n);
        }

        for (ViwnEdge e : forest.getEdges()) {
            if (e instanceof ViwnEdgeSynset || e instanceof ViwnEdgeCand) {
                new_g.addEdge(e, forest.getEndpoints(e));
            }
        }
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
                    ViwnNodeSynset n = synsetCache.get(ves.getChild());
                    if (n != null)
                        n.rereadDB();
                }

                if (!ves.getParent().equals(syns.getId())) {
                    ViwnNodeSynset n = synsetCache.get(ves.getParent());
                    if (n != null)
                        n.rereadDB();
                }
            }
        }

        relationDeleted(forest.getIncidentEdges(syns));

        forest.removeVertex(syns);
        synsetCache.remove(syns.getId());
    }

    public void updateSynset(ViwnNodeSynset syns) {
        if (!forest.containsVertex(syns))
            return;

        syns.rereadDB();

        addMissingRelations(syns);

        checkAllStates();
        recreateLayoutWithFix(syns, syns);
    }

    /**
     * @param edges
     */
    public void relationDeleted(Collection<ViwnEdge> edges) {
        if (edges == null)
            return;

        ViwnNode center = null;

        for (ViwnEdge col_e : edges) {
            forest.removeEdge(col_e);
        }

        for (ViwnEdge col_e : edges) {
            ViwnEdgeSynset edge;
            if (col_e instanceof ViwnEdgeSynset)
                edge = (ViwnEdgeSynset) col_e;
            else
                continue;

            ViwnNodeSynset first = synsetCache.get(edge.getChild());
            ViwnNodeSynset second = synsetCache.get(edge.getParent());

            for (ViwnNodeSynset node : new ViwnNodeSynset[]{first, second}) {
                if (node == null)
                    continue;

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
                        } else {
                            if (node instanceof ViwnNodeCand) {
                                node.setSpawner(rootNode, Direction.BOTTOM);
                                forest.addEdge(new ViwnEdgeCand(), rootNode, node);
                                center = node;
                            } else {
                                for (Direction dir : Direction.values())
                                    hideRelation(node, dir);
                                forest.removeVertex(node);
                                if (first.equals(node)) {
                                    center = second;
                                } else
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
     */
    public void vertexSelectionChange(ViwnNode node) {
        if (selectedNode != node) {
            selectedNode = node;
        }

        // TODO: check me : if we are in make relation mode
        ViWordNetService s = ((ViWordNetService) workbench
                .getService("pl.edu.pwr.wordnetloom.plugins.viwordnet.ViWordNetService"));
        if (s.isMakeRelationModeOn()) {
            s.makeRelation(node);
            return;
        }

        if (s.isMergeSynsetsModeOn()) {
            s.mergeSynsets(node);
            return;
        }

        for (SynsetSelectionChangeListener l : synsetSelectionChangeListeners)
            l.synsetSelectionChangeListener(node);
    }

    /**
     * Invoke graph change events.
     *
     * @author amusial
     */
    public void graphChanged() {
        for (GraphChangeListener gcl : graphChangeListeners) {
            gcl.graphChanged();
        }
    }

    /**
     * selected node setter
     *
     * @param selected_node actually clicked
     */
    public void setSelectedNode(ViwnNode selected_node) {
        selectedNode = selected_node;
    }

    /**
     * @return visualization viewer
     */
    public VisualizationViewer<ViwnNode, ViwnEdge> getVisualizationViewer() {
        return vv;
    }

    /**
     * @return graph
     */
    public Graph<ViwnNode, ViwnEdge> getGraph() {
        return forest;
    }

    /**
     * @return layout
     */
    public Layout<ViwnNode, ViwnEdge> getLayout() {
        return layout;
    }

    /**
     * @return rootNode
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
     */
    public Workbench getWorkbench() {
        return workbench;
    }

    /**
     * @param cursor new value of <code>VisualizationViewer</code> cursor
     * @author amusial
     */
    public void setCursor(Cursor cursor) {
        vv.setCursor(cursor);
    }

    /**
     * v1 and v2 could be same node, then it will stay in same point of view
     *
     * @param v1 node in first point of transformation vector
     * @param v2 node in second point of transformation vector
     * @author amusial
     */
    public void recreateLayoutWithFix(ViwnNode v1, ViwnNode v2) {
        /* remember location of group node */
        Point2D p1 = (Point2D) layout.transform(v1).clone();
        /* recreate layout */
        layout.mapNodes2Points(rootNode);
        Point2D p2 = (Point2D) layout.transform(v2).clone();
        /* transform layout */
        vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(p1.getX() - p2.getX(),
                p1.getY() - p2.getY());
        /* fire graph changed event, do it somewhere else... */
        graphChanged();
    }

    public void saveToFile(String filename) {
        Dimension size = layout.getSize();

        VisualizationImageServer<ViwnNode, ViwnEdge> vv = new VisualizationImageServer<>(layout,
                size);

        vv.getRenderer().setVertexRenderer(new ViwnVertexRenderer(vv.getRenderer().getVertexRenderer()));

        RenderContext<ViwnNode, ViwnEdge> rc = vv.getRenderContext();

        rc.setVertexShapeTransformer(ViwnNode::getShape);

        rc.setVertexFillPaintTransformer(new ViwnVertexFillColor(vv.getPickedVertexState(), rootNode));

        rc.setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<>(0.5, 0.5));

        rc.setEdgeIncludePredicate(new Predicate<Context<Graph<ViwnNode, ViwnEdge>, ViwnEdge>>() {
            public boolean evaluate(Context<Graph<ViwnNode, ViwnEdge>, ViwnEdge> context) {
                if (context.element instanceof ViwnEdgeCand) {
                    ViwnEdgeCand cand = (ViwnEdgeCand) context.element;
                    return !cand.isHidden();
                }
                return true;
            }
        });

        Transformer<ViwnEdge, Paint> edgeDrawColor = new Transformer<ViwnEdge, Paint>() {
            public Paint transform(ViwnEdge e) {
                return e.getColor();
            }
        };

        rc.setEdgeDrawPaintTransformer(edgeDrawColor);
        rc.setArrowDrawPaintTransformer(edgeDrawColor);
        rc.setArrowFillPaintTransformer(edgeDrawColor);

        rc.setEdgeStrokeTransformer(new ViwnEdgeStrokeTransformer());

        Transformer<ViwnEdge, String> stringer = new Transformer<ViwnEdge, String>() {
            public String transform(ViwnEdge rel) {
                return rel.toString();
            }
        };

        rc.setEdgeLabelTransformer(stringer);
        rc.setEdgeShapeTransformer(new EdgeShape.Line<>());

        ViwnGraphViewModalGraphMouse gm = new ViwnGraphViewModalGraphMouse(this);
        vv.addKeyListener(gm.getModeKeyListener());

        vv.getRenderer().setEdgeLabelRenderer(new AstrideLabelRenderer<>());

        BufferedImage myImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = myImage.createGraphics();

        Image img = vv.getImage(new Point2D.Float(0, 0), size);
        g2.drawImage(img, 0, 0, null);

        try {
            OutputStream out = new FileOutputStream(filename);
            ImageIO.write(myImage, "png", out);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public CriteriaDTO getCriteria() {
        return criteria;
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