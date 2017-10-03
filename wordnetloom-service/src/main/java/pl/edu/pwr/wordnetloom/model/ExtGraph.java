package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.persistence.Table;

@Entity
@Table(name = "extgraph")
@NamedQueries({
		@NamedQuery(name = "ExtGraph.dbFullGet", query = "SELECT e FROM ExtGraph e"),
		@NamedQuery(name = "ExtGraph.dbFullGetWord", query = "SELECT e FROM ExtGraph e where e.word = :word"),
		@NamedQuery(name = "ExtGraph.dbFullGetIDs", query = "SELECT e FROM ExtGraph e where e.id in ( :ids )"),
		@NamedQuery(name = "ExtGraph.dbGetNewWords", query = "SELECT e.word FROM ExtGraph e where e.packageno = :packageno AND e.pos = :pos"),
		@NamedQuery(name = "ExtGraph.GetPackages", query = "SELECT e.packageno FROM ExtGraph e WHERE e.pos = :pos GROUP BY e.packageno ORDER BY e.packageno ASC"),
		@NamedQuery(name = "ExtGraph.GetMaxPackageNo", query = "SELECT e.packageno FROM ExtGraph e WHERE e.pos = :pos ORDER BY e.packageno desc"),
		@NamedQuery(name = "ExtGraph.getIDsFromWord", query = "SELECT e.id FROM ExtGraph e where e.word = :word"),
		@NamedQuery(name = "ExtGraph.getIDsFromWordAndPkgNo", query = "SELECT e.id FROM ExtGraph e where e.word = :word AND e.packageno = :packageno"),

		@NamedQuery(name = "ExtGraph.deleteForSynsetID", query = "DELETE FROM ExtGraph e WHERE e.synset.id = :synset"), })
public class ExtGraph implements Serializable {

	private static final long serialVersionUID = 3152263756676683954L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "word")
	@Basic(fetch = FetchType.EAGER)
	private String word;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "synid", referencedColumnName = "id")
	private Synset synset;

	@Column(name = "score1")
	private Double score1;

	@Column(name = "score2")
	private Double score2;

	@Column(name = "weak", columnDefinition = "BIT", length = 1)
	private Boolean weak;

	@Column(name = "packageno", nullable = true)
	private Long packageno;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pos", referencedColumnName = "id", nullable = true)
	private PartOfSpeech pos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Synset getSynset() {
		return synset;
	}

	public void setSynset(Synset synset) {
		this.synset = synset;
	}

	public Double getScore1() {
		return score1;
	}

	public void setScore1(Double score1) {
		this.score1 = score1;
	}

	public Double getScore2() {
		return score2;
	}

	public void setScore2(Double score2) {
		this.score2 = score2;
	}

	public Boolean isWeak() {
		return weak;
	}

	public void setWeak(Boolean weak) {
		this.weak = weak;
	}

	public Long getPackageno() {
		return packageno;
	}

	public void setPackageno(Long packageno) {
		this.packageno = packageno;
	}

	public PartOfSpeech getPos() {
		return pos;
	}

	public void setPos(PartOfSpeech pos) {
		this.pos = pos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof ExtGraph))
			return false;
		ExtGraph e = (ExtGraph) o;

		if (id == null)
			return false;
		return id.equals(e.getId());
	}

	@Override
	public int hashCode() {
		int hashCode = (id.hashCode());
		if (hashCode == 0)
			return super.hashCode();
		return hashCode;
	}
}
