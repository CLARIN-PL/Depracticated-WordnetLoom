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

import edu.uci.ics.jung.visualization.decorators.SettableVertexShapeTransformer;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;
import org.apache.commons.collections15.Transformer;
import pl.edu.pwr.wordnetloom.ui.visualisation.structure.ViwnNode;

import java.awt.*;

/**
 * @author amusial
 */
public class ViwnSatelliteNodeSizeTransformer implements
        SettableVertexShapeTransformer<ViwnNode>, Transformer<ViwnNode, Shape> {

    protected Transformer<ViwnNode, Integer> vsf;
    protected Transformer<ViwnNode, Float> varf;
    protected VertexShapeFactory<ViwnNode> factory;

    public final static int DEFAULT_ROOT_SIZE = 8;
    public final static int DEFAULT_MARKED_SIZE = 9;
    public final static int DEFAULT_SIZE = 4;
    public final static float DEFAULT_ASPECT_RATIO = 1.0f;

    /**
     * @param vsf
     * @param varf
     */
    public ViwnSatelliteNodeSizeTransformer(Transformer<ViwnNode, Integer> vsf,
                                            Transformer<ViwnNode, Float> varf) {
        this.vsf = vsf;
        this.varf = varf;
        factory = new VertexShapeFactory<>(vsf, varf);
    }

    public ViwnSatelliteNodeSizeTransformer() {
        vsf = (ViwnNode arg0) -> (arg0.getSpawner() == null
                ? DEFAULT_ROOT_SIZE
                : arg0.isMarked()
                ? DEFAULT_MARKED_SIZE
                : DEFAULT_SIZE);
        varf = (ViwnNode arg0) -> DEFAULT_ASPECT_RATIO;
        factory = new VertexShapeFactory<>(vsf, varf);
    }

    @Override
    public void setSizeTransformer(Transformer<ViwnNode, Integer> vsf) {
        this.vsf = vsf;
        factory = new VertexShapeFactory<>(vsf, varf);
    }

    @Override
    public void setAspectRatioTransformer(Transformer<ViwnNode, Float> varf) {
        this.varf = varf;
        factory = new VertexShapeFactory<>(vsf, varf);
    }

    @Override
    public Shape transform(ViwnNode arg0) {
        return (arg0.getSpawner() == null
                ? factory.getEllipse(arg0)
                : (arg0.isMarked()
                ? factory.getRegularStar(arg0, 5)
                : factory.getRectangle(arg0)));
    }
}
