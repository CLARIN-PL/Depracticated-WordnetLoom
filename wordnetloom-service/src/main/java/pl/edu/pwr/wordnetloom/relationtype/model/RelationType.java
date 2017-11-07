package pl.edu.pwr.wordnetloom.relationtype.model;

import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.common.model.Localised;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "relation_type")
public class RelationType extends GenericEntity {

    @OneToMany
    @JoinTable(
            name = "relation_type_allowed_lexicons",
            joinColumns = @JoinColumn(name = "relation_type_id"),
            inverseJoinColumns = @JoinColumn(name = "lexicon_id")
    )
    @NotNull
    @Size(min = 1)
    private List<Lexicon> lexicons = new ArrayList<>();

    @OneToMany
    @JoinTable(
            name = "relation_type_allowed_parts_of_speech",
            joinColumns = @JoinColumn(name = "relation_type_id"),
            inverseJoinColumns = @JoinColumn(name = "part_of_speech_id")
    )
    @NotNull
    @Size(min = 1)
    private List<PartOfSpeech> partsOfSpeech = new ArrayList<>();

    @Valid
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "name_id")
    private final Localised nameStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "description_id")
    private final Localised descriptionStrings = new Localised();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "display_text_id")
    private final Localised displayStrings = new Localised();

    @Valid
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "short_display_text_id")
    private final Localised shortDisplayStrings = new Localised();

    @OneToMany(mappedBy = "relationType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RelationTest> relationTests = new ArrayList<>();

    @NotNull
    @Column(name = "relation_argument")
    @Enumerated(EnumType.STRING)
    private RelationArgument relationArgument;

    @Basic
    @Column(name = "auto_reverse", columnDefinition = "bit default 0")
    private Boolean autoReverse = false;

    @Basic
    @Column(name = "multilingual", columnDefinition = "bit default 0")
    private Boolean multilingual = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_relation_type_id", nullable = true)
    private RelationType parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reverse_relation_type_id", nullable = true)
    private RelationType reverse;

    @NotNull
    @Column(name = "node_position")
    @Enumerated(EnumType.STRING)
    private NodeDirection nodePosition = NodeDirection.IGNORE;

    @Column
    private String color = "#ffffff";

    public RelationType() {
    }

    public RelationType(String locale, String name, String shortDisp, List<Lexicon> lexicons,
                        List<PartOfSpeech> partsOfSpeech,
                        RelationArgument relationArgument) {

        this.lexicons = lexicons;
        this.partsOfSpeech = partsOfSpeech;
        this.relationArgument = relationArgument;
        setName(locale, name);
        setShortDisplayText(locale, shortDisp);
    }

    public boolean addLexicon(Lexicon lexicon) {
        if (!getLexicons().contains(lexicon)) {
            getLexicons().add(lexicon);
            return true;
        }
        return false;
    }

    public boolean addPartOfSpeech(PartOfSpeech pos) {
        if (!getPartsOfSpeech().contains(pos)) {
            getPartsOfSpeech().add(pos);
            return true;
        }
        return false;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public List<Lexicon> getLexicons() {
        return lexicons;
    }

    public void setLexicons(List<Lexicon> lexicons) {
        this.lexicons = lexicons;
    }

    public List<PartOfSpeech> getPartsOfSpeech() {
        return partsOfSpeech;
    }

    public void setPartsOfSpeech(List<PartOfSpeech> partsOfSpeech) {
        this.partsOfSpeech = partsOfSpeech;
    }

    public void setMultilingual(Boolean multilingual) {
        this.multilingual = multilingual;
    }

}
