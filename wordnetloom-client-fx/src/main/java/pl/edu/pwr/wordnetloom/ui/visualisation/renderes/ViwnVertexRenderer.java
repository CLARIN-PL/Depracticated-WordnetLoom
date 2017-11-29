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
package pl.edu.pwr.wordnetloom.ui.visualisation.renderes;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.ui.visualisation.structure.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ViwnVertexRenderer implements Renderer.Vertex<ViwnNode, ViwnEdge> {

    public static HashMap<PartOfSpeech, Color> PosFrameColors = new HashMap<>();

    private Renderer.Vertex<ViwnNode, ViwnEdge> delegate = null;

    public ViwnVertexRenderer(Renderer.Vertex<ViwnNode, ViwnEdge> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void paintVertex(RenderContext<ViwnNode, ViwnEdge> rc,
                            Layout<ViwnNode, ViwnEdge> layout, ViwnNode v) {
        Graph<ViwnNode, ViwnEdge> graph = layout.getGraph();
        if (rc.getVertexIncludePredicate().evaluate(Context.<Graph<ViwnNode, ViwnEdge>, ViwnNode>getInstance(graph, v))) {
            paintVertex(rc, v, layout);
        }
    }

    private void drawVertexArea(ViwnNodeSynset.State state, Shape shape, boolean horiz, GraphicsDecorator g) {
        switch (state) {
            case NOT_EXPANDED:
                g.setColor(Color.blue);
                g.fill(shape);
                break;

            case EXPANDED:
                g.setColor(Color.red);
                g.fill(shape);
                break;

            case SEMI_EXPANDED:
                Rectangle old_clip = g.getClipBounds();
                Rectangle rect = shape.getBounds();

                g.clipRect(rect.x, rect.y, rect.width, rect.height);
                g.setColor(Color.red);
                g.fill(shape);

                if (horiz) {
                    g.clipRect(rect.x + rect.width / 2, rect.y,
                            rect.width, rect.height);
                } else {
                    g.clipRect(rect.x, rect.y + rect.height / 2,
                            rect.width, rect.height);
                }
                g.setColor(Color.blue);
                g.fill(shape);
                g.setClip(old_clip);
                break;
        }
        g.setColor(Color.black);
        g.draw(shape);
    }

    private void drawFrame(GraphicsDecorator g, Shape shape, Color color, Point2D.Float pos) {
        Shape old_clip = g.getClip();
        AffineTransform old = g.getTransform();
        AffineTransform t = g.getTransform();
        t.concatenate(AffineTransform.getTranslateInstance(pos.x, pos.y));
        g.setTransform(t);
        g.setColor(color);
        Stroke old_stroke = g.getStroke();
        g.setStroke(new BasicStroke(10.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        Area a = new Area(g.getClip());
        a.subtract(new Area(shape));
        g.setClip(a);
        g.draw(shape);
        g.setTransform(old);
        g.setStroke(old_stroke);
        g.setClip(old_clip);
    }

    private void renderSynset(
            RenderContext<ViwnNode, ViwnEdge> rc,
            Point2D p,
            ViwnNodeSynset node,
            Layout<ViwnNode, ViwnEdge> layout) {
        Point2D.Float pos = new Point2D.Float((float) p.getX(), (float) p.getY());

        Shape shape = rc.getVertexShapeTransformer().transform(node);
        GraphicsDecorator g = rc.getGraphicsContext();

        if (node instanceof ViwnNodeCand) {
            ViwnNodeCand cand = (ViwnNodeCand) node;
            if (cand.isAdded() || cand.isCenter()) {
                if (cand.isAdded()) {
                    drawFrame(g, shape, Color.GRAY, pos);
                } else if (cand.isCenter()) {
                    drawFrame(g, shape, new Color(50, 132, 255), pos);
                }
            }
        } else if (PosFrameColors.containsKey(node.getPos())) {
            drawFrame(g, shape, PosFrameColors.get(node.getPos()), pos);
        } else if (node.getFrame()) {
            drawFrame(g, shape, new Color(50, 132, 255), pos);
        }

        delegate.paintVertex(rc, layout, node);

        for (NodeDirection rclass : NodeDirection.values()) {
            boolean horiz = true;
            if (rclass == NodeDirection.LEFT
                    || rclass == NodeDirection.RIGHT) {
                horiz = false;
            }

            if (node.getRelation(rclass).size() > 0) {

                boolean hide = false;

                if (node.getState(rclass) == ViwnNodeSynset.State.EXPANDED) {
                    hide = true;
                    Iterator<ViwnEdgeSynset> itr = node.getRelation(rclass).iterator();
                    while (hide && itr.hasNext()) {
                        ViwnEdgeSynset e = itr.next();
                        if (layout.getGraph().containsEdge(e)) {
                            Collection<ViwnNode> inc = layout.getGraph().getIncidentVertices(e);
                            for (ViwnNode s : inc) {
                                ViwnNodeSynset ss = (ViwnNodeSynset) s;
                                if (ss.getSpawner() == node) {
                                    hide = false;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (!hide) {
                    Area area = new Area(node.getButtonArea(rclass));
                    area.transform(AffineTransform.getTranslateInstance(pos.x, pos.y));
                    drawVertexArea(node.getState(rclass), area, horiz, g);
                }
            }
        }

        String text = node.getLabel();

        Font old_font = g.getFont();
        Font font = old_font;

        FontMetrics metrics = g.getFontMetrics(font);
        int width = metrics.stringWidth(text);

        while (width > shape.getBounds().width - 20
                && font.getSize() >= 10) {
            float s = font.getSize();
            font = font.deriveFont(s - 1);
            metrics = g.getFontMetrics(font);
            width = metrics.stringWidth(text);
        }

        if (width > shape.getBounds().width - 20) {
            text = text.split(" \\(.*\\)")[0];
            width = metrics.stringWidth(text);
        }

        if (width > shape.getBounds().width - 20) {
            text = text.split(" [0-9]")[0];
            width = metrics.stringWidth(text);
        }

        boolean shorten = false;
        while (width > shape.getBounds().width - 20) {
            text = text.substring(0, text.length() - 1);
            width = metrics.stringWidth(text + "...");
            shorten = true;
        }

        if (shorten) {
            text += "...";
        }

        g.setFont(font);
        g.setColor(Color.black);
        g.drawString(text, pos.x - width / 2,
                pos.y + 0.25f * metrics.getHeight());

        /*if (LexiconManager.getInstance().getLexiconMarker()) {
            renderLexiconMarker(node, pos, g);
        }*/

        g.setFont(old_font);
    }

    private void renderLexiconMarker(ViwnNodeSynset node, Point2D.Float pos,
                                     GraphicsDecorator g) {

        Shape lexicon = new Area(new RoundRectangle2D.Float(pos.x - 40, pos.y - 17, 23, 10, 12.5f, 5));
        g.setColor(ViwnNodeSynset.PosBgColors.get(node.getPos()));
        g.fillRoundRect(Math.round(pos.x - 40), Math.round(pos.y - 17), 23, 10, 12, 5);
        g.setColor(Color.black);
        g.draw(lexicon);
        Font smallFont = new Font("Tahoma", Font.BOLD, 6);
        g.setFont(smallFont);
        g.drawString(node.getLexiconLabel(), pos.x - 38, pos.y - 10);

    }

    private void renderSet(
            RenderContext<ViwnNode, ViwnEdge> rc,
            Point2D p,
            ViwnNodeSet node) {
        GraphicsDecorator g = rc.getGraphicsContext();
        String text = "" + node.getSynsets().size();

        Font f = new Font("Sansserif", Font.BOLD, 14);
        Font old_font = g.getFont();
        g.setFont(f);

        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int width = metrics.stringWidth(text);
        int height = metrics.getHeight();

        g.setColor(Color.black);
        g.drawString(text,
                (int) p.getX() - width / 2,
                (int) p.getY());

        width = metrics.stringWidth("...");
        g.drawString("...",
                (int) p.getX() - width / 2,
                (int) p.getY() + 0.25f * height);
        g.setFont(old_font);
    }

    private void renderWord(
            RenderContext<ViwnNode, ViwnEdge> rc,
            Point2D p,
            ViwnNodeWord node) {
        GraphicsDecorator g = rc.getGraphicsContext();
        String text = node.getLabel();

        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int width = metrics.stringWidth(text);

        g.setColor(Color.black);
        g.drawString(text,
                (int) p.getX() - width / 2,
                (int) p.getY() + 5);
    }

    /**
     * Paint vertex <code>v</code> on <code>g</code> at <code>(x,y)</code>.
     *
     * @param rc
     * @param v
     * @param layout
     */
    protected void paintVertex(
            RenderContext<ViwnNode, ViwnEdge> rc,
            ViwnNode v,
            Layout<ViwnNode, ViwnEdge> layout) {
        Point2D p = layout.transform(v);
        p = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p);

        if (v instanceof ViwnNodeSet) {
            delegate.paintVertex(rc, layout, v);
            renderSet(rc, p, (ViwnNodeSet) v);
        } else if (v instanceof ViwnNodeSynset) {
            renderSynset(rc, p, (ViwnNodeSynset) v, layout);
        } else if (v instanceof ViwnNodeWord) {
            delegate.paintVertex(rc, layout, v);
            renderWord(rc, p, (ViwnNodeWord) v);
        }
    }
}
