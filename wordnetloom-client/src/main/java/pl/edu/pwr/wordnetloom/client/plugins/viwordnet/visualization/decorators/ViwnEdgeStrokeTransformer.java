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
package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.visualization.decorators;

import edu.uci.ics.jung.visualization.RenderContext;
import java.awt.BasicStroke;
import java.awt.Stroke;
import org.apache.commons.collections15.Transformer;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdge;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure.ViwnEdgeSynset;

public class ViwnEdgeStrokeTransformer
        implements Transformer<ViwnEdge, Stroke> {

    protected final Stroke solid = new BasicStroke(1);

    @Override
    public Stroke transform(ViwnEdge e) {
        if (e instanceof ViwnEdgeSynset) {
            ViwnEdgeSynset es = (ViwnEdgeSynset) e;
            if (es.getRelation().intValue() != 10
                    && es.getRelation().intValue() != 11) {
                return RenderContext.DASHED;
            } else {
                return solid;
            }
        }
        return solid;
    }
}
