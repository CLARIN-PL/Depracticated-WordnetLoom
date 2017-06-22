package pl.edu.pwr.wordnetloom.model.wordnet;

import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "part_of_speech")
@NamedQueries({
    @NamedQuery(name = "PartOfSpeech.getAllPOSes",
            query = "SELECT DISTINCT p FROM PartOfSpeech p LEFT JOIN FETCH p.name LEFT JOIN FETCH p.domains WHERE p.lexicon.id IN (:ids)"),
    @NamedQuery(name = "PartOfSpeech.getAllPOSesNoLexicon",
            query = "SELECT DISTINCT p FROM PartOfSpeech p LEFT JOIN FETCH p.name LEFT JOIN FETCH p.domains")
})
public class PartOfSpeech implements Serializable {

    private static final long serialVersionUID = 6240656223603990725L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "name", referencedColumnName = "id", nullable = false)
    private Text name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_lexicon", referencedColumnName = "id", nullable = false)
    private Lexicon lexicon;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "domain_allowed_posses",
            joinColumns = @JoinColumn(name = "id_pos"),
            inverseJoinColumns = @JoinColumn(name = "id_domain")
    )
    private Set<Domain> domains;

    @Column(name = "uby_lmf_type")
    @Enumerated(EnumType.STRING)
    private pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech ubyLmfType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }

    public Set<Domain> getDomains() {
        return domains;
    }

    public void setDomains(Set<Domain> domains) {
        this.domains = domains;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    public Lexicon getLexicon() {
        return lexicon;
    }

    public void setLexicon(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    public boolean contains(Domain domain) {
        Set<Domain> currentDomains = getDomains();
        return currentDomains.stream().anyMatch((d) -> (d.toString().equals(domain.toString())));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof PartOfSpeech)) {
            return false;
        }
        PartOfSpeech e = (PartOfSpeech) o;
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

    public pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech getUbyType() {
        return ubyLmfType;
    }

    public void setUbyType(
            pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech ubyType) {
        this.ubyLmfType = ubyType;
    }

}
