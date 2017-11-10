package pl.edu.pwr.wordnetloom.partofspeech.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static pl.edu.pwr.wordnetloom.commontests.partofspeech.PartOfSpeechForTestsRepository.noun;
import static pl.edu.pwr.wordnetloom.commontests.partofspeech.PartOfSpeechForTestsRepository.verb;

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
        Long posAddedId = dbCommandExecutor.executeCommand(() -> {
            return posRepository.persist(verb()).getId();
        });

        assertThat(posAddedId, is(notNullValue()));

        PartOfSpeech pos = posRepository.findById(posAddedId);
        assertThat(pos, is(notNullValue()));
        assertThat(pos.getName(), is(equalTo(verb().getName())));
        assertThat(pos.getName(), is(equalTo(verb().getName())));
    }

    @Test
    public void shouldFindLexiconByIdList() {

        Long posAddedId1 = dbCommandExecutor.executeCommand(() -> {
            return posRepository.persist(verb()).getId();
        });

        Long posAddedId2 = dbCommandExecutor.executeCommand(() -> {
            return posRepository.persist(noun()).getId();
        });

        assertThat(posAddedId1, is(notNullValue()));
        assertThat(posAddedId2, is(notNullValue()));

        List<PartOfSpeech> list = posRepository.findAll("id");

        assertThat(list.size(), equalTo(2));
    }

    @Test
    public void findLexiconByIdNotFound() {
        PartOfSpeech p = posRepository.findById(999L);
        assertThat(p, is(nullValue()));
    }
}
