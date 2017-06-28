package pl.edu.pwr.wordnetloom.relation.model;

import java.io.Serializable;
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
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Entity
@Table(name = "synset_relation")
@NamedQueries({
    @NamedQuery(name = "SynsetRelation.DeleteSynsetRelationBySenseFromORSenseTo", query = "DELETE FROM SynsetRelation r where r.synsetFrom.id =:synset OR r.synsetTo.id =:synset"),
    @NamedQuery(name = "SynsetRelation.AllSynsetBySynsetFromORSynsetTo", query = "Select sr FROM SynsetRelation sr WHERE sr.relation.lexicon.id IN(:lexicons) AND (sr.synsetFrom.id =:synset OR sr.synsetTo.id = :synset)"),
    @NamedQuery(name = "SynsetRelation.SynsetsBySynsetsIDFromORSynsetsIDTo", query = "Select sr FROM SynsetRelation sr WHERE sr.synsetFrom.id IN (:synsets) OR sr.synsetTo.id IN (:synsets)"),

    @NamedQuery(name = "SynsetRelation.AllSynsetBySynsetTo", query = "Select r FROM SynsetRelation r where r.synsetFrom.id =:synset"),
    @NamedQuery(name = "SynsetRelation.dbGetSynsetRelationByID", query = "SELECT sr FROM SynsetRelation sr WHERE sr.id = :id"),

    @NamedQuery(name = "SynsetRelation.dbDelete", query = "DELETE FROM SynsetRelation sr WHERE sr.synsetFrom.id = :synsetFrom AND sr.synsetTo.id = :synsetTo AND sr.relation.id = :relation"),
    @NamedQuery(name = "SynsetRelation.dbDeleteByRelation", query = "DELETE FROM SynsetRelation sr WHERE sr.relation.id = :relation"),
    @NamedQuery(name = "SynsetRelation.dbDeleteAll", query = "DELETE FROM SynsetRelation sr"),

    @NamedQuery(name = "SynsetRelation.dbGetSubRelations", query = "SELECT sr FROM SynsetRelation sr LEFT JOIN FETCH sr.relation "
            + "LEFT JOIN FETCH sr.synsetFrom LEFT JOIN FETCH sr.synsetTo WHERE sr.synsetFrom.id = :synsetFrom AND sr.relation.lexicon.id IN(:lexicons)"),
    @NamedQuery(name = "SynsetRelation.dbGetSubRelationsWithRelation", query = "SELECT sr FROM SynsetRelation sr "
            + "WHERE sr.synsetFrom.id = :synsetFrom AND sr.relation.id = :relation  AND sr.relation.lexicon.id IN(:lexicons)"),
    @NamedQuery(name = "SynsetRelation.dbGetUpperRelations", query = "SELECT sr FROM SynsetRelation sr LEFT JOIN FETCH sr.relation LEFT JOIN FETCH sr.synsetFrom "
            + "LEFT JOIN FETCH sr.synsetTo WHERE sr.synsetTo.id = :synsetTo  AND sr.relation.lexicon.id IN(:lexicons)"),
    @NamedQuery(name = "SynsetRelation.dbGetUpperRelationsWithRelation", query = "SELECT sr FROM SynsetRelation sr "
            + "WHERE sr.synsetTo.id = :synsetTo AND sr.relation.id = :relation  AND sr.relation.lexicon.id IN(:lexicons)"),

    @NamedQuery(name = "SynsetRelation.dbDeleteConnection", query = "DELETE FROM SynsetRelation sr WHERE sr.synsetFrom.id = :synset OR sr.synsetTo.id = :synset"),
    @NamedQuery(name = "SynsetRelation.dbRelationExists", query = "SELECT sr FROM SynsetRelation sr WHERE sr.synsetFrom.id = :synsetFrom AND sr.synsetTo.id = :synsetTo AND sr.relation.id = :relation"),
    @NamedQuery(name = "SynsetRelation.dbGetTopPath", query = "SELECT sr FROM SynsetRelation sr WHERE sr.synsetFrom.id = :id_s AND sr.relation.id = :id_r"),

    @NamedQuery(name = "SynsetRelation.countSynsetRelations", query = "SELECT COUNT(sr) FROM SynsetRelation sr WHERE sr.synsetFrom.id = :synsetID OR sr.synsetTo.id = :synsetID"),})
public class SynsetRelation implements Serializable {

    private static final long serialVersionUID = 1624355230288462084L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "relation_type_id", referencedColumnName = "id", nullable = false)
    private SynsetRelationType relationType;

    @OneToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id", nullable = false)
    private Synset parent;

    @OneToOne
    @JoinColumn(name = "child_id", referencedColumnName = "id", nullable = false)
    private Synset child;

    public SynsetRelation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(SynsetRelationType relationType) {
        this.relationType = relationType;
    }

    public Synset getParent() {
        return parent;
    }

    public void setParent(Synset parent) {
        this.parent = parent;
    }

    public Synset getChild() {
        return child;
    }

    public void setChild(Synset child) {
        this.child = child;
    }

}
