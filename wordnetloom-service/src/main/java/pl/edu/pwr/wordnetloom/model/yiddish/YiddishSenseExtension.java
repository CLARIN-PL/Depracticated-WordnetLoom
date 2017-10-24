package pl.edu.pwr.wordnetloom.model.yiddish;

import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.*;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "yiddish_sense_extension")
public class YiddishSenseExtension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sense", referencedColumnName = "id", nullable = false)
    private Sense sense;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dialectal_id", referencedColumnName = "id")
    private DialectalDictionary dialectalDictionary;

    @Enumerated(EnumType.STRING)
    private VariantType variant = VariantType.Yiddish_Primary_Lemma;

    @Column(name = "spelling_latin")
    private String latinSpelling;

    @Column(name = "spelling_yivo")
    private String yivoSpelling;

    @Column(name = "spelling_yiddish")
    private String yiddishSpelling;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "extension_id")
    private Set<Transcription> transcriptions = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    @JoinColumn(name = "extension_id")
    private Set<YiddishDomain> yiddishDomains = new LinkedHashSet<>();

    @Lob
    private String meaning;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grammatical_gender_id", referencedColumnName = "id")
    private GrammaticalGenderDictionary grammaticalGender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "style_id", referencedColumnName = "id")
    private StyleDictionary style;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private StatusDictionary status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lexical_characteristic_id", referencedColumnName = "id")
    private LexicalCharacteristicDictionary lexicalCharcteristic;

    @Lob
    @Column
    private String etymology;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "yiddish_extension_source",
            joinColumns = @JoinColumn(name = "sense_extension_id"),
            inverseJoinColumns = @JoinColumn(name = "source_dictionary_id")
    )
    private Set<SourceDictionary> source = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attested_id", referencedColumnName = "id")
    private AgeDictionary age;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "extension_id")
    private Set<Inflection> inflection = new LinkedHashSet<>();

    @Column(name = "etymological_root")
    private String etymologicalRoot;

    @Lob
    @Column
    private String comment;

    @Lob
    @Column
    private String context;

    @OneToMany(mappedBy = "extension", fetch = FetchType.EAGER)
    private Set<Particle> particels = new LinkedHashSet<>();

    public YiddishSenseExtension() {
    }

    public YiddishSenseExtension(Sense se) {
        sense = se;
    }

    public YiddishSenseExtension(YiddishSenseExtension ext) {
        sense = ext.sense;
        dialectalDictionary = ext.dialectalDictionary;
        variant = ext.variant;
        latinSpelling = ext.latinSpelling;
        yivoSpelling = ext.yivoSpelling;
        yiddishSpelling = ext.yiddishSpelling;

        for (Transcription t : ext.transcriptions) {
            transcriptions.add(new Transcription(t));
        }

        for (YiddishDomain d : ext.yiddishDomains) {
            yiddishDomains.add(new YiddishDomain(d, this));
        }

        meaning = ext.meaning;
        grammaticalGender = ext.grammaticalGender;
        style = ext.style;
        status = ext.status;
        lexicalCharcteristic = ext.lexicalCharcteristic;

        for (SourceDictionary sc : ext.source) {
            source.add(sc);
        }

        age = ext.age;
        etymology = ext.etymology;

        for (Inflection i : ext.inflection) {
            inflection.add(new Inflection(i));
        }

        etymologicalRoot = ext.etymologicalRoot;
        comment = ext.comment;
        context = ext.context;

        for (Particle p : ext.particels) {
            Particle np = null;
            if (p instanceof InterfixParticle) {
                np = new InterfixParticle((InterfixParticle) p, this);
            }
            if (p instanceof PrefixParticle) {
                np = new PrefixParticle((PrefixParticle) p, this);
            }
            if (p instanceof RootParticle) {
                np = new RootParticle((RootParticle) p, this);
            }
            if (p instanceof SuffixParticle) {
                np = new SuffixParticle((SuffixParticle) p, this);
            }
            if (np != null)
                particels.add(np);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sense getSense() {
        return sense;
    }

    public void setSense(Sense sense) {
        this.sense = sense;
    }

    public String getLatinSpelling() {
        return latinSpelling;
    }

    public void setLatinSpelling(String latinSpelling) {
        this.latinSpelling = latinSpelling;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public GrammaticalGenderDictionary getGrammaticalGender() {
        return grammaticalGender;
    }

    public void setGrammaticalGender(GrammaticalGenderDictionary grammaticalGender) {
        this.grammaticalGender = grammaticalGender;
    }

    public StyleDictionary getStyle() {
        return style;
    }

    public void setStyle(StyleDictionary style) {
        this.style = style;
    }

    public StatusDictionary getStatus() {
        return status;
    }

    public void setStatus(StatusDictionary status) {
        this.status = status;
    }

    public String getEtymologicalRoot() {
        return etymologicalRoot;
    }

    public void setEtymologicalRoot(String etymologicalRoot) {
        this.etymologicalRoot = etymologicalRoot;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public LexicalCharacteristicDictionary getLexicalCharcteristic() {
        return lexicalCharcteristic;
    }

    public void setLexicalCharcteristic(LexicalCharacteristicDictionary lexicalCharcteristic) {
        this.lexicalCharcteristic = lexicalCharcteristic;
    }

    public Set<Transcription> getTranscriptions() {
        return transcriptions;
    }

    public void setTranscriptions(Set<Transcription> transcriptions) {
        this.transcriptions = transcriptions;
    }

    public Set<Inflection> getInflection() {
        return inflection;
    }

    public void setInflection(Set<Inflection> inflection) {
        this.inflection = inflection;
    }

    public Set<Particle> getParticels() {
        return particels;
    }

    public void setParticels(Set<Particle> particels) {
        this.particels = particels;
    }

    public DialectalDictionary getDialectalDictionary() {
        return dialectalDictionary;
    }

    public void setDialectalDictionary(DialectalDictionary dialectalDictionary) {
        this.dialectalDictionary = dialectalDictionary;
    }

    public VariantType getVariant() {
        return variant;
    }

    public void setVariant(VariantType variant) {
        this.variant = variant;
    }

    public Set<YiddishDomain> getYiddishDomains() {
        return yiddishDomains;
    }

    public void setYiddishDomains(Set<YiddishDomain> yiddishDomains) {
        this.yiddishDomains = yiddishDomains;
    }

    public String getYivoSpelling() {
        return yivoSpelling;
    }

    public void setYivoSpelling(String yivoSpelling) {
        this.yivoSpelling = yivoSpelling;
    }

    public String getYiddishSpelling() {
        return yiddishSpelling;
    }

    public void setYiddishSpelling(String yiddishSpelling) {
        this.yiddishSpelling = yiddishSpelling;
    }

    public String getEtymology() {
        return etymology;
    }

    public void setEtymology(String etymology) {
        this.etymology = etymology;
    }

    public Set<SourceDictionary> getSource() {
        return source;
    }

    public void setSource(Set<SourceDictionary> source) {
        this.source = source;
    }

    public AgeDictionary getAge() {
        return age;
    }

    public void setAge(AgeDictionary age) {
        this.age = age;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((latinSpelling == null) ? 0 : latinSpelling.hashCode());
        result = prime * result + ((sense == null) ? 0 : sense.hashCode());
        result = prime * result + ((variant == null) ? 0 : variant.hashCode());
        result = prime * result + ((yiddishSpelling == null) ? 0 : yiddishSpelling.hashCode());
        result = prime * result + ((yivoSpelling == null) ? 0 : yivoSpelling.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        YiddishSenseExtension other = (YiddishSenseExtension) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (latinSpelling == null) {
            if (other.latinSpelling != null)
                return false;
        } else if (!latinSpelling.equals(other.latinSpelling))
            return false;
        if (sense == null) {
            if (other.sense != null)
                return false;
        } else if (!sense.equals(other.sense))
            return false;
        if (variant != other.variant)
            return false;
        if (yiddishSpelling == null) {
            if (other.yiddishSpelling != null)
                return false;
        } else if (!yiddishSpelling.equals(other.yiddishSpelling))
            return false;
        if (yivoSpelling == null) {
            if (other.yivoSpelling != null)
                return false;
        } else if (!yivoSpelling.equals(other.yivoSpelling))
            return false;
        return true;
    }
}
