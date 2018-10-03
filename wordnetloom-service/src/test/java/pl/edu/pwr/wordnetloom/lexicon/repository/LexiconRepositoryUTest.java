package pl.edu.pwr.wordnetloom.lexicon.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static pl.edu.pwr.wordnetloom.commontests.lexicon.LexiconForTestsRepository.*;

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
        Long lexiconAddedId = dbCommandExecutor.executeCommand(() -> {
            return lexiconRepository.persist(princenton()).getId();
        });

        assertThat(lexiconAddedId, is(notNullValue()));

        Lexicon lexicon = lexiconRepository.findById(lexiconAddedId);
        assertThat(lexicon, is(notNullValue()));
        assertThat(lexicon.getName(), is(equalTo(princenton().getName())));
    }

    @Test
    public void findLexiconByIdNotFound() {
        Lexicon l = lexiconRepository.findById(999L);
        assertThat(l, is(nullValue()));
    }

    @Test
    public void shouldReturnAllLexiconIds() {
        Long lexiconAddedId1 = dbCommandExecutor.executeCommand(() -> {
            return lexiconRepository.persist(princenton()).getId();
        });

        Long lexiconAddedId2 = dbCommandExecutor.executeCommand(() -> {
            return lexiconRepository.persist(slowosiec()).getId();
        });

        Long lexiconAddedId3 = dbCommandExecutor.executeCommand(() -> {
            return lexiconRepository.persist(germanet()).getId();
        });

        assertThat(lexiconAddedId1, is(notNullValue()));
        assertThat(lexiconAddedId2, is(notNullValue()));
        assertThat(lexiconAddedId3, is(notNullValue()));

        List<Long> list = lexiconRepository.findAllLexiconIds();

        assertThat(list.size(), equalTo(3));
    }
}
