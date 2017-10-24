package pl.edu.pwr.wordnetloom.model.yiddish;

import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainModifierDictionary;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "yiddish_domain")
public class YiddishDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_id", referencedColumnName = "id")
    private DomainDictionary domain;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modifier_id", referencedColumnName = "id")
    private DomainModifierDictionary modifier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "extension_id", referencedColumnName = "id")
    private YiddishSenseExtension senseExtension;

    public YiddishDomain() {
    }

    public YiddishDomain(YiddishDomain domain, YiddishSenseExtension ext) {
        super();
        this.domain = domain.domain;
        modifier = domain.modifier;
        senseExtension = ext;
    }

    public YiddishDomain(DomainDictionary domain, DomainModifierDictionary modifier) {
        super();
        this.domain = domain;
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(domain.getName());
        if (modifier != null) {
            sb.append(" ( ").append(modifier.getName()).append(" )");
        }
        return sb.toString();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DomainDictionary getDomain() {
        return domain;
    }

    public void setDomain(DomainDictionary domain) {
        this.domain = domain;
    }

    public DomainModifierDictionary getModifier() {
        return modifier;
    }

    public void setModifier(DomainModifierDictionary modifier) {
        this.modifier = modifier;
    }
}
