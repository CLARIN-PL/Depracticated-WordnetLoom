package pl.edu.pwr.wordnetloom.partofspeech.service.impl;

import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.partofspeech.exception.PartOfSpeechNotFoundException;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.partofspeech.repository.PartOfSpeechRepository;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.edu.pwr.wordnetloom.commontests.partofspeech.PartOfSpeechForTestsRepository.*;

public class PartOfSpeechServicesUTest {

    private PartOfSpeechServiceBean partOfSpeechService;
    private PartOfSpeechRepository partOfSpeechRepository;
    private Validator validator;

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        partOfSpeechRepository = mock(PartOfSpeechRepository.class);

        partOfSpeechService = new PartOfSpeechServiceBean();
        partOfSpeechService.validator = validator;
        partOfSpeechService.partOfSpeechRepository = partOfSpeechRepository;
    }

    @Test
    public void findLexiconById() {
        when(partOfSpeechRepository.findById(1L)).thenReturn(partOfSpeechWithId(verb(), 1L));
        PartOfSpeech pos = partOfSpeechService.findById(1L);
        assertThat(pos, is(notNullValue()));
        assertThat(pos.getId(), is(equalTo(1L)));
        assertThat(pos.getName(), is(equalTo(verb().getName())));
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

        List<PartOfSpeech> list = partOfSpeechService.findAll();
        assertThat(list.size(), is(equalTo(2)));
        assertThat(list.get(0).getName(), is(equalTo(verb().getName())));
        assertThat(list.get(1).getName(), is(equalTo(noun().getName())));
        assertThat(list.get(0).getName(), is(equalTo(verb().getName())));
    }

    @Test
    public void findAllNoPartOfSpeech() {
        when(partOfSpeechRepository.findAll("id")).thenReturn(new ArrayList<>());

        List<PartOfSpeech> list = partOfSpeechService.findAll();
        assertThat(list.isEmpty(), is(equalTo(true)));
    }

}
