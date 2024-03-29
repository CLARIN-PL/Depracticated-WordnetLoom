package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views;

import com.alee.laf.panel.WebPanel;
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
import org.apache.commons.collections15.Transformer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.SynsetData;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.ViWordNetService;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.GraphChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.SynsetSelectionChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.VertexSelectionChangeListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners.ViwnGraphMouseListener;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.*;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.control.ViwnGraphViewModalGraphMouse;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.ViwnEdgeStrokeTransformer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.ViwnVertexToolTipTransformer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.layout.ViwnLayout;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.renderers.AstrideLabelRenderer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.renderers.ViwnVertexFillColor;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.renderers.ViwnVertexRenderer;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractViewUI;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import se.datadosen.component.RiverLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author boombel
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 */
public class ViwnGraphViewUI extends AbstractViewUI implements
        VertexSelectionChangeListener<ViwnNode>, Loggable {

    /**
     * A visualisation of synset and relations between synsets.
     */
    private final DirectedGraph<ViwnNode, ViwnEdge> forest = new DirectedSparseMultigraph<>();

    private final HashMap<Long, ViwnNodeSynset> cache = new HashMap<>();

    private final ViwnLayout layout = new ViwnLayout(forest);

    private VisualizationViewer<ViwnNode, ViwnEdge> vv = null;

    private ViwnNodeRoot rootNode = null;

    // Collection of object which listen for an event of synset selection
    // change.
    protected Collection<SynsetSelectionChangeListener> synsetSelectionChangeListeners = new ArrayList<>();

    // Collection of objects which listen for an event of visualisation changes
    protected Collection<GraphChangeListener> graphChangeListeners = new ArrayList<>();

    private ViwnNode selectedNode = null;

//    private CriteriaDTO criteria = new CriteriaDTO();
    private CriteriaDTO criteria;
    private int openedFromTabIndex;

    private final ScalingControl scaler = new ViewScalingControl();

    // Graph mouse listener, handles mouse clicks at vertices
    private ViwnGraphMouseListener graphMouseListener = null;

    private SynsetData synsetData;
    private Synset rootSynset;

    private static final float EDGE_PICK_SIZE = 10f;
    private final int MAX_SYNSETS_SHOWN = 4;
    private final int MIN_SYNSETS_IN_GROUP = 2;

    public Synset getRootSynset() {
        return rootSynset;
    }

    public void setRoot(ViwnNodeRoot root){
        rootNode = root;
        if(root == null) {
            rootSynset = null;
        } else if(root instanceof ViwnNodeSynset){
            rootSynset = ((ViwnNodeSynset)root).getSynset();
        }
    }

    public void addSynsetToCash(Long synsetId, ViwnNodeSynset node) {
        cache.put(synsetId, node);
    }

    /* End of transient cache for visualisation biulding */
    @Override
    public JComponent getRootComponent() {
        return null;
    }

    /**
     * @param content JPanel from workbench
     * @author amusial
     */
    @Override
    protected void initialize(WebPanel content) {
        content.removeAll();
        content.setLayout(new RiverLayout());

        // Create a panel for visualisation visualisation.
        JPanel graph;

        try {
            graph = getSampleGraphViewer();
            content.add(graph, "hfill vfill");
        } catch (IOException ex) {
            logger().error("IO exception", ex);
        }

        synsetData = ServiceManager.getViWordNetService(workbench).getSynsetData();

    }

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

        rc.setVertexShapeTransformer((ViwnNode v) -> v.getShape());

        rc.setVertexFillPaintTransformer(new ViwnVertexFillColor(vv
                .getPickedVertexState(), rootNode));

        rc.setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<>(
                0.5, 0.5));

        rc.setEdgeIncludePredicate((Context<Graph<ViwnNode, ViwnEdge>, ViwnEdge> context) -> {
            if (context.element instanceof ViwnEdgeCandidate) {
                ViwnEdgeCandidate cand = (ViwnEdgeCandidate) context.element;
                return !cand.isHidden();
            }
            return true;
        });

        Transformer<ViwnEdge, Paint> edgeDrawColor = (ViwnEdge e) -> e.getColor();
        rc.setEdgeDrawPaintTransformer(edgeDrawColor);
        rc.setArrowDrawPaintTransformer(edgeDrawColor);
        rc.setArrowFillPaintTransformer(edgeDrawColor);

        rc.setEdgeStrokeTransformer(new ViwnEdgeStrokeTransformer());

        graphMouseListener = new ViwnGraphMouseListener(this);
        vv.addGraphMouseListener(graphMouseListener);

        Transformer<ViwnEdge, String> stringer = (ViwnEdge rel) -> rel.toString();
        rc.setEdgeLabelTransformer(stringer);
        rc.setEdgeShapeTransformer(new EdgeShape.Line<>());

        ViwnGraphViewModalGraphMouse gm = new ViwnGraphViewModalGraphMouse(this);
        vv.addKeyListener(gm.getModeKeyListener());

        vv.getRenderer().setEdgeLabelRenderer(new AstrideLabelRenderer<>());

        vv.setGraphMouse(gm);

        GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
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

    public void addSynsetNode(ViwnNodeSynset synset) {
        clear();
//        cache.clear();
        // TODO refaktor, część wspólna z metodą refreshView
        if (!cache.containsKey(synset.getSynset().getId())) {
            ViwnNodeSynset rootNodeSynset = synset;
            rootNodeSynset.construct();

            rootNode = rootNodeSynset;
            for (NodeDirection direction : NodeDirection.values()) {
                (rootNodeSynset).setDownloadedRelation(direction, true);
            }
            cache.put(synset.getId(), rootNodeSynset);

            vv.getRenderContext().setVertexFillPaintTransformer(
                    new ViwnVertexFillColor(vv.getPickedVertexState(), rootNodeSynset));

            if (!forest.containsVertex(rootNodeSynset)) {
                forest.addVertex(rootNodeSynset);
            }

            for (NodeDirection dir : NodeDirection.values()) {
                if (rootNode instanceof ViwnNodeSynset) {
                    (rootNodeSynset).setState(dir,
                            ViwnNodeSynset.State.NOT_EXPANDED);
                    rootNodeSynset.setDownloadedRelation(dir, false);
                }
            }
            addMissingRelationInForest();
            checkAllStates();
            recreateLayout();
            center();
            vv.setVisible(true);
        }
    }

    /**
     * @param synset
     */
    public void refreshView(Synset synset) {
        // Clear the visualisation.
        clear();
        rootSynset = synset;

        ViwnNodeSynset rootNodeSynset;
        if (cache.containsKey(synset.getId())) {
            rootNodeSynset = cache.get(synset.getId());
        } else {
            rootNodeSynset = new ViwnNodeSynset(synset, this);
        }
        rootNode = rootNodeSynset;
        for (NodeDirection direction : NodeDirection.values()) {
            (rootNodeSynset).setDownloadedRelation(direction, true);
        }
        cache.put(synset.getId(), rootNodeSynset);

        vv.getRenderContext().setVertexFillPaintTransformer(
                new ViwnVertexFillColor(vv.getPickedVertexState(), rootNodeSynset));

        if (!forest.containsVertex(rootNodeSynset)) {
            forest.addVertex(rootNodeSynset);
        }

        for (NodeDirection dir : NodeDirection.values()) {
            if (rootNode instanceof ViwnNodeSynset) {
                (rootNodeSynset).setState(dir,
                        ViwnNodeSynset.State.EXPANDED);
            }
            if (dir != NodeDirection.IGNORE) {
                showRelationGUI(rootNodeSynset, dir, null);
            }
        }
        addMissingRelationInForest();
        checkAllStates();
        recreateLayout();
        center();
        vv.setVisible(true);
    }

    /**
     * Recreate a visualisation layout. Currently fires graphChanged event, this should
     * be done somewhere else
     *
     * @author amusial
     */
    public void recreateLayout() {
        layout.mapNodes2Points(rootNode);

        graphChanged();
        /*
         * this event above (graphChanged) should be fired only when visualisation
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
     * deselects all visualisation nodes
     */
    public void deselectAll() {
        vv.getPickedVertexState().clear();
    }

    /**
     * scale visualisation to fill full visualization viewer space
     *
     * @author amusial
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
        // get visualisation layout size
        Dimension ld = vv.getGraphLayout().getSize();
        // finally scale it if view bounds are different than visualisation layer bounds
        if (vd.equals(ld) == false) {
            float heightRatio = (float) (vd.getWidth() / ld.getWidth());
            float widthRatio = (float) (vd
                    .getHeight() / ld.getHeight());

            scaler.scale(vv, (heightRatio < widthRatio ? heightRatio
                    : widthRatio), new Point2D.Double());
        }
    }

    /**
     * @param node
     * @param direction
     */
    public void checkState(ViwnNodeSynset node, NodeDirection direction) {
        node.setState(direction, ViwnNodeSynset.State.NOT_EXPANDED);
        boolean all_ok = true;

        ViwnNodeSynset other;
        for (ViwnEdgeSynset e : node.getRelation(direction)) {
            if (e.getParent().equals(node.getId())) {
                other = cache.get(e.getChild());
            } else {
                other = cache.get(e.getParent());
            }
            if (forest.containsVertex(other)) {
                node.setState(direction, ViwnNodeSynset.State.SEMI_EXPANDED);
            } else if (!node.getSynsetSet(direction).getSynsets().contains(other)) {
                all_ok = false;
            }
        }

        if (all_ok) {
            if (forest.containsVertex(node.getSynsetSet(direction))
                    || node.getSynsetSet(direction).getSynsets().isEmpty()) {
                node.setState(direction, ViwnNodeSynset.State.EXPANDED);
            }
        }
    }

    /**
     * @param node
     * @param dir
     */
    public void checkStateAllInRel(ViwnNodeSynset node, NodeDirection dir) {
        for (ViwnEdgeSynset e : node.getRelation(dir)) {
            ViwnNodeSynset test = cache.get(e.getParent());
            if (test != null && test.equals(node)) {
                test = cache.get(e.getChild());
            }
            if (test != null) {
                checkState(test, dir.getOpposite());
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
                                        NodeDirection rel) {
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
        ViwnNodeSynset parent;
        ViwnNodeSynset child;
        ViwnNodeSynset inner;
        for (NodeDirection dir : NodeDirection.values()) {
            for (ViwnEdgeSynset e : synset.getRelation(dir)) {
                if (!forest.containsEdge(e)) {
                    parent = cache.get(e.getParent());
                    child = cache.get(e.getChild());
                    inner = synset == parent ? child : parent;
                    // jeżeli oba węzły są widoczne na grafie, rysujemy łącząca je linie
                    if (forest.containsVertex(parent) && forest.containsVertex(child)) {
                        addEdge(e, e.getParent(), e.getChild());

                        for (NodeDirection in_dir : NodeDirection.values()) {
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
                                NodeDirection dir) {
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
//        selectedNode = synset;

    }

    public void addSynsetsFromSet(List<ViwnNodeSynset> synsets) {
        if (synsets.isEmpty()) {
            return;
        }
        for (ViwnNodeSynset synset : synsets) {
            synsetData.load(synset.getSynset(), LexiconManager.getInstance().getUserChosenLexiconsIds());
            synset.construct();
            addSynsetFromSet(synset);
        }
        addMissingRelationInForest();
        checkAllStates();
        recreateLayout();
    }

    private void addSynsetFromSet_(ViwnNodeSynset synset) {
        synset.setSet(null);
        forest.addVertex(synset);
//        addMissingRelations(synset);
        ViwnNodeSynset spawner = (ViwnNodeSynset) synset.getSpawner();
        ViwnNodeSet set = getSetFrom(synset);

        set.remove(synset);

        ViwnEdgeSynset e = findRelation(spawner, synset, synset.getSpawnDir());

        addEdge(e, loadSynsetNode(e.getSynsetFrom()),
                loadSynsetNode(e.getSynsetTo()));

        if (set.getSynsets().isEmpty()) {
            forest.removeVertex(set);
        }
    }

    public void clearNodeCache() {
        cache.clear();
    }

    /**
     * @param synset
     * @return set
     */
    public ViwnNodeSet addSynsetToSet(ViwnNodeSynset synset) {

        for (NodeDirection dir : NodeDirection.values()) {
            hideRelation(synset, dir);
        }
        forest.removeVertex(synset);
        ViwnNodeSet set = getSetFrom(synset);
        set.add(synset);
        synset.setSet(set);

        // if this is first synset, add the set to the visualisation
        if (set.getSynsets().size() == 1) {
            forest.addVertex(set);
            addEdgeSynsSet((ViwnNodeSynset) synset.getSpawner(), set,
                    synset.getSpawnDir());
            set.setSpawner(synset.getSpawner(), synset.getSpawnDir());
        }
//        selectedNode = set;

        checkAllStates();

        return set;
    }

    /**
     * @param synsetNode
     * @param hide_dir
     */
    public void hideRelation(ViwnNodeSynset synsetNode, NodeDirection hide_dir) {
        synchronized (forest) {
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
                        for (NodeDirection rel : NodeDirection.values()) {
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
                for (NodeDirection d : NodeDirection.values()) {
                    checkState((ViwnNodeSynset) node, d);
                }
            }
        }
    }

    private void addMissingRelationInForest() {
//        Collection<ViwnNode> nodes = forest.getVertices();
//        for(ViwnNode node : nodes)
//        {
//            if(node instanceof ViwnNodeSynset)
//                addMissingRelations((ViwnNodeSynset)node);
//        }
        //TODO zlikwidowac kopiowanie i usunąć błąd powodujący dodawanie drzew do lasu podczas iterowania po forest
        List<ViwnNode> nodes = new ArrayList<>(forest.getVertices());
        nodes.forEach(viwnNode -> {
            if (viwnNode instanceof ViwnNodeSynset)
                addMissingRelations((ViwnNodeSynset) viwnNode);
        });
    }

    public void showRelation(Synset synset, RelationType relationType) {
        ViwnNodeSynset nodeSynset = new ViwnNodeSynset(synset, this);
        forest.addVertex(nodeSynset);
        NodeDirection[] directions  = new NodeDirection[]{relationType.getNodePosition()};
        showRelation(nodeSynset, directions, relationType);

    }

    public void showRelation(ViwnNodeSynset synsetNode, NodeDirection[] directions, RelationType relationType){
        SwingUtilities.invokeLater(() -> workbench.setBusy(true));
        // jeżeli relacje nie były pobrane wczesniej, zostaną pobrane
        if (!checkNodeWasExtended(synsetNode, directions)) {
            if(!checkRelationIsDownloaded(synsetNode, directions)){
                synsetData.load(synsetNode.getSynset(), LexiconManager.getInstance().getUserChosenLexiconsIds(), directions);
            }
            synsetNode.construct(directions);
            synsetNode.setDownloadedRelation(directions, true);
        }

        for (NodeDirection dir : directions) {
            showRelationGUI(synsetNode, dir, relationType);
        }
        addMissingRelationInForest();
        checkAllStates();

        SwingUtilities.invokeLater(() -> {
            recreateLayoutWithFix(synsetNode, synsetNode);
            recreateLayout();
            vv.repaint();
            workbench.setBusy(false);
            vv.setVisible(true);
        });
    }

    private boolean checkRelationIsDownloaded(ViwnNodeSynset synset, NodeDirection[] directions) {
        for(NodeDirection direction : directions){
            for(ViwnEdgeSynset edge : synset.getRelation(direction)){
                if(synsetData.getById(edge.getChild()) == null){
                    return false;
                }
            }
        }
        return true;
    }

    public void showRelation(ViwnNodeSynset synsetNode, NodeDirection[] dirs) {
        showRelation(synsetNode, dirs, null);
    }

    private boolean checkNodeWasExtended(ViwnNodeSynset node, NodeDirection[] directions) {
        for (NodeDirection direction : directions) {
            if (!node.isDownloadedRelations(direction)) {
                return false;
            }
        }
        return true;
    }

    public void addToEntrySet(DataEntry dataEntry) {
        ServiceManager.getViWordNetService(workbench).getSynsetData().addData(dataEntry);
    }

    /**
     * @param synsetNode node which relations will be shown
     * @param direction  relation class which will be shown
     */
    private void showRelationGUI(ViwnNodeSynset synsetNode, NodeDirection direction, RelationType relationType) {
        List<ViwnEdgeSynset> relations = (List<ViwnEdgeSynset>) synsetNode.getRelation(direction);
        if(relationType != null){
            relations = relations.stream().filter(e->e.getRelationType().getId().equals(relationType.getId())).collect(Collectors.toList());
        }
        int toShow = Math.min(MAX_SYNSETS_SHOWN, relations.size()); // number synset to show on the graph
        int insertedNodes = insertVisibleSynsetNodes(relations, synsetNode, direction, toShow);
        if (insertedNodes < relations.size()) {
            insertInvisibleSynsetNodes(relations, synsetNode, direction, insertedNodes);
        }
    }

    private int insertVisibleSynsetNodes(List<ViwnEdgeSynset> relations, ViwnNodeSynset nodeSynset, NodeDirection direction, int maxShowedNodes) {
        int i = 0;
        ViwnNodeSynset node;
        while (i < maxShowedNodes && i < relations.size()) {
            node = getNodeSynsetFromEdge(relations.get(i), nodeSynset);
            tryInsertNodeToForest(node, nodeSynset, direction);
            i++;
        }
        return i;
    }

    private void insertInvisibleSynsetNodes(List<ViwnEdgeSynset> relations, ViwnNodeSynset nodeSynset, NodeDirection direction, int insertedNodes) {
        ViwnNodeSet set = nodeSynset.getSynsetSet(direction);
        ViwnNodeSynset node;
        for (int i = insertedNodes; i < relations.size(); i++) {
            node = getNodeSynsetFromEdge(relations.get(i), nodeSynset);
            if (!forest.containsVertex(node)) {
                if (!set.contains(node)) {
                    node.setSpawner(nodeSynset, direction);
                    node.setSet(set);
                    set.add(node);
                    node.setDirty(true);
                }
            }
        }

        if (!set.getSynsets().isEmpty()) {
            if (set.getSynsets().size() == 1) { // if set contains only one element, we put out node from set and put in on the graph. Set is not added to graph
                node = set.getSynsets().iterator().next();
                tryInsertNodeToForest(node, nodeSynset, direction);
            } else {
                set.setSpawner(nodeSynset, direction);
                forest.addVertex(set);
                addEdgeSynsSet(nodeSynset, set, direction);
            }
        }
    }

    private void tryInsertNodeToForest(ViwnNodeSynset node, ViwnNodeSynset spawnerNode, NodeDirection direction) {
        if (!forest.containsVertex(node)) {
            if (node.getSet() != null) {
                node.getSet().remove(node);
                node.setSet(null);
            }
            node.setSpawner(spawnerNode, direction);
            forest.addVertex(node);
        }
    }

    private ViwnNodeSynset getNodeSynsetFromEdge(ViwnEdgeSynset edge, ViwnNodeSynset originalNode) {
        if (edge.getSynsetFrom().getId().equals(originalNode.getId())) {
            return loadSynsetNode(edge.getSynsetTo());
        } else {
            return loadSynsetNode(edge.getSynsetFrom());
        }
    }

    /**
     * Clear visualisation (removes all edges and nodes).
     */
    public void clear() {
        // Lock the visualisation object
        synchronized (forest) {
            new ArrayList<>(forest.getEdges()).forEach(forest::removeEdge);
            new ArrayList<>(forest.getVertices()).forEach(forest::removeVertex);
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

    private NodeDirection findCommonRelationDir(ViwnNodeSynset parent,
                                                ViwnNodeSynset child) {
        for (NodeDirection dir : NodeDirection.values()) {
            for (ViwnEdgeSynset e : parent.getRelation(dir)) {
                if (e.getChild().equals(child.getSynset().getId())
                        || e.getParent().equals(child.getSynset().getId())) {
                    return dir;
                }
            }
        }
        return null;
    }

    public void addConnectedSynsetsToGraph(ViwnNodeSynset first, List<Synset> synsets, NodeDirection direction) {
        ViwnNodeSynset prev = first;
        for (Synset synset : synsets) {
            ViwnNodeSynset node = loadSynsetNode(synset);
            tryInsertNodeToForest(node, prev, direction);
//            recreateLayoutWithFix(node, prev);
            prev = node;
        }
        checkMissing();
        recreateLayoutWithFix(null, null);
        center();
    }

    public void relationAdded(ViwnNodeSynset from, ViwnNodeSynset to) {
        ViwnNodeSynset newSynset;
        // if synset node not belong to forest, we create new copy of node
        if (!forest.containsVertex(from)) {
            newSynset = new ViwnNodeSynset(from.getSynset(), this);
            insertNodeInForest(newSynset, to);
        } else if (!forest.containsVertex(to)) {
            newSynset = new ViwnNodeSynset(to.getSynset(), this);
            insertNodeInForest(newSynset, from);
        }
    }

    private void insertNodeInForest(ViwnNodeSynset first, ViwnNodeSynset second) {
        NodeDirection cdir = findCommonRelationDir(second, first);
        if (cdir != null) {
            first.setSpawner(second, cdir);
            forest.addVertex(first);
            if (!cache.containsKey(first.getId())) {
                cache.put(first.getId(), first);
            }
        }
        checkMissing(first);
        recreateLayoutWithFix(first, second);
    }

    private void checkMissing(ViwnNodeSynset node) {
        addMissingRelations(node);
        // TODO może dorobić checkState
    }

    protected void checkMissing() {
        List<ViwnNode> nodes = new ArrayList<>(forest.getVertices());

        nodes.stream().filter((node) -> ((node instanceof ViwnNodeSynset)
                && !(node instanceof ViwnNodeCandExtension))).forEachOrdered((node) -> {
            addMissingRelations((ViwnNodeSynset) node);
            for (NodeDirection d : NodeDirection.values()) {
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
            logger().error("node of edge not in cache");
        }

        NodeDirection d = findCommonRelationDir(second, node);
        if (d == null) {
            d = findCommonRelationDir(node, second);
            if (d != null) {
                d = d.getOpposite();
            }
        }

        if (d == null) {
            logger().error("can't find relation in any direction");
        }

        node.setSpawner(second, d);
    }

    private Graph<ViwnNode, ViwnEdge> undirect(Graph<ViwnNode, ViwnEdge> g) {
        Graph<ViwnNode, ViwnEdge> new_g = new UndirectedSparseGraph<>();
        forest.getVertices().stream().filter((n) -> (n instanceof ViwnNodeRoot)).forEachOrdered((n) -> {
            new_g.addVertex(n);
        });

        forest.getEdges().stream().filter((e) -> (e instanceof ViwnEdgeSynset || e instanceof ViwnEdgeCandidate)).forEachOrdered((e) -> {
            new_g.addEdge(e, forest.getEndpoints(e));
        });
        return new_g;
    }

    public void removeSynset(ViwnNodeSynset syns) {
        if (syns.equals(rootNode)) {
            clear();
            return;
        }

        for (NodeDirection dir : NodeDirection.values()) {
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

        edges.forEach(forest::removeEdge);

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

                node.construct();

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
                            if (e instanceof ViwnEdgeCandidate) {
                                node.setSpawner(rootNode, NodeDirection.BOTTOM);
                            } else {
                                setSpawnerByEdge(node, (ViwnEdgeSynset) e);
                            }
                        } else if (node instanceof ViwnNodeCand) {
                            node.setSpawner(rootNode, NodeDirection.BOTTOM);
                            forest.addEdge(new ViwnEdgeCandidate(), rootNode, node);
                            center = node;
                        } else {
                            for (NodeDirection dir : NodeDirection.values()) {
                                hideRelation(node, dir);
                            }
                            forest.removeVertex(node);
                            cache.remove(node.getId());
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
     * @param synset
     * @author amusial
     */
    @Override
    public void vertexSelectionChange(ViwnNode synset) {
        if (selectedNode != synset) {
//            selectedNode = synset;
        }

        // TODO: check me : if we are in make relation mode
        ViWordNetService s = ServiceManager.getViWordNetService(workbench);
        if (s.isMakeRelationModeOn()) {
            s.makeRelation(synset);
            return;
        }

        if (s.isMergeSynsetsModeOn()) {
            s.mergeSynsets(synset);
            return;
        }

        synsetSelectionChangeListeners.forEach((l) -> l.synsetSelectionChangeListener(synset));
    }

    /**
     * Invoke visualisation change events.
     *
     * @author amusial
     */
    public void graphChanged() {
        graphChangeListeners.forEach(GraphChangeListener::graphChanged);
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
     * @return visualisation
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
        vv.getRenderContext().getMultiLayerTransformer()
                .getTransformer(Layer.LAYOUT)
                .translate(p1.getX() - p2.getX(), p1.getY() - p2.getY());
        /* fire visualisation changed event, do it somewhere else... */
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
            if (context.element instanceof ViwnEdgeCandidate) {
                ViwnEdgeCandidate cand = (ViwnEdgeCandidate) context.element;
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
            logger().error("File not found", ex);
        } catch (IOException ex) {
            logger().error("IO Error", ex);
        }

    }

    public CriteriaDTO getCriteria() {
        System.out.println("ViwnGraphViewUI - getCriteria : admin@clarin-pl.eu" + criteria);

        return criteria;
    }

    public void setCriteria(CriteriaDTO criteria) {
        this.criteria = criteria;

//        this.criteria.setLemma(criteria.getLemma());
//        this.criteria.setLexicon(criteria.getLexicon());
//        this.criteria.setPartOfSpeech(criteria.getPartOfSpeech());
//        this.criteria.setDomain(criteria.getDomain());
//        this.criteria.setRegister(criteria.getRegister());
//        this.criteria.setDefinition(criteria.getDefinition());
//        this.criteria.setComment(criteria.getComment());
//        this.criteria.setExample(criteria.getExample());
//        this.criteria.setRelation(criteria.getRelation());
//        this.criteria.setSense(criteria.getSense());
    }

    public int getOpenedFromTabIndex() {
        return openedFromTabIndex;
    }

    public void setOpenedFromTabIndex(int openedFromTabIndex) {
        this.openedFromTabIndex = openedFromTabIndex;
    }

}
