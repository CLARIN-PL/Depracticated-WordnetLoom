package pl.edu.pwr.wordnetloom.relationtype.model;

import pl.edu.pwr.wordnetloom.common.model.Localised;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "relation_type")
public class RelationType implements Serializable {

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

    @OneToMany
    @JoinTable(
            name = "relation_type_allowed_parts_of_speech",
            joinColumns = @JoinColumn(name = "relation_type_id"),
            inverseJoinColumns = @JoinColumn(name = "part_of_speech_id")
    )
    private Set<PartOfSpeech> partsOfSpeech;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    private final Localised nameStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id")
    private final Localised descriptionStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "display_text_id")
    private final Localised displayStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "short_display_text_id")
    private final Localised shortDisplayStrings = new Localised();

    @OneToMany(mappedBy = "relationType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<RelationTest> relationTests = new ArrayList<>();

    @NotNull
    @Column(name = "relation_argument")
    @Enumerated(EnumType.STRING)
    private RelationArgument relationArgument;

    @Basic
    @Column(name = "auto_reverse", columnDefinition = "bit default 0")
    private Boolean autoReverse;

    @Basic
    @Column(name = "multilingual", columnDefinition = "bit default 0")
    private final Boolean multilingual = false;

    @ManyToOne
    @JoinColumn(name = "parent_relation_type_id", nullable = true)
    private RelationType parent;

    @ManyToOne
    @JoinColumn(name = "reverse_relation_type_id", nullable = true)
    private RelationType reverse;

    @Column(name = "node_position")
    @Enumerated(EnumType.STRING)
    private NodeDirection nodePosition;

    private String color;


    public enum RelationArgument {
        SYNSET_RELATION,
        SENSE_RELATION
    }

    public String getName(String locale) {
        return nameStrings.getString(locale);
    }

    public void setName(String locale, String name) {
        nameStrings.addString(locale, name);
    }

    public String getDescription(String locale) {
        return descriptionStrings.getString(locale);
    }

    public void setDescription(String locale, String description) {
        descriptionStrings.addString(locale, description);
    }

    public String getDisplayText(String locale) {
        return displayStrings.getString(locale);
    }

    public void setDisplayText(String locale, String display) {
        displayStrings.addString(locale, display);
    }

    public String getShortDisplayText(String locale) {
        return shortDisplayStrings.getString(locale);
    }

    public void setShortDisplayText(String locale, String shortDisplay) {
        shortDisplayStrings.addString(locale, shortDisplay);
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<PartOfSpeech> getPartsOfSpeech() {
        return partsOfSpeech;
    }

    public void setPartsOfSpeech(Set<PartOfSpeech> partsOfSpeech) {
        this.partsOfSpeech = partsOfSpeech;
    }

    public List<RelationTest> getRelationTests() {
        return relationTests;
    }

    public RelationArgument getRelationArgument() {
        return relationArgument;
    }

    public void setRelationArgument(RelationArgument relationArgument) {
        this.relationArgument = relationArgument;
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

    public NodeDirection getNodePosition() {
        return nodePosition;
    }

    public void setNodePosition(NodeDirection nodePosition) {
        this.nodePosition = nodePosition;
    }
}
