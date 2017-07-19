package pl.edu.pwr.wordnetloom.relationtype.repository;

import org.junit.After;
import org.junit.Before;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;

public class SenseRelationTypeRepositoryUTest extends TestBaseRepository {

    private SenseRelationTypeRepository repository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        repository = new SenseRelationTypeRepository();
        repository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

  
}
