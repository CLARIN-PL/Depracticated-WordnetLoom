package pl.edu.pwr.wordnetloom.word.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.word.exception.WordNotFoundException;
import pl.edu.pwr.wordnetloom.word.model.Word;
import pl.edu.pwr.wordnetloom.word.repository.WordRepository;
import pl.edu.pwr.wordnetloom.word.service.WordServiceLocal;
import pl.edu.pwr.wordnetloom.word.service.WordServiceRemote;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.Validator;
import java.util.List;

@Stateless
@SecurityDomain("wordnetloom")
@DeclareRoles({"USER", "ADMIN"})
@Remote(WordServiceRemote.class)
@Local(WordServiceLocal.class)
public class WordServiceBean implements WordServiceLocal {

    @Inject
    WordRepository wordRepository;

    @Inject
    Validator validator;

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public Word add(Word word) {
        ValidationUtils.validateEntityFields(validator, word);
        if (wordRepository.alreadyExists("word", word.getWord(), null)) {
            return findByWord(word.getWord());
        }
        return wordRepository.persist(word);
    }

    @PermitAll
    @Override
    public Word findByWord(String word) {
        Word w = wordRepository.findByWord(word);
        if(w == null){
            throw new WordNotFoundException();
        }
        return w;
    }

    @PermitAll
    @Override
    public Word findById(Long id) {
        Word w = wordRepository.findById(id);
        if(w == null){
            throw new WordNotFoundException();
        }
        return w;
    }

    @PermitAll
    @Override
    public Integer countByWord(String word) {
        return wordRepository.countByWord(word);
    }

    @PermitAll
    @Override
    public List<Word> findAll() {
        return wordRepository.findAll("word");
    }
}
