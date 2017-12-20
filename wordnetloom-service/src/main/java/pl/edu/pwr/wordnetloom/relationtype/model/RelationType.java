package pl.edu.pwr.wordnetloom.relationtype.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "relation_type")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RelationType extends GenericEntity {

    @OneToMany
    @JoinTable(
            name = "relation_type_allowed_lexicons",
            joinColumns = @JoinColumn(name = "relation_type_id"),
            inverseJoinColumns = @JoinColumn(name = "lexicon_id")
    )

    @NotNull
    @Size(min = 1)
    private Set<Lexicon> lexicons = new HashSet<>();

    @OneToMany
    @JoinTable(
            name = "relation_type_allowed_parts_of_speech",
            joinColumns = @JoinColumn(name = "relation_type_id"),
            inverseJoinColumns = @JoinColumn(name = "part_of_speech_id")
    )
    @NotNull
    @Size(min = 1)
    private Set<PartOfSpeech> partsOfSpeech = new HashSet<>();

    @NotNull
    @Column(name = "name_id")
    private Long name;

    @Column(name = "description_id")
    private Long description;

    @Column(name = "display_text_id")
    private Long displayText;

    @NotNull
    @Column(name = "short_display_text_id")
    private Long shortDisplayText;

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
    @JoinColumn(name = "parent_relation_type_id")
    private RelationType parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reverse_relation_type_id")
    private RelationType reverse;

    @NotNull
    @Column(name = "node_position")
    @Enumerated(EnumType.STRING)
    private NodeDirection nodePosition = NodeDirection.IGNORE;

    @Column
    private String color = "#ffffff";

    public RelationType() {
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

    public boolean addRelationTest(RelationTest test) {
        if (!getRelationTests().contains(test)) {
            getRelationTests().add(test);
            return true;
        }
        return false;
    }

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public Long getDescription() {
        return description;
    }

    public void setDescription(Long description) {
        this.description = description;
    }

    public Long getDisplayText() {
        return displayText;
    }

    public void setDisplayText(Long displayText) {
        this.displayText = displayText;
    }

    public Long getShortDisplayText() {
        return shortDisplayText;
    }

    public void setShortDisplayText(Long shortDisplayText) {
        this.shortDisplayText = shortDisplayText;
    }

    public void setRelationTests(List<RelationTest> relationTests) {
        this.relationTests = relationTests;
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

    public Set<Lexicon> getLexicons() {
        return lexicons;
    }

    public void setLexicons(Set<Lexicon> lexicons) {
        this.lexicons = lexicons;
    }

    public Set<PartOfSpeech> getPartsOfSpeech() {
        return partsOfSpeech;
    }

    public void setPartsOfSpeech(Set<PartOfSpeech> partsOfSpeech) {
        this.partsOfSpeech = partsOfSpeech;
    }

    public void setMultilingual(Boolean multilingual) {
        this.multilingual = multilingual;
    }

}
