package pl.edu.pwr.wordnetloom.word.service.impl;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.word.exception.WordNotFoundException;
import pl.edu.pwr.wordnetloom.word.model.Word;
import pl.edu.pwr.wordnetloom.word.repository.WordRepository;
import pl.edu.pwr.wordnetloom.word.service.WordServiceLocal;
import pl.edu.pwr.wordnetloom.word.service.WordServiceRemote;

@Stateless
@Remote(WordServiceRemote.class)
@Local(WordServiceLocal.class)
public class WordServiceBean implements WordServiceLocal {

    @Inject
    WordRepository wordRepository;

    @Inject
    Validator validator;

    @Override
    public Word add(Word word) {
        ValidationUtils.validateEntityFields(validator, word);
        if (wordRepository.alreadyExists("word", word.getWord(), null)) {
            return findByWord(word.getWord());
        }
        return wordRepository.persist(word);
    }

    @Override
    public Word findByWord(String word) {
        Word w = wordRepository.findByWord(word);
        if (w == null) {
            throw new WordNotFoundException();
        }
        return w;
    }

    @Override
    public Word findById(Long id) {
        Word w = wordRepository.findById(id);
        if (w == null) {
            throw new WordNotFoundException();
        }
        return w;
    }

    @Override
    public Integer countByWord(String word) {
        return wordRepository.countByWord(word);
    }

    @Override
    public List<Word> findAll() {
        return wordRepository.findAll("word");
    }
}
