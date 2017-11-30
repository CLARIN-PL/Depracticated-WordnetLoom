package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure;

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.*;
import java.util.List;

public class ViwnNodeSynset extends ViwnNodeRoot implements Comparable<ViwnNodeSynset> {

    public enum State {
        EXPANDED, SEMI_EXPANDED, NOT_EXPANDED
    }

    public static HashMap<PartOfSpeech, Color> PosBgColors;

    public final static Color vertexBackgroundColorVerbStroke = new Color(239, 224, 52);
    public final static Color vertexBackgroundColorNounStroke = new Color(27, 221, 27);
    public final static Color vertexBackgroundColorAdjStroke = new Color(28, 193, 177);
    public final static Color vertexBackgroundColorAdvStroke = new Color(81, 115, 193);

    public final static Color vertexBackgroundColorSelected = Color.yellow;
    public final static Color vertexBackgroundColorRoot = new Color(255, 178, 178);
    public final static Color vertexBackgroundColorMarked = new Color(255, 195, 195);

    protected static SynsetNodeShape geom;

//    public static Set<RelationTypeManager>[] relTypes;
    public static Set<RelationType>[] relTypes;

    private final Set<ViwnEdgeSynset> edges_to_this_ = new HashSet<>();
    private final Set<ViwnEdgeSynset> edges_from_this_ = new HashSet<>();

    private final Synset synset;
    private final List<Sense> units;
    private ViwnNodeSet in_set_ = null;
    private boolean hasFrame = false;
    private boolean is_dirty_cache_;

    private String ret = null;
    private PartOfSpeech pos = null;

    private final ArrayList<ViwnEdgeSynset>[] relations;

    private final ViwnGraphViewUI ui;

    private final boolean hadCheckedPOS = false;

    protected State[] states = new State[NodeDirection.values().length];

    //TODO zrefaktorować to
    /** Określa, stronę z której zostały w pełni pobrane relacje */
    private boolean[] fullRelation = new boolean[NodeDirection.values().length];

    public boolean isFullRelation(NodeDirection direction){
        return fullRelation[direction.ordinal()];
    }

    public void setFullRelation(NodeDirection direction, boolean isFullRelation){
        fullRelation[direction.ordinal()] = isFullRelation;
    }

    public void setAllFullRelation(boolean isFullRelation){
        for(int i=0; i < fullRelation.length; i++){
            fullRelation[i] = isFullRelation;
        }
    }

    public ViwnNodeSynset(Synset synset, ViwnGraphViewUI ui) {
        this.synset = synset;
        this.ui = ui;
        this.ui.addSynsetToCash(synset.getId(), this);
        this.relations = new ArrayList[NodeDirection.values().length];
        for(int i = 0; i<relations.length; i++){
            relations[i] = new ArrayList<>();
        }

        units = null; //RemoteUtils.lexicalUnitRemote.dbFastGetUnits(synset, LexiconManager.getInstance().getLexicons());
        setup();
    }

    static{
        PosBgColors = new HashMap<>();
        geom = new SynsetNodeShape();
        relTypes = initializeRelTypes();
    }

//    private static Set<RelationTypeManager>[] initializeRelTypes() {
//        Set<RelationTypeManager>[] tempRelTypes = new Set[NodeDirection.values().length];
//        Arrays.fill(tempRelTypes, new HashSet<>());
//        return tempRelTypes;
//    }

    private static Set<RelationType>[] initializeRelTypes(){
        Set<RelationType>[] tempRelTypes = new Set[NodeDirection.values().length];
        Arrays.fill(tempRelTypes, new HashSet<>());
        return tempRelTypes;
    }

    @Override
    public int compareTo(ViwnNodeSynset o) {
        return synset.getId().compareTo(o.synset.getId());
    }

    public void setSet(ViwnNodeSet set) {
        in_set_ = set;
    }

    public ViwnNodeSet getSet() {
        return in_set_;
    }

    public static void reproduceCache(HashMap<Long, ViwnNodeSynset> cache) {
        for (Long l : cache.keySet()) {
            ViwnNodeSynset s = cache.get(l);
            for (NodeDirection d : NodeDirection.values()) {
                s.states[d.ordinal()] = State.NOT_EXPANDED;
                s.relations[d.ordinal()].clear();
            }
            s.is_dirty_cache_ = true;
        }
    }

    public boolean isDirty() {
        return is_dirty_cache_;
    }

