package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure;

import pl.edu.pwr.wordnetloom.common.model.NodeDirection;

import java.awt.*;
import java.awt.geom.Area;

public class SynsetNodeShape {

    final public Shape buttons[] = new Shape[4];
    final public Shape shape;

    SynsetNodeShape() {
        Polygon p = new Polygon();
        p.addPoint(-50, 10);
        p.addPoint(-10, 15);
        p.addPoint(10, 15);
        p.addPoint(50, 10);
        p.addPoint(50, -10);
        p.addPoint(10, -15);
        p.addPoint(-10, -15);
        p.addPoint(-50, -10);

        shape = p;
        Rectangle rec = shape.getBounds();

        int left = rec.x;
        int top = rec.y;
        int width = rec.width;
        int height = rec.height;

        buttons[0] = new Area(new Polygon(new int[]{left, left, left + 10},
                new int[]{top, top + height + 10, 0}, 3));

        buttons[1] = new Area(new Polygon(new int[]{left + width,
                left + width, left + width - 10}, new int[]{top,
                top + height, 0}, 3));

        buttons[2] = new Area(new Polygon(new int[]{-10, 10, 0}, new int[]{
                top + height, top + height, 5}, 3));

        buttons[3] = new Area(new Polygon(new int[]{-10, 10, 0}, new int[]{
                top, top, -5}, 3));

        for (NodeDirection dir : NodeDirection.values()) {
            if(dir.ordinal() !=  0){ // jeżeli IGNORE nic nie dodajemy
                Area tmp = new Area(buttons[dir.ordinal()-1]);
                tmp.intersect(new Area(shape));
                buttons[dir.ordinal()-1] = tmp;
            }
        }
    }
}
