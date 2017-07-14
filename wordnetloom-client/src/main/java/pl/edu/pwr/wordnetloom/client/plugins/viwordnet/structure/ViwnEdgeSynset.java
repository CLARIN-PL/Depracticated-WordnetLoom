package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure;

import java.awt.Color;
import java.util.HashMap;
import pl.edu.pwr.wordnetloom.client.systems.enums.RelationTypes;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

/**
 * Edge between synsets.
 *
 * @author boombel
 */
public class ViwnEdgeSynset extends ViwnEdge {

    public static HashMap<Long, Color> relsColors = new HashMap<>();

    private final SynsetRelation srel_dto_;
    private ViwnNodeSynset s1_ = null;
    private ViwnNodeSynset s2_ = null;

    ViwnEdgeSynset(SynsetRelation srel_dto) {
        srel_dto_ = srel_dto;
    }

    /**
     * checks if this edge is equal to the reversed edge
     *
     * @param edge second edge
     * @return return true if this edge is equal to the reversed edge
     */
    public boolean equalsReverse(ViwnEdgeSynset edge) {
        Long rev_id = edge.getRelationType().rev_id();
        return getRelation().equals(rev_id)
                && getChild().equals(edge.getParent())
                && getParent().equals(edge.getChild());
    }

    /**
     * @return relation id
     */
    public Long getRelation() {
        return srel_dto_.getRelationType().getId();
    }

    public void setSynset1(ViwnNodeSynset s1) {
        s1_ = s1;
    }

    public void setSynset2(ViwnNodeSynset s2) {
        s2_ = s2;
    }

    /**
     * @return first synset in relation
     */
    public ViwnNodeSynset getSynset1() {
        return s1_;
    }

    /**
     * @return second synset in relation
     */
    public ViwnNodeSynset getSynset2() {
        return s2_;
    }

    /**
     * @return child node id
     */
    public Long getChild() {
        return srel_dto_.getChild().getId();
    }

    /**
     * @return parent node id
     */
    public Long getParent() {
        return srel_dto_.getParent().getId();
    }

    public Synset getSynsetFrom() {
        return srel_dto_.getParent();
    }

    public Synset getSynsetTo() {
        return srel_dto_.getChild();
    }

    public RelationTypes getRelationType() {
        RelationTypes rt = RelationTypes.get(srel_dto_.getRelationType().getId());
        if (rt == null) {
            throw new RuntimeException("relation type doesn't exist");
        }
        return rt;
    }

    @Override
    public String toString() {
        try {
            return "";//getRelationType().Sname();
        } catch (Exception e) {
            return "";//getRelationType().name();
        }
    }

    @Override
    public Color getColor() {
        Color col = relsColors.get(srel_dto_.getRelationType().getId());
        if (col == null) {
            return Color.black;
        } else {
            return col;
        }
    }

    public ViwnEdgeSynset createDummyReverse() {
        if (RelationTypes.get(srel_dto_.getRelationType().getId()).rev_id() == null) {
            return null;
        }

        SynsetRelation rdto = new SynsetRelation();
        rdto.setChild(srel_dto_.getParent());
        rdto.setParent(srel_dto_.getChild());

//        SynsetRelationType currentRelation = RelationTypes.get(srel_dto_.getRelationType().getId()).getRelationType();
//        if (LexiconManager.getInstance().getLexicons().contains(currentRelation.getReverse().getLexicon().getId())) {
//            SynsetRelationType reverseRelation = RelationTypes.get(currentRelation.getReverse().getId()).getRelationType();
//            rdto.setRelationType(reverseRelation);
//        }
        ViwnEdgeSynset e = new ViwnEdgeSynset(rdto);
        return e;
    }

    /**
     * @return <code>SynsetRelation</code>
     */
    public SynsetRelation getSynsetRelation() {
        return srel_dto_;
    }

    @Override
    public int hashCode() {
        if (srel_dto_ == null) {
            throw new IllegalStateException();
        }

        return srel_dto_.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ViwnEdgeSynset)) {
            return false;
        }
        if (srel_dto_ == null) {
            throw new IllegalStateException();
        }
        return srel_dto_.equals(((ViwnEdgeSynset) o).srel_dto_);
    }
}
