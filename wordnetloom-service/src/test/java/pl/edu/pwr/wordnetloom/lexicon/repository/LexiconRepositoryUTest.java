package pl.edu.pwr.wordnetloom.lexicon.repository;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static pl.edu.pwr.wordnetloom.commontests.lexicon.LexiconForTestsRepository.*;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

public class LexiconRepositoryUTest extends TestBaseRepository {

    private LexiconRepository lexiconRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        lexiconRepository = new LexiconRepository();
        lexiconRepository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void saveLexiconAndFindIt() {
        final Long lexiconAddedId = dbCommandExecutor.executeCommand(() -> {
            return lexiconRepository.persist(princenton()).getId();
        });

        assertThat(lexiconAddedId, is(notNullValue()));

        final Lexicon lexicon = lexiconRepository.findById(lexiconAddedId);
        assertThat(lexicon, is(notNullValue()));
        assertThat(lexicon.getName(), is(equalTo(princenton().getName())));
    }

    @Test
    public void shouldFindLexiconByIdList() {

        final Long lexiconAddedId1 = dbCommandExecutor.executeCommand(() -> {
            return lexiconRepository.persist(princenton()).getId();
        });

        final Long lexiconAddedId2 = dbCommandExecutor.executeCommand(() -> {
            return lexiconRepository.persist(slowosiec()).getId();
        });

        assertThat(lexiconAddedId1, is(notNullValue()));
        assertThat(lexiconAddedId2, is(notNullValue()));

        final List<Lexicon> list = lexiconRepository.findByLexicons(Arrays.asList(lexiconAddedId1, lexiconAddedId2));

        assertThat(list.size(), equalTo(2));
    }

    @Test
    public void findLexiconByIdNotFound() {
        final Lexicon l = lexiconRepository.findById(999L);
        assertThat(l, is(nullValue()));
    }

    @Test
    public void shouldReturnAllLexiconIds() {
        final Long lexiconAddedId1 = dbCommandExecutor.executeCommand(() -> {
            return lexiconRepository.persist(princenton()).getId();
        });

        final Long lexiconAddedId2 = dbCommandExecutor.executeCommand(() -> {
            return lexiconRepository.persist(slowosiec()).getId();
        });

        final Long lexiconAddedId3 = dbCommandExecutor.executeCommand(() -> {
            return lexiconRepository.persist(germanet()).getId();
        });

        assertThat(lexiconAddedId1, is(notNullValue()));
        assertThat(lexiconAddedId2, is(notNullValue()));
        assertThat(lexiconAddedId3, is(notNullValue()));

        final List<Long> list = lexiconRepository.findAllLexiconIds();

        assertThat(list.size(), equalTo(3));
    }
}
