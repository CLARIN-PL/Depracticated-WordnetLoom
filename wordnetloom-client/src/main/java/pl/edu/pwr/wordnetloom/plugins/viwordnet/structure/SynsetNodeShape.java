package pl.edu.pwr.wordnetloom.plugins.viwordnet.structure;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

import pl.edu.pwr.wordnetloom.plugins.viwordnet.structure.ViwnNode.Direction;

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

		buttons[0] = new Area(new Polygon(new int[] { left, left, left + 10 },
				new int[] { top, top + height + 10, 0 }, 3));

		buttons[1] = new Area(new Polygon(new int[] { left + width,
				left + width, left + width - 10 }, new int[] { top,
				top + height, 0 }, 3));

		buttons[2] = new Area(new Polygon(new int[] { -10, 10, 0 }, new int[] {
				top + height, top + height, 5 }, 3));

		buttons[3] = new Area(new Polygon(new int[] { -10, 10, 0 }, new int[] {
				top, top, -5 }, 3));

		for (Direction dir : Direction.values()) {
			Area tmp = new Area(buttons[dir.ordinal()]);
			tmp.intersect(new Area(shape));
			buttons[dir.ordinal()] = tmp;
		}
	}
}