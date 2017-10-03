package pl.edu.pwr.wordnetloom.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.dao.WordFormDaoLocal;
import pl.edu.pwr.wordnetloom.model.Text;
import pl.edu.pwr.wordnetloom.model.Word;
import pl.edu.pwr.wordnetloom.model.WordForm;
import pl.edu.pwr.wordnetloom.model.Sense;

@Stateless
public class WordFormServiceBean implements WordFormServiceRemote {

	@EJB private WordFormDaoLocal wordForm;
	
	@Override
	public void deleteAllForms() {
		wordForm.deleteAllForms();
	}

	@Override
	public WordForm getFormFor(Word word, List<String> tags) {
		List<Text> texts = new ArrayList<Text>();
		for(String t : tags)
			texts.add(new Text(t));
		
		return wordForm.getFormFor(word, texts);
	}

	@Override
	public List<Sense> getUnitsWithoutForm() {
		return wordForm.getUnitsWithoutForm();
	}

}