    @Override
    public Shape getShape() {
        return geom.shape;
    }

    public Shape getButtonArea(NodeDirection dir) {
        return geom.buttons[dir.ordinal()];
    }

    public Long getId() {
        return synset.getId();
    }

    public boolean containsRelation(ViwnEdgeSynset rel) {
        return edges_from_this_.contains(rel) || edges_to_this_.contains(rel);
    }

    private void add_if_new(SynsetRelation rel) {
        ViwnEdgeSynset newEdge = new ViwnEdgeSynset(rel);
        if(synset.getId().equals(rel.getChild().getId())){
            edges_to_this_.add(newEdge);
        } else if(synset.getId().equals(rel.getParent().getId())){
            edges_from_this_.add(newEdge);
        } else {
            System.err.println("Database sanity error");
        }
//        if (LexiconManager.getInstance().getLexicons().contains(rel.getRelationType().getLexicon().getId())) {
//            if (rel.getChild().getId().equals(synset.getId())) {
//                ViwnEdgeSynset new_edge = new ViwnEdgeSynset(rel);
//                edges_to_this_.add(new_edge);
//            } else if (rel.getParent()
//                    .getId().equals(synset.getId())) {
//                ViwnEdgeSynset new_edge = new ViwnEdgeSynset(rel);
//                edges_from_this_.add(new_edge);
//            } else {
//                System.err.println("Database sanity error");
//            }
//        }
    }

    private void addEdgeToThis(SynsetRelation rel){
        ViwnEdgeSynset edge = new ViwnEdgeSynset(rel);
        edges_to_this_.add(edge);
    }

    private void addEdgeFromThis(SynsetRelation rel){
        ViwnEdgeSynset edge = new ViwnEdgeSynset(rel);
        edges_from_this_.add(edge);
    }

    public void removeRelation(ViwnEdgeSynset e) {
        ArrayList<Iterator<ViwnEdgeSynset>> iters = new ArrayList<>();

        for (NodeDirection dir : NodeDirection.values()) {
            iters.add(relations[dir.ordinal()].iterator());
        }
        iters.add(edges_from_this_.iterator());
        iters.add(edges_to_this_.iterator());

        for (Iterator<ViwnEdgeSynset> it : iters) {
            while (it.hasNext()) {
                ViwnEdgeSynset ee = it.next();
                if (e.equals(ee) || e.equalsReverse(ee)) {
                    it.remove();
                    break;
                }
            }
        }
    }

    public void construct() {
        Set<ViwnEdgeSynset> relationsFrom = new HashSet<>(edges_from_this_);
        Set<ViwnEdgeSynset> relationsTo = new HashSet<>(edges_to_this_);

        for (NodeDirection dir : NodeDirection.values()) {
            relations[dir.ordinal()].clear();
        }

        NodeDirection direction;

        Set<ViwnEdgeSynset>[] edgesSet = new HashSet[4]; // set zapobiegający powstawaniu powtórzeń
        for(int i = 0; i<edgesSet.length; i++){
            edgesSet[i] = new HashSet<>();
        }
        for(ViwnEdgeSynset edge : relationsFrom){
            direction = edge.getRelationType().getNodePosition();
//            relations[direction.ordinal()].add(edge); //TODO sprawdzić, czy nie będzie potrzebne sprawdzanie, czy już dany typ relacji jest na liście
            edgesSet[direction.ordinal()].add(edge);
        }

        for(ViwnEdgeSynset edge : relationsTo){
            if(edge.getRelationType() != null){ //TODO po usunięciu błędnych relacji usunąć tego ifa
                direction = edge.getRelationType().getNodePosition().getOpposite();

//                relations[direction.ordinal()].add(edge);
                edgesSet[direction.ordinal()].add(edge);
            }
        }

        for(int i=0; i<edgesSet.length; i++){
            relations[i] = new ArrayList<>(edgesSet[i]);
        }
//        Iterator<ViwnEdgeSynset> it = relationsFrom.iterator();
//        while (it.hasNext()) {
//            ViwnEdgeSynset e = it.next();
//
//            for (NodeDirection dir : NodeDirection.values()) {
//
//                if (relTypes[dir.ordinal()].contains(e.getRelationType())) {
//                    relations[dir.ordinal()].add(e);
//                    relationsFrom.remove(e);
//
//                    relationsTo.remove(e);
//
//                    ViwnEdgeSynset rev = e.createDummyReverse();
//                    if (rev != null) {
//                        relationsTo.remove(rev);
//                    }
//
//                    it = relationsFrom.iterator();
//                }
//            }
//        }
//
//        it = relationsFrom.iterator();
//        while (it.hasNext()) {
//            ViwnEdgeSynset e = it.next();
//            if (skip(e)) {
//                continue;
//            }
//            for (NodeDirection dir : NodeDirection.values()) {
//                if (e.getRelationType() != null) {
//                    // TODO odkomentować i przerobić tak, aby działało
////                    if (relTypes[dir.ordinal()].contains(RelationTypeManager.get(e
////                            .getRelationType().rev_id()))) {
////                        if (skip(e)) {
////                            continue;
////                        }
////                        relations[dir.ordinal()].add(e);
////                        to.remove(e);
////                        it = to.iterator();
////                    } else if (relTypes[dir.getOpposite().ordinal()].contains(e
////                            .getRelationType())) {
////                        if (skip(e)) {
////                            continue;
////                        }
////                        relations[dir.ordinal()].add(e);
////                        to.remove(e);
////                        it = to.iterator();
////                    }
//                }
//            }
//        }

        is_dirty_cache_ = false;
    }

