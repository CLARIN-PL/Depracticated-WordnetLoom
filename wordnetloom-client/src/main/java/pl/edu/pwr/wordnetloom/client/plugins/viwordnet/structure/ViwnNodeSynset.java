package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure;

import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.SynsetData;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators.SenseFormat;
import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.workbench.implementation.ServiceManager;
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

    public final static Color vertexBackgroundColorSelected = Color.yellow;
    public final static Color vertexBackgroundColorRoot = new Color(255, 178, 178);
    public final static Color vertexBackgroundColorMarked = new Color(255, 195, 195);

    protected static SynsetNodeShape geom;

    public static Set<RelationType>[] relTypes;

    private final Set<ViwnEdgeSynset> edges_to_this_ = new HashSet<>();
    private final Set<ViwnEdgeSynset> edges_from_this_ = new HashSet<>();

    private final Synset synset;
    private final List<Sense> units;
    private ViwnNodeSet in_set_ = null;
    private boolean hasFrame = false;
    private boolean is_dirty_cache_ = true;

    private String ret = null;
    private PartOfSpeech pos = null;

    private final ArrayList<ViwnEdgeSynset>[] relations;

    private final ViwnGraphViewUI ui;

    protected State[] states = new State[NodeDirection.values().length];

    private SynsetData synsetData;

    /** Określa, stronę z której zostały w pełni pobrane relacje */
    private boolean[] fullRelation = new boolean[NodeDirection.values().length];

    public boolean isFullLoadedRelation(NodeDirection direction){
        return fullRelation[direction.ordinal()];
    }

    public void setFullLoadedRelation(NodeDirection direction, boolean isFullRelation){
        fullRelation[direction.ordinal()] = isFullRelation;
    }

    public void setFullLoadedRelation(NodeDirection[] directions, boolean isFullLoadedRelation) {
        for(NodeDirection direction : directions) {
            setFullLoadedRelation(direction, isFullLoadedRelation);
        }
    }
    
    public void setAllFullRelation(boolean isFullRelation){
        for(int i=0; i < fullRelation.length; i++){
            fullRelation[i] = isFullRelation;
        }
    }

    public ViwnNodeSynset(Synset synset, ViwnGraphViewUI ui) {
        this.synset = synset;
        this.ui = ui;
//        this.ui.addSynsetToCash(synset.getId(), this);
        this.relations = new ArrayList[NodeDirection.values().length];
        for(int i = 0; i<relations.length; i++){
            relations[i] = new ArrayList<>();
        }
        for(int i =0 ; i <states.length; i++){
            states[i] = State.NOT_EXPANDED;
        }
        units = null; //RemoteUtils.lexicalUnitRemote.dbFastGetUnits(synset, LexiconManager.getInstance().getLexicons());
        synsetData = ServiceManager.getViWordNetService(ui.getWorkbench()).getSynsetData();
        construct();
    }

    static{
        PosBgColors = new HashMap<>();
        geom = new SynsetNodeShape();
        relTypes = initializeRelTypes();
    }

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

    public void setDirty(boolean dirty)
    {
        is_dirty_cache_ = dirty;
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

    private boolean skip(ViwnEdgeSynset e) {
        String ee = e.toString();
        return ee.equals("fzn") || ee.equals("bzn") || ee.equals("Sim")
                || ee.equals("Similar_to");
    }

    public void rereadDB() {
        construct();
    }

    private void addSynsetEdges(DataEntry dataEntry, NodeDirection[] directions) {
        for(NodeDirection direction : directions){
            if(direction != NodeDirection.IGNORE){
//                for(SynsetRelation relation : dataEntry.getRelationsFrom(direction)){
//                    addEdgeSynsetToRelations(relation, direction);
//                }
//                for(SynsetRelation relation : dataEntry.getRelationsTo(direction)){
//                    addEdgeSynsetToRelations(relation, direction);
//                }
                for(SynsetRelation relation : dataEntry.getRelations(direction)) {
                    addEdgeSynsetToRelations(relation, direction);
                }
            }
        }
    }

    private void addEdgeSynsetToRelations(SynsetRelation relation, NodeDirection direction) {
        ViwnEdgeSynset edge = new ViwnEdgeSynset(relation);
        relations[direction.ordinal()].add(edge);
    }

    public void construct(NodeDirection[] directions)
    {
        edges_to_this_.clear();
        edges_from_this_.clear();

        for(int i=0; i < directions.length; i++){
            relations[directions[i].ordinal()].clear();
        }
        DataEntry dataEntry = synsetData.getById(synset.getId());
        pos = PartOfSpeechManager.getInstance().getById(dataEntry.getPosID());
        if(dataEntry != null){
            addSynsetEdges(dataEntry, directions);
        }
    }

    public void refresh(){
        DataEntry dataEntry = RemoteService.synsetRemote.findSynsetDataEntry(getSynset().getId(), LexiconManager.getInstance().getUserChosenLexiconsIds());
        ui.addToEntrySet(dataEntry);
        construct();
        ui.recreateLayout(); //TODO zmienić to na coś, co nie powoduje zmiany pozycji synsetów
    }

    public void construct() {
        construct(NodeDirection.values());
        is_dirty_cache_ = false;
    }

    /**
     * Get synset part of speech.
     *
     * @return Pos --- synset part of speech
     */
    public PartOfSpeech getPos() {
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
        // expansion of synset relations working only on left mouse button
        if(me.getButton() != MouseEvent.BUTTON1){
            return;
        }

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
                        case NOT_EXPANDED:
                            ui.showRelation(this, new NodeDirection[]{rel});
//                            ui.recreateLayout();
                            break;
                    }
                }
            }
        }
    }

    @Override
    public String getLabel() {


        if (ret == null) {
            ret = "";
            DataEntry dataEntry = synsetData.getById(getId());
            if(dataEntry != null){
                Synset synset = dataEntry.getSynset();
                //if(synset.getSynsetAttributes() != null && synset.getSynsetAttributes().getIsAbstract()) {
                //    ret = "S ";
               // }
                ret += SenseFormat.getText(dataEntry);
            } else {
                ret = "";
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
        return synsetData.getById(getId()).getLexicon();
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
