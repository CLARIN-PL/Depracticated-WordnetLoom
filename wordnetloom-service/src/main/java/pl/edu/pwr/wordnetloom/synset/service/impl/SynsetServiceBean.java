package pl.edu.pwr.wordnetloom.synset.service.impl;

import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.common.model.NodeDirection;
import pl.edu.pwr.wordnetloom.common.utils.ValidationUtils;
import pl.edu.pwr.wordnetloom.dictionary.repository.DictionaryRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.repository.SenseAttributesRepository;
import pl.edu.pwr.wordnetloom.sense.service.SenseServiceLocal;
import pl.edu.pwr.wordnetloom.synset.dto.SynsetCriteriaDTO;
import pl.edu.pwr.wordnetloom.synset.exception.InvalidLexiconException;
import pl.edu.pwr.wordnetloom.synset.exception.InvalidPartOfSpeechException;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.synset.model.SynsetExample;
import pl.edu.pwr.wordnetloom.synset.repository.SynsetAttributesRepository;
import pl.edu.pwr.wordnetloom.synset.repository.SynsetRepository;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceLocal;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceRemote;
import pl.edu.pwr.wordnetloom.synsetrelation.model.SynsetRelation;
import pl.edu.pwr.wordnetloom.synsetrelation.repository.SynsetRelationRepository;
import pl.edu.pwr.wordnetloom.user.model.User;
import pl.edu.pwr.wordnetloom.user.service.UserServiceLocal;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ExecutionException;

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
    SynsetRelationRepository synsetRelationRepository;

    @Inject
    SenseServiceLocal senseService;

    @Inject
    UserServiceLocal userService;

    @Inject
    DictionaryRepository dictionaryRepository;

    @Inject
    SenseAttributesRepository senseAttributesRepository;

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
        synsetAttributesRepository.delete(synset.getId());
        synsetRepository.delete(synset);
        return true;
    }

    @PermitAll
    @Override
    public Synset findSynsetBySense(Sense sense, List<Long> lexicons) {
        Synset synset = synsetRepository.findSynsetBySense(sense, lexicons);
        Hibernate.initialize(synset.getLexicon());
        return synset;
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
    public DataEntry findSynsetDataEntry(UUID synsetUUID, List<Long> lexicons) {
        return synsetRepository.findSynsetDataEntry(synsetUUID, lexicons);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public Synset save(Synset synset) {
        ValidationUtils.validateEntityFields(validator, synset);
        if (synset.getId() == null) {
            Synset newSynset = synsetRepository.persist(synset);
            newSynset.setStatus(dictionaryRepository.findStatusDefaultValue());
            saveAttributes(newSynset);
        }
        return synsetRepository.update(synset);
    }

    private void saveAttributes(Synset newSynset) {
        SynsetAttributes attributes = new SynsetAttributes();
        attributes.setSynset(newSynset);
        attributes.setId(newSynset.getUuid());
        synsetAttributesRepository.persist(attributes);
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
        try {
            return synsetRepository.prepareCacheForRootNode(synset.getUuid(), lexicons, 4, directions);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public Synset addSenseToSynset(Sense sense, Synset synset) throws InvalidLexiconException, InvalidPartOfSpeechException {

        Synset saved = synset;

        if (synset.getId() == null) {
            saved = save(synset);
            saveOwner(saved);
        }
        Sense fetchedSense = senseService.fetchSense(sense.getUuid());
        List<Sense> sensesInSynset = senseService.findBySynset(synset.getId());
        sensesInSynset.stream()
                .findFirst()
                .ifPresent(s -> {
                    if(!fetchedSense.getPartOfSpeech().equals(s.getPartOfSpeech())){
                        throw new InvalidPartOfSpeechException();
                    }
                    if(!fetchedSense.getLexicon().equals(s.getLexicon())){
                        throw new InvalidLexiconException();
                    }
                });

        if (sensesInSynset.contains(sense)) {
            return saved;
        }

        fetchedSense.setSynset(saved);
        fetchedSense.setSynsetPosition(sensesInSynset.size());
        senseService.save(fetchedSense);

        return saved;
    }

    private void saveOwner(Synset saved) {
        SynsetAttributes attributes = synsetAttributesRepository.findById(saved.getId());
        String email = principal.getName();
        User user = userService.findUserByEmail(email);
        attributes.setOwner(user);
        save(attributes);
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
            sense = senseService.fetchSense(sense.getUuid());
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
    public Synset fetchSynset(Synset synset) {
        return synsetRepository.fetchSynset(synset);
    }

    @PermitAll
    @Override
    public SynsetAttributes fetchSynsetAttributes(Synset synset) {
        return synsetRepository.fetchSynsetAttributes(synset);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public void merge(Synset target, Synset source){
        moveSenses(target, source);
        moveRelations(target, source);
        delete(source);
    }

    private void moveRelations(Synset target, Synset source) {
        List<SynsetRelation> sourceRelations = synsetRelationRepository.findRelations(source);
        for(SynsetRelation relation : sourceRelations){
            RelationType relationType = relation.getRelationType();
            if(source == relation.getParent()){
                Synset child = relation.getChild();
                if(!synsetRelationRepository.checkRelationExists(target, child, relationType)){
                    synsetRelationRepository.create(target, child, relationType);
                }
            } else { //source == relation.getChild()
                Synset parent = relation.getParent();
                if (!synsetRelationRepository.checkRelationExists(parent, target, relationType)) {
                    synsetRelationRepository.create(parent, target, relationType);
                }
            }
            synsetRelationRepository.delete(relation);
        }
    }

    private void moveSenses(Synset target, Synset source) {
        List<Sense> sourceSenses = senseService.findBySynset(source.getId());
        int sensesInTarget = synsetRepository.findSynsetSenseCount(target);
        int positionInSynset = sensesInTarget;
        for(Sense sense : sourceSenses){
            positionInSynset++;
            sense.setSynset(target);
            sense.setSynsetPosition(positionInSynset);
            senseService.save(sense);
        }
    }


    @RolesAllowed({"USER", "ADMIN"})
    @Override
    public  void remove(Synset synset) {
        synsetAttributesRepository.delete(synset.getId());
        synsetRelationRepository.deleteConnection(synset);
        List<Sense> senses = senseService.findBySynset(synset.getId());
        removeSenses(senses);
        synsetRepository.delete(synset);
    }

    private void removeSenses(List<Sense> senses){
        for(Sense sense : senses) {
            senseService.delete(sense);
        }
    }
}