    private boolean skip(ViwnEdgeSynset e) {
        String ee = e.toString();
        return ee.equals("fzn") || ee.equals("bzn") || ee.equals("Sim")
                || ee.equals("Similar_to");
    }

    public void rereadDB() {
        setup();
    }

    private void addSynsetEdges(DataEntry dataEntry)
    {
        for(NodeDirection direction : NodeDirection.values()){
            if(direction != NodeDirection.IGNORE){
                for(SynsetRelation relation : dataEntry.getRelationsFrom(direction)){
                    addEdgeSynsetToRelations(relation, direction);
                }
                for(SynsetRelation relation : dataEntry.getRelationsTo(direction)){
//                    if(relation.getRelationType().getAutoReverse()){
//                        addEdgeSynsetToRelations(relation, direction);
//                    } else {
                        addEdgeSynsetToRelations(relation, direction.getOpposite());
//                    }
                }
            }
        }
    }

    private void addEdgeSynsetToRelations(SynsetRelation relation, NodeDirection direction) {
        ViwnEdgeSynset edge = new ViwnEdgeSynset(relation);
        relations[direction.ordinal()].add(edge);
    }

    public void setup() {
        edges_to_this_.clear();
        edges_from_this_.clear();

        // first - primary cache
//        Set<SynsetRelation> relsUP = ui.getUpperRelationsFor(synset.getId());
//        Set<SynsetRelation> relsDW = ui.getSubRelationsFor(synset.getId());

        for(int i=0; i < relations.length; i++){
            relations[i].clear();
        }
        DataEntry dataEntry = ui.getEntrySetFor(synset.getId());
        if(dataEntry != null){
            addSynsetEdges(dataEntry);
        }
        // no cache? fetch from database
//        if (relsUP == null) {
//            relsUP = RemoteUtils.synsetRelationRemote.dbGetUpperRelations(
//                    synset, null, LexiconManager.getInstance().getLexicons());
//        }
//        if (relsDW == null) {
//            relsDW = RemoteUtils.synsetRelationRemote.dbGetSubRelations(synset,
//                    null, LexiconManager.getInstance().getLexicons());
//        }
        // Get relations 'OTHER synset' -> 'THIS synset'

//
//        for (SynsetRelation rel : relsUP) {
////            add_if_new(rel);
//            addEdgeFromThis(rel);
//        }
//
//
//        // Get relations 'THIS synset' -> 'OTHER synset'
//        for (SynsetRelation rel : relsDW) {
////            add_if_new(rel);
//            addEdgeToThis(rel);
//        }

        // fetching poses from temporary cache
        getPos();

        // adding relations to appropiate groups
//        construct();
    }

    /**
     * Get synset part of speech.
     *
     * @return Pos --- synset part of speech
     */
    public PartOfSpeech getPos() {
        if (pos == null && !hadCheckedPOS) {
            DataEntry dataEntry = ui.getEntrySetFor(getId());
            pos = PartOfSpeechManager.getInstance().getById(dataEntry.getPosID());

//            DataEntry dataSet = ui.getEntrySetFor(getId());
//            Long l = null;
//
//            if (dataSet == null || dataSet.getPosID() == null) {
//                l = RemoteUtils.synsetRemote.fastGetPOSID(this.synset);
//            } else {
//                l = dataSet.getPosID();
//            }
//
//            if (l == null) {
//                pos = null;
//                hadCheckedPOS = true;
//                pos = PosManager.getInstance().getFromID(0);
//            } else {
//                pos = PosManager.getInstance().getFromID(l.intValue());
//            }
        }
        return pos;
    }

