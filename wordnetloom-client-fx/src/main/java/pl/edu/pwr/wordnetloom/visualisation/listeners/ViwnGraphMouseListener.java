package pl.edu.pwr.wordnetloom.visualisation.listeners;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import pl.edu.pwr.wordnetloom.visualisation.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.visualisation.structure.ViwnNode;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * class ViwnGraphMouseListener implements GraphMouseListener and handles mouse
 * actions in visualisation panel
 */
public class ViwnGraphMouseListener implements GraphMouseListener<ViwnNode> {

    private final ViwnGraphViewUI owner;

    /**
     * @param owner ViwnGraphViewUI owner of this visualisation listener
     */
    public ViwnGraphMouseListener(ViwnGraphViewUI owner) {
        this.owner = owner;
    }

    @Override
    public void graphClicked(ViwnNode v, MouseEvent me) {
    }

    @Override
    public void graphPressed(ViwnNode v, MouseEvent me) {
    }

    @Override
    public void graphReleased(ViwnNode v, MouseEvent me) {
        /* the 'everything wrecking jumping graphs' ultimate problem solver */

        // remember node location
        Point2D p1 = (Point2D) owner.getLayout().transform(v).clone();

        // propagate event to nodes
        v.mouseClick(me, owner);

        // find new node location
        Point2D p2 = (Point2D) owner.getLayout().transform(v).clone();

        double xdif = p1.getX() - p2.getX();
        double ydif = p1.getY() - p2.getY();

        owner.getVisualizationViewer().getRenderContext().
                getMultiLayerTransformer().getTransformer(Layer.LAYOUT)
                .translate(xdif, ydif);
        if (me.getButton() == MouseEvent.BUTTON1) {
            //owner.vertexSelectionChange(v);
        }
    }

}
