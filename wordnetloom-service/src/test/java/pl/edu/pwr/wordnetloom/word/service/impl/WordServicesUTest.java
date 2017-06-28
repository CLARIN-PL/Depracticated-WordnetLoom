package pl.edu.pwr.wordnetloom.word.service.impl;

import javax.validation.Validation;
import javax.validation.Validator;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import pl.edu.pwr.wordnetloom.common.exception.FieldNotValidException;
import static pl.edu.pwr.wordnetloom.commontests.word.WordForTestsRepository.*;
import pl.edu.pwr.wordnetloom.word.exception.WordNotFoundException;
import pl.edu.pwr.wordnetloom.word.model.Word;
import pl.edu.pwr.wordnetloom.word.repository.WordRepository;

public class WordServicesUTest {
    
    private WordServiceBean wordService;
    private WordRepository wordRepository;
    private Validator validator;

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

       wordRepository = mock(WordRepository.class);

        wordService = new WordServiceBean();
        ((WordServiceBean) wordService).validator = validator;
        ((WordServiceBean) wordService).wordRepository = wordRepository;
    }

    @Test
    public void findWordById() {
        when(wordRepository.findById(1L)).thenReturn(wordWithId(pisac(), 1L));
        final Word w = wordService.findById(1L);
        assertThat(w, is(notNullValue()));
        assertThat(w.getId(), is(equalTo(1L)));
        assertThat(w.getWord(), is(equalTo(pisac().getWord())));
    }

     @Test
    public void findWordByWord() {
        when(wordRepository.findByWord("krowa")).thenReturn(wordWithId(krowa(), 1L));
        final Word w = wordService.findByWord("krowa");
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
       
    private void addWordWithInvalidWord(final String word) {
        try {
            wordService.add(new Word(word));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("word")));
        }
    }
    
}
