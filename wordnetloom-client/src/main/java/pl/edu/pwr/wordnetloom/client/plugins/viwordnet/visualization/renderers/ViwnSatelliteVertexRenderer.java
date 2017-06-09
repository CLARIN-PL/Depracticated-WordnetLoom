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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.renderers;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnNode;

/**
 * @author amusial
 *
 * FIXME: unused class
 *
 */
public class ViwnSatelliteVertexRenderer extends BasicVertexRenderer<ViwnNode, ViwnEdge> {

    public void paintVertex(RenderContext<ViwnNode, ViwnEdge> rc, Layout<ViwnNode, ViwnEdge> layout, ViwnNode v) {
        Graph<ViwnNode, ViwnEdge> graph = layout.getGraph();
        if (rc.getVertexIncludePredicate().evaluate(Context.<Graph<ViwnNode, ViwnEdge>, ViwnNode>getInstance(graph, v))) {
            if (v.isMarked()) {

            } else if (v.getSpawner() == null) {

            } else {
                //super.paintIconForVertex(rc, v, layout);
            }
        }
    }

}
