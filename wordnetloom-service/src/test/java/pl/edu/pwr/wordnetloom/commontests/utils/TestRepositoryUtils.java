package pl.edu.pwr.wordnetloom.commontests.utils;

import javax.persistence.EntityManager;

public final class TestRepositoryUtils {

    private TestRepositoryUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T findByPropertyNameAndValue(final EntityManager em, final Class<T> clazz,
            final String propertyName, final String propertyValue) {
        return (T) em
                .createQuery("Select o From " + clazz.getSimpleName()
                        + " o Where o." + propertyName + " = :propertyValue")
                .setParameter("propertyValue", propertyValue)
                .getSingleResult();
    }

}
