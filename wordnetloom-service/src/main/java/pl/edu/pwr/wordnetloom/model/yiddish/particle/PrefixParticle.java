package pl.edu.pwr.wordnetloom.model.yiddish.particle;

import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.PrefixDictionary;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PrefixParticle extends Particle {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prefix_id")
    private PrefixDictionary prefix;

    public PrefixParticle() {
    }

    public PrefixParticle(PrefixParticle p, YiddishSenseExtension ext) {
        setExtension(ext);
        setPosition(p.getPosition());
        prefix = p.getPrefix();
    }

    public PrefixParticle(PrefixDictionary dic) {
        prefix = dic;
    }

    public PrefixDictionary getPrefix() {
        return prefix;
    }

    public void setPrefix(PrefixDictionary prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return prefix.getName() + " ( Prefix )";
    }
}
