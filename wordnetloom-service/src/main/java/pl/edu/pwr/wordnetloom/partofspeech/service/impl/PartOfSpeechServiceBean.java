package pl.edu.pwr.wordnetloom.partofspeech.service.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.exception.PartOfSpeechNotFoundException;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.partofspeech.repository.PartOfSpeechRepository;
import pl.edu.pwr.wordnetloom.partofspeech.service.PartOfSpeechServiceLocal;

@Stateless
public class PartOfSpeechServiceBean implements PartOfSpeechServiceLocal {

    @Inject
    PartOfSpeechRepository partOfSpeechRepository;

    @Inject
    Validator validator;

    @Override
    public PartOfSpeech findById(Long id) {
        final PartOfSpeech pos = partOfSpeechRepository.findById(id);
        if (pos == null) {
            throw new PartOfSpeechNotFoundException();
        }
        return pos;
    }

    @Override
    public List<PartOfSpeech> findByLexicon(Lexicon lexicon) {
        return partOfSpeechRepository.findByLexicon(lexicon);
    }

    @Override
    public List<PartOfSpeech> findAll() {
        return partOfSpeechRepository.findAll("id");
    }

    @Override
    public PartOfSpeech add(final PartOfSpeech pos) {
        ValidationUtils.validateEntityFields(validator, pos);
        return partOfSpeechRepository.persist(pos);
    }

}
