package pl.edu.pwr.wordnetloom.relationtype.service.impl;

import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.relationtype.exception.RelationTypeNotFoundException;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.relationtype.repository.RelationTypeRepository;
import pl.edu.pwr.wordnetloom.relationtype.service.RelationTypeServiceLocal;
import pl.edu.pwr.wordnetloom.relationtype.service.RelationTypeServiceRemote;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.Collection;
import java.util.List;

@Stateless
@Remote(RelationTypeServiceRemote.class)
@Local(RelationTypeServiceLocal.class)
public class RelationTypeServiceBean implements RelationTypeServiceLocal {

    @Inject
    RelationTypeRepository relationTypeRepository;

    @Inject
    Validator validator;


    @Override
    public RelationType findById(Long id) {
        RelationType rel = relationTypeRepository.findById(id);
        if (rel == null) {
            throw new RelationTypeNotFoundException();
        }
        return rel;
    }

    @Override
    public boolean isReverseRelation(Collection<RelationType> relations, RelationType test) {
        return relationTypeRepository.isReverse(relations, test);
    }

    @Override
    public RelationType findByIdWithDependencies(Long id) {
        RelationType rel = relationTypeRepository.findByIdWithDependencies(id);
        if (rel == null) {
            throw new RelationTypeNotFoundException();
        }
        return rel;
    }

    @Override
    public void deleteAll(RelationType type) {

        relationTypeRepository.deleteRelationWithChilds(type);
    }

    @Override
    public Long findReverseId(Long relationTypeId) {
        return relationTypeRepository.findReverseId(relationTypeId);
    }


    @Override
    public RelationType findReverseByRelationType(Long relationTypeId) {
        return relationTypeRepository.findReverseByRelationType(relationTypeId);
    }

    @Override
    public RelationType save(RelationType rel) {
        ValidationUtils.validateEntityFields(validator, rel);
        return relationTypeRepository.persist(rel);
    }

    @Override
    public List<RelationType> findHighest(RelationArgument arg) {
        return relationTypeRepository.findHighestLeafs(arg);
    }

    @Override
    public List<RelationType> findLeafs(RelationArgument arg) {
        return relationTypeRepository.findLeafs(arg);
    }

    @Override
    public List<RelationType> findChildren(Long relationTypeId) {
        return relationTypeRepository.findChildren(relationTypeId);
    }

    @Override
    public List<RelationType> findAll() {
        return relationTypeRepository.findAll();
    }

    @Override
    public void delete(RelationType relation) {
        relationTypeRepository.delete(relation);
    }

}
