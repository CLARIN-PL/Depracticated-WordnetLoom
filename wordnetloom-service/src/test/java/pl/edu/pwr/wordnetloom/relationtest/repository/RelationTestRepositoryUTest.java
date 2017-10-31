package pl.edu.pwr.wordnetloom.relationtest.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.commontests.relationtest.RelationTestForTestsRepository;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.relationtest.model.RelationTest;

public class RelationTestRepositoryUTest extends TestBaseRepository {

    private RelationTestRepository repository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        repository = new RelationTestRepository();
        repository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void shouldSaveRelationTest() {

        RelationTest test = RelationTestForTestsRepository.relationTestA();
        repository.persist(test);
    }
}
