package pl.edu.pwr.wordnetloom.commontests.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Ignore;

@Ignore
public class TestBaseRepository {

    private EntityManagerFactory emf;
    protected EntityManager em;
    protected DBCommandTransactionalExecutor dbCommandExecutor;

    protected void initializeTestDB() {
        emf = Persistence.createEntityManagerFactory("plWordnetPU");
        em = emf.createEntityManager();

        dbCommandExecutor = new DBCommandTransactionalExecutor(em);
    }

    protected void closeEntityManager() {
        em.close();
        emf.close();
    }

}
