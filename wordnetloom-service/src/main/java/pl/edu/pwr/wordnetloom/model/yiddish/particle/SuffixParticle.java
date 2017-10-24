package pl.edu.pwr.wordnetloom.model.yiddish.particle;

import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SuffixDictionary;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SuffixParticle extends Particle {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "suffix_id")
    private SuffixDictionary suffix;

    public SuffixParticle() {
    }

    public SuffixParticle(SuffixParticle p, YiddishSenseExtension ext) {
        setExtension(ext);
        setPosition(p.getPosition());
        suffix = p.getSuffix();
    }

    public SuffixParticle(SuffixDictionary dic) {
        suffix = dic;
    }

    public SuffixDictionary getSuffix() {
        return suffix;
    }

    public void setSuffix(SuffixDictionary suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return suffix.getName() + " ( Suffix )";
    }
}
