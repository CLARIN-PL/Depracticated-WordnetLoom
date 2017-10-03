package pl.edu.pwr.wordnetloom.model;

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
 * The persistent class for the sense_to_synset database table.
 * 
 */
@Entity
@Table(name = "sense_to_synset")
@NamedQueries({
	@NamedQuery(name = "SenseToSynset.findAll",
			query = "SELECT s FROM SenseToSynset s ORDER BY s.idSense, s.senseIndex asc"),
	@NamedQuery(name = "SenseToSynset.findAllBySense",
			query = "SELECT s FROM SenseToSynset s WHERE s.idSense =:idSense"),
	@NamedQuery(name = "SenseToSynset.findAllBySynset",
			query = "SELECT s FROM SenseToSynset s WHERE s.synset.id =:idSynset ORDER BY s.senseIndex"),
	@NamedQuery(name = "SenseToSynset.findAllBySynsets",
			query = "SELECT s FROM SenseToSynset s WHERE s.idSynset in(:ids)"),
	@NamedQuery(name = "SenseToSynset.CountSenseBySense",
			query = "SELECT COUNT(s) FROM SenseToSynset s WHERE s.sense.id =:idSense"),
	@NamedQuery(name = "SenseToSynset.CountSynsetBySense",
			query = "SELECT COUNT(s.synset) FROM SenseToSynset s WHERE s.idSense =:idSense"),
	@NamedQuery(name = "SenseToSynset.DeleteBySynsetID",
		query = "DELETE FROM SenseToSynset s WHERE s.idSynset =:idSynset"),
	@NamedQuery(name = "SenseToSynset.DeleteBySynsetIdAndSenseId",
		query = "DELETE FROM SenseToSynset s WHERE s.idSynset =:idSynset AND s.idSense =:idSense"),
	@NamedQuery(name = "SenseToSynset.dbUsedUnitsIDs",
		query = "SELECT sts.idSense FROM SenseToSynset sts GROUP BY sts.idSense"),
	@NamedQuery(name = "SenseToSynset.dbGetUnitsAppearingInMoreThanOneSynset",
		query = "SELECT sts.sense FROM SenseToSynset sts GROUP BY sts.idSense HAVING COUNT(sts.idSynset) > 1 "),
})
public class SenseToSynset implements Serializable {

	private static final long serialVersionUID = 657891223603990725L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "id_synset", nullable = false)
	private Long idSynset;

	@Column(name = "id_sense", nullable = false)
	private Long idSense;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_synset", referencedColumnName = "id", updatable = false, insertable = false)
	private Synset synset;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_sense", referencedColumnName = "id", updatable = false, insertable = false)
	private Sense sense;

	@Column(name = "sense_index", nullable = false)
	private Integer senseIndex = 0;

	public SenseToSynset() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdSynset() {
		return idSynset;
	}

	public void setIdSynset(Long idSynset) {
		this.idSynset = idSynset;
	}

	public Long getIdSense() {
		return idSense;
	}

	public void setIdSense(Long idSense) {
		this.idSense = idSense;
	}

	public Synset getSynset() {
		return synset;
	}

	public void setSynset(Synset synset) {
		this.synset = synset;
	}

	public Sense getSense() {
		return sense;
	}

	public void setSense(Sense sense) {
		this.sense = sense;
	}

	public Integer getSenseIndex() {
		return senseIndex;
	}

	public void setSenseIndex(Integer senseIndex) {
		this.senseIndex = senseIndex;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || !(o instanceof SenseToSynset))
			return false;
		SenseToSynset e = (SenseToSynset) o;

		if(id == null) return false;
		return id.equals(e.getId());
	}

	@Override
	public int hashCode(){
		int hashCode = (id.hashCode());
		if(hashCode == 0)
			return super.hashCode();
		return hashCode;
	}

}