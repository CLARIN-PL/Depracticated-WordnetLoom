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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.layout;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNodeAlphabeticComparator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * @author amusial
 */
public class ViwnLayout implements Layout<ViwnNode, ViwnEdge> {

    /**
     * total size of layout
     */
    protected Dimension size = new Dimension(100, 100);

    /**
     * visualisation to draw
     */
    protected Graph<ViwnNode, ViwnEdge> graph;

    /**
     * nodes and its locations
     */
    protected Map<ViwnNode, Point2D> locations = LazyMap.decorate(new HashMap<ViwnNode, Point2D>(), (ViwnNode arg0) -> new Point2D.Double());

    protected transient Set<ViwnNode> alreadyDone = new HashSet<>();

    /**
     * default distance from node to node at x axis
     */
    protected final static int DEFAULT_DISTX = 120;

    /**
     * default distance from node to node at y axis
     */
    protected final static int DEFAULT_DISTY = 35;

    protected int distX = DEFAULT_DISTX;
    protected int distY = DEFAULT_DISTY;

    /**
     * Creates an instance for the specified visualisation with default X and Y
     * distances.
     *
     * @param g visualisation to draw
     */
    public ViwnLayout(Graph<ViwnNode, ViwnEdge> g) {
        this(g, DEFAULT_DISTX, DEFAULT_DISTY);
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
            throw new IllegalArgumentException("X and Y distances must each be positive");
        }
        graph = g;
        distX = distx;
        distY = disty;
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

    /**
     * allow to drag 'n' drop all nodes
     *
     * @param v
     * @return
     */
    @Override
    public boolean isLocked(ViwnNode v) {
        return false;
    }

    /**
     * at the moment this feature is not implemented in this visualisation layout
     */
    @Override
    public void lock(ViwnNode v, boolean state) {
    }

    // TODO Check
    @Override
    public void reset() {
        alreadyDone.clear();
        ViwnNode center = findRoot();
        if (center != null) {
            mapNodes2Points(center, null, null);
        }
    }

    /**
     * method tries to find root node of the visualisation
     *
     * @return root node of visualisation or null if cannot find it
     */
    private ViwnNode findRoot() {
        for (ViwnNode n : graph.getVertices()) {
            if (n.getSpawner() == null) {
                return n;
            }
        }
        return null;
    }

    @Override
    public void setGraph(Graph<ViwnNode, ViwnEdge> graph) {
        this.graph = graph;
    }

    @Override
    public void setInitializer(Transformer<ViwnNode, Point2D> initializer) {
        graph.getVertices().stream().map((n) -> {
            setLocation(n, initializer.transform(n));
            return n;
        }).forEach((n) -> {
            alreadyDone.add(n);
        });
    }

    @Override
    public void setLocation(ViwnNode v, Point2D location) {
        locations.get(v).setLocation(location);
    }

    @Override
    public void setSize(Dimension d) {
        size = d;
    }

    /**
     * @param arg0
     * @return location of the node
     */
    @Override
    public Point2D transform(ViwnNode arg0) {
        return locations.get(arg0);
    }

    /**
     * @return center of layout area
     */
    public Point2D getCenter() {
        return new Point2D.Double(size.getWidth() / 2, size.getHeight() / 2);
    }

    /**
     * set visualisation nodes locations
     *
     * @param center main, central node
     */
    public void mapNodes2Points(ViwnNode center) {
        mapNodes2Points(center, null, null);
    }

    /*
     * to allow user permanently change node placing: 1. when node(s) is taken
	 * from set of nodes - refresh layout 2. when relation was added - refresh
	 * layout after expanding a node - place only expanded and its children
	 * after reducing a node?
     */

    private void insertNodeToList(List<ViwnNode> list, ViwnNode element){
        if(!list.contains(element)){
            list.add(element);
        }
    }

    /**
     * set visualisation nodes locations
     * <p>
     * TODO: bug fix #1 cycles in visualisation causes stack overflow, wont fix
     *
     * @param center main, central node
     * @param loc    location in which central node should stay, null for root node
     * @param placed nodes mapped in previous method calls
     * @return mapped in current call
     */
    public Set<ViwnNode> mapNodes2Points(ViwnNode center, Point2D loc, Set<ViwnNode> placed) {

        boolean root = false;

        if (loc == null) {
            loc = new Point2D.Double(size.getHeight() / 2, size.getWidth() / 2);
            root = true;
        }
        if (placed == null) {
            placed = new HashSet<>();
        }

        // nodes mapped in actual method call
        Set<ViwnNode> actual = new HashSet<>();

        // map all nodes
        if (!placed.contains(center)) {

            // place it in the center
            setLocation(center, loc);
            // mark as mapped
            actual.add(center);

            // now, time to place its neighbors
            // Divide neighbors according to relation type, and future place
            // in visualisation
            List<ViwnNode> bottom = new ArrayList<>();
            List<ViwnNode> top = new ArrayList<>();
            List<ViwnNode> right = new ArrayList<>();
            List<ViwnNode> left = new ArrayList<>();

            Collection<ViwnEdge> edges = graph.getIncidentEdges(center);

            if(edges!=null){
                for (ViwnEdge edge : edges) {
                    ViwnNode opposite = graph.getOpposite(center, edge);

                    if (center.equals(opposite.getSpawner())
                            && (opposite.getSpawnDir() != null)) {
                        switch (opposite.getSpawnDir()) {
                            case BOTTOM:
//                                bottom.add(opposite);
                                insertNodeToList(bottom, opposite);
                                break;
                            case TOP:
//                                top.add(opposite);
                                insertNodeToList(top, opposite);
                                break;
                            case RIGHT:
//                                right.add(opposite);
                                insertNodeToList(right, opposite);
                                break;
                            case LEFT:
//                                left.add(opposite);
                                insertNodeToList(left, opposite);
                                break;
                        }
                    }
                }
            }

            // sort children alphabetically
            bottom.sort(new ViwnNodeAlphabeticComparator());
            top.sort(new ViwnNodeAlphabeticComparator());
            right.sort(new ViwnNodeAlphabeticComparator());
            left.sort(new ViwnNodeAlphabeticComparator());

            // place a node here ;-)
            Point p = new Point();
            double ile;
            int i = 0;

            // place lower
            placeLower(loc, actual, bottom, p, i);
            placeUpper(loc, actual, top, p);
            double updownmax = placeRight(loc, actual, bottom, top, right, p);
            placeLeft(loc, actual, left, p, updownmax);

            // =========================== lower level
            for (ViwnNode vn : bottom) {
                    Set<ViwnNode> children = mapNodes2Points(vn, locations.get(vn), placed);

                    correctSubGraphMapping(center, vn, actual, children);
                    actual.addAll(children);
            }
            for (ViwnNode vn : top) {
                    Set<ViwnNode> children = mapNodes2Points(vn, locations.get(vn), placed);
                    correctSubGraphMapping(center, vn, actual, children);

                    actual.addAll(children);
            }
            for (ViwnNode vn : right) {
                    Set<ViwnNode> children = mapNodes2Points(vn, locations.get(vn), placed);

                    correctSubGraphMapping(center, vn, actual, children);
                    actual.addAll(children);
            }
            for (ViwnNode vn : left) {
                    Set<ViwnNode> children = mapNodes2Points(vn, locations.get(vn), placed);

                    correctSubGraphMapping(center, vn, actual, children);
                    actual.addAll(children);
            }

        }
        if (root) {
            alreadyDone.clear();
            alreadyDone.addAll(graph.getVertices());

            // correct vertices locations and set size
            correctGraph(alreadyDone, center);
        }

        return actual;
    }

    private void placeLeft(Point2D loc, Set<ViwnNode> actual, List<ViwnNode> left, Point p, double updownmax) {
        double ile;
        int i;
        if (updownmax < left.size()) {
            p.x = (int) loc.getX() - distX
                    - (int) ((updownmax - 1) * distX) / 2;
        } else {
            p.x = (int) loc.getX() - distX - ((left.size() - 1) * distX) / 2;
        }
        p.y = (int) loc.getY() - ((left.size() - 1) * distY) / 2 + distY / 3;
        ile = left.size();
        i = 0;

        for (ViwnNode vn : left) {
            // place it
            setLocation(vn, new Point2D.Double(p.x - Math.sqrt(ile) * distY
                    * Math.cos(Math.PI * ((ile / 2D - i - 0.5D) / ile)),
                    p.y));
            // mark as mapped
            actual.add(vn);

            p.y += distY;
            ++i;
        }
    }

    private double placeRight(Point2D loc, Set<ViwnNode> actual, List<ViwnNode> bottom, List<ViwnNode> top, List<ViwnNode> right, Point p) {
        double ile;
        int i;
        double updownmax = Math.max((double) Math.max(bottom.size(), top.size()), 1);
        if (updownmax < right.size()) {
            p.x = (int) loc.getX() + distX
                    + (int) ((updownmax - 1) * distX) / 2;
        } else {
            p.x = (int) loc.getX() + distX + ((right.size() - 1) * distX) / 2;
        }
        p.y = (int) loc.getY() - ((right.size() - 1) * distY) / 2 + distY / 3;
        ile = right.size();
        i = 0;
        for (ViwnNode vn : right) {
            // place it
            setLocation(
                    vn,
                    new Point2D.Double((p.x + Math.sqrt(ile)
                            * distY
                            * Math.cos(Math.PI
                            * ((ile / 2D - i - 0.5D) / ile))), p.y));
            // mark as mapped
            actual.add(vn);

            p.y += distY;
            ++i;
        }
        return updownmax;
    }

