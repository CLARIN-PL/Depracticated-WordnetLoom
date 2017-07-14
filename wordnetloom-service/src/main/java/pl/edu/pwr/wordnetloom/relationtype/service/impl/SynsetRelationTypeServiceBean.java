package pl.edu.pwr.wordnetloom.relationtype.service.impl;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.relationtype.model.SynsetRelationType;
import pl.edu.pwr.wordnetloom.relationtype.repository.SynsetRelationTypeRepository;
import pl.edu.pwr.wordnetloom.relationtype.service.SynsetRelationTypeServiceLocal;
import pl.edu.pwr.wordnetloom.relationtype.service.SynsetRelationTypeServiceRemote;

@Stateless
@Remote(SynsetRelationTypeServiceRemote.class)
@Local(SynsetRelationTypeServiceLocal.class)
public class SynsetRelationTypeServiceBean implements SynsetRelationTypeServiceLocal {

    @Inject
    private SynsetRelationTypeRepository relationTypeRepository;

    @Override
    public SynsetRelationType findById(Long id) {
        return relationTypeRepository.findById(id);
    }

    @Override
    public boolean isReverseRelation(SynsetRelationType[] relations, SynsetRelationType relationType) {
        return relationTypeRepository.isReverse(relations, relationType);
    }

    @Override
    public boolean isReverseRelation(Collection<SynsetRelationType> relations, SynsetRelationType test) {
        return relationTypeRepository.isReverse(relations, test);
    }

    @Override
    public void deleteAll() {
        relationTypeRepository.deleteAll();
    }

    @Override
    public Long findReverseId(SynsetRelationType relationType) {
        return relationTypeRepository.findReverseId(relationType);
    }

    @Override
    public SynsetRelationType findFullRelationType(SynsetRelationType rt) {
        return relationTypeRepository.findFullByRelationType(rt);
    }

    @Override
    public SynsetRelationType findReverseByRelationType(SynsetRelationType relationType) {
        return relationTypeRepository.findReverseByRelationType(relationType);
    }

    @Override
    public SynsetRelationType save(SynsetRelationType rel) {
        return relationTypeRepository.persist(rel);
    }

    @Override
    public List<SynsetRelationType> findtHighest(List<Long> lexicons) {
        return relationTypeRepository.findHighestLeafs(lexicons);
    }

    @Override
    public List<SynsetRelationType> findLeafs(List<Long> lexicons) {
        return relationTypeRepository.findLeafs(lexicons);
    }

    @Override
    public List<SynsetRelationType> findFullRelationTypes(List<Long> lexicons) {
        return relationTypeRepository.findFullRelationTypes(lexicons);
    }

    @Override
    public List<SynsetRelationType> findChildren(SynsetRelationType relation, List<Long> lexicons) {
        return relationTypeRepository.findChildren(relation);
    }

    @Override
    public void delete(SynsetRelationType relation) {
        relationTypeRepository.delete(relation);
    }

}
