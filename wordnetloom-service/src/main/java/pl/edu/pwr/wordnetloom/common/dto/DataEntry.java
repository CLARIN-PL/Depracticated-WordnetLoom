package pl.edu.pwr.wordnetloom.common.dto;

import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class DataEntry implements Serializable {

    private static final long serialVersionUID = 5708346930324155121L;

    private final int DIRECTIONS_NUMBER = 4;

    private Synset synset;
    private Set[] relationsFrom;
    private Set[] relationsTo;

    private String label;
    private Long posID;
    private String lexicon;

    public DataEntry() {
        relationsFrom = new Set[DIRECTIONS_NUMBER];
        relationsTo = new Set[DIRECTIONS_NUMBER];
        for(int i = 0; i<DIRECTIONS_NUMBER; i++){
            relationsFrom[i] = new HashSet();
            relationsTo[i] = new HashSet();
        }
    }

    public Synset getSynset() {
        return synset;
    }

    public void setSynset(Synset synset) {
        this.synset = synset;
    }

    public Set<SynsetRelation> getRelationsFrom(NodeDirection direction) {
        if(direction != NodeDirection.IGNORE){
            return relationsFrom[direction.ordinal()];
        }
        return new HashSet<>();
    }

    public Set<SynsetRelation> getRelationsTo(NodeDirection direction){
        if(direction != NodeDirection.IGNORE){
            return relationsTo[direction.ordinal()];
        }
        return new HashSet<>();
    }

    public void setRelationsFrom(Set<SynsetRelation> relations, NodeDirection direction){
        if(direction != NodeDirection.IGNORE){
            relationsFrom[direction.ordinal()] = relations;
        }
    }

    public void setRelationsTo(Set<SynsetRelation> relations, NodeDirection direction){
        if(direction != NodeDirection.IGNORE){
            relationsTo[direction.ordinal()] = relations;
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
