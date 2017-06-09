package pl.edu.pwr.wordnetloom.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.Word;
import pl.edu.pwr.wordnetloom.model.wordnet.WordForm;

@Remote
public interface WordFormServiceRemote {

    void deleteAllForms();

    WordForm getFormFor(Word word, List<String> tags);

    List<Sense> getUnitsWithoutForm();

}
