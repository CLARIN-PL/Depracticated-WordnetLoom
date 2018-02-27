package pl.edu.pwr.wordnetloom.domain.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.domain.exception.DomainNotFoundException;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.domain.repository.DomainRepository;
import pl.edu.pwr.wordnetloom.domain.service.DomainServiceLocal;
import pl.edu.pwr.wordnetloom.domain.service.DomainServiceRemote;

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
@Local(DomainServiceLocal.class)
@Remote(DomainServiceRemote.class)
public class DomainServiceBean implements DomainServiceLocal {

    @Inject
    DomainRepository domainRepository;

    @Inject
    Validator validator;

    @PermitAll
    @Override
    public Domain findById(Long id) {
        Domain domain = domainRepository.findById(id);
        if (domain == null) {
            throw new DomainNotFoundException();
        }
        return domain;
    }

    @PermitAll
    @Override
    public List<Domain> findAllByLexiconAndPartOfSpeech(Long lexiconId, Long partOfSpeechId) {
        return domainRepository.findByLexiconAndPartOfSpeech(lexiconId, partOfSpeechId);
    }

    @PermitAll
    @Override
    public List<Domain> findAll() {
        return domainRepository.findAll("id");
    }

    @RolesAllowed("ADMIN")
    @Override
    public Domain add(Domain domain) {
        ValidationUtils.validateEntityFields(validator, domain);
        return domainRepository.persist(domain);
    }


}
