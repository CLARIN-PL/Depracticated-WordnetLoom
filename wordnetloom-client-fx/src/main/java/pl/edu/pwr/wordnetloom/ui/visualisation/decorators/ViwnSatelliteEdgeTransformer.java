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
package pl.edu.pwr.wordnetloom.ui.visualisation.decorators;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import pl.edu.pwr.wordnetloom.ui.visualisation.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.ui.visualisation.structure.ViwnNode;

import java.awt.*;

/**
 * @author amusial
 */
public class ViwnSatelliteEdgeTransformer implements Transformer<ViwnEdge, Stroke>, Predicate<Context<Graph<ViwnNode, ViwnEdge>, ViwnEdge>> {

    /**
     * default line width, should be less than 1, because edges on a preview are
     * not very important
     */
    public static final float DEFAULT_LINE_WIDHT = 0.5f;

    @Override
    public Stroke transform(ViwnEdge arg0) {
        return new BasicStroke(DEFAULT_LINE_WIDHT);
    }

    @Override
    public boolean evaluate(Context<Graph<ViwnNode, ViwnEdge>, ViwnEdge> arg0) {
        /* false, because we do not want any arrows on preview */
        return false;
    }

}
