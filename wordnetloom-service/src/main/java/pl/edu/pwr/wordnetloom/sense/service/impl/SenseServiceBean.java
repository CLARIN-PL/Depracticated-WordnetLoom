package pl.edu.pwr.wordnetloom.sense.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.model.SenseAttributes;
import pl.edu.pwr.wordnetloom.sense.repository.SenseAttributesRepository;
import pl.edu.pwr.wordnetloom.sense.repository.SenseRepository;
import pl.edu.pwr.wordnetloom.sense.service.SenseServiceLocal;
import pl.edu.pwr.wordnetloom.sense.service.SenseServiceRemote;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

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
@Remote(SenseServiceRemote.class)
@Local(SenseServiceLocal.class)
public class SenseServiceBean implements SenseServiceLocal {

    @Inject
    SenseRepository senseRepository;

    @Inject
    SenseAttributesRepository senseAttributesRepository;

    @Inject
    Validator validator;

    @Override
    public Sense clone(Sense unit) {
        return senseRepository.clone(unit);
    }

    @PermitAll
    @Override
    public Sense findById(Long id) {
        return senseRepository.findById(id);
    }


    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public Sense save(Sense sense) {
        ValidationUtils.validateEntityFields(validator, sense);
        if (sense.getId() != null) {
            return senseRepository.update(sense);
        }
        sense.setVariant(senseRepository.findNextVariant(sense.getWord().getWord(), sense.getPartOfSpeech()));
        return senseRepository.persist(sense);
    }


    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public SenseAttributes addSenseAttribute(Long senseId, SenseAttributes attributes) {
        ValidationUtils.validateEntityFields(validator, attributes);

        if (attributes.getId() != null) {
            return senseAttributesRepository.update(attributes);
        }

        Sense s = findById(senseId);
        attributes.setSense(s);
        return senseAttributesRepository.persist(attributes);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public void deleteAll() {
        senseRepository.deleteAll();
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public int delete(List<Sense> list) {
        return senseRepository.delete(list);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public void delete(Sense sense) {
        senseRepository.delete(sense);
    }

    @PermitAll
    @Override
    public List<Sense> findByCriteria(SenseCriteriaDTO dto) {
        return senseRepository.findByCriteria(dto);
    }

    @PermitAll
    @Override
    public int getCountUnitsByCriteria(SenseCriteriaDTO dto) {
        return senseRepository.getCountUnitsByCriteria(dto);
    }

    @PermitAll
    @Override
    public List<Sense> filterSenseByLexicon(List<Sense> senses, List<Long> lexicons) {
        return senseRepository.filterSenseByLexicon(senses, lexicons);
    }

    @PermitAll
    @Override
    public List<Sense> findByLikeLemma(String lemma, List<Long> lexicons) {
        return senseRepository.findByLikeLemma(lemma, lexicons);
    }

    @PermitAll
    @Override
    public Integer findCountByLikeLemma(String filter) {
        return senseRepository.findCountByLikeLemma(filter);
    }

    @PermitAll
    @Override
    public List<Sense> findBySynset(Synset synset, List<Long> lexicons) {
        return senseRepository.findBySynset(synset, lexicons);
    }

    @PermitAll
    @Override
    public List<Sense> findBySynset(Long synsetId) {
        return senseRepository.findBySynset(synsetId);
    }

    @PermitAll
    @Override
    public int findCountBySynset(Synset synset, List<Long> lexicons) {
        return senseRepository.findCountBySynset(synset, lexicons);
    }

    @PermitAll
    @Override
    public int findNextVariant(String lemma, PartOfSpeech pos) {
        return senseRepository.findNextVariant(lemma, pos);
    }

    @PermitAll
    @Override
    public int findInSynsetsCount() {
        return senseRepository.findInSynsetsCount();
    }

    @PermitAll
    @Override
    public Integer countAll() {
        return senseRepository.countAll();
    }

    @PermitAll
    @Override
    public int findHighestVariant(String word, Lexicon lexicon) {
        return senseRepository.findHighestVariant(word, lexicon);
    }

    @PermitAll
    @Override
    public List<Sense> findNotInAnySynset(String filter, PartOfSpeech pos) {
        return senseRepository.findNotInAnySynset(filter, pos);
    }

    @PermitAll
    @Override
    public boolean checkIsInSynset(Sense unit) {
        return senseRepository.checkIsInSynset(unit);
    }

    @PermitAll
    @Override
    public List<Long> getAllLemmasForLexicon(List<Long> lexicon) {
        return senseRepository.getAllLemmasForLexicon(lexicon);
    }

    @PermitAll
    @Override
    public List<Sense> findSensesByWordId(Long id, Long lexicon) {
        return senseRepository.findSensesByWordId(id, lexicon);
    }


    @PermitAll
    @Override
    public Sense findHeadSenseOfSynset(Long synsetId) {
        return senseRepository.findHeadSenseOfSynset(synsetId);
    }

    @PermitAll
    @Override
    public Sense fetchSense(Long senseId) {
        return senseRepository.fetchSense(senseId);
    }

    @PermitAll
    @Override
    public SenseAttributes fetchSenseAttribute(Long senseId) {
        return senseRepository.fetchSenseAttribute(senseId);
    }
}
