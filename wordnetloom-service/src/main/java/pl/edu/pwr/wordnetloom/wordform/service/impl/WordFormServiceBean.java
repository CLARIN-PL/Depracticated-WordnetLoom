package pl.edu.pwr.wordnetloom.wordform.service.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.word.model.Word;
import pl.edu.pwr.wordnetloom.wordform.model.WordForm;
import pl.edu.pwr.wordnetloom.wordform.repository.WordFormRepository;
import pl.edu.pwr.wordnetloom.wordform.service.WordFormServiceLocal;

@Stateless
public class WordFormServiceBean implements WordFormServiceLocal {

    @Inject
    private WordFormRepository wordFormRepository;

    @Override
    public void deleteAll() {
        wordFormRepository.deleteAll();
    }

    @Override
    public WordForm findByWordAndTags(Word word, List<String> tags) {
        return wordFormRepository.findByWordAndTags(word, tags);
    }

    @Override
    public List<Sense> findSensesWithoutForm() {
        return wordFormRepository.findSensesWithoutForm();
    }

}
