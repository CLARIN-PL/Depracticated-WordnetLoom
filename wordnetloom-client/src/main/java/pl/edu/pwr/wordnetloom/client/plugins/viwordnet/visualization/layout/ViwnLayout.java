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

 /*
 * Copyright (c) 2005, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 *
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 *
 * Created on Jul 9, 2005
 */
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.layout;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdgeSynset;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TODO: This layout won't work with new version of visualisation (synset groups). The
 * problem is about unchecked casts of ViwnEdge to ViwnEdgeSynset.
 *
 * @author Michał Marcińczuk (modification of TreeLayout)
 * @author Karlheinz Toni
 * @author Tom Nelson - converted to jung2
 */
public class ViwnLayout implements Layout<ViwnNode, ViwnEdge> {

    protected Dimension size = new Dimension(600, 600);
    protected Graph<ViwnNode, ViwnEdge> graph;
    protected Map<ViwnNode, Integer> basePositions = new HashMap<>();

    protected Map<ViwnNode, Point2D> locations = LazyMap.decorate(new HashMap<ViwnNode, Point2D>(), (ViwnNode arg0) -> new Point2D.Double());

    protected transient Set<ViwnNode> alreadyDone = new HashSet<>();

    /**
     * The default horizontal vertex spacing. Initialized to 50.
     */
    public static int DEFAULT_DISTX = 180;

    /**
     * The default vertical vertex spacing. Initialized to 50.
     */
    public static int DEFAULT_DISTY = 120;

    /**
     * The horizontal vertex spacing. Defaults to {@code DEFAULT_XDIST}.
     */
    protected int distX = 180;

    /**
     * The vertical vertex spacing. Defaults to {@code DEFAULT_YDIST}.
     */
    protected int distY = 120;

    protected transient Point m_currentPoint = new Point();

    /**
     * Creates an instance for the specified visualisation with default X and Y
     * distances.
     *
     * @param g
     */
    public ViwnLayout(Graph<ViwnNode, ViwnEdge> g) {
        this(g, DEFAULT_DISTX, DEFAULT_DISTY);
    }

    /**
     * Creates an instance for the specified visualisation and X distance with default Y
     * distance.
     *
     * @param g
     * @param distx
     */
    public ViwnLayout(Graph<ViwnNode, ViwnEdge> g, int distx) {
        this(g, distx, DEFAULT_DISTY);
    }

    /**
     * Creates an instance for the specified visualisation, X distance, and Y distance.
     *
     * @param g
     * @param distx
     * @param disty
     */
    public ViwnLayout(Graph<ViwnNode, ViwnEdge> g, int distx, int disty) {
        if (g == null) {
            throw new IllegalArgumentException("Graph must be non-null");
        }
        if (distx < 1 || disty < 1) {
            throw new IllegalArgumentException(
                    "X and Y distances must each be positive");
        }
        graph = g;
        distX = distx;
        distY = disty;
    }

    public void buildTree(ViwnNode rootNode) {
        m_currentPoint = new Point(200, 200);
        calculateDimensionX(rootNode);
        buildTreeR(rootNode);
    }

