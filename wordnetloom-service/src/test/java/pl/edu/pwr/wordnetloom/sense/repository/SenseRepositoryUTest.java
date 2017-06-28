package pl.edu.pwr.wordnetloom.sense.repository;

import org.junit.After;
import org.junit.Before;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;

public class SenseRepositoryUTest extends TestBaseRepository {

    private SenseRepository senseRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        senseRepository = new SenseRepository();
        senseRepository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

  
}
