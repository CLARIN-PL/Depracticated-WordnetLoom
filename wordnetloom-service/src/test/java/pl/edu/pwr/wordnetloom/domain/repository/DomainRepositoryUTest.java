package pl.edu.pwr.wordnetloom.domain.repository;

import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static pl.edu.pwr.wordnetloom.commontests.domain.DomainForTestsRepository.*;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.domain.model.Domain;

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
        final Long domainAddedId = dbCommandExecutor.executeCommand(() -> {
            return domainRepository.persist(bhp()).getId();
        });

        assertThat(domainAddedId, is(notNullValue()));

        final Domain domain = domainRepository.findById(domainAddedId);
        assertThat(domain, is(notNullValue()));
        assertThat(domain.getName("PL"), is(equalTo(bhp().getName("PL"))));
    }

    @Test
    public void shouldFindAllDomain() {

        final Long domainAddedId1 = dbCommandExecutor.executeCommand(() -> {
            return domainRepository.persist(bhp()).getId();
        });

        final Long domainAddedId2 = dbCommandExecutor.executeCommand(() -> {
            return domainRepository.persist(jak()).getId();
        });

        assertThat(domainAddedId1, is(notNullValue()));
        assertThat(domainAddedId2, is(notNullValue()));

        final List<Domain> list = domainRepository.findAll("id");

        assertThat(list.size(), equalTo(2));
    }

    @Test
    public void findDomainByIdNotFound() {
        final Domain d = domainRepository.findById(999L);
        assertThat(d, is(nullValue()));
    }
}
