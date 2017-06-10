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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.listeners;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;

/**
 * class ViwnGraphMouseListener implements GraphMouseListener and handles mouse
 * actions in graph panel
 *
 * @author amusial
 *
 */
public class ViwnGraphMouseListener implements GraphMouseListener<ViwnNode> {

    private final ViwnGraphViewUI owner;

    /**
     * @param owner ViwnGraphViewUI owner of this graph listener
     */
    public ViwnGraphMouseListener(ViwnGraphViewUI owner) {
        this.owner = owner;
    }

    @Override
    public void graphClicked(ViwnNode v, MouseEvent me) {
    }

    /* we don't need this */
    @Override
    public void graphPressed(ViwnNode v, MouseEvent me) {

    }

    @Override
    public void graphReleased(ViwnNode v, MouseEvent me) {
        /* the 'everything wrecking jumping graphs' ultimate problem solver */

        // remember node location
        Point2D p1 = (Point2D) owner.getLayout().transform(v).clone();

        // propagate event to nodes
        v.mouseClick(me, this.owner);

        // find new node location
        Point2D p2 = (Point2D) owner.getLayout().transform(v).clone();

        double xdif = p1.getX() - p2.getX();
        double ydif = p1.getY() - p2.getY();

        owner.getVisualizationViewer().getRenderContext().
                getMultiLayerTransformer().getTransformer(Layer.LAYOUT)
                .translate(xdif, ydif);
        if (me.getButton() == MouseEvent.BUTTON1) {
            owner.vertexSelectionChange(v);
        }
    }

}
