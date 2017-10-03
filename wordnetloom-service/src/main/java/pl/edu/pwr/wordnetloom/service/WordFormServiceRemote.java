package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.WordForm;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Word;

@Remote
public interface WordFormServiceRemote {
	
	public void deleteAllForms();
	public WordForm getFormFor(Word word, List<String> tags);
	public List<Sense> getUnitsWithoutForm();
	
}
