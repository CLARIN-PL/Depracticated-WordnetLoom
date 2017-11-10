package pl.edu.pwr.wordnetloom.domain.service.impl;

import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.domain.exception.DomainNotFoundException;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.domain.repository.DomainRepository;
import pl.edu.pwr.wordnetloom.domain.service.DomainServiceLocal;
import pl.edu.pwr.wordnetloom.domain.service.DomainServiceRemote;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
@Local(DomainServiceLocal.class)
@Remote(DomainServiceRemote.class)
public class DomainServiceBean implements DomainServiceLocal {

    @Inject
    DomainRepository domainRepository;

    @Inject
    Validator validator;

    @Override
    public Domain findById(Long id) {
        Domain domain = domainRepository.findById(id);
        if (domain == null) {
            throw new DomainNotFoundException();
        }
        return domain;
    }

    @Override
    public List<Domain> findAllByLexiconAndPartOfSpeech(Long lexiconId, Long partOfSpeechId) {
        return domainRepository.findByLexiconAndPartOfSpeech(lexiconId, partOfSpeechId);
    }

    @Override
    public List<Domain> findAll() {
        return domainRepository.findAll("id");
    }

    @Override
    public Domain add(Domain domain) {
        ValidationUtils.validateEntityFields(validator, domain);
        return domainRepository.persist(domain);
    }


}
