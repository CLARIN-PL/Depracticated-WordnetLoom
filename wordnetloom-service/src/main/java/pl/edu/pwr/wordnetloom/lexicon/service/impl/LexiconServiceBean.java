package pl.edu.pwr.wordnetloom.lexicon.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.lexicon.exception.LexiconNotFoundException;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.lexicon.repository.LexiconRepository;
import pl.edu.pwr.wordnetloom.lexicon.service.LexiconServiceLocal;
import pl.edu.pwr.wordnetloom.lexicon.service.LexiconServiceRemote;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
@SecurityDomain("wordnetloom")
@DeclareRoles({"USER", "ADMIN"})
@Remote(LexiconServiceRemote.class)
@Local(LexiconServiceLocal.class)
public class LexiconServiceBean implements LexiconServiceLocal {

    @Inject
    LexiconRepository lexiconRepository;

    @Inject
    Validator validator;

    @Override
    public Lexicon findById(Long id) {
        Lexicon lexicon = lexiconRepository.findById(id);
        if (lexicon == null) {
            throw new LexiconNotFoundException();
        }
        return lexicon;
    }

    @PermitAll
    @Override
    public List<Lexicon> findAll() {
        return lexiconRepository.findAll("name");
    }


    @RolesAllowed("ADMIN")
    @Override
    public Lexicon add(Lexicon lexicon) {
        ValidationUtils.validateEntityFields(validator, lexicon);
        return lexiconRepository.persist(lexicon);
    }

    @PermitAll
    @Override
    public List<Long> findAllLexiconIds() {
        return lexiconRepository.findAllLexiconIds();
    }

}
