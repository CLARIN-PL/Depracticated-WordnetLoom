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

import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseRelation;
import pl.edu.pwr.wordnetloom.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.systems.managers.LexiconManager;

import java.awt.*;
import java.util.HashMap;

/**
 * Edge between senses.
 */
public class ViwnEdgeSense extends ViwnEdge {

    public static HashMap<Long, Color> relationColors = new HashMap<>();

    private final SenseRelation relation;
    private ViwnNodeSense firstSense = null;
    private ViwnNodeSense secondSense = null;

    ViwnEdgeSense(SenseRelation relation) {
        this.relation = relation;
    }

    /**
     * checks if this edge is equal to the reversed edge
     *
     * @param edge second edge
     * @return return true if this edge is equal to the reversed edge
     */
    public boolean equalsReverse(ViwnEdgeSense edge) {
        Long rev_id = edge.getRelationType().rev_id();
        return getRelation().equals(rev_id) &&
                getChild().equals(edge.getParent()) &&
                getParent().equals(edge.getChild());
    }

    /**
     * @return relation id
     */
    public Long getRelation() {
        return relation.getRelation().getId();
    }

    public void setFirstSense(ViwnNodeSense s1) {
        firstSense = s1;
    }

    public void setSecondSense(ViwnNodeSense s2) {
        secondSense = s2;
    }

    public ViwnNodeSense getFirstSense() {
        return firstSense;
    }

    public ViwnNodeSense getSecondSense() {
        return secondSense;
    }

    /**
     * @return child node id
     */
    public Long getChild() {
        return relation.getSense_to();
    }

    /**
     * @return parent node id
     */
    public Long getParent() {
        return relation.getSense_from();
    }

    public Sense getSenseFrom() {
        return relation.getSenseFrom();
    }

    public Sense getSenseTo() {
        return relation.getSenseTo();
    }

    /**
     * @return relation type
     */
    public RelationTypes getRelationType() {
        RelationTypes rt = RelationTypes.get(relation.getRelation().getId());
        if (rt == null) {
            throw new RuntimeException("relation type doesn't exist");
        }
        return rt;
    }

    @Override
    public String toString() {
        try {
            return getRelationType().Sname();
        } catch (Exception e) {
            return getRelationType().name();
        }
    }

    @Override
    public Color getColor() {
        Color col = relationColors.get(relation.getRelation().getId());
        if (col == null)
            return Color.black;
        else
            return col;
    }

    public ViwnEdgeSense createDummyReverse() {
        if (RelationTypes.get(relation.getRelation().getId()).rev_id() == null)
            return null;

        SenseRelation rdto = new SenseRelation();
        rdto.setSenseTo(relation.getSenseFrom());
        rdto.setSenseFrom(relation.getSenseTo());

        RelationType currentRelation = RelationTypes.get(relation.getRelation().getId()).getRelationType();
        if (LexiconManager.getInstance().getLexicons().contains(currentRelation.getReverse().getLexicon().getId())) {
            RelationType reverseRelation = RelationTypes.get(currentRelation.getReverse().getId()).getRelationType();
            rdto.setRelation(reverseRelation);
        }

        ViwnEdgeSense e = new ViwnEdgeSense(rdto);
        return e;
    }

    public SenseRelation getSenseRelation() {
        return relation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViwnEdgeSense)) return false;

        ViwnEdgeSense that = (ViwnEdgeSense) o;

        return relation != null ? relation.equals(that.relation) : that.relation == null;
    }

    @Override
    public int hashCode() {
        return relation != null ? relation.hashCode() : 0;
    }
}
