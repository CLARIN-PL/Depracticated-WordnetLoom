package pl.edu.pwr.wordnetloom.model.yiddish.particle;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.InterfixDictionary;

@Entity
public class InterfixParticle extends Particle{

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "interfix_id")
	private InterfixDictionary interfix;

	public InterfixParticle() {}

	public InterfixParticle(InterfixDictionary dic) {
		this.interfix = dic;
	}

	public InterfixDictionary getInterfix() {
		return interfix;
	}

	public void setInterfix(InterfixDictionary interfix) {
		this.interfix = interfix;
	}

	@Override
    public String toString() {
        return interfix.getName()+ " ( Interfix )";
    }
}
