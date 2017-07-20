package pl.edu.pwr.wordnetloom.relationtype.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import pl.edu.pwr.wordnetloom.common.model.Localised;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.relationtest.model.SynsetRelationTest;

@Entity
@Table(name = "synset_relation_type")
public class SynsetRelationType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinTable(
            name = "synset_relation_type_allowed_lexicons",
            joinColumns = @JoinColumn(name = "synset_relation_type_id"),
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

    @OneToMany(mappedBy = "relationType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SynsetRelationTest> relationTests = new ArrayList<>();

    @Basic
    @Column(name = "auto_reverse", columnDefinition = "bit default 0")
    private Boolean autoReverse;

    @Basic
    @Column(name = "multilingual", columnDefinition = "bit default 0")
    private Boolean multilingual = false;

    @ManyToOne
    @JoinColumn(name = "parent_relation_type_id", nullable = true)
    private SynsetRelationType parent;

    @ManyToOne
    @JoinColumn(name = "reverse_relation_type_id", nullable = true)
    private SynsetRelationType reverse;

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

    public Boolean getAutoReverse() {
        return autoReverse;
    }

    public void setAutoReverse(Boolean autoReverse) {
        this.autoReverse = autoReverse;
    }

    public Boolean getMultilingual() {
        return multilingual;
    }

    public void setMultilingual(Boolean multilingual) {
        this.multilingual = multilingual;
    }

    public SynsetRelationType getParent() {
        return parent;
    }

    public void setParent(SynsetRelationType parent) {
        this.parent = parent;
    }

    public SynsetRelationType getReverse() {
        return reverse;
    }

    public void setReverse(SynsetRelationType reverse) {
        this.reverse = reverse;
    }

    public List<SynsetRelationTest> getRelationTests() {
        return relationTests;
    }

    public void setRelationTests(List<SynsetRelationTest> relationTests) {
        this.relationTests = relationTests;
    }

}
