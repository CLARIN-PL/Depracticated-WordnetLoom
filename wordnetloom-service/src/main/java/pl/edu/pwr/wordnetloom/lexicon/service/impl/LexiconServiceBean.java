package pl.edu.pwr.wordnetloom.lexicon.service.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.lexicon.exception.LexiconNotFoundException;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.lexicon.repository.LexiconRepository;
import pl.edu.pwr.wordnetloom.lexicon.service.LexiconServiceLocal;

@Stateless
public class LexiconServiceBean implements LexiconServiceLocal {

    @Inject
    LexiconRepository lexiconRepository;

    @Inject
    Validator validator;

    @Override
    public Lexicon findById(Long id) {
        final Lexicon lexicon = lexiconRepository.findById(id);
        if (lexicon == null) {
            throw new LexiconNotFoundException();
        }
        return lexicon;
    }

    @Override
    public List<Lexicon> findAllByLexicon(List<Long> lexiconIds) {
        return lexiconRepository.findByLexicons(lexiconIds);
    }

    @Override
    public List<Lexicon> findAll() {
        return lexiconRepository.findAll("name");
    }

    @Override
    public Lexicon add(final Lexicon lexicon) {
        ValidationUtils.validateEntityFields(validator, lexicon);
        return lexiconRepository.save(lexicon);
    }

    @Override
    public List<Long> findAllLexiconIds() {
        return lexiconRepository.findAllLexiconIds();
    }

}
