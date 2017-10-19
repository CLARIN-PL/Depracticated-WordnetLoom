package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Named;
import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;

@Entity
@Table(name="sense")
@NamedQueries({
	@NamedQuery(name = "Sense.findAll", 
		query = "SELECT s FROM Sense s"),
	@NamedQuery(name = "Sense.findSenseByID",
		query = "SELECT s FROM Sense s where s.id =:id"),
	@NamedQuery(name = "Sense.findCountAll",
		query = "SELECT COUNT(s) FROM Sense s"),
	@NamedQuery(name = "Sense.findByLema", 
		query = "SELECT s FROM Sense s join fetch s.domain join fetch s.lemma join fetch s.partOfSpeech WHERE s.lexicon.id IN (:lexicons) AND LOWER(s.lemma.word) LIKE :lemma ORDER BY s.lemma.word asc"),
	@NamedQuery(name = "Sense.findSenseByListID",
		query = "SELECT s FROM Sense s join fetch s.domain join fetch s.lemma join fetch s.partOfSpeech WHERE s.id in (:ids)"),
	@NamedQuery(name = "Sense.findSenseBySynsetID",
		query = "SELECT s FROM Sense s WHERE s.lexicon.id IN (:lexicon) AND s.synset.id = :idSynset ORDER BY s.senseIndex"),
//		query = "select s.sense from SenseToSynset s where s.sense.lexicon.id IN( :lexicons ) and s.idSynset =:idSynset order by s.senseIndex"),
//			query = "select s.sense from SenseToSynset s where s.sense.lexicon.id IN( :lexicons ) and s.synset.id =:idSynset order by s.senseIndex"),
	@NamedQuery(name = "Sense.CountSenseBySynsetID",
		query = "SELECT COUNT(1) FROM Sense s WHERE s.synset.id = :idSynset"),
//		query = "select count(s.sense) from SenseToSynset s where s.idSynset =:idSynset"),
//			query = "select count(s.sense) from SenseToSynset s where s.synset.id =:idSynset"),
	@NamedQuery(name = "Sense.dbGetNextVariant",
		query = "SELECT MAX(s.senseNumber) FROM Sense AS s WHERE LOWER(s.lemma.word) = :word AND s.partOfSpeech.id = :pos"),
	@NamedQuery(name = "Sense.findSensesBySynsetIDs",
		query = "SELECT s FROM Sense s WHERE s.synset.id IN (:ids)"),
//		query = "select s.sense from SenseToSynset s where s.idSynset in (:ids)")
//			query = "select s.sense from SenseToSynset s where s.synset.id in (:ids)")
		@NamedQuery(name="Sense.getUnitsAppearingInMoreThanOneSynset",
		query = "SELECT s FROM Sense s GROUP BY s.id HAVING COUNT (s.synset.id) > 1"),
		@NamedQuery(name = "Sense.countSenseBySense",
		query = "SELECT COUNT(s) FROM Sense s WHERE s.synset.id = :id"),
		@NamedQuery(name = "Sense.usedUnitsIDs",
		query = "SELECT s.id FROM Sense s GROUP BY s.id")
	})

public class Sense implements Serializable, Comparable<Sense> {

	private static final long serialVersionUID = 800201228216890725L;

	public static final String SOURCE = "source";
	public static final String DEFINITION = "definition";
	public static final String COMMENT = "comment";
	public static final String PROJECT = "project";
	public static final String REGISTER = "register";
	public static final String LINK = "link";
	public static final String USE_CASES = "use_cases";
	
	public static final String A1_MARKEDNESS = "a1_markedness";
	public static final String A1_EMOTIONS = "a1_emotions";
	public static final String A1_EMOTIONS_VALUES = "a1_emotions_values";
	public static final String A1_EMOTIONAL_MARKEDNESS = "a1_emotional_markedness";
	public static final String A1_EXAMPLES = "a1_examples";

	public static final String A2_MARKEDNESS = "a2_markedness";
	public static final String A2_EMOTIONS = "a2_emotions";
	public static final String A2_EMOTIONS_VALUES = "a2_emotions_values";
	public static final String A2_EMOTIONAL_MARKEDNESS = "a2_emotional_markedness";
	public static final String A2_EXAMPLES = "a2_examples";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="domain", referencedColumnName="id", nullable = false)
	private Domain domain;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="lemma", referencedColumnName="id", nullable = false)
	private Word lemma;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="part_of_speech", referencedColumnName="id", nullable = false)
	private PartOfSpeech partOfSpeech;

	@Column(name="sense_number", nullable = false, columnDefinition="int default 1")
	private Integer senseNumber;

