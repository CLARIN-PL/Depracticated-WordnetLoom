package pl.edu.pwr.wordnetloom.relationtype.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.relationtype.exception.RelationTypeNotFoundException;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.relationtype.repository.RelationTypeRepository;
import pl.edu.pwr.wordnetloom.relationtype.service.RelationTypeServiceLocal;
import pl.edu.pwr.wordnetloom.relationtype.service.RelationTypeServiceRemote;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.Collection;
import java.util.List;

@Stateless
@SecurityDomain("wordnetloom")
@DeclareRoles({"USER", "ADMIN"})
@Remote(RelationTypeServiceRemote.class)
@Local(RelationTypeServiceLocal.class)
public class RelationTypeServiceBean implements RelationTypeServiceLocal {

    @Inject
    RelationTypeRepository relationTypeRepository;

    @Inject
    Validator validator;

    @PermitAll
    @Override
    public RelationType findById(Long id) {
        RelationType rel = relationTypeRepository.findById(id);
        if (rel == null) {
            throw new RelationTypeNotFoundException();
        }
        return rel;
    }

    @PermitAll
    @Override
    public boolean isReverseRelation(Collection<RelationType> relations, RelationType test) {
        return relationTypeRepository.isReverse(relations, test);
    }

    @PermitAll
    @Override
    public RelationType findByIdWithDependencies(Long id) {
        RelationType rel = relationTypeRepository.findByIdWithDependencies(id);
        if (rel == null) {
            throw new RelationTypeNotFoundException();
        }
        return rel;
    }

    @RolesAllowed("ADMIN")
    @Override
    public void deleteAll(RelationType type) {

        relationTypeRepository.deleteRelationWithChilds(type);
    }

    @PermitAll
    @Override
    public Long findReverseId(Long relationTypeId) {
        return relationTypeRepository.findReverseId(relationTypeId);
    }


    @PermitAll
    @Override
    public RelationType findReverseByRelationType(Long relationTypeId) {
        return relationTypeRepository.findReverseByRelationType(relationTypeId);
    }

    @RolesAllowed("ADMIN")
    @Override
    public RelationType save(RelationType rel) {
        ValidationUtils.validateEntityFields(validator, rel);
        if (rel.getId() == null) {
            return relationTypeRepository.persist(rel);
        } else {
            return relationTypeRepository.update(rel);
        }
    }

    @PermitAll
    @Override
    public List<RelationType> findHighest(RelationArgument arg) {
        return relationTypeRepository.findHighestLeafs(arg);
    }

    @PermitAll
    @Override
    public List<RelationType> findLeafs(RelationArgument arg) {
        return relationTypeRepository.findLeafs(arg);
    }

    @PermitAll
    @Override
    public List<RelationType> findChildren(Long relationTypeId) {
        return relationTypeRepository.findChildren(relationTypeId);
    }

    @PermitAll
    @Override
    public List<RelationType> findAll() {
        return relationTypeRepository.findAll();
    }

    @RolesAllowed("ADMIN")
    @Override
    public void delete(RelationType relation) {
        relationTypeRepository.delete(relation);
    }

    @PermitAll
    @Override
    public RelationType findByName(String name) {
        return relationTypeRepository.findByName(name);
    }

    @PermitAll
    @Override
    public RelationType findParent(Long childId) {
        return relationTypeRepository.findParent(childId);
    }
}
