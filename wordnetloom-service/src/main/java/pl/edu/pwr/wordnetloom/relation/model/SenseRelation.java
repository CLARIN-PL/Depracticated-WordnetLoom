package pl.edu.pwr.wordnetloom.relation.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "relation_type_id", referencedColumnName = "id", nullable = false)
    private SenseRelationType relationType;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = false)
    private Sense parent;

    @ManyToOne
    @JoinColumn(name = "child_id", referencedColumnName = "id", nullable = false)
    private Sense child;

    public SenseRelation() {
        super();
    }

    public SenseRelation(SenseRelationType relationType, Sense parent, Sense child) {
        this.relationType = relationType;
        this.parent = parent;
        this.child = child;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SenseRelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(SenseRelationType relationType) {
        this.relationType = relationType;
    }

    public Sense getParent() {
        return parent;
    }

    public void setParent(Sense parent) {
        this.parent = parent;
    }

    public Sense getChild() {
        return child;
    }

    public void setChild(Sense child) {
        this.child = child;
    }

}
