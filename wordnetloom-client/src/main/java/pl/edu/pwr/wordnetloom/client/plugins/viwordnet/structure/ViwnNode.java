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
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;

/**
 *
 * Graph node.
 *
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 *
 */
abstract public class ViwnNode {

    public enum Direction {
        LEFT("LEFT"),
        RIGHT("RIGHT"),
        BOTTOM("BOTTOM"),
        TOP("TOP");

        private String str;

        Direction(String name) {
            str = name;
        }

        public String getAsString() {
            return str;
        }

        public Direction getOposite() {
            switch (this) {
                case BOTTOM:
                    return TOP;
                case TOP:
                    return BOTTOM;
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
                default:
                    return null;
            }
        }
    }

    private Direction spawn_direction = null;

    public Direction getSpawnDir() {
        return spawn_direction;
    }

    private ViwnNode spawner = null;

    public ViwnNode getSpawner() {
        return spawner;
    }

    public void setSpawner(ViwnNode spawner, Direction spawn_direction) {
        this.spawn_direction = spawn_direction;
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

    /**
     * @return marked
     *
     */
    public boolean isMarked() {
        return marked;
    }

    /**
     * set marked property
     *
     * @param state set marked to
     *
     */
    public void setMarked(boolean state) {
        this.marked = state;
    }

    /**
     * @return text for label
     */
    abstract public String getLabel();
}
