package pl.edu.pwr.wordnetloom.lexicon.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "lexicon_allowed_part_of_speech")
public class LexiconAllowedPartOfSpeech extends GenericEntity {

    private static final long serialVersionUID = -1256292370070216845L;

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
