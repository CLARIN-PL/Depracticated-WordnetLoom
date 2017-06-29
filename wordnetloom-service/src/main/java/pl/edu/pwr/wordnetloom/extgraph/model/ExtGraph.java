package pl.edu.pwr.wordnetloom.extgraph.model;

import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "extgraph")
public class ExtGraph implements Serializable {

    private static final long serialVersionUID = 3152263756676683954L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word")
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
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof ExtGraph)) {
            return false;
        }
        ExtGraph e = (ExtGraph) o;

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
