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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

@Entity
@Table(name = "synset")
@NamedQueries({
    @NamedQuery(name = "Synset.findCountAll",
            query = "SELECT COUNT(s) FROM Synset s"),
    @NamedQuery(name = "Synset.findSynsetBySensID",
            query = "SELECT s.synset FROM SenseToSynset s WHERE s.idSense = :senseID AND s.sense.lexicon.id IN (:lexicons)"),
    @NamedQuery(name = "Synset.findListSynsetByID",
            query = "SELECT s FROM Synset s WHERE s.id IN ( :synsetsID )"),
    @NamedQuery(name = "Synset.getAllIDs",
            query = "SELECT s FROM Synset s"),
    @NamedQuery(name = "Synset.dbGetUnit",
            query = "SELECT s FROM Synset s JOIN s.senseToSynset AS sts LEFT JOIN sts.sense AS sen LEFT JOIN sen.senseAttributes AS sea LEFT JOIN s.synsetAttributes AS sa WHERE sen.lexicon.id IN( :lexicons) AND  s.id = :synsetID ORDER BY s.id, sts.senseIndex"),
    @NamedQuery(name = "Synset.dbGetUnitsByIDs",
            query = "SELECT s FROM Synset s JOIN s.senseToSynset AS sts LEFT JOIN sts.sense AS sen LEFT JOIN sen.senseAttributes AS sea LEFT JOIN s.synsetAttributes AS sa WHERE s.id IN ( :synsetsIDs ) ORDER BY s.id, sts.senseIndex"),
    @NamedQuery(name = "Synset.dbGetSynsetRels",
            query = "SELECT sr.synsetFrom FROM SynsetRelation sr WHERE sr.synsetFrom.id IN ( SELECT sr.synsetFrom.id FROM SynsetRelation sr WHERE sr.synsetFrom.id = :synsetID ) OR sr.synsetFrom.id IN ( SELECT sr.synsetTo.id FROM SynsetRelation sr WHERE sr.synsetTo.id = :synsetID )"),
    @NamedQuery(name = "Synset.dbGetSynsetsRels",
            query = "SELECT sr.synsetFrom FROM SynsetRelation sr WHERE sr.id IN ( SELECT sr.id FROM SynsetRelation sr WHERE sr.synsetFrom IN ( :synsetList ) ) OR sr.id IN ( SELECT sr.id FROM SynsetRelation sr WHERE sr.synsetTo IN ( :synsetList ) )"),
    @NamedQuery(name = "Synset.dbGetSimilarityCount",
            query = "SELECT COUNT( a.idSense ) FROM SenseToSynset a, SenseToSynset b WHERE a.idSynset = :idA AND b.idSynset = :idB AND a.idSense = b.idSense"),
    @NamedQuery(name = "Synset.fastGetPOSID",
            query = "SELECT s.sense.partOfSpeech.id FROM SenseToSynset s WHERE s.idSynset = :idSynset ORDER BY s.senseIndex"),})
public class Synset implements Serializable {

    private static final long serialVersionUID = 800201223603990725L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "split")
    private Integer split = 0;

    @OneToMany(mappedBy = "synset")
    private List<Sense> senses = new ArrayList<>();

    @NotNull
    @OneToOne(mappedBy = "synset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SynsetAttributes synsetAttributes;

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

}