//	@OneToOne(mappedBy="sense", fetch=FetchType.LAZY)
//	private SenseToSynset senseToSynset;

	@OneToMany(mappedBy="sense", fetch=FetchType.LAZY)
	private List<SenseAttribute> senseAttributes = new ArrayList<SenseAttribute>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "id_lexicon", referencedColumnName = "id", nullable = false)
	private Lexicon lexicon;

	@OneToMany(mappedBy="sense", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@NotFound(action =NotFoundAction.IGNORE)
	private Set<YiddishSenseExtension> yiddishSenseExtension = new LinkedHashSet<YiddishSenseExtension>();

	@Column(name = "sense_index")
	private Integer senseIndex;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_synset", referencedColumnName = "id")
	private Synset synset;
	
	@Transient
	private ArrayList<Synset> synsets;

	public Sense() {}

	/**
	 * Konstruktor kopiujący //TODO do sprawdzenia, to nie jest konstruktor kopiujacy
	 * @param sense
	 */
	public Sense(Sense sense){
		this.id = sense.id;
		this.domain = sense.domain;
		this.lemma = sense.lemma;
		this.partOfSpeech = sense.partOfSpeech;
		this.senseNumber = sense.senseNumber;
		this.lexicon = sense.lexicon;
		this.yiddishSenseExtension = sense.yiddishSenseExtension;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Domain getDomain() {
		return this.domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Word getLemma() {
		return this.lemma;
	}

	public void setLemma(Word lemma) {
		if (null != lemma && null != lemma.getWord() && lemma.getWord().length()>255) {
			this.lemma.setWord(lemma.getWord().substring(0,255));
		}
		this.lemma = lemma;
	}

	public PartOfSpeech getPartOfSpeech() {
		return this.partOfSpeech;
	}

	public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}

	public int getSenseNumber() {
		return this.senseNumber;
	}

	public void setSenseNumber(int senseNumber) {
		this.senseNumber = senseNumber;
	}

//	public SenseToSynset getSenseToSynset() {
//		return senseToSynset;
//	}
//
//	public void setSenseToSynset(SenseToSynset senseToSynset) {
//		this.senseToSynset = senseToSynset;
//	}

	public ArrayList<Synset> getSynsets() {
		return synsets;
	}

	public void setSynsets(ArrayList<Synset> synsets) {
		this.synsets = synsets;
	}

	public List<SenseAttribute> getSenseAttributes() {
		return senseAttributes;
	}

	public void setSenseAttributes(List<SenseAttribute> senseAttributes) {
		this.senseAttributes = senseAttributes;
	}

	public Lexicon getLexicon() {
		return lexicon;
	}

	public void setLexicon(Lexicon lexicon) {
		this.lexicon = lexicon;
	}

	public Set<YiddishSenseExtension> getYiddishSenseExtension() {
		return yiddishSenseExtension;
	}

	public void setYiddishSenseExtension(Set<YiddishSenseExtension> yiddishSenseExtension) {
		this.yiddishSenseExtension = yiddishSenseExtension;
	}

	public Integer getSenseIndex(){
		return this.senseIndex;
	}

	public void setSenseIndex(Integer senseIndex){
		this.senseIndex = senseIndex;
	}

	public Synset getSynset(){
		return this.synset;
	}

	public void setSynset(Synset synset){
		this.synset = synset;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		
		if(null != getLemma()) b.append(getLemma().getWord());
		b.append(" ");
		b.append(getSenseNumber());

		if (getDomain()!= null){
			b.append(" (");
			String domainName = getDomain().getName().getText();
			b.append(domainName.substring(domainName.lastIndexOf('_') + 1));
			b.append(")");
	}

		return b.toString();
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || !(o instanceof Sense))
			return false;
		Sense e = (Sense) o;
	
		if(id == null) return false;
		return id.equals(e.getId());
	}

	@Override
	public int hashCode(){
		int hashCode = (id !=null ? id.hashCode() : 0);
		if(hashCode == 0)
			return super.hashCode();
		return hashCode;
	}

	@Override
	public int compareTo(Sense o) {
		int pos = toString().compareTo(o.toString());
		return pos;
	}
}