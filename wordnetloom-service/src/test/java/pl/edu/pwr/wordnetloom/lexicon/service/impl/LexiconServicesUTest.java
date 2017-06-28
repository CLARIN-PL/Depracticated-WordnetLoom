package pl.edu.pwr.wordnetloom.lexicon.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import pl.edu.pwr.wordnetloom.common.exception.FieldNotValidException;
import static pl.edu.pwr.wordnetloom.commontests.lexicon.LexiconForTestsRepository.*;
import pl.edu.pwr.wordnetloom.lexicon.exception.LexiconNotFoundException;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.lexicon.repository.LexiconRepository;

public class LexiconServicesUTest {

    private LexiconServiceBean lexiconService;
    private LexiconRepository lexiconRepository;
    private Validator validator;

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        lexiconRepository = mock(LexiconRepository.class);

        lexiconService = new LexiconServiceBean();
        ((LexiconServiceBean) lexiconService).validator = validator;
        ((LexiconServiceBean) lexiconService).lexiconRepository = lexiconRepository;
    }

    @Test
    public void findLexiconById() {
        when(lexiconRepository.findById(1L)).thenReturn(lexiconWithId(princenton(), 1L));

        final Lexicon lexicon = lexiconService.findById(1L);
        assertThat(lexicon, is(notNullValue()));
        assertThat(lexicon.getId(), is(equalTo(1L)));
        assertThat(lexicon.getName(), is(equalTo(princenton().getName())));
    }

    @Test(expected = LexiconNotFoundException.class)
    public void LexiconByIdNotFound() {
        when(lexiconRepository.findById(1L)).thenReturn(null);

        lexiconService.findById(1L);
    }

    @Test
    public void findAllLexicons() {
        when(lexiconRepository.findAll("name")).thenReturn(
                Arrays.asList(lexiconWithId(princenton(), 1L), lexiconWithId(slowosiec(), 2L)));

        final List<Lexicon> categories = lexiconService.findAll();
        assertThat(categories.size(), is(equalTo(2)));
        assertThat(categories.get(0).getName(), is(equalTo(princenton().getName())));
        assertThat(categories.get(1).getName(), is(equalTo(slowosiec().getName())));
    }

    @Test
    public void findAllNoLexicons() {
        when(lexiconRepository.findAll("name")).thenReturn(new ArrayList<>());

        final List<Lexicon> lexicons = lexiconService.findAll();
        assertThat(lexicons.isEmpty(), is(equalTo(true)));
    }

    @Test
    public void addLexiconWithNullName() {
        addLexiconWithInvalidName(null);
    }

    @Test
    public void addLexiconWithNullIdentifier() {
        addLexiconWithInvalidIdentifier(null);
    }

    private void addLexiconWithInvalidName(final String name) {
        try {
            lexiconService.add(new Lexicon(null, "PLWN", "Polish"));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }

    private void addLexiconWithInvalidIdentifier(final String identifier) {
        try {
            lexiconService.add(new Lexicon("Princenton", identifier, "Polish"));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("identifier")));
        }
    }
}
