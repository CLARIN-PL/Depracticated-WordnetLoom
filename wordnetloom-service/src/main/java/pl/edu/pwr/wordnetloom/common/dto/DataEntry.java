package pl.edu.pwr.wordnetloom.common.dto;

import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataEntry implements Serializable {

    private static final long serialVersionUID = 5708346930324155121L;

    private final int DIRECTIONS_NUMBER = 4;

    private Synset synset;
    private List[] relationsFrom;
    private List[] relationsTo;

    private String label;
    private Long posID;
    private String lexicon;

    public DataEntry() {
        relationsFrom = new List[DIRECTIONS_NUMBER];
        relationsTo = new List[DIRECTIONS_NUMBER];
        for(int i =0; i<DIRECTIONS_NUMBER; i++){
            relationsFrom[i] = new ArrayList();
            relationsTo[i] = new ArrayList();
        }
    }

    public Synset getSynset() {
        return synset;
    }

    public void setSynset(Synset synset) {
        this.synset = synset;
    }

    public List<SynsetRelation> getRelationsFrom(NodeDirection direction) {
        if(direction != NodeDirection.IGNORE){
            return relationsFrom[direction.ordinal()];
        }
        return new ArrayList<>();
    }

    public List<SynsetRelation> getRelationsTo(NodeDirection direction){
        if(direction != NodeDirection.IGNORE){
            return relationsTo[direction.ordinal()];
        }
        return new ArrayList<>();
    }

    public void setRelationsFrom(List<SynsetRelation> relations, NodeDirection direction){
        if(direction != NodeDirection.IGNORE){
            relationsFrom[direction.ordinal()] = relations;
        }
    }

    public void setRelationsTo(List<SynsetRelation> relations, NodeDirection direction){
        if(direction != NodeDirection.IGNORE){
            relationsTo[direction.ordinal()] = relations;
        }
    }

    public void addRelationFrom(SynsetRelation relation, NodeDirection direction){
        if(direction != NodeDirection.IGNORE){
            relationsFrom[direction.ordinal()].add(relation);
        }
    }

    public void addRelationTo(SynsetRelation relation, NodeDirection direction){
        if(direction != NodeDirection.IGNORE){
            relationsTo[direction.ordinal()].add(relation);
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getPosID() {
        return posID;
    }

    public void setPosID(Long posID) {
        this.posID = posID;
    }

    public String getLexicon() {
        return lexicon;
    }

    public void setLexicon(String lexicon) {
        this.lexicon = lexicon;
    }
}
