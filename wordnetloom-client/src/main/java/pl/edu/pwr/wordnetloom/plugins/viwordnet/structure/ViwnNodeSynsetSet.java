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

import pl.edu.pwr.wordnetloom.plugins.viwordnet.views.ViwnGraphViewUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class ViwnNodeSynsetSet extends ViwnNode {

    public final static Color vertexBackgroundColor = Color.getHSBColor(0.25f, 0.5f, 1);
    public final static Shape shape = new Ellipse2D.Float(-20, -20, 40, 40);

    private HashSet<ViwnNodeSynset> syns_ = new HashSet<>();

    public void add(ViwnNodeSynset synset) {
        syns_.add(synset);
    }

    public boolean remove(ViwnNodeSynset synset) {
        return syns_.remove(synset);
    }

    public void removeAll() {
        syns_.clear();
    }

    public Collection<ViwnNodeSynset> getSynsets() {
        return Collections.unmodifiableSet(syns_);
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void mouseClick(MouseEvent me, ViwnGraphViewUI ui) {
    }

    @Override
    public String getLabel() {
        return "Synset collection";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "zbiór: [[ " + syns_.toString() + "]]";
    }

    public boolean contains(ViwnNodeSynset node) {
        return syns_.contains(node);
    }
}
