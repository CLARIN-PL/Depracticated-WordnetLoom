package pl.edu.pwr.wordnetloom.dao;

import java.util.List;

import javax.ejb.Local;

import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Text;
import pl.edu.pwr.wordnetloom.model.Word;
import pl.edu.pwr.wordnetloom.model.WordForm;

@Local
public interface WordFormDaoLocal {
	
	void deleteAllForms();
	WordForm getFormFor(Word word, List<Text> tags);
	List<Sense> getUnitsWithoutForm();
	
}
