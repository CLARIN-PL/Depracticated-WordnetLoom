package pl.edu.pwr.wordnetloom.synset.service.impl;

import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.repository.SenseRepository;
import pl.edu.pwr.wordnetloom.sense.service.SenseServiceLocal;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.synset.repository.SynsetAttributesRepository;
import pl.edu.pwr.wordnetloom.synset.repository.SynsetRepository;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceLocal;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceRemote;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Stateless
@Remote(SynsetServiceRemote.class)
@Local(SynsetServiceLocal.class)
public class SynsetServiceBean implements SynsetServiceLocal {

    @Inject
    SynsetRepository synsetRepository;

    @Inject
    SynsetAttributesRepository synsetAttributesRepository;

    @Inject
    SenseServiceLocal senseService;

    @Inject
    Validator validator;

    @Override
    public void clone(Synset synset) {
        //add sense cloning
    }

    @Override
    public boolean delete(Synset synset) {
        synsetRepository.delete(synset);
        return true;
    }

    @Override
    public Synset findSynsetBySense(Sense sense, List<Long> lexicons) {
        return synsetRepository.findSynsetBySense(sense, lexicons);
    }

    public void delete(RelationType relation, List<Long> lexicons) {
        //Removes relation with subrelations
//        Collection<SynsetRelationType> children = findChildren(relation);
//        for (SynsetRelationType item : children) {
//            synsetRelationRepository.delete(relation);
//            delete(item);
//        }
//        delete(relation);
    }

    @Override
    public DataEntry findSynsetDataEntry(Long synsetId, List<Long> lexicons){
        return synsetRepository.findSynsetDataEntry(synsetId, lexicons);
    }

    @Override
    public Synset save(Synset synset){
        ValidationUtils.validateEntityFields(validator, synset);
        if(synset.getId() == null){
            return synsetRepository.persist(synset);
        }
        return synsetRepository.update(synset);
    }

    @Override
    public SynsetAttributes save(SynsetAttributes attributes) {
        return  synsetAttributesRepository.update(attributes);
    }

    @Override
    public Map<Long, DataEntry> prepareCacheForRootNode(Synset synset, List<Long> lexicons, NodeDirection[] directions) {
        return synsetRepository.prepareCacheForRootNode(synset.getId(), lexicons, 4, directions);
    }

    @Override
    public void addSenseToSynset(final Sense sense, final Synset synset){

        List<Sense> sensesInSynset = senseService.findBySynset(synset.getId());
        if(sensesInSynset.contains(sense)){
            return;
        }
        sense.setSynset(synset);
        sense.setSynsetPosition(sensesInSynset.size());
        senseService.save(sense);

    }

    private int reindexSensesInSynset(Synset synset){

        List<Sense> sensesInSynset = senseService.findBySynset(synset.getId());
        int index = 0;
        for(Sense sense : sensesInSynset) {
            sense.setSynsetPosition(index++);
            senseService.save(sense);
        }
        return index;
    }

    @Override
    public void deleteSensesFromSynset(Collection<Sense> senses, Synset synset){
        for(Sense sense : senses){
            sense.setSynset(null);
            sense.setSynsetPosition(null);
            senseService.save(sense);
        }
        reindexSensesInSynset(synset);
    }

    @Override
    public List<Synset> findSynsetsByCriteria(SynsetCriteriaDTO criteria) {
        return synsetRepository.findSynsetsByCriteria(criteria);
    }

    @Override
    public int getCountSynsetsByCriteria(SynsetCriteriaDTO criteria) {
        return synsetRepository.getCountSynsetsByCriteria(criteria);
    }

    @Override
    public Synset fetchSynset(Long synsetId){
        return synsetRepository.fetchSynset(synsetId);
    }

    @Override
    public SynsetAttributes fetchSynsetAttributes(Long synsetId) {
        return synsetRepository.fetchSynsetAttributes(synsetId);
    }
}
