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

import edu.uci.ics.jung.visualization.VisualizationViewer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

abstract public class ViwnNode {


    private NodeDirection spawn_Node_direction = null;

    public NodeDirection getSpawnDir() {
        return spawn_Node_direction;
    }

    private ViwnNode spawner = null;

    public ViwnNode getSpawner() {
        return spawner;
    }

    public void setSpawner(ViwnNode spawner, NodeDirection spawn_Node_direction) {
        this.spawn_Node_direction = spawn_Node_direction;
        this.spawner = spawner;
    }

    /**
     * Get vertex shape (clickable area of the Vertex).
     *
     * @return shape
     */
    abstract public Shape getShape();

    abstract public void mouseClick(MouseEvent me, ViwnGraphViewUI ui);

    /**
     * Transform an absolute screen point to relative vertex point.
     *
     * @param ps
     * @param v
     * @param vv
     * @return Point2D --- vertex relative point.
     */
    public static Point absToVertexRel(Point ps, ViwnNode v, VisualizationViewer<ViwnNode, ViwnEdge> vv) {
        Point2D p = vv.getGraphLayout().transform(v);
        Point2D ps2 = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(ps);
        return new Point((int) (ps2.getX() - p.getX()), (int) (ps2.getY() - p.getY()));
    }

    private boolean marked = false;

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean state) {
        marked = state;
    }

    abstract public String getLabel();
}
