package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "relation_argument")
public class RelationArgument implements Serializable {

	public static final RelationArgument LEXICAL = new RelationArgument(0L,"relacja leksykalna");
	public static final RelationArgument SYNSET = new RelationArgument(1L,"relacja pomiędzy synsetami");
	public static final RelationArgument LEXICAL_SPECIAL = new RelationArgument(2L, "relacja synonimii");

	private static final long serialVersionUID = 9122691733246478907L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "name", referencedColumnName = "id", nullable = false)
	private Text name;

	public RelationArgument() {
	}

	public RelationArgument(long i, String s) {
		setId(i);
		setName(new Text(s));
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

	@Override
	public String toString() {
		return name.getText();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof RelationArgument))
			return false;
		RelationArgument e = (RelationArgument) o;

		if (id == null)
			return false;
		return id.equals(e.getId());
	}

	@Override
	public int hashCode() {
		int hashCode = (id.hashCode());
		if (hashCode == 0)
			return super.hashCode();
		return hashCode;
	}

	public static RelationArgument[] values() {
		return new RelationArgument[] { LEXICAL, LEXICAL_SPECIAL, SYNSET };
	}
}