    protected void buildTreeR(ViwnNode v) {

        if (!alreadyDone.contains(v)) {
            alreadyDone.add(v);

            setCurrentPositionFor(v);

            int sizeXofCurrent = 0;
            if (basePositions.containsKey(v)) {
                sizeXofCurrent = basePositions.get(v);
            }

            int lastX = m_currentPoint.x - sizeXofCurrent / 2;
            int lastY = 0;

            int startX = m_currentPoint.x;
            int startY = m_currentPoint.y;
            int sizeXofChild;
            int startXofChild;

            // Divide accessors according to relation type (minor and major)
            Set<ViwnNode> minorVertex = new HashSet<>();
            Set<ViwnNode> majorVertex = new HashSet<>();
            for (ViwnEdge edge : graph.getOutEdges(v)) {
                System.out.println(((ViwnEdgeSynset) edge).getRelation());
                if (((ViwnEdgeSynset) edge).getRelation() != 11) {
                    minorVertex.add(graph.getOpposite(v, edge));
                } else {
                    majorVertex.add(graph.getOpposite(v, edge));
                }
            }
            System.out.println("Right: " + minorVertex.size());

            // Layout major children below the node
            for (ViwnNode element : majorVertex) {
                sizeXofChild = basePositions.get(element);
                startXofChild = lastX + sizeXofChild / 2;

                m_currentPoint.y += distY;
                m_currentPoint.x = startXofChild;
                buildTreeR(element);
                m_currentPoint.y -= distY;

                lastX = lastX + sizeXofChild + distX;
            }

            // Layout minor children on the right
            lastY = startY;
            for (ViwnNode element : minorVertex) {
                m_currentPoint.x = startX + distX;
                m_currentPoint.y = lastY;

                buildTreeR(element);

                lastY += distY / 2;
            }

            // Divide accessors according to relation type (minor and major)
            Set<ViwnNode> minorVertexParent = new HashSet<>();
            Set<ViwnNode> majorVertexParent = new HashSet<>();
            for (ViwnEdge edge : graph.getInEdges(v)) {
                System.out.println(((ViwnEdgeSynset) edge).getRelation());
                if (((ViwnEdgeSynset) edge).getRelation() != 11) {
                    minorVertexParent.add(graph.getOpposite(v, edge));
                } else {
                    majorVertexParent.add(graph.getOpposite(v, edge));
                }
            }

            // Layout major children below the node
            lastX = startX;
            m_currentPoint.y = startY;
            for (ViwnNode element : majorVertexParent) {
                // sizeXofChild = this.basePositions.get(element);
                startXofChild = lastX;

                m_currentPoint.y -= distY;
                m_currentPoint.x = lastX;
                buildTreeR(element);
                m_currentPoint.y += distY;

                lastX = lastX + distX;
            }

            // Layout minor children on the right
            lastY = startY;
            m_currentPoint.x += distX;
            for (ViwnNode element : minorVertexParent) {
                m_currentPoint.y = lastY;
                m_currentPoint.x = startX - distX;
                buildTreeR(element);

                lastY += distY / 2;
            }
        }
    }

    /**
     * Calculate a width of node subtree.
     *
     * @param v
     * @return
     */
    private int calculateDimensionX(ViwnNode v) {

        // Divide accessors according to relation type (minor and major)
        Set<ViwnNode> minorVertex = new HashSet<>();
        Set<ViwnNode> majorVertex = new HashSet<>();
        for (ViwnEdge edge : graph.getOutEdges(v)) {

            if (((ViwnEdgeSynset) edge).getRelation() != 11) {
                minorVertex.add(graph.getOpposite(v, edge));
            } else {
                majorVertex.add(graph.getOpposite(v, edge));
            }
        }

        int sizeWidth = 0;
        for (ViwnNode element : majorVertex) {
            sizeWidth += calculateDimensionX(element) + distX;
        }
        basePositions.put(v, Math.max(0, sizeWidth - distX));

        return sizeWidth;
    }

    /**
     * This method is not supported by this class. The size of the layout is
     * determined by the topology of the tree, and by the horizontal and
     * vertical spacing (optionally set by the constructor).
     *
     * @param size
     */
    @Override
    public void setSize(Dimension size) {
        throw new UnsupportedOperationException("Size of TreeLayout is set"
                + " by vertex spacing in constructor");
    }

    protected void setCurrentPositionFor(ViwnNode vertex) {
        int x = m_currentPoint.x;
        int y = m_currentPoint.y;

        // Expand a window if a point is outside the window.
        if (x < 0) {
            size.width -= x;
        }
        if (x > size.width - distX) {
            size.width = x + distX;
        }
        if (y < 0) {
            size.height -= y;
        }
        if (y > size.height - distY) {
            size.height = y + distY;
        }

        // Set node location.
        locations.get(vertex).setLocation(m_currentPoint);
    }

    @Override
    public Graph<ViwnNode, ViwnEdge> getGraph() {
        return graph;
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean isLocked(ViwnNode v) {
        return false;
    }

    @Override
    public void lock(ViwnNode v, boolean state) {
    }

    @Override
    public void reset() {
    }

    @Override
    public void setGraph(Graph<ViwnNode, ViwnEdge> graph) {
        if (graph instanceof Forest) {
            this.graph = (Forest<ViwnNode, ViwnEdge>) graph;
        } else {
            throw new IllegalArgumentException("visualisation must be a Forest");
        }
    }

    @Override
    public void setInitializer(Transformer<ViwnNode, Point2D> initializer) {
    }

    /**
     * @return Returns the center of this layout's area.
     */
    public Point2D getCenter() {
        return new Point2D.Double(size.getWidth() / 2, size.getHeight() / 2);
    }

    @Override
    public void setLocation(ViwnNode v, Point2D location) {
        locations.get(v).setLocation(location);
    }

    @Override
    public Point2D transform(ViwnNode v) {
        return locations.get(v);
    }
}
