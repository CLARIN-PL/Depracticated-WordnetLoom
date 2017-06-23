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

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.common.model.CustomColor;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraph;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

public class ViwnNodeCand extends ViwnNodeSynset {

    static class Geometry {

        final public Shape shape;

        Geometry() {
            Polygon p = new Polygon();
            p.addPoint(-50, 15);
            p.addPoint(-50, -15);
            p.addPoint(50, -15);
            p.addPoint(50, 15);
            shape = p;
        }
    }

    private final static Geometry geom = new Geometry();

    private final ExtGraph ext;

    private final boolean added;
    private boolean center;
    private boolean evaluated;

    public ViwnNodeCand(Synset synset, ExtGraph ext, boolean added, ViwnGraphViewUI ui) {
        super(synset, ui);
        this.ext = ext;
        this.added = added;
        center = false;
    }

    public boolean isCenter() {
        return center;
    }

    public void setCenter() {
        center = true;
    }

    public void setEvaluated() {
        evaluated = true;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public ExtGraph getExt() {
        return ext;
    }

    public boolean isAdded() {
        return added;
    }

    public Color getColor() {
        if (ext.isWeak()) {
            return CustomColor.nodeWeakMatch;
        } else if (ext.getScore1() >= 5) {
            return CustomColor.nodeSynsetScore50;
        } else if (ext.getScore1() >= 4) {
            return CustomColor.nodeSynsetScore40;
        } else if (ext.getScore1() >= 3) {
            return CustomColor.nodeSynsetScore30;
        } else if (ext.getScore1() >= 2.5) {
            return CustomColor.nodeSynsetScore25;
        } else if (ext.getScore1() >= 1.5) {
            return CustomColor.nodeSynsetScore15;
        } else {
            return Color.WHITE;
        }
    }

    @Override
    public Shape getShape() {
        if (ext.isWeak()) {
            return geom.shape;
        } else {
            return super.getShape();
        }
    }
}
