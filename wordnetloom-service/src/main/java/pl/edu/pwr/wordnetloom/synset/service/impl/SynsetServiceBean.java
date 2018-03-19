package pl.edu.pwr.wordnetloom.synset.service.impl;

import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.service.SenseServiceLocal;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.synset.repository.SynsetAttributesRepository;
import pl.edu.pwr.wordnetloom.synset.repository.SynsetRepository;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceLocal;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceRemote;
import pl.edu.pwr.wordnetloom.user.model.User;
import pl.edu.pwr.wordnetloom.user.service.UserServiceLocal;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Stateless
@SecurityDomain("wordnetloom")
@DeclareRoles({"USER", "ADMIN"})
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
    UserServiceLocal userService;

    @Inject
    Principal principal;

    @Inject
    Validator validator;

    @Override
    public void clone(Synset synset) {
        //add sense cloning
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public boolean delete(Synset synset) {
        synsetRepository.delete(synset);
        return true;
    }

    @PermitAll
    @Override
    public Synset findSynsetBySense(Sense sense, List<Long> lexicons) {
        return synsetRepository.findSynsetBySense(sense, lexicons);
    }

    @PermitAll
    @Override
    public Synset findById(Long id) {
        return synsetRepository.findById(id);
    }

    @RolesAllowed({"USER", "ADMIN"})
    public void delete(RelationType relation, List<Long> lexicons) {
        //Removes relation with subrelations
//        Collection<SynsetRelationType> children = findChildren(relation);
//        for (SynsetRelationType item : children) {
//            synsetRelationRepository.delete(relation);
//            delete(item);
//        }
//        delete(relation);
    }

    @PermitAll
    @Override
    public DataEntry findSynsetDataEntry(Long synsetId, List<Long> lexicons) {
        return synsetRepository.findSynsetDataEntry(synsetId, lexicons);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public Synset save(Synset synset) {
        ValidationUtils.validateEntityFields(validator, synset);
        if (synset.getId() == null) {
            return synsetRepository.persist(synset);
        }
        return synsetRepository.update(synset);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public SynsetAttributes save(SynsetAttributes attributes) {
        ValidationUtils.validateEntityFields(validator, attributes);
        if (attributes.getId() == null) {
            return synsetAttributesRepository.persist(attributes);
        }
        return synsetAttributesRepository.update(attributes);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public SynsetAttributes addSynsetAttribute(Long synsetId, SynsetAttributes attributes) {
        ValidationUtils.validateEntityFields(validator, attributes);

        if (attributes.getId() != null) {
            return synsetAttributesRepository.update(attributes);
        }

        Synset s = findById(synsetId);
        attributes.setSynset(s);

        return synsetAttributesRepository.persist(attributes);
    }

    @PermitAll
    @Override
    public Map<Long, DataEntry> prepareCacheForRootNode(Synset synset, List<Long> lexicons, NodeDirection[] directions) {
        return synsetRepository.prepareCacheForRootNode(synset.getId(), lexicons, 4, directions);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public Synset addSenseToSynset(Sense sense, Synset synset) {

        Synset saved = synset;

        if (synset.getId() == null) {

            saved = save(synset);

            SynsetAttributes synsetAttributes = new SynsetAttributes();
            String email = principal.getName();
            User user = userService.findUserByEmail(email);
            synsetAttributes.setOwner(user);
            synsetAttributes.setSynset(saved);
            save(synsetAttributes);

        }

        List<Sense> sensesInSynset = senseService.findBySynset(synset.getId());

        if (sensesInSynset.contains(sense)) {
            return saved;
        }

        sense.setSynset(saved);
        sense.setSynsetPosition(sensesInSynset.size());
        senseService.save(sense);

        return saved;
    }

    private int reindexSensesInSynset(Synset synset) {

        List<Sense> sensesInSynset = senseService.findBySynset(synset.getId());
        int index = 0;
        for (Sense sense : sensesInSynset) {
            sense.setSynsetPosition(index++);
            senseService.save(sense);
        }
        return index;
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public void deleteSensesFromSynset(Collection<Sense> senses, Synset synset) {
        for (Sense sense : senses) {
            sense.setSynset(null);
            sense.setSynsetPosition(null);
            senseService.save(sense);
        }
        reindexSensesInSynset(synset);
    }

    @PermitAll
    @Override
    public List<Synset> findSynsetsByCriteria(SynsetCriteriaDTO criteria) {
        return synsetRepository.findSynsetsByCriteria(criteria);
    }

    @PermitAll
    @Override
    public int getCountSynsetsByCriteria(SynsetCriteriaDTO criteria) {
        return synsetRepository.getCountSynsetsByCriteria(criteria);
    }

    @PermitAll
    @Override
    public Synset fetchSynset(Long synsetId) {
        return synsetRepository.fetchSynset(synsetId);
    }

    @PermitAll
    @Override
    public SynsetAttributes fetchSynsetAttributes(Long synsetId) {
        return synsetRepository.fetchSynsetAttributes(synsetId);
    }
}
