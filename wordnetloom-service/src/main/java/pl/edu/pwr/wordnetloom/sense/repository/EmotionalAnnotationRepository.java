package pl.edu.pwr.wordnetloom.sense.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.sense.model.EmotionalAnnotation;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.List;

public class EmotionalAnnotationRepository extends GenericRepository<EmotionalAnnotation> {

    @Inject
    EntityManager entityManager;


    public List<EmotionalAnnotation> getEmotionalAnnotations(Long senseId) {
        return getEntityManager().createQuery("FROM EmotionalAnnotation e WHERE e.sense.id = :senseId")
                .setParameter("senseId", senseId)
                .getResultList();
    }

    @Override
    protected Class<EmotionalAnnotation> getPersistentClass() {
        return EmotionalAnnotation.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
