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
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.*;

public class ViwnNodeSynset extends ViwnNodeRoot implements Comparable<ViwnNodeSynset> {

    public enum State {
        EXPANDED,
        SEMI_EXPANDED,
        NOT_EXPANDED
    }

    private final String SYNSET_ARTIFICIAL_SYMBOL = "S ";

    public static HashMap<PartOfSpeech, Color> PosBgColors;

    public final static Color vertexBackgroundColorSelected = Color.yellow;
    public final static Color vertexBackgroundColorRoot = new Color(255, 178, 178);
    public final static Color vertexBackgroundColorMarked = new Color(255, 195, 195);

    protected static SynsetNodeShape geom;

    private final Set<ViwnEdgeSynset> edges_to_this_ = new HashSet<>();
    private final Set<ViwnEdgeSynset> edges_from_this_ = new HashSet<>();

    private final Synset synset;
    private ViwnNodeSet in_set_ = null;
    private boolean hasFrame = false;
    private boolean is_dirty_cache_ = true;

    private String ret = null;
    private PartOfSpeech pos = null;

    private final ArrayList<ViwnEdgeSynset>[] relations;

    private final ViwnGraphViewUI ui;

    private State[] states = new State[NodeDirection.values().length];

    private SynsetData synsetData;

    /** Contains information about downloaded relation for directions */
    private boolean[] downloadedRelations = new boolean[NodeDirection.values().length];

    static{
        PosBgColors = new HashMap<>();
        geom = new SynsetNodeShape();
    }

    private static Set<RelationType>[] initializeRelTypes(){
        Set<RelationType>[] tempRelTypes = new Set[NodeDirection.values().length];
        Arrays.fill(tempRelTypes, new HashSet<>());
        return tempRelTypes;
    }

    public boolean isDownloadedRelations(NodeDirection direction){
        return downloadedRelations[direction.ordinal()];
    }

    public void setDownloadedRelation(NodeDirection direction, boolean isFullRelation){
        downloadedRelations[direction.ordinal()] = isFullRelation;
    }

    public void setDownloadedRelation(NodeDirection[] directions, boolean isFullLoadedRelation) {
        for(NodeDirection direction : directions) {
            setDownloadedRelation(direction, isFullLoadedRelation);
        }
    }

    public ViwnNodeSynset(Synset synset, ViwnGraphViewUI ui) {
        this.synset = synset;
        this.ui = ui;
        this.relations = new ArrayList[NodeDirection.values().length];
        for(int i = 0; i<relations.length; i++){
            relations[i] = new ArrayList<>();
        }
        for(int i =0 ; i <states.length; i++){
            states[i] = State.NOT_EXPANDED;
        }
        synsetData = ServiceManager.getViWordNetService(ui.getWorkbench()).getSynsetData();
        construct();
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

    public void rereadDB() {
        construct();
    }

    private void addSynsetEdges(DataEntry dataEntry, NodeDirection[] directions) {
        for(NodeDirection direction : directions){
            if(direction != NodeDirection.IGNORE){
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

    public void construct(NodeDirection[] directions) {
        edges_to_this_.clear();
        edges_from_this_.clear();

        for (NodeDirection direction : directions) {
            relations[direction.ordinal()].clear();
        }
        DataEntry dataEntry = synsetData.getById(synset.getId());
        updateSynset(dataEntry.getSynset()); // TODO try update status without this
        pos = PartOfSpeechManager.getInstance().getById(dataEntry.getPosID());
        addSynsetEdges(dataEntry, directions);
    }

    private void updateSynset(Synset synset){
        this.synset.setStatus(synset.getStatus());
    }

    public void refresh(){
        DataEntry dataEntry = RemoteService.synsetRemote.findSynsetDataEntry(getSynset().getId(), LexiconManager.getInstance().getUserChosenLexiconsIds());
        ui.addToEntrySet(dataEntry);
        construct();
        ui.recreateLayout();
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
                            hideRelations(ui, rel);
                            break;
                        case SEMI_EXPANDED:
                        case NOT_EXPANDED:
                            showRelations(ui, rel);
                            break;
                    }
                }
            }
        }
    }

    private void showRelations(ViwnGraphViewUI ui, NodeDirection rel) {
        ui.showRelation(this, new NodeDirection[]{rel});
    }

    private void hideRelations(ViwnGraphViewUI ui, NodeDirection rel) {
        ui.setSelectedNode(this);
        ui.hideRelation(this, rel);
        ui.recreateLayout();
    }

    @Override
    public String getLabel() {
        if (ret == null) {
            ret = createLabel();
        }
        return ret;
    }

    private String createLabel() {
        String label = "";
        DataEntry dataEntry = synsetData.getById(getId());
        if(dataEntry != null){
            Synset synset = dataEntry.getSynset();
            if(synset.getAbstract()) {
                label = SYNSET_ARTIFICIAL_SYMBOL;
            }
            label += SenseFormat.getText(dataEntry);
        }
        return label;
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

    public Collection<ViwnEdgeSynset> getRelations(RelationType relationType){
        Collection<ViwnEdgeSynset>  result = new ArrayList<>();
        for(ViwnEdgeSynset edge : relations[relationType.getNodePosition().ordinal()]){
            if(edge.getRelationType().getId().equals(relationType.getId())){
                result.add(edge);
            }
        }
        return result;
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
                return ret;
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
