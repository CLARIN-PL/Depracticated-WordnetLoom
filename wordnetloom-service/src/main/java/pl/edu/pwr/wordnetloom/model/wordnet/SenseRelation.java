package pl.edu.pwr.wordnetloom.model.wordnet;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the sense_relation database table.
 *
 */
@NamedQueries({
    @NamedQuery(name = "SenseRelation.findAllBySenseFrom", query = "SELECT r FROM SenseRelation r where r.senseFrom.id =:senseFrom"),
    @NamedQuery(name = "SenseRelation.findAllBySenseTo", query = "SELECT r FROM SenseRelation r where r.senseTo.id =:senseTo"),
    @NamedQuery(name = "SenseRelation.findAllBySenseFromORSenseTo", query = "SELECT r FROM SenseRelation r where r.senseFrom.id =:senseFrom OR r.senseTo.id =:senseTo"),
    @NamedQuery(name = "SenseRelation.findSenseRelationByID", query = "SELECT r FROM SenseRelation r where r.id =:id"),

    @NamedQuery(name = "LexicalRelation.dbDelete", query = "DELETE FROM SenseRelation s WHERE s.relation = :relation"),
    @NamedQuery(name = "LexicalRelation.dbDeleteAll", query = "DELETE FROM SenseRelation s"),

    @NamedQuery(name = "LexicalRelation.dbGetSubRelations", query = "SELECT s FROM SenseRelation s WHERE s.senseFrom = :parent"),
    @NamedQuery(name = "LexicalRelation.dbGetSubRelationsWithRelation", query = "SELECT s FROM SenseRelation s WHERE s.senseFrom = :parent AND s.relation = :relation"),

    @NamedQuery(name = "LexicalRelation.dbFastGetRelations", query = "SELECT s FROM SenseRelation s"),
    @NamedQuery(name = "LexicalRelation.dbFastGetRelationsWithRelation", query = "SELECT s FROM SenseRelation s WHERE s.relation = :relation"),

    @NamedQuery(name = "LexicalRelation.dbSelftRelations", query = "SELECT s FROM SenseRelation s WHERE s.senseFrom = s.senseTo"),

    @NamedQuery(name = "LexicalRelation.dbGetUpperRelations", query = "SELECT s FROM SenseRelation s WHERE s.senseTo = :child"),
    @NamedQuery(name = "LexicalRelation.dbGetUpperRelationsWithRelation", query = "SELECT s FROM SenseRelation s WHERE s.senseTo = :child AND s.relation = :relation"),

    @NamedQuery(name = "LexicalRelation.dbGetRelations", query = "SELECT s FROM SenseRelation s WHERE s.senseTo = :child AND s.senseFrom = :parent"),
    @NamedQuery(name = "LexicalRelation.dbGetRelationsWithRelation", query = "SELECT s FROM SenseRelation s WHERE s.senseTo = :child AND s.senseFrom = :parent AND s.relation = :relation"),

    @NamedQuery(name = "LexicalRelation.dbDeleteConnection", query = "DELETE FROM SenseRelation s WHERE s.senseFrom.id = :senseID OR s.senseTo.id = :senseID"),

    @NamedQuery(name = "LexicalRelation.dbGetRelationsCount", query = "SELECT COUNT(s) FROM SenseRelation s"),
    @NamedQuery(name = "LexicalRelation.dbGetRelationUseCount", query = "SELECT COUNT(s) FROM SenseRelation s WHERE s.relation = :relation"),

    @NamedQuery(name = "LexicalRelation.dbGetRelationTypesOfUnit", query = "SELECT r FROM RelationType r, SenseRelation s WHERE s.senseFrom = :sense AND r.id = s.relation.id"),

    @NamedQuery(name = "LexicalRelation.dbGetRelationCountOfUnit", query = "SELECT COUNT(s) FROM SenseRelation s WHERE s.senseFrom = :sense OR s.senseTo = :sense"),

    @NamedQuery(name = "LexicalRelation.dbDeleteImproper", query = "DELETE FROM SenseRelation s WHERE s.senseFrom NOT IN ( :senses ) OR s.senseTo NOT IN ( :senses )"),})
@Entity
@Table(name = "sense_relation")
public class SenseRelation implements Serializable {

    private static final long serialVersionUID = -9013001788054096196L;

    public final static int IS_PARENT = 0;
    public final static int IS_CHILD = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sense_from", insertable = false, updatable = false)
    private Long sense_from;

    @Column(name = "sense_to", insertable = false, updatable = false)
    private Long synset_to;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "relation", referencedColumnName = "id", nullable = false)
    private RelationType relation;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sense_from", referencedColumnName = "id", nullable = false)
    private Sense senseFrom;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sense_to", referencedColumnName = "id", nullable = false)
    private Sense senseTo;

    public SenseRelation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RelationType getRelation() {
        return relation;
    }

    public void setRelation(RelationType relation) {
        this.relation = relation;
    }

    public Sense getSenseFrom() {
        return senseFrom;
    }

    public void setSenseFrom(Sense senseFrom) {
        this.senseFrom = senseFrom;
    }

    public Sense getSenseTo() {
        return senseTo;
    }

    public void setSenseTo(Sense senseTo) {
        this.senseTo = senseTo;
    }

    public Long getSense_from() {
        return sense_from;
    }

    public void setSense_from(Long sense_from) {
        this.sense_from = sense_from;
    }

    public Long getSynset_to() {
        return synset_to;
    }

    public void setSynset_to(Long synset_to) {
        this.synset_to = synset_to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof SenseRelation)) {
            return false;
        }
        SenseRelation e = (SenseRelation) o;

        if (id == null) {
            return false;
        }
        return id.equals(e.getId());
    }

    @Override
    public int hashCode() {
        int hashCode = (id.hashCode());
        if (hashCode == 0) {
            return super.hashCode();
        }
        return hashCode;
    }

}
