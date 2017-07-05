package pl.edu.pwr.wordnetloom.word.repository;

import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import static pl.edu.pwr.wordnetloom.commontests.word.WordForTestsRepository.*;
import pl.edu.pwr.wordnetloom.word.model.Word;

public class WordRepositoryUTest extends TestBaseRepository {

    private WordRepository wordRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        wordRepository = new WordRepository();
        wordRepository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void saveWordAndFindIt() {
        final Long wordAddedId = dbCommandExecutor.executeCommand(() -> {
            return wordRepository.persist(czerwony()).getId();
        });

        assertThat(wordAddedId, is(notNullValue()));

        final Word w = wordRepository.findById(wordAddedId);
        assertThat(w, is(notNullValue()));
        assertThat(w.getWord(), is(equalTo(czerwony().getWord())));
    }

    @Test
    public void findWordByWord() {

        final Long wordAddedId1 = dbCommandExecutor.executeCommand(() -> {
            return wordRepository.persist(czerwony()).getId();
        });

        final Long wordAddedId2 = dbCommandExecutor.executeCommand(() -> {
            return wordRepository.persist(krowa()).getId();
        });

        final Long wordAddedId3 = dbCommandExecutor.executeCommand(() -> {
            return wordRepository.persist(pisac()).getId();
        });

        assertThat(wordAddedId1, is(notNullValue()));
        assertThat(wordAddedId2, is(notNullValue()));
        assertThat(wordAddedId3, is(notNullValue()));

        final Word w = wordRepository.findById(wordAddedId2);
        assertThat(w, is(notNullValue()));

        final Word krowa = wordRepository.findByWord(krowa().getWord());

        assertThat(w, is(equalTo(krowa)));
    }

}
