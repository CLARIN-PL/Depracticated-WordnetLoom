package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "word")
@NamedQueries({
	@NamedQuery(name = "Word.countWordByLemma", 
		query = "select count(w.word) from Word w where w.word =:lemma"),
		@NamedQuery(name = "Word.getWordByLemma", 
		query = "select w from Word w where w.word =:lemma"),
})
public class Word implements Serializable {

	private static final long serialVersionUID = -1256292370070216845L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String word;

	public Word(){}

	public Word(String lemma){
		setWord(lemma);
	}

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

	@Override
	public String toString(){
		return this.word;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || !(o instanceof Word))
			return false;
		Word e = (Word) o;
		
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