    private void placeUpper(Point2D loc, Set<ViwnNode> actual, List<ViwnNode> top, Point p) {
        double ile;
        int i;
//        p.x = (int) loc.getX() - ((top.size() - 1) * distX) / 2 + distX / 11;

        p.x = (int)loc.getX() - (top.size() - 1) * distX /2;
        p.y = (int) loc.getY() - (distY * top.size()) / 2;
        ile = top.size();
        i = 0;
        for (ViwnNode vn : top) {
            setLocation(
                    vn,
                    new Point2D.Double(p.x,
                            p.y - Math.sqrt(ile) * distY * Math.cos(Math.PI * ((ile / 2D - i - 0.5D) / ile))));
            // mark as mapped
            actual.add(vn);

            p.x += distX;
            ++i;
        }
    }

    private void placeLower(Point2D loc, Set<ViwnNode> actual, List<ViwnNode> bottom, Point p, int i) {
        double ile;
//        p.x = (int) loc.getX() - ((bottom.size() - 1) * distX) / 2 + distX / 11;
        p.x = (int) loc.getX() - (bottom.size() - 1) * distX /2;
        p.y = (int) loc.getY() + (distY * bottom.size()) / 2;
        ile = bottom.size();
        for (ViwnNode vn : bottom) {
            setLocation(vn, new Point2D.Double(p.x,
                    p.y + Math.sqrt(ile) * distY * Math.cos(Math.PI * ((ile / 2D - i - 0.5D) / ile))));
            // mark as mapped
            actual.add(vn);

            p.x += distX;
            ++i;
        }
    }

    /**
     * @param col collection of nodes which location should be corrected
     * @param dx  x axis correction
     * @param dy  y axis correction
     */
    protected void correctNode2PointMapping(Collection<ViwnNode> col, int dx,
                                            int dy) {
        col.stream().forEach((vn) -> {
            Point2D current = locations.get(vn);
            setLocation(vn, new Point2D.Double(current.getX() + dx, current.getY() + dy));
        });
    }

    /**
     * @param parent parent of central node of collection to correct
     * @param center central node of set to correct
     * @param upper  parent node neighbors
     * @param lower  central node neighbors, nodes to correct
     */
    protected void correctSubGraphMapping(ViwnNode parent, ViwnNode center,
                                          Collection<ViwnNode> upper, Collection<ViwnNode> lower) {
        // calculate direction vector from parent to center
        Point2D par = locations.get(parent);
        Point2D cen = locations.get(center);
        Point2D dir = new Point2D.Double((cen.getX() - par.getX()),
                (cen.getY() - par.getY()));

        double dist = dir.distance(0, 0);
        dir.setLocation(dir.getX() / dist, dir.getY() / dist);

        boolean end = false;
        int[] dim;

        Set<ViwnNode> ad = new HashSet<>(upper);
        ad.removeAll(lower);

        // calculate boundaries of subgraph
        dim = findMappedGraphBoundaries(center, lower);
        // check for conflicts
        end = !isAnyInsideBounds(ad, dim);

        while (!end) {
            // move subgraph
            correctNode2PointMapping(lower, (int) (100 * dir.getX()),
                    (int) (100 * dir.getY()));
            // calculate boundaries of subgraph
            dim = findMappedGraphBoundaries(center, lower);
            // check for conflicts
            end = !isAnyInsideBounds(ad, dim);
        }

    }

    /**
     * @param center center of subgraph to get
     * @return subgraph from node center
     */
    private Set<ViwnNode> getSubGraphOf(ViwnNode center) {
        Set<ViwnNode> ret = new HashSet<>();
        ret.add(center);

        for (ViwnEdge edge : graph.getIncidentEdges(center)) {
            ViwnNode opposite = graph.getOpposite(center, edge);
            if (center.equals(opposite.getSpawner())
                    && (opposite.getSpawnDir() != null)) {
                ret.addAll(getSubGraphOf(opposite));
            }
        }

        return ret;
    }

