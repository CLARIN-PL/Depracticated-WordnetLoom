package pl.edu.pwr.wordnetloom.domain.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static pl.edu.pwr.wordnetloom.commontests.domain.DomainForTestsRepository.bhp;
import static pl.edu.pwr.wordnetloom.commontests.domain.DomainForTestsRepository.jak;

public class DomainRepositoryUTest extends TestBaseRepository {

    private DomainRepository domainRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        domainRepository = new DomainRepository();
        domainRepository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void saveDomianAndFindIt() {
        Long domainAddedId = dbCommandExecutor.executeCommand(() -> {
            return domainRepository.persist(bhp()).getId();
        });

        assertThat(domainAddedId, is(notNullValue()));

        Domain domain = domainRepository.findById(domainAddedId);
        assertThat(domain, is(notNullValue()));
        assertThat(domain.getName(), is(equalTo(bhp().getName())));
    }

    @Test
    public void shouldFindAllDomain() {

        Long domainAddedId1 = dbCommandExecutor.executeCommand(() -> {
            return domainRepository.persist(bhp()).getId();
        });

        Long domainAddedId2 = dbCommandExecutor.executeCommand(() -> {
            return domainRepository.persist(jak()).getId();
        });

        assertThat(domainAddedId1, is(notNullValue()));
        assertThat(domainAddedId2, is(notNullValue()));

        List<Domain> list = domainRepository.findAll("id");

        assertThat(list.size(), equalTo(2));
    }

    @Test
    public void findDomainByIdNotFound() {
        Domain d = domainRepository.findById(999L);
        assertThat(d, is(nullValue()));
    }
}
