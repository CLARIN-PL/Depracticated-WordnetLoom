package pl.edu.pwr.wordnetloom.synset.service.impl;

import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.repository.SynsetRepository;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceLocal;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceRemote;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Stateless
@Remote(SynsetServiceRemote.class)
@Local(SynsetServiceLocal.class)
public class SynsetServiceBean implements SynsetServiceLocal {

    @Inject
    SynsetRepository synsetRepository;

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
        throw new NotImplementedException();
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
    public Synset updateSynset(Synset synset){
        return synsetRepository.updateSynset(synset);
    }

    @Override
    public Map<Long, DataEntry> prepareCacheForRootNode(Synset synset, List<Long> lexicons, NodeDirection[] directions) {
        return synsetRepository.prepareCacheForRootNode(synset.getId(), lexicons, 4, directions);
    }

    @Override
    public void addSenseToSynset(Sense sense, Synset synset){
        synsetRepository.addSenseToSynset(sense, synset);
    }

    @Override
    public void deleteSensesFromSynset(Collection<Sense> senses, Synset synset){
        synsetRepository.deleteSensesFromSynset(senses, synset);
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
}
