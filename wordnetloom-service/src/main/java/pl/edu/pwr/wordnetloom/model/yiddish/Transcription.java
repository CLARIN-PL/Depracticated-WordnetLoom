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

import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.TranscriptionDictionary;

@Entity
@Table(name = "yiddish_transcriptions")
public class Transcription implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    protected Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transcription_id", referencedColumnName="id")
	private TranscriptionDictionary transcriptionDictionary;

	private String phonography;

	public Transcription() {
	}
	
	public Transcription(Transcription trans) {
		this.transcriptionDictionary = trans.transcriptionDictionary;
		this.phonography = trans.phonography;
	}

	public Transcription(TranscriptionDictionary dic , String phon) {
		this.transcriptionDictionary = dic;
		this.phonography = phon;
	}

	@Override
	public String toString(){
		return phonography + " ( "+transcriptionDictionary.getName()+" )";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TranscriptionDictionary getTranscriptionDictionary() {
		return transcriptionDictionary;
	}

	public void setTranscriptionDictionary(TranscriptionDictionary transcriptionDictionary) {
		this.transcriptionDictionary = transcriptionDictionary;
	}

	public String getPhonography() {
		return phonography;
	}

	public void setPhonography(String phonography) {
		this.phonography = phonography;
	}
	
}
