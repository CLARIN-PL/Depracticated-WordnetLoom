package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "corpus_example")
@NamedQueries({ @NamedQuery(name = "CorpusExample.getCorpusExamplesFor", query = "SELECT c FROM CorpusExample c WHERE c.word.word = :word"), })
public class CorpusExample implements Serializable {

	private static final long serialVersionUID = -86878893575269138L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	@Column(columnDefinition = "text")
	private String text;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "word", referencedColumnName = "id", nullable = false)
	private Word word;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof CorpusExample))
			return false;
		CorpusExample e = (CorpusExample) o;

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
