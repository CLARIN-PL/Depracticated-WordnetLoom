package pl.edu.pwr.wordnetloom.common.dto;

import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DataEntry implements Serializable {

    private static final long serialVersionUID = 5708346930324155121L;

    private final int DIRECTIONS_NUMBER = 4;

    private Synset synset;
    private List[] relations;

    private String name;
    private String variant;
    private Long domain;
    private Long posID;
    private String lexicon;

    public DataEntry() {
        relations = new List[DIRECTIONS_NUMBER];
        for (int i = 0; i < DIRECTIONS_NUMBER; i++) {
            relations[i] = new ArrayList();
        }
    }

    public Synset getSynset() {
        return synset;
    }

    public void setSynset(Synset synset) {
        this.synset = synset;
    }

    public List<SynsetRelation> getRelations(NodeDirection direction) {
        if (direction != NodeDirection.IGNORE) {
            return relations[direction.ordinal()];
        }
        return new ArrayList<>();
    }

    public void setRelations(List<SynsetRelation> relations, NodeDirection direction) {
        if (direction != NodeDirection.IGNORE) {
            this.relations[direction.ordinal()] = relations;
        }
    }

    public void addRelation(SynsetRelation relation, NodeDirection direction) {
        if (direction != NodeDirection.IGNORE) {
            relations[direction.ordinal()].add(relation);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public Long getDomain() {
        return domain;
    }

    public void setDomain(Long domain) {
        this.domain = domain;
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
