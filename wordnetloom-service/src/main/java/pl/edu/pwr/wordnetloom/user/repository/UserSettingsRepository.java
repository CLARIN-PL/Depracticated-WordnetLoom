package pl.edu.pwr.wordnetloom.user.repository;

import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.user.model.UserSettings;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class UserSettingsRepository extends GenericRepository<UserSettings> {

    @Inject
    EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(UserSettings settings) {
         em.merge(settings);
    }

    @Override
    protected Class<UserSettings> getPersistentClass() {
        return UserSettings.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
