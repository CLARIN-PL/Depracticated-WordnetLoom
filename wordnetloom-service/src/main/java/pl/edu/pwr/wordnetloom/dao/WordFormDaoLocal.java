package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;
import pl.edu.pwr.wordnetloom.word.model.Word;
import pl.edu.pwr.wordnetloom.wordform.model.WordForm;

@Local
public interface WordFormDaoLocal {

    void deleteAllForms();

    WordForm getFormFor(Word word, List<Text> tags);

    List<Sense> getUnitsWithoutForm();

}