    public boolean hasCache() {
        return pos != null || ret != null;
    }

    public ViwnNodeSynset setPos(PartOfSpeech pos) {
        this.pos = pos;
        if (pos == null) {
            unitsStr = null;
        }
        return this;
    }

    /**
     * Get synset DTO object.
     *
     * @return Synset --- synset object.
     */
    public Synset getSynset() {
        return synset;
    }

    @Override
    public void mouseClick(MouseEvent me, ViwnGraphViewUI ui) {
        Point p = absToVertexRel(me.getPoint(), this,
                ui.getVisualizationViewer());

        for (NodeDirection rel : NodeDirection.values()) {
            Collection<ViwnEdgeSynset> edges = getRelation(rel);
            if (edges.size() > 0) {
                Area ar = new Area(getButtonArea(rel));

                if (ar.contains(p)) {
                    switch (getState(rel)) {
                        case EXPANDED:
                            ui.setSelectedNode(this);
                            ui.hideRelation(this, rel);
                            ui.recreateLayout();
                            break;
                        case SEMI_EXPANDED:
                            ui.showRelation(this, new NodeDirection[]{rel});
                            ui.recreateLayout();
                            break;
                        case NOT_EXPANDED:
                            ui.showRelation(this, new NodeDirection[]{rel});
                            ui.recreateLayout();
                            break;
                    }
                }
            }
        }
    }

    @Override
    public String getLabel() {
        if (ret == null) {
            DataEntry dataEntry = ui.getEntrySetFor(getId());
            if(dataEntry != null && dataEntry.getLabel() != null){
                ret = dataEntry.getLabel();
            } else {
                ret = "";
            }
            if(ret.equals("")){
                System.out.println();
            }
//            DataEntry dataSet = ui.getEntrySetFor(getId());
//            if (dataSet == null || dataSet.getLabel() == null) {
//                String ret = "";
//                if (Synset.isAbstract(Common.getSynsetAttribute(synset,
//                        Synset.ISABSTRACT))) {
//                    ret = "S ";
//                }
//                // check if synset isnt null or empty
//                if (units != null && !units.isEmpty()) {
//                    ret += ((Sense) units.iterator().next()).toString();
//                    if (units.size() > 1) {
//                        ret += " ...";
//                    }
//                } else {
//                    ret = "! S.y.n.s.e.t p.u.s.t.y !";
//                }
//                this.ret = ret;
//            } else {
//                this.ret = dataSet.getLabel();
//            }
        }
        return ret;
    }

    public String getLexiconLabel() {
//        if (units != null && !units.isEmpty()) {
//            Sense unit = ((Sense) units.iterator().next());
//            return unit.getLexicon().getLexiconIdentifier().getText();
//        }
        return "";
    }

    public ViwnNodeSynset setLabel(String s) {
        ret = s;
        if (s == null) {
            unitsStr = null;
        }
        return this;
    }

    /**
     * @param dir
     * @return relations of specified class
     */
    public Collection<ViwnEdgeSynset> getRelation(NodeDirection dir) {
        return relations[dir.ordinal()];
    }

    public void setState(NodeDirection dir, State state_new) {
        states[dir.ordinal()] = state_new;
    }

    public State getState(NodeDirection dir) {
        return states[dir.ordinal()];
    }

    public void setFrame(boolean frame) {
        hasFrame = frame;
    }

    public boolean getFrame() {
        return hasFrame;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    private String unitsStr;

    public String getUnitsStr() {
        if (getSynset() != null) {
            if (unitsStr == null) {
//                unitsStr = RemoteUtils.synsetRemote
//                        .dbRebuildUnitsStr(getSynset(), LexiconManager
//                                .getInstance().getLexicons());
            }
            if(unitsStr == null || unitsStr.equals("")){
                System.out.println();
            }
            return unitsStr;
        }
        return null;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((synset == null) ? 0 : synset.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ViwnNodeSynset other = (ViwnNodeSynset) obj;
        if (synset == null) {
            if (other.synset != null) {
                return false;
            }
        } else if (!synset.equals(other.synset)) {
            return false;
        }
        return true;
    }
}
