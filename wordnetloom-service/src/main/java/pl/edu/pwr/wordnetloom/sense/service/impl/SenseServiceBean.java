package pl.edu.pwr.wordnetloom.sense.service.impl;

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

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
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

    @Override
    public Sense findById(Long id) {
        return senseRepository.findById(id);
    }

    @Override
    public Sense save(final Sense sense) {
        ValidationUtils.validateEntityFields(validator, sense);
        if (sense.getId() != null) {
            return senseRepository.update(sense);
        }
        sense.setVariant(senseRepository.findNextVariant(sense.getWord().getWord(), sense.getPartOfSpeech()));
        return senseRepository.persist(sense);
    }

    @Override
    public SenseAttributes addSenseAttribute(final Long senseId, final SenseAttributes attributes) {
        ValidationUtils.validateEntityFields(validator, attributes);

        if (attributes.getId() != null) {
            return senseAttributesRepository.update(attributes);
        }
        Sense s = findById(senseId);
        attributes.setSense(s);
        return senseAttributesRepository.persist(attributes);
    }

    @Override
    public void deleteAll() {
        senseRepository.deleteAll();
    }

    @Override
    public List<Sense> findByCriteria(SenseCriteriaDTO dto) {
        return senseRepository.findByCriteria(dto);
    }

    @Override
    public int getCountUnitsByCriteria(SenseCriteriaDTO dto) {
        return senseRepository.getCountUnitsByCriteria(dto);
    }

    @Override
    public List<Sense> filterSenseByLexicon(List<Sense> senses, List<Long> lexicons) {
        return senseRepository.filterSenseByLexicon(senses, lexicons);
    }

    @Override
    public List<Sense> findByLikeLemma(String lemma, List<Long> lexicons) {
        return senseRepository.findByLikeLemma(lemma, lexicons);
    }

    @Override
    public Integer findCountByLikeLemma(String filter) {
        return senseRepository.findCountByLikeLemma(filter);
    }

    @Override
    public List<Sense> findBySynset(Synset synset, List<Long> lexicons) {
        return senseRepository.findBySynset(synset, lexicons);
    }

    @Override
    public List<Sense> findBySynset(Long synsetId) {
        return senseRepository.findBySynset(synsetId);
    }

    @Override
    public int findCountBySynset(Synset synset, List<Long> lexicons) {
        return senseRepository.findCountBySynset(synset, lexicons);
    }

    @Override
    public int findNextVariant(String lemma, PartOfSpeech pos) {
        return senseRepository.findNextVariant(lemma, pos);
    }

    @Override
    public int delete(List<Sense> list) {
        return senseRepository.delete(list);
    }

    @Override
    public int findInSynsetsCount() {
        return senseRepository.findInSynsetsCount();
    }

    @Override
    public Integer countAll() {
        return senseRepository.countAll();
    }

    @Override
    public int findHighestVariant(String word, Lexicon lexicon) {
        return senseRepository.findHighestVariant(word, lexicon);
    }

    @Override
    public List<Sense> findNotInAnySynset(String filter, PartOfSpeech pos) {
        return senseRepository.findNotInAnySynset(filter, pos);
    }

    @Override
    public boolean checkIsInSynset(Sense unit) {
        return senseRepository.checkIsInSynset(unit);
    }

    @Override
    public List<Long> getAllLemmasForLexicon(List<Long> lexicon) {
        return senseRepository.getAllLemmasForLexicon(lexicon);
    }

    @Override
    public List<Sense> findSensesByWordId(Long id, Long lexicon) {
        return senseRepository.findSensesByWordId(id, lexicon);
    }

    @Override
    public void delete(Sense sense) {
        senseRepository.delete(sense);
    }

    @Override
    public Sense findHeadSenseOfSynset(Long synsetId) {
        return senseRepository.findHeadSenseOfSynset(synsetId);
    }

    @Override
    public Sense fetchSense(Long senseId) {
        return senseRepository.fetchSense(senseId);
    }

    @Override
    public SenseAttributes fetchSenseAttribute(Long senseId) {
        return senseRepository.fetchSenseAttribute(senseId);
    }
}
