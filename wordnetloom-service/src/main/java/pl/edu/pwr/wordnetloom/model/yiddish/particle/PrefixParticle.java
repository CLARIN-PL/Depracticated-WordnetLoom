package pl.edu.pwr.wordnetloom.model.yiddish.particle;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.PrefixDictionary;

@Entity
public class PrefixParticle extends Particle {

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prefix_id")
	private PrefixDictionary prefix;

	public PrefixParticle() {
	}
	
	public PrefixParticle(PrefixDictionary dic) {
		this.prefix = dic;
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
