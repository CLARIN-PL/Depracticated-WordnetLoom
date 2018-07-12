package pl.edu.pwr.wordnetloom.localisation.service.impl;

import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.common.exception.FieldNotValidException;
import pl.edu.pwr.wordnetloom.localisation.exception.LocalisedStringNotFoundException;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;
import pl.edu.pwr.wordnetloom.localisation.repository.LocalisedStringRepository;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.edu.pwr.wordnetloom.commontests.localisation.LocalistaionForTestsRepository.*;

public class LocalisedServicesUTest {

    private LocalisedStringRepository repository;
    private Validator validator;
    private LocalisedStringServiceBean service;
    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        repository = mock(LocalisedStringRepository.class);

        service = new LocalisedStringServiceBean();
        service.validator = validator;
        service.repository = repository;
    }

    @Test
    public void findByKey() {
        LocalisedKey key = new LocalisedKey(1L, "pl");
        when(repository.findByKey(key)).thenReturn(withId(nameString(), 1L, "pl"));

        LocalisedString string = service.findStringsByKey(key);
        assertThat(string, is(notNullValue()));
        assertThat(string.getKey(), is(equalTo(key)));
        assertThat(string.getValue(), is(equalTo(nameString().getValue())));
    }

    @Test(expected = LocalisedStringNotFoundException.class)
    public void LexiconByIdNotFound() {
        LocalisedKey key = new LocalisedKey(1L, "pl");
        when(repository.findByKey(key)).thenReturn(null);
        service.findStringsByKey(key);
    }

    @Test
    public void findAllByLanguage() {
        when(repository.findAllByLanguage("pl")).thenReturn(
                Arrays.asList(withId(nameString(), 1L, "pl"), withId(descriptionString(), 1L, "pl")));

        List<LocalisedString> pl = service.findAllStringsByLanguage("pl");

        assertThat(pl.size(), is(equalTo(2)));
        assertThat(pl.get(0).getValue(), is(equalTo(nameString().getValue())));
        assertThat(pl.get(1).getValue(), is(equalTo(descriptionString().getValue())));
    }

    @Test
    public void findAllEmpty() {
        when(repository.findAllByLanguage("pl")).thenReturn(new ArrayList<>());

        List<LocalisedString> lexicons = service.findAllStringsByLanguage("pl");
        assertThat(lexicons.isEmpty(), is(equalTo(true)));
    }

//    @Test
//    public void addStringWithNullLanguage() {
//        try {
//            service.add(new LocalisedString(1L, null, "nazwa"));
//            fail("An error should have been thrown");
//        } catch (FieldNotValidException e) {
//            assertThat(e.getFieldName(), is(equalTo("key.language")));
//        }
//    }

    @Test
    public void addStringWithNullKey() {
        try {
            service.add(new LocalisedString());
            fail("An error should have been thrown");
        } catch (FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("key")));
        }
    }

}
