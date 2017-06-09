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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure;

import java.util.ArrayList;
import java.util.Comparator;
import pl.edu.pwr.wordnetloom.client.systems.enums.RelationTypes;

/**
 * @author amusial
 * @author boombel
 *
 */
public class ViwnNodeAlphabeticComparator implements Comparator<ViwnNode> {

    public static ArrayList<RelationTypes> order = null;

    private ArrayList<RelationTypes> rel_order = null;

    public ViwnNodeAlphabeticComparator() {
        if (order == null) {
            rel_order = new ArrayList<RelationTypes>();

            rel_order.add(RelationTypes.getByName("hiperonimia"));
            rel_order.add(RelationTypes.getByName("hiponimia"));
            rel_order.add(RelationTypes.getByName("mieszkaniec"));
            rel_order.add(RelationTypes.getByName("bliskoznaczność"));
        } else {
            rel_order = order;
        }
    }

    public int compare(ViwnNode arg0, ViwnNode arg1) {
        if (!(arg1 instanceof ViwnNodeSynset)) {
            return -1;
        }
        if (!(arg0 instanceof ViwnNodeSynset)) {
            return 1;
        }

        if (!(arg0.getSpawner() instanceof ViwnNodeSynset)) {
            return arg0.getLabel().compareTo(arg1.getLabel());
        }

        ViwnNodeSynset s1 = (ViwnNodeSynset) arg0;
        ViwnNodeSynset s2 = (ViwnNodeSynset) arg1;
        ViwnNodeSynset spawner = (ViwnNodeSynset) (arg0.getSpawner());

        ViwnEdgeSynset ee1 = null;
        ViwnEdgeSynset ee2 = null;

        for (ViwnEdgeSynset e : spawner.getRelation(arg0.getSpawnDir())) {
            if (e.getChild().equals(s1.getSynset().getId())
                    || e.getParent().equals(s1.getSynset().getId())) {
                ee1 = e;
                break;
            }
        }

        if (ee1 == null) {
            return -1;
        }

        for (ViwnEdgeSynset e : spawner.getRelation(arg1.getSpawnDir())) {
            if (e.getChild().equals(s2.getSynset().getId())
                    || e.getParent().equals(s2.getSynset().getId())) {
                ee2 = e;
                break;
            }
        }

        if (ee2 == null) {
            return -1;
        }

        int idx1 = rel_order.indexOf(ee1.getRelationType());
        int idx2 = rel_order.indexOf(ee2.getRelationType());

        if (idx1 == idx2) {
            return arg0.getLabel().compareTo(arg1.getLabel());
        } else {
            return new Integer(idx1).compareTo(idx2);
        }
    }

}
