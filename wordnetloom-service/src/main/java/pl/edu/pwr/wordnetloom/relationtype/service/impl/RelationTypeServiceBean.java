package pl.edu.pwr.wordnetloom.relationtype.service.impl;

import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.relationtype.repository.RelationTypeRepository;
import pl.edu.pwr.wordnetloom.relationtype.service.RelationTypeServiceLocal;
import pl.edu.pwr.wordnetloom.relationtype.service.RelationTypeServiceRemote;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

@Stateless
@Remote(RelationTypeServiceRemote.class)
@Local(RelationTypeServiceLocal.class)
public class RelationTypeServiceBean implements RelationTypeServiceLocal {

    @Inject
    private RelationTypeRepository relationTypeRepository;

    @Override
    public RelationType findById(Long id) {
        return relationTypeRepository.findById(id);
    }

    @Override
    public boolean isReverseRelation(RelationType[] relations, RelationType test) {
        return relationTypeRepository.isReverse(relations, test);
    }

    @Override
    public boolean isReverseRelation(Collection<RelationType> relations, RelationType test) {
        return relationTypeRepository.isReverse(relations, test);
    }

    @Override
    public void deleteAll(RelationType type) {
        relationTypeRepository.deleteRelationWithChilds(type);
    }

    @Override
    public Long findReverseId(RelationType relationType) {
        return relationTypeRepository.findReverseId(relationType);
    }

    @Override
    public RelationType findFullRelationType(RelationType rt) {
        return relationTypeRepository.findFullByRelationType(rt);
    }

    @Override
    public RelationType findReverseByRelationType(RelationType relationType) {
        return relationTypeRepository.findReverseByRelationType(relationType);
    }

    @Override
    public RelationType save(RelationType rel) {
        return relationTypeRepository.persist(rel);
    }

    @Override
    public List<RelationType> findtHighest(List<Long> lexicons) {
        return relationTypeRepository.findHighestLeafs(lexicons);
    }

    @Override
    public List<RelationType> findLeafs(List<Long> lexicons) {
        return relationTypeRepository.findLeafs(lexicons);
    }

    @Override
    public List<RelationType> findFullRelationTypes(List<Long> lexicons) {
        return relationTypeRepository.findFullRelationTypes(lexicons);
    }

    @Override
    public List<RelationType> findChildren(RelationType relation, List<Long> lexicons) {
        return relationTypeRepository.findChildren(relation);
    }

    @Override
    public void delete(RelationType relation) {
        relationTypeRepository.delete(relation);
    }

}
