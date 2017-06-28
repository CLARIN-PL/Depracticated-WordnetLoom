package pl.edu.pwr.wordnetloom.partofspeech.service.impl;

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
import static pl.edu.pwr.wordnetloom.commontests.partofspeech.PartOfSpeechForTestsRepository.*;
import pl.edu.pwr.wordnetloom.partofspeech.exception.PartOfSpeechNotFoundException;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.partofspeech.repository.PartOfSpeechRepository;

public class PartOfSpeechServicesUTest {

    private PartOfSpeechServiceBean partOfSpeechService;
    private PartOfSpeechRepository partOfSpeechRepository;
    private Validator validator;

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        partOfSpeechRepository = mock(PartOfSpeechRepository.class);

        partOfSpeechService = new PartOfSpeechServiceBean();
        ((PartOfSpeechServiceBean) partOfSpeechService).validator = validator;
        ((PartOfSpeechServiceBean) partOfSpeechService).partOfSpeechRepository = partOfSpeechRepository;
    }

    @Test
    public void findLexiconById() {
        when(partOfSpeechRepository.findById(1L)).thenReturn(partOfSpeechWithId(verb(), 1L));
        final PartOfSpeech pos = partOfSpeechService.findById(1L);
        assertThat(pos, is(notNullValue()));
        assertThat(pos.getId(), is(equalTo(1L)));
        assertThat(pos.getName("EN"), is(equalTo(verb().getName("EN"))));
    }

    @Test(expected = PartOfSpeechNotFoundException.class)
    public void partOfSpeechByIdNotFound() {
        when(partOfSpeechRepository.findById(1L)).thenReturn(null);

        partOfSpeechService.findById(1L);
    }

    @Test
    public void findAllPartOfSpeech() {
        when(partOfSpeechRepository.findAll("id")).thenReturn(
                Arrays.asList(partOfSpeechWithId(verb(), 1L), partOfSpeechWithId(noun(), 2L)));

        final List<PartOfSpeech> list = partOfSpeechService.findAll();
        assertThat(list.size(), is(equalTo(2)));
        assertThat(list.get(0).getName("EN"), is(equalTo(verb().getName("EN"))));
        assertThat(list.get(1).getName("EN"), is(equalTo(noun().getName("EN"))));
        assertThat(list.get(0).getName("PL"), is(equalTo(verb().getName("PL"))));
    }

    @Test
    public void findAllNoPartOfSpeech() {
        when(partOfSpeechRepository.findAll("id")).thenReturn(new ArrayList<>());

        final List<PartOfSpeech> list = partOfSpeechService.findAll();
        assertThat(list.isEmpty(), is(equalTo(true)));
    }

}
