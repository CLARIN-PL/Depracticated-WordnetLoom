package pl.edu.pwr.wordnetloom.synset.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

@Entity
@Table(name = "synset")
public class Synset implements Serializable {

    private static final long serialVersionUID = 800201223603990725L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "split")
    private Integer split = 0;

    @OneToMany(mappedBy = "synset")
    @OrderBy("synsetPosition")
    private List<Sense> senses = new ArrayList<>();

    @NotNull
    @Valid
    @OneToOne(mappedBy = "synset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SynsetAttributes synsetAttributes;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lexicon_id", referencedColumnName = "id", nullable = false)
    private Lexicon lexicon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSplit() {
        return split;
    }

    public void setSplit(Integer split) {
        this.split = split;
    }

    public List<Sense> getSenses() {
        return senses;
    }

    public void setSenses(List<Sense> senses) {
        this.senses = senses;
    }

    public SynsetAttributes getSynsetAttributes() {
        return synsetAttributes;
    }

    public void setSynsetAttributes(SynsetAttributes synsetAttributes) {
        this.synsetAttributes = synsetAttributes;
    }

    public Lexicon getLexicon() {
        return lexicon;
    }

    public void setLexicon(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

}
