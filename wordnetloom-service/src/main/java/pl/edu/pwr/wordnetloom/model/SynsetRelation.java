package pl.edu.pwr.wordnetloom.model;

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

/**
 * The persistent class for the synset_relation database table.
 * 
 */
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

		@NamedQuery(name = "SynsetRelation.countSynsetRelations", query = "SELECT COUNT(sr) FROM SynsetRelation sr WHERE sr.synsetFrom.id = :synsetID OR sr.synsetTo.id = :synsetID"), })
public class SynsetRelation implements Serializable {

	private static final long serialVersionUID = 1624355230288462084L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "relation", referencedColumnName = "id", nullable = false)
	private RelationType relation;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "synset_from", referencedColumnName = "id", nullable = false)
	private Synset synsetFrom;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "synset_to", referencedColumnName = "id", nullable = false)
	private Synset synsetTo;

	public SynsetRelation() {
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

	public Synset getSynsetFrom() {
		return synsetFrom;
	}

	public void setSynsetFrom(Synset synsetFrom) {
		this.synsetFrom = synsetFrom;
	}

	public Synset getSynsetTo() {
		return synsetTo;
	}

	public void setSynsetTo(Synset synsetTo) {
		this.synsetTo = synsetTo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof SynsetRelation))
			return false;
		SynsetRelation e = (SynsetRelation) o;

		if (id == null)
			return false;
		return id.equals(e.getId());
	}

	@Override
	public int hashCode() {
		if (id == null)
			return super.hashCode();

		int hashCode = (id.hashCode());
		if (hashCode == 0)
			return super.hashCode();
		return hashCode;
	}

}