package pl.edu.pwr.wordnetloom.wordform.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.word.model.Word;
import pl.edu.pwr.wordnetloom.wordform.model.WordForm;

@Remote
public interface WordFormServiceRemote {

    void deleteAll();

    WordForm findByWordAndTags(Word word, List<String> tags);

    List<Sense> findSensesWithoutForm();

}
