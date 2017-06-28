package pl.edu.pwr.wordnetloom.relation.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.common.model.Localised;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;


@NamedQueries({
    @NamedQuery(name = "RelationType.dbDelete",
            query = "DELETE FROM RelationType rt WHERE rt.parent = :parent"),
    @NamedQuery(name = "RelationType.dbGetChildren",
            query = "SELECT rt FROM RelationType rt WHERE rt.parent = :parent  AND rt.lexicon.id IN (:lexicons)"),
    @NamedQuery(name = "RelationType.dbDeleteAll",
            query = "DELETE FROM RelationType rt"),
    @NamedQuery(name = "RelationType.dbFullGetRelationTypes",
            query = "SELECT rt FROM RelationType rt LEFT JOIN FETCH rt.displayText LEFT JOIN FETCH rt.name LEFT JOIN FETCH rt.description "
            + "LEFT JOIN FETCH rt.shortDisplayText LEFT JOIN FETCH rt.parent LEFT JOIN FETCH rt.reverse WHERE rt.lexicon.id IN (:lexicons)"),
    @NamedQuery(name = "RelationType.dbGetReverseByRelationTypeID",
            query = "select rt.reverse FROM RelationType rt where rt.id = :ID"),
    @NamedQuery(name = "RelationType.dbGetHighest",
            query = "SELECT rt FROM RelationType rt WHERE rt.parent = NULL AND rt.lexicon.id IN (:lexicons)"),
    @NamedQuery(name = "RelationType.dbGetHighestArgument",
            query = "SELECT rt FROM RelationType rt WHERE rt.parent = NULL AND rt.argumentType.id = :argument AND rt.lexicon.id IN (:lexicons)")})
@Entity
@Table(name = "relation_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "dtype",
        discriminatorType = DiscriminatorType.STRING)
public abstract class RelationType implements Serializable {

    private static final long serialVersionUID = -1464680230571457108L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinTable(
            name = "relation_type_allowed_lexicons",
            joinColumns = @JoinColumn(name = "relation_type_id"),
            inverseJoinColumns = @JoinColumn(name = "lexicon_id")
    )
    private Set<Lexicon> lexicons;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    private Localised nameStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id")
    private Localised descriptionStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "display_text_id")
    private Localised displayStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "short_display_text_id")
    private Localised shortDisplayStrings = new Localised();

    @OneToMany(mappedBy = "relationType", cascade = CascadeType.ALL)
    private List<RelationTest> relationTests;

    @Basic
    @Column(name = "auto_reverse", nullable = false, columnDefinition = "bit")
    private Boolean autoReverse;

    public RelationType() {
        super();
    }

    public String getName(String locale) {
        return this.nameStrings.getString(locale);
    }

    public void setName(String locale, String name) {
        this.nameStrings.addString(locale, name);
    }

    public String getDescription(String locale) {
        return this.descriptionStrings.getString(locale);
    }

    public void setDescription(String locale, String description) {
        this.descriptionStrings.addString(locale, description);
    }

    public String getDisplayText(String locale) {
        return this.displayStrings.getString(locale);
    }

    public void setDispalyText(String locale, String display) {
        this.displayStrings.addString(locale, display);
    }

    public String getShortDisplayText(String locale) {
        return this.shortDisplayStrings.getString(locale);
    }

    public void setShortDispalyText(String locale, String shortDisplay) {
        this.shortDisplayStrings.addString(locale, shortDisplay);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Lexicon> getLexicons() {
        return lexicons;
    }

    public void setLexicons(Set<Lexicon> lexicons) {
        this.lexicons = lexicons;
    }

      public List<RelationTest> getRelationTests() {
        return relationTests;
    }

    public void setRelationTests(List<RelationTest> relationTests) {
        this.relationTests = relationTests;
    }

    public Boolean getAutoReverse() {
        return autoReverse;
    }

    public void setAutoReverse(Boolean autoReverse) {
        this.autoReverse = autoReverse;
    }

}
