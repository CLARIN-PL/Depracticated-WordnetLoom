package pl.edu.pwr.wordnetloom.model.yiddish;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.InflectionDictionary;

@Entity
@Table(name = "yiddish_inflection")
public class Inflection implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prefix_id",referencedColumnName="id")
	private InflectionDictionary inflectionDictionary;
	
	private String text;
	
	public Inflection() {
	}
	
	public Inflection(Inflection inf) {
		this.inflectionDictionary = inf.inflectionDictionary;
		this.text = inf.text;
	}

	public Inflection(InflectionDictionary dic, String text) {
		this.inflectionDictionary = dic;
		this.text = text;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public InflectionDictionary getInflectionDictionary() {
		return inflectionDictionary;
	}
	public void setInflectionDictionary(InflectionDictionary inflectionDictionary) {
		this.inflectionDictionary = inflectionDictionary;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString(){
		return inflectionDictionary.getName() + " " + text;
	}
}
