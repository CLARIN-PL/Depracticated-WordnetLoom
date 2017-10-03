package pl.edu.pwr.wordnetloom.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.model.Text;
import pl.edu.pwr.wordnetloom.model.WordForm;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Word;

@Stateless
public class WordFormDaoBean implements WordFormDaoLocal {

	@EJB private LexicalUnitDAOLocal lexUnit;
	@EJB private DAOLocal local;
	
	@Override
	public void deleteAllForms() {
		local.getEM().createNamedQuery("WordForm.deleteAllForms", WordForm.class)
		.executeUpdate();
	}

	@Override
	public WordForm getFormFor(Word word, List<Text> tags) {
		ArrayList<String> tagS = new ArrayList<String>();
		for (Text text : tags) {
			tagS.add(text.getText());
		}
		
		List<WordForm> list = local.getEM().createNamedQuery("WordForm.getFormFor", WordForm.class)
				.setParameter("word", word.getWord())
				.setParameter("tags", tagS)
				.getResultList();
		if(list.isEmpty() || list.get(0)==null)
			return null;
		return list.get(0);
	}

	@Override
	public List<Sense> getUnitsWithoutForm() {
		return lexUnit.dbGetUnitsWithoutForms();
	}

}
