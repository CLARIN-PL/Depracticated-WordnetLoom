package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "word_form")
@NamedQueries({
		@NamedQuery(name = "WordForm.deleteAllForms", query = "DELETE FROM WordForm w"),
		@NamedQuery(name = "WordForm.getFormFor", query = "SELECT w FROM WordForm w WHERE w.word LIKE :word OR w.form IN (:tags)"), })
public class WordForm implements Serializable {

	private static final long serialVersionUID = 1984202638795803311L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// dawny baseform
	private String word;

	// dawny definition
	private String tag;

	// dawny finalform
	private String form;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof WordForm))
			return false;
		WordForm e = (WordForm) o;

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
}
