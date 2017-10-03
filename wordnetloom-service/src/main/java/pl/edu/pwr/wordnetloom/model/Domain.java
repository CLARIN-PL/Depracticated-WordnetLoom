package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

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
@Table(name = "domain")
@NamedQueries({
	@NamedQuery(name = "Domain.getFromSenses",
			query = "SELECT DISTINCT s.domain FROM Sense s ORDER BY s.domain ASC"),
	@NamedQuery(name = "Domain.getAllDomains",
			query = "SELECT DISTINCT d FROM Domain d LEFT JOIN FETCH d.name LEFT JOIN FETCH d.description WHERE d.lexicon.id IN (:lexicons)")

})
public class Domain implements Serializable {

	private static final long serialVersionUID = -9015379547562677206L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "name", referencedColumnName = "id", nullable = false)
	private Text name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "description", referencedColumnName = "id")
	private Text description;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "id_lexicon", referencedColumnName = "id", nullable = false)
	private Lexicon lexicon;

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

	public Text getDescription() {
		return description;
	}

	public void setDescription(Text description) {
		this.description = description;
	}

	public String toUserString(){
		return ""+getName()+" => "+getDescription();
	}

	@Override
	public String toString(){
		return getName().toString();
	}

	public Lexicon getLexicon() {
		return lexicon;
	}

	public void setLexicon(Lexicon lexicon) {
		this.lexicon = lexicon;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || !(o instanceof Domain))
			return false;
		Domain e = (Domain) o;

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

}
