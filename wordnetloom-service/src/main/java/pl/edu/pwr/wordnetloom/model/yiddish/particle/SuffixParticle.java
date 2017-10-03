package pl.edu.pwr.wordnetloom.model.yiddish.particle;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SuffixDictionary;

@Entity
public class SuffixParticle extends Particle{

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "suffix_id")
	private SuffixDictionary suffix;

	public SuffixParticle() {
	}

	public SuffixParticle(SuffixDictionary dic) {
		this.suffix = dic;
	}
	
	public SuffixDictionary getSuffix() {
		return suffix;
	}

	public void setSuffix(SuffixDictionary suffix) {
		this.suffix = suffix;
	}

	@Override
    public String toString() {
        return suffix.getName()+ " ( Suffix )";
    }
}
