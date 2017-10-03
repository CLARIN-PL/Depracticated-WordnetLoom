package pl.edu.pwr.wordnetloom.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "text_translation")
public class TextTranslation implements Serializable {

	private static final long serialVersionUID = -3763691209798632315L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_text", referencedColumnName = "id", nullable = false)
	private Text text;

	@Column(nullable = false)
	private String translation;

	@Column(name = "language_code", nullable = false)
	private String languageCode = "PL";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof TextTranslation))
			return false;
		TextTranslation e = (TextTranslation) o;

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
