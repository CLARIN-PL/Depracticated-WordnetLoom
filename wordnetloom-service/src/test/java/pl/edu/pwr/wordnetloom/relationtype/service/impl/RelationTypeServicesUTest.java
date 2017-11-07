package pl.edu.pwr.wordnetloom.relationtype.service.impl;

import org.junit.Before;
import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.relationtype.repository.RelationTypeRepository;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.mockito.Mockito.mock;

@Ignore
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

 /*   @Test
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
*//*

        when(service.findById(anyLong())).thenReturn(cleanCode().getCategory());
        when(authorServices.findById(anyLong())).thenReturn(robertMartin());
        when(bookRepository.add(bookEq(cleanCode()))).thenReturn(bookWithId(cleanCode(), 1L));

        Book bookAdded = bookServices.add(cleanCode());
        assertThat(bookAdded.getId(), equalTo(1L));
*//*

        RelationType rt =    new RelationType("pl", "kolokacyjnosc", "kol", lex, pos, RelationArgument.SYNSET_RELATION);

        rt = service.save(rt);
        assertThat(rt.getId(), equalTo(1L));
    }

    @Test
    public void shouldFailOnNoName() {

        RelationType rt = new RelationType("pl", null, "kol", lex, pos, RelationArgument.SYNSET_RELATION);

        saveWithInvalidProperty(rt, "nameStrings");
    }

    @Test
    public void shouldFailOnNoLocaleForName() {

        RelationType rt = new RelationType(null, "test", "kol", lex, pos, RelationArgument.SYNSET_RELATION);

        saveWithInvalidProperty(rt, "nameStrings");
    }

    @Test
    public void shouldFailOnNoShortDisplay() {


        RelationType rt = new RelationType("pl", "test", null, lex, pos, RelationArgument.SYNSET_RELATION);

        saveWithInvalidProperty(rt, "shortDisplayStrings");
    }

    @Test
    public void shouldFailOnLexiconIsNull() {

        RelationType rt = new RelationType("pl", "test", "kol", null, pos, RelationArgument.SYNSET_RELATION);

        saveWithInvalidProperty(rt, "lexicons");
    }

    @Test
    public void shouldFailOnLexiconIsEmpty() {


        RelationType rt = new RelationType("pl", "test", "kol", lex, pos, RelationArgument.SYNSET_RELATION);

        saveWithInvalidProperty(rt, "lexicons");
    }

    private void saveWithInvalidProperty(RelationType type, String failedPropertyName) {
        try {
            service.save(type);
            fail("An error should have been thrown");
        } catch (FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo(failedPropertyName)));
        }
    }*/

}
