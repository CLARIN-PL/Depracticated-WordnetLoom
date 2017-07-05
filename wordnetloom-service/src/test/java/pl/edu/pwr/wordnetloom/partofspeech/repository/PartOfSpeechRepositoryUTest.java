package pl.edu.pwr.wordnetloom.partofspeech.repository;

import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static pl.edu.pwr.wordnetloom.commontests.partofspeech.PartOfSpeechForTestsRepository.*;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

public class PartOfSpeechRepositoryUTest extends TestBaseRepository {

    private PartOfSpeechRepository posRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        posRepository = new PartOfSpeechRepository();
        posRepository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void savePartOfSpeechAndFindIt() {
        final Long posAddedId = dbCommandExecutor.executeCommand(() -> {
            return posRepository.persist(verb()).getId();
        });

        assertThat(posAddedId, is(notNullValue()));

        final PartOfSpeech pos = posRepository.findById(posAddedId);
        assertThat(pos, is(notNullValue()));
        assertThat(pos.getName("EN"), is(equalTo(verb().getName("EN"))));
        assertThat(pos.getName("PL"), is(equalTo(verb().getName("PL"))));
    }

    @Test
    public void shouldFindLexiconByIdList() {

        final Long posAddedId1 = dbCommandExecutor.executeCommand(() -> {
            return posRepository.persist(verb()).getId();
        });

        final Long posAddedId2 = dbCommandExecutor.executeCommand(() -> {
            return posRepository.persist(noun()).getId();
        });

        assertThat(posAddedId1, is(notNullValue()));
        assertThat(posAddedId2, is(notNullValue()));

        final List<PartOfSpeech> list = posRepository.findAll("id");

        assertThat(list.size(), equalTo(2));
    }

    @Test
    public void findLexiconByIdNotFound() {
        final PartOfSpeech p = posRepository.findById(999L);
        assertThat(p, is(nullValue()));
    }
}
