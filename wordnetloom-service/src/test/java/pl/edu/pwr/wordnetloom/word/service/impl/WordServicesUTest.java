package pl.edu.pwr.wordnetloom.word.service.impl;

import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.common.exception.FieldNotValidException;
import pl.edu.pwr.wordnetloom.word.exception.WordNotFoundException;
import pl.edu.pwr.wordnetloom.word.model.Word;
import pl.edu.pwr.wordnetloom.word.repository.WordRepository;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.edu.pwr.wordnetloom.commontests.word.WordForTestsRepository.*;

public class WordServicesUTest {

    private WordServiceBean wordService;
    private WordRepository wordRepository;
    private Validator validator;

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        wordRepository = mock(WordRepository.class);

        wordService = new WordServiceBean();
        wordService.validator = validator;
        wordService.wordRepository = wordRepository;
    }

    @Test
    public void findWordById() {
        when(wordRepository.findById(1L)).thenReturn(wordWithId(pisac(), 1L));
        Word w = wordService.findById(1L);
        assertThat(w, is(notNullValue()));
        assertThat(w.getId(), is(equalTo(1L)));
        assertThat(w.getWord(), is(equalTo(pisac().getWord())));
    }

    @Test
    public void findWordByWord() {
        when(wordRepository.findByWord("krowa")).thenReturn(wordWithId(krowa(), 1L));
        Word w = wordService.findByWord("krowa");
        assertThat(w, is(notNullValue()));
        assertThat(w.getId(), is(equalTo(1L)));
        assertThat(w.getWord(), is(equalTo(krowa().getWord())));
    }

    @Test(expected = WordNotFoundException.class)
    public void wordByIdNotFound() {
        when(wordRepository.findById(1L)).thenReturn(null);
        wordService.findById(1L);
    }

    @Test(expected = WordNotFoundException.class)
    public void wordByWordNotFound() {
        when(wordRepository.findByWord("zomo")).thenReturn(null);
        wordService.findByWord("zomo");
    }

    @Test
    public void addWordWithNullWord() {
        addWordWithInvalidWord(null);
    }

    @Test
    public void wordShouldBeUnique() {
        when(wordRepository.alreadyExists("word", "czerwony", null)).thenReturn(true);
        when(wordRepository.findByWord("czerwony")).thenReturn(wordWithId(czerwony(), 1L));
        when(wordRepository.persist(wordWithId(czerwony(), 1L))).thenReturn(wordWithId(czerwony(), 1L));

        Word w1 = wordService.add(wordWithId(czerwony(), 1L));
        Word w2 = wordService.add(wordWithId(czerwony(), 2L));

        assertThat(w1, is(equalTo(w2)));
    }

    private void addWordWithInvalidWord(String word) {
        try {
            wordService.add(new Word(word));
            fail("An error should have been thrown");
        } catch (FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("word")));
        }
    }

}
