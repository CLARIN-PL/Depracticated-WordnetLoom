package pl.edu.pwr.wordnetloom.partofspeech.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.exception.PartOfSpeechNotFoundException;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.partofspeech.repository.PartOfSpeechRepository;
import pl.edu.pwr.wordnetloom.partofspeech.service.PartOfSpeechServiceLocal;
import pl.edu.pwr.wordnetloom.partofspeech.service.PartOfSpeechServiceRemote;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
@SecurityDomain("wordnetloom")
@DeclareRoles({"USER", "ADMIN"})
@Remote(PartOfSpeechServiceRemote.class)
@Local(PartOfSpeechServiceLocal.class)
public class PartOfSpeechServiceBean implements PartOfSpeechServiceLocal {

    @Inject
    PartOfSpeechRepository partOfSpeechRepository;

    @Inject
    Validator validator;

    @PermitAll
    @Override
    public PartOfSpeech findById(Long id) {
        PartOfSpeech pos = partOfSpeechRepository.findById(id);
        if (pos == null) {
            throw new PartOfSpeechNotFoundException();
        }
        return pos;
    }

    @PermitAll
    @Override
    public List<PartOfSpeech> findByLexicon(Lexicon lexicon) {
        return partOfSpeechRepository.findByLexicon(lexicon);
    }

    @PermitAll
    @Override
    public List<PartOfSpeech> findAll() {
        return partOfSpeechRepository.findAll("id");
    }

    @RolesAllowed("ADMIN")
    @Override
    public PartOfSpeech add(PartOfSpeech pos) {
        ValidationUtils.validateEntityFields(validator, pos);
        return partOfSpeechRepository.persist(pos);
    }

}
