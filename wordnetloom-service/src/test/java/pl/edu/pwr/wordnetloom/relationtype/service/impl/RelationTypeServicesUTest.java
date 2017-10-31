package pl.edu.pwr.wordnetloom.relationtype.service.impl;

import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.common.exception.FieldNotValidException;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.relationtype.exception.RelationTypeNotFoundException;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.relationtype.repository.RelationTypeRepository;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.edu.pwr.wordnetloom.commontests.lexicon.LexiconForTestsRepository.princenton;
import static pl.edu.pwr.wordnetloom.commontests.partofspeech.PartOfSpeechForTestsRepository.verb;
import static pl.edu.pwr.wordnetloom.commontests.relationtype.RelationTypeForTestsRepository.antonimia;
import static pl.edu.pwr.wordnetloom.commontests.relationtype.RelationTypeForTestsRepository.relationWithId;


public class RelationTypeServicesUTest {

    private RelationTypeServiceBean service;
    private RelationTypeRepository repository;
    private Validator validator;

    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        repository = mock(RelationTypeRepository.class);

        service = new RelationTypeServiceBean();
        service.validator = validator;
        service.relationTypeRepository = repository;
    }

    @Test
    public void findById() {
        when(repository.findById(1L)).thenReturn(relationWithId(antonimia(), 1L));

        RelationType rel = service.findById(1L);
        assertThat(rel, is(notNullValue()));
        assertThat(rel.getId(), is(equalTo(1L)));
        assertThat(rel.getName("pl"), is(equalTo(rel.getName("pl"))));
    }

    @Test(expected = RelationTypeNotFoundException.class)
    public void RelationTypeByIdNotFound() {
        when(repository.findById(1L)).thenReturn(null);

        service.findById(1L);
    }

    @Test(expected = RelationTypeNotFoundException.class)
    public void RelationTypeFullByIdNotFound() {
        when(repository.findFullByRelationType(1L)).thenReturn(null);

        service.findById(1L);
    }


    @Test
    public void shouldSaveRelationType() {

        Set<Lexicon> lex = new HashSet<>();
        lex.add(princenton());

        Set<PartOfSpeech> pos = new HashSet<>();
        pos.add(verb());

        RelationType rt = new RelationType("pl", "kolokacyjnosc", "kol", lex, pos, RelationArgument.SYNSET_RELATION);

        service.save(rt);
    }

    @Test
    public void shouldFailOnNoName() {

        Set<Lexicon> lex = new HashSet<>();
        lex.add(princenton());

        Set<PartOfSpeech> pos = new HashSet<>();
        pos.add(verb());

        RelationType rt = new RelationType("pl", null, "kol", lex, pos, RelationArgument.SYNSET_RELATION);

        saveWithInvalidProperty(rt, "nameStrings");
    }

    private void saveWithInvalidProperty(RelationType type, String failedPropertyName) {
        try {
            service.save(type);
            fail("An error should have been thrown");
        } catch (FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo(failedPropertyName)));
        }
    }

}
