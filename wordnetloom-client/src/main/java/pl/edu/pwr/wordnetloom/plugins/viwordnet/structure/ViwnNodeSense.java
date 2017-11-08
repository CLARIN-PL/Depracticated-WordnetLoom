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

package pl.edu.pwr.wordnetloom.plugins.viwordnet.structure;

import pl.edu.pwr.wordnetloom.dto.SenseDataEntry;
import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseRelation;
import pl.edu.pwr.wordnetloom.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.utils.RemoteUtils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.*;
import java.util.List;

public class ViwnNodeSense extends ViwnNodeRoot implements Comparable<ViwnNodeSense> {
    public enum State {
        EXPANDED, SEMI_EXPANDED, NOT_EXPANDED
    }

    public static HashMap<PartOfSpeech, Color> PosBgColors = new HashMap<>();

    public final static Color vertexBackgroundColorSelected = Color.yellow;
    public final static Color vertexBackgroundColorRoot = new Color(255, 178, 178);
    public final static Color vertexBackgroundColorMarked = new Color(255, 195, 195);

    protected static SynsetNodeShape geom = new SynsetNodeShape();

    public static Set<RelationTypes>[] relTypes = new Set[]{
            new HashSet<RelationTypes>(), new HashSet<RelationTypes>(),
            new HashSet<RelationTypes>(), new HashSet<RelationTypes>()};

    private Set<ViwnEdgeSense> edges_to_this_ = new HashSet<>();
    private Set<ViwnEdgeSense> edges_from_this_ = new HashSet<>();

    private Sense sense;
    private ViwnNodeSenseSet in_set_ = null;
    private boolean hasFrame = false;
    private boolean is_dirty_cache_;

    private String ret = null;
    private PartOfSpeech pos = null;

    private ArrayList<ViwnEdgeSense>[] relations = new ArrayList[]{
            new ArrayList<ViwnEdgeSense>(), new ArrayList<ViwnEdgeSense>(),
            new ArrayList<ViwnEdgeSense>(), new ArrayList<ViwnEdgeSense>()};

    protected State[] states = new State[]{State.NOT_EXPANDED,
            State.NOT_EXPANDED, State.NOT_EXPANDED, State.NOT_EXPANDED};

    public int compareTo(ViwnNodeSense o) {
        return sense.getId().compareTo(o.sense.getId());
    }

    public void setSet(ViwnNodeSenseSet set) {
        in_set_ = set;
    }

    public ViwnNodeSenseSet getSet() {
        return in_set_;
    }

    public static void reproduceCache(HashMap<Long, ViwnNodeSense> cache) {
        for (Long l : cache.keySet()) {
            ViwnNodeSense s = cache.get(l);
            for (Direction d : Direction.values()) {
                s.states[d.ordinal()] = State.NOT_EXPANDED;
                s.relations[d.ordinal()].clear();
            }
            s.is_dirty_cache_ = true;
        }
    }

    public boolean isDirty() {
        return is_dirty_cache_;
    }

    public Shape getShape() {
        return geom.shape;
    }

    public Shape getButtonArea(Direction dir) {
        return geom.buttons[dir.ordinal()];
    }

    public Long getId() {
        return sense.getId();
    }

    public boolean containsRelation(ViwnEdgeSynset rel) {
        if (edges_from_this_.contains(rel) || edges_to_this_.contains(rel))
            return true;

        return false;
    }

    private void addIfNew(SenseRelation rel) {
        if (LexiconManager.getInstance().getLexicons().contains(rel.getRelation().getLexicon().getId())) {
            if (rel.getSenseTo().getId().equals(sense.getId())) {
                ViwnEdgeSense new_edge = new ViwnEdgeSense(rel);
                edges_to_this_.add(new_edge);
            } else if (rel.getSenseFrom().getId().equals(sense.getId())) {
                ViwnEdgeSense new_edge = new ViwnEdgeSense(rel);
                edges_from_this_.add(new_edge);
            } else
                System.err.println("Database sanity error");
        }
    }

    public void removeRelation(ViwnEdgeSense edge) {
        ArrayList<Iterator<ViwnEdgeSense>> iters = new ArrayList<>();

        for (Direction dir : Direction.values())
            iters.add(relations[dir.ordinal()].iterator());
        iters.add(edges_from_this_.iterator());
        iters.add(edges_to_this_.iterator());

        for (Iterator<ViwnEdgeSense> it : iters)
            while (it.hasNext()) {
                ViwnEdgeSense ee = it.next();
                if (edge.equals(ee) || edge.equalsReverse(ee)) {
                    it.remove();
                    break;
                }
            }
    }

