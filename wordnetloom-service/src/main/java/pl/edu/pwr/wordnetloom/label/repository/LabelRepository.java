package pl.edu.pwr.wordnetloom.label.repository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class LabelRepository {

    @Inject
    EntityManager em;

    public List<Object[]> findAllLabels(String locale){
        return em.createNativeQuery("SELECT label_key, value FROM application_labels WHERE language = :locale")
                .setParameter("locale", locale).getResultList();
    }
}
