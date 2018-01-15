package pl.edu.pwr.wordnetloom.model.yiddish.particle;

import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ConstituentParticle extends Particle {

    @Column
    private String constituent;

    public ConstituentParticle() {
    }

    public ConstituentParticle(ConstituentParticle p, YiddishSenseExtension ext) {
        setExtension(ext);
        setPosition(p.getPosition());
        constituent = p.getConstituent();
    }

    public ConstituentParticle(String constituent) {
        this.constituent = constituent;
    }

    public String getConstituent() {
        return constituent;
    }

    public void setConstituent(String constituent) {
        this.constituent = constituent;
    }

    @Override
    public String toString() {
        return constituent + " ( Constituent )";
    }
}
