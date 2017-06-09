package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;
import pl.edu.pwr.wordnetloom.model.wordnet.Word;
import pl.edu.pwr.wordnetloom.model.wordnet.WordForm;

@Local
public interface WordFormDaoLocal {

    void deleteAllForms();

    WordForm getFormFor(Word word, List<Text> tags);

    List<Sense> getUnitsWithoutForm();

}
