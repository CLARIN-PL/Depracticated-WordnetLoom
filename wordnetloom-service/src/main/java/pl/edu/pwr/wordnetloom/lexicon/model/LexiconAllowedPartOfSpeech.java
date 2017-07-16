package pl.edu.pwr.wordnetloom.lexicon.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

@Entity
@Table(name = "lexicon_allowed_part_of_speech")
public class LexiconAllowedPartOfSpeech implements Serializable {

    private static final long serialVersionUID = -1256292370070216845L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lexicon_id", referencedColumnName = "id")
    private Lexicon lexicon;

    @ManyToOne
    @JoinColumn(name = "part_of_speech_id", referencedColumnName = "id")
    private PartOfSpeech partOfSpeech;

    @OneToMany
    @JoinTable(
            name = "part_of_speech_allowed_domain",
            joinColumns = @JoinColumn(name = "lexicon_allowed_part_of_speech_id"),
            inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private Set<Domain> domain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lexicon getLexicon() {
        return lexicon;
    }

    public void setLexicon(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public Set<Domain> getDomain() {
        return domain;
    }

    public void setDomain(Set<Domain> domain) {
        this.domain = domain;
    }
}
