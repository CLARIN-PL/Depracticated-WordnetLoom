package pl.edu.pwr.wordnetloom.client.plugins.viwordnet.structure;

import java.awt.Color;
import java.util.HashMap;

import pl.edu.pwr.wordnetloom.client.systems.managers.LocalisationManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.RelationTypeManager;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

/**
 * Edge between synset.
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
        if(edge.getRelationType().getReverse()==null){ // nie ma relacji odwrotnej
            return false;
        } else {
            Long revId = edge.getRelationType().getReverse().getId();
            return getRelation().equals(revId)
                    && getChild().equals(edge.getParent())
                    && getParent().equals(edge.getChild());
        }
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

    public RelationType getRelationType() {
        return RelationTypeManager.getInstance().get(srel_dto_.getRelationType().getId(), RelationArgument.SYNSET_RELATION);
    }

    @Override
    public String toString() {
        return LocalisationManager.getInstance().getLocalisedString(getRelationType().getShortDisplayText());
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
        if (RelationTypeManager.getInstance().get(srel_dto_.getRelationType().getId(), RelationArgument.SYNSET_RELATION).getReverse() == null) {
            return null;
        }

        SynsetRelation rdto = new SynsetRelation();
        rdto.setChild(srel_dto_.getParent());
        rdto.setParent(srel_dto_.getChild());

//        SynsetRelationType currentRelation = RelationTypeManager.get(srel_dto_.getRelationType().getId()).getRelationType();
//        if (LexiconManager.getInstance().getLexicons().contains(currentRelation.getReverse().getLexicon().getId())) {
//            SynsetRelationType reverseRelation = RelationTypeManager.get(currentRelation.getReverse().getId()).getRelationType();
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
