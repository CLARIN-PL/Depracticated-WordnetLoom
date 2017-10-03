package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "lexicon")
@NamedQueries({
	@NamedQuery(name = "Lexicon.findLexById",
		query = "SELECT l FROM Lexicon l WHERE l.id =:id"),
	@NamedQuery(name = "Lexicon.allLexicons",
		query = "SELECT l FROM Lexicon l JOIN FETCH l.name")
})
public class Lexicon implements Serializable {

	private static final long serialVersionUID = -1256292370070216845L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "name", referencedColumnName = "id", nullable = false)
	private Text name;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "lexicon_identifier", referencedColumnName = "id", nullable = false)
	private Text lexiconIdentifier;

	@Column(name="language")
	private String language;

	public Lexicon(){}

	public Lexicon(Text text) {
		this.name = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Text getName() {
		return name;
	}

	public void setName(Text name) {
		this.name = name;
	}

	public Text getLexiconIdentifier() {
		return lexiconIdentifier;
	}

	public void setLexiconIdentifier(Text lexiconIdentifier) {
		this.lexiconIdentifier = lexiconIdentifier;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || !(o instanceof Lexicon))
			return false;
		Lexicon e = (Lexicon) o;
		
		if(id == null) return false;
		return id.equals(e.getId());
	}

	@Override
	public int hashCode(){
		int hashCode = (id.hashCode());
		if(hashCode == 0)
			return super.hashCode();
		return hashCode;
	}

	@Override
	public String toString() {
		return name.getText();
	}
}