    public void construct() {
        Set<ViwnEdgeSense> rels = new HashSet<>(edges_from_this_);
        Set<ViwnEdgeSense> to = new HashSet<>(edges_to_this_);

        for (Direction dir : Direction.values()) {
            relations[dir.ordinal()].clear();
        }

        Iterator<ViwnEdgeSense> it = rels.iterator();
        while (it.hasNext()) {
            ViwnEdgeSense e = it.next();

            for (Direction dir : Direction.values()) {

                if (relTypes[dir.ordinal()].contains(e.getRelationType())) {
                    relations[dir.ordinal()].add(e);
                    rels.remove(e);

                    to.remove(e);

                    ViwnEdgeSense rev = e.createDummyReverse();
                    if (rev != null) {
                        to.remove(rev);
                    }

                    it = rels.iterator();
                }
            }
        }

        it = to.iterator();
        while (it.hasNext()) {
            ViwnEdgeSense e = it.next();
            if (skip(e))
                continue;
            for (Direction dir : Direction.values()) {
                if (e.getRelationType() != null) {
                    if (relTypes[dir.ordinal()].contains(RelationTypes.get(e
                            .getRelationType().rev_id()))) {
                        if (skip(e))
                            continue;
                        relations[dir.ordinal()].add(e);
                        to.remove(e);
                        it = to.iterator();
                    } else if (relTypes[dir.getOposite().ordinal()].contains(e
                            .getRelationType())) {
                        if (skip(e))
                            continue;
                        relations[dir.ordinal()].add(e);
                        to.remove(e);
                        it = to.iterator();
                    }
                }
            }
        }

        is_dirty_cache_ = false;
    }

    private boolean skip(ViwnEdgeSense e) {
        String ee = e.toString();
        return ee.equals("fzn") || ee.equals("bzn") || ee.equals("Sim")
                || ee.equals("Similar_to");
    }

    public void rereadDB() {
        setup();
    }

    private void setup() {
        edges_to_this_.clear();
        edges_from_this_.clear();

        // first - primary cache
        List<SenseRelation> relsUP = ui.getUpperRelationsForSense(sense.getId());
        List<SenseRelation> relsDW = ui.getSenseSubRelationsForSense(sense.getId());

        // no cache? fetch from database
        if (relsUP == null)
            relsUP = RemoteUtils.lexicalRelationRemote.dbGetUpperRelations(sense, null);
        if (relsDW == null)
            relsDW = RemoteUtils.lexicalRelationRemote.dbGetSubRelations(sense, null);

        // Get relations 'OTHER sense' -> 'THIS sense'
        for (SenseRelation rel : relsUP)
            addIfNew(rel);

        // Get relations 'THIS sense' -> 'OTHER sense'
        for (SenseRelation rel : relsDW)
            addIfNew(rel);

        // fetching poses from temporary cache
        getPos();

        // adding relations to appropiate groups
        construct();
    }

    private ViwnGraphViewUI ui;

    public ViwnNodeSense(Sense sense, ViwnGraphViewUI ui) {
        this.sense = sense;
        this.ui = ui;
        this.ui.addSenseToCash(sense.getId(), this); // check me
        setup();
    }

    private boolean hadCheckedPOS = false;

    /**
     * Get synset part of speech.
     *
     * @return Pos --- synset part of speech
     */
    public PartOfSpeech getPos() {
        if (pos == null && !hadCheckedPOS) {
            SenseDataEntry dataSet = ui.getEntrySetForSense(getId());
            Long l = null;

            if (dataSet == null || dataSet.getPosID() == null)
                l = sense.getPartOfSpeech().getId();
            else
                l = dataSet.getPosID();

            if (l == null) {
                pos = null;
                hadCheckedPOS = true;
                pos = PosManager.getInstance().getFromID(0);
            } else
                pos = PosManager.getInstance().getFromID(l.intValue());
        }
        return pos;
    }

    public boolean hasCache() {
        return pos != null || ret != null;
    }

    public ViwnNodeSense setPos(PartOfSpeech pos) {
        this.pos = pos;
        return this;
    }

    public Sense getSense() {
        return sense;
    }

    public void mouseClick(MouseEvent me, ViwnGraphViewUI ui) {
        Point p = absToVertexRel(me.getPoint(), this,
                ui.getVisualizationViewer());

        for (Direction rel : Direction.values()) {
            Collection<ViwnEdgeSense> edges = getRelation(rel);
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
                            ui.showRelation(this, new Direction[]{rel});
                            ui.recreateLayout();
                            break;
                        case NOT_EXPANDED:
                            ui.showRelation(this, new Direction[]{rel});
                            ui.recreateLayout();
                            break;
                    }
                }
            }
        }
    }

    @Override
    public String getLabel() {
        return sense.toString();
    }

    public String getLexiconLabel() {
        return sense.getLexicon().getLexiconIdentifier().getText();
    }

    public ViwnNodeSense setLabel(String s) {
        ret = s;
        return this;
    }

    /**
     * @param dir
     * @return relations of specified class
     */
    public Collection<ViwnEdgeSense> getRelation(Direction dir) {
        return relations[dir.ordinal()];
    }

    public void setState(Direction dir, State state_new) {
        states[dir.ordinal()] = state_new;
    }

    public State getState(Direction dir) {
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

    public String getUnitsStr() {
        return sense.toString();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((sense == null) ? 0 : sense.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ViwnNodeSense other = (ViwnNodeSense) obj;
        if (sense == null) {
            if (other.sense != null)
                return false;
        } else if (!sense.equals(other.sense))
            return false;
        return true;
    }

}
