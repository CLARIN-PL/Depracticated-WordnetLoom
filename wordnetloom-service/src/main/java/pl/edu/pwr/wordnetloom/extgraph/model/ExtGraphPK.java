package pl.edu.pwr.wordnetloom.extgraph.model;

import java.io.Serializable;

public class ExtGraphPK implements Serializable {

    private static final long serialVersionUID = -6586779589981439676L;

    private Long extGraph;
    private Long relationType;
    private Long lexicalUnit;

    public ExtGraphPK() {
    }

    public Long getExtGraph() {
        return extGraph;
    }

    public void setExtGraph(Long extGraph) {
        this.extGraph = extGraph;
    }

    public Long getRelationType() {
        return relationType;
    }

    public void setRelationType(Long relationType) {
        this.relationType = relationType;
    }

    public Long getLexicalUnit() {
        return lexicalUnit;
    }

    public void setLexicalUnit(Long lexicalUnit) {
        this.lexicalUnit = lexicalUnit;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((extGraph == null) ? 0 : extGraph.hashCode());
        result = prime * result
                + ((lexicalUnit == null) ? 0 : lexicalUnit.hashCode());
        result = prime * result
                + ((relationType == null) ? 0 : relationType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExtGraphPK other = (ExtGraphPK) obj;
        if (extGraph == null) {
            if (other.extGraph != null) {
                return false;
            }
        } else if (!extGraph.equals(other.extGraph)) {
            return false;
        }
        if (lexicalUnit == null) {
            if (other.lexicalUnit != null) {
                return false;
            }
        } else if (!lexicalUnit.equals(other.lexicalUnit)) {
            return false;
        }
        if (relationType == null) {
            if (other.relationType != null) {
                return false;
            }
        } else if (!relationType.equals(other.relationType)) {
            return false;
        }
        return true;
    }
}
