package pl.edu.pwr.wordnetloom.sense.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Entity
@Table(name = "sense")
@NamedQueries({
    @NamedQuery(name = "Sense.findAll",
            query = "SELECT s FROM Sense s"),
    @NamedQuery(name = "Sense.findCountAll",
            query = "SELECT COUNT(s) FROM Sense s"),
    @NamedQuery(name = "Sense.findByLema",
            query = "SELECT s FROM Sense s join fetch s.domain join fetch s.lemma join fetch s.partOfSpeech WHERE s.lexicon.id IN (:lexicons) AND LOWER(s.lemma.word) LIKE :lemma ORDER BY s.lemma.word asc"),
    @NamedQuery(name = "Sense.findSenseByListID",
            query = "SELECT s FROM Sense s join fetch s.domain join fetch s.lemma join fetch s.partOfSpeech WHERE s.id in (:ids)"),
    @NamedQuery(name = "Sense.findSenseBySynsetID",
            query = "select s.sense from SenseToSynset s where s.sense.lexicon.id IN( :lexicons ) and s.idSynset =:idSynset order by s.senseIndex"),
    @NamedQuery(name = "Sense.CountSenseBySynsetID",
            query = "select count(s.sense) from SenseToSynset s where s.idSynset =:idSynset"),
    @NamedQuery(name = "Sense.findSensesBySynsetIDs",
            query = "select s.sense from SenseToSynset s where s.idSynset in (:ids)")
})
public class Sense implements Serializable {

    private static final long serialVersionUID = 800201228216890725L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "domain_id", referencedColumnName = "id", nullable = false)
    private Domain domain;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "word_id", referencedColumnName = "id", nullable = false)
    private Word word;

    @ManyToOne
    @JoinColumn(name = "part_of_speech_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private PartOfSpeech partOfSpeech;

    @NotNull
    @Column(name = "variant", nullable = false, columnDefinition = "int default = '1'")
    private Integer variant = 1;

    @ManyToOne
    @JoinColumn(name = "synset_id", referencedColumnName = "id")
    private Synset synset;

    @Column(name = "synset_position")
    private Integer synsetPosition = 0;

    @OneToOne(mappedBy = "sense", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Valid
    @NotNull
    private SenseAttributes senseAttributes;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lexicon_id", referencedColumnName = "id", nullable = false)
    private Lexicon lexicon;

    public Sense() {
        super();
    }

    public Sense(Sense sense) {
        this.id = sense.id;
        this.domain = sense.domain;
        this.word = sense.word;
        this.partOfSpeech = sense.partOfSpeech;
        this.variant = sense.variant;
        this.lexicon = sense.lexicon;
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

    public PartOfSpeech getPartOfSpeech() {
        return this.partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public Lexicon getLexicon() {
        return lexicon;
    }

    public void setLexicon(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Integer getVariant() {
        return variant;
    }

    public void setVariant(Integer variant) {
        this.variant = variant;
    }

    public Synset getSynset() {
        return synset;
    }

    public void setSynset(Synset synset) {
        this.synset = synset;
    }

    public SenseAttributes getSenseAttributes() {
        return senseAttributes;
    }

    public void setSenseAttributes(SenseAttributes senseAttributes) {
        this.senseAttributes = senseAttributes;
    }

    public Integer getSynsetPosition() {
        return synsetPosition;
    }

    public void setSynsetPosition(Integer synsetPposition) {
        this.synsetPosition = synsetPposition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Sense)) {
            return false;
        }
        Sense e = (Sense) o;

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
