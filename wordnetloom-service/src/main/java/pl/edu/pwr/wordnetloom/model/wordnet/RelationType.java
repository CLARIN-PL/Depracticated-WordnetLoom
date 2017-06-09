package pl.edu.pwr.wordnetloom.model.wordnet;

import java.io.Serializable;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "relation_type")
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
public class RelationType implements Serializable, Comparable<RelationType> {

    private static final long serialVersionUID = -1464680230571457108L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "name", referencedColumnName = "id", nullable = false)
    private Text name;

    @Column(name = "multilingual", nullable = false, columnDefinition = "bit")
    private boolean multilingual = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_lexicon", referencedColumnName = "id", nullable = false)
    private Lexicon lexicon;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "description", referencedColumnName = "id", nullable = true)
    private Text description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "display_text", referencedColumnName = "id", nullable = false)
    private Text displayText;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "short_display_text", referencedColumnName = "id", nullable = false)
    private Text shortDisplayText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "argument_type", referencedColumnName = "id", nullable = false)
    private RelationArgument argumentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", nullable = true)
    private RelationType parent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reverse", nullable = true)
    private RelationType reverse;

    @OneToMany(mappedBy = "relationType", cascade = CascadeType.ALL)
    private List<RelationTest> relationTests;

    @Column(name = "auto_reverse", nullable = false, columnDefinition = "bit")
    private boolean autoReverse;

    public RelationType() {
    }

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

    public Text getDescription() {
        return description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

    public Text getDisplayText() {
        return displayText;
    }

    public void setDisplayText(Text displayText) {
        this.displayText = displayText;
    }

    public Text getShortDisplayText() {
        return shortDisplayText;
    }

    public void setShortDisplayText(Text shortDisplayText) {
        this.shortDisplayText = shortDisplayText;
    }

    public RelationType getParent() {
        return parent;
    }

    public void setParent(RelationType parent) {
        this.parent = parent;
    }

    public RelationType getReverse() {
        return reverse;
    }

    public void setReverse(RelationType reverse) {
        this.reverse = reverse;
    }

    public boolean isAutoReverse() {
        return autoReverse;
    }

    public void setAutoReverse(boolean autoReverse) {
        this.autoReverse = autoReverse;
    }

    public List<RelationTest> getRelationTests() {
        return relationTests;
    }

    public void setRelationTests(List<RelationTest> relationTests) {
        this.relationTests = relationTests;
    }

    public RelationArgument getArgumentType() {
        return argumentType;
    }

    public void setArgumentType(RelationArgument argumentType) {
        this.argumentType = argumentType;
    }

    public Lexicon getLexicon() {
        return lexicon;
    }

    public void setLexicon(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof RelationType)) {
            return false;
        }
        RelationType e = (RelationType) o;

        if (id == null) {
            return false;
        }
        return id.equals(e.getId());
    }

    public boolean isMultilingual() {
        return multilingual;
    }

    public void setMultilingual(boolean multilingual) {
        this.multilingual = multilingual;
    }

    @Override
    public int hashCode() {
        int hashCode = (id.hashCode());
        if (hashCode == 0) {
            return super.hashCode();
        }
        return hashCode;
    }

    @Override
    public String toString() {
        return this.name.getText();
    }

    @Override
    public int compareTo(RelationType o) {
        int byName = name.getText().compareTo(o.getName().getText());
        return byName;
    }
}
