package pl.edu.pwr.wordnetloom.sense.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static pl.edu.pwr.wordnetloom.commontests.domain.DomainForTestsRepository.allDomains;
import static pl.edu.pwr.wordnetloom.commontests.lexicon.LexiconForTestsRepository.allLexicons;
import static pl.edu.pwr.wordnetloom.commontests.lexicon.LexiconForTestsRepository.slowosiec;
import static pl.edu.pwr.wordnetloom.commontests.partofspeech.PartOfSpeechForTestsRepository.allPartOfSpeechs;
import static pl.edu.pwr.wordnetloom.commontests.partofspeech.PartOfSpeechForTestsRepository.verb;
import static pl.edu.pwr.wordnetloom.commontests.word.WordForTestsRepository.allWords;
import static pl.edu.pwr.wordnetloom.commontests.word.WordForTestsRepository.zamek;

@Ignore
public class SenseRepositoryUTest extends TestBaseRepository {

    private SenseRepository senseRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        senseRepository = new SenseRepository();
        senseRepository.em = em;

        prepareData();
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }


    @Test
    public void saveSenseAndFindIt() {
        Long senseId = dbCommandExecutor.executeCommand(() -> {
            Sense s = new Sense();
            s.setLexicon(slowosiec());
            s.setPartOfSpeech(verb());
            s.setWord(zamek());

            return senseRepository.persist(s).getId();
        });

        assertThat(senseId, is(notNullValue()));

        Sense sense = senseRepository.findById(senseId);
        assertThat(sense, is(notNullValue()));
        assertThat(sense.getLexicon(), is(equalTo(slowosiec())));
        assertThat(sense.getPartOfSpeech(), is(equalTo(verb())));
        assertThat(sense.getWord(), is(equalTo(zamek())));
    }


    private void prepareData() {
        dbCommandExecutor.executeCommand(() -> {
            allLexicons().forEach(em::persist);
            allPartOfSpeechs().forEach(em::persist);
            allDomains().forEach(em::persist);
            allWords().forEach(em::persist);
            return null;
        });
    }
}
