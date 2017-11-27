package pl.edu.pwr.wordnetloom.visualisation.renderes;

import edu.uci.ics.jung.visualization.picking.PickedInfo;
import org.apache.commons.collections15.Transformer;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.visualisation.structure.*;

import java.awt.*;

/**
 * Class responsible for setting color of a vertex.
 *
 * @author Michał Marcińczuk <michal.marcinczuk@pwr.wroc.pl>
 */
public final class ViwnVertexFillColor implements Transformer<ViwnNode, Paint> {

    protected PickedInfo<ViwnNode> pi;
    protected ViwnNode root;

    /**
     * @param pi
     * @param root_node
     */
    public ViwnVertexFillColor(PickedInfo<ViwnNode> pi, ViwnNode root_node) {
        this.pi = pi;
        root = root_node;
    }

    @Override
    public Paint transform(ViwnNode v) {
        if (pi.isPicked(v)) {
            return ViwnNodeSynset.vertexBackgroundColorSelected;
        } else if (v == root) {
            return ViwnNodeSynset.vertexBackgroundColorRoot;
        } else if (v.isMarked()) {
            return ViwnNodeSynset.vertexBackgroundColorMarked;
        }

        if (v instanceof ViwnNodeCandExtension) {
            ViwnNodeCandExtension ext = (ViwnNodeCandExtension) v;
            return ext.getColor();
        } else if (v instanceof ViwnNodeCand) {
            ViwnNodeCand cand = (ViwnNodeCand) v;
            return cand.getColor();
        } else if (v instanceof ViwnNodeWord) {
            return ((ViwnNodeWord) v).getColor();
        } else if (v instanceof ViwnNodeSynset) {
            ViwnNodeSynset synset = (ViwnNodeSynset) v;
            PartOfSpeech pos = synset.getPos();
            if (pos == null || "! S.y.n.s.e.t p.u.s.t.y !".equals(synset.getLabel())) {
                return Color.RED;
            }

            if (ViwnNodeSynset.PosBgColors.containsKey(pos)) {
                return ViwnNodeSynset.PosBgColors.get(pos);
            } else {
                return Color.BLACK;
            }

        } else if (v instanceof ViwnNodeSet) {
            return ViwnNodeSet.vertexBackgroundColor;
        }

        return new Color(255, 0, 255);
    }
}