    /**
     * @param col    collection of vertices to check
     * @param bounds bounds in which vertices should not be
     * @return true if at least one node from col is inside bounds
     */
    protected boolean isAnyInsideBounds(Collection<ViwnNode> col, int[] bounds) {
        for (ViwnNode vn : col) {
            if (bounds[0] < locations.get(vn).getX()
                    && bounds[1] > locations.get(vn).getX()
                    && bounds[2] < locations.get(vn).getY()
                    && bounds[3] > locations.get(vn).getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param col collection of visualisation nodes
     * @return visualisation dimension
     */
    protected Dimension calcSetSize(Collection<ViwnNode> col) {
        if (col.size() > 0) {
            int minx, maxx, miny, maxy;
            ViwnNode n = col.iterator().next();
            minx = maxx = (int) locations.get(n).getX();
            miny = maxy = (int) locations.get(n).getY();

            for (ViwnNode vn : col) {
                Point2D p = locations.get(vn);
                if (p.getX() > maxx) {
                    maxx = (int) p.getX();
                } else if (p.getX() < minx) {
                    minx = (int) p.getX();
                }
                if (p.getY() > maxy) {
                    maxy = (int) p.getY();
                } else if (p.getY() < miny) {
                    miny = (int) p.getY();
                }
            }

            return (new Dimension((maxx - minx), (maxy - miny)));
        } else {
            return new Dimension();
        }
    }

    /**
     * @param central central node
     * @param col     collection of already mapped vertices
     * @return table of 4 integers minx, maxx, miny, maxy
     */
    protected int[] findMappedGraphBoundaries(ViwnNode central,
                                              Collection<ViwnNode> col) {

        // boundary coordinates
        int minx, maxx, miny, maxy;

        // random node to initialize boundary coordinates
        minx = maxx = (int) locations.get(central).getX();
        miny = maxy = (int) locations.get(central).getY();

        // for all nodes already placed, check their locations
        for (ViwnNode vn : col) {
            Point2D p = locations.get(vn);
            if (p.getX() > maxx) {
                maxx = (int) p.getX();
            } else if (p.getX() < minx) {
                minx = (int) p.getX();
            }
            if (p.getY() > maxy) {
                maxy = (int) p.getY();
            } else if (p.getY() < miny) {
                miny = (int) p.getY();
            }
        }

        return new int[]{minx - distX, maxx + distX, miny - distY,
                maxy + distY};
    }

    /*
     * place all vertices in positive coordinates set size value to contain full
	 * visualisation in it
	 *
	 * @param toCorrect collection of nodes which position should be corrected
	 *
	 * @param anode node from visualisation
     */
    protected void correctGraph(Collection<ViwnNode> toCorrect, ViwnNode anode) {

        // boundary coordinates
        int minx, maxx, miny, maxy;

        // initialize boundary coordinates
        minx = maxx = (int) locations.get(anode).getX();
        miny = maxy = (int) locations.get(anode).getY();

        // for all nodes already place, check their locations
        for (ViwnNode vn : toCorrect) {
            Point2D p = locations.get(vn);
            if (p.getX() > maxx) {
                maxx = (int) p.getX();
            } else if (p.getX() < minx) {
                minx = (int) p.getX();
            }
            if (p.getY() > maxy) {
                maxy = (int) p.getY();
            } else if (p.getY() < miny) {
                miny = (int) p.getY();
            }
        }

        // replace nodes
        correctNode2PointMapping(toCorrect, -minx + distX, -miny + distY);

        // set visualisation size
        size = new Dimension((maxx - minx) + 2 * distX, (maxy - miny) + 2 * distY);

    }

    /**
     * place all vertices in positive coordinates set size value to contain full
     * visualisation in it
     *
     * @param toCorrect collection of nodes which position should be corrected
     */
    protected void correctGraph(Collection<ViwnNode> toCorrect) {

        // boundary coordinates
        int minx, maxx, miny, maxy;

        // initialize boundary coordinates
        minx = maxx = size.width / 2;
        miny = maxy = size.height / 2;

        // for all nodes already place, check their locations
        for (ViwnNode vn : toCorrect) {
            Point2D p = locations.get(vn);
            if (p.getX() > maxx) {
                maxx = (int) p.getX();
            } else if (p.getX() < minx) {
                minx = (int) p.getX();
            }
            if (p.getY() > maxy) {
                maxy = (int) p.getY();
            } else if (p.getY() < miny) {
                miny = (int) p.getY();
            }
        }

        // replace nodes
        correctNode2PointMapping(toCorrect, -minx + distX, -miny + distY);

        // set visualisation size
        size = new Dimension((maxx - minx) + 2 * distX, (maxy - miny) + 2 * distY);

    }

}
