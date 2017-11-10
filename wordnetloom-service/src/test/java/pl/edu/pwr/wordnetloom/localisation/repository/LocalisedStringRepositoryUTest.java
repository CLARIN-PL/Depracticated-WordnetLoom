package pl.edu.pwr.wordnetloom.localisation.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedKey;
import pl.edu.pwr.wordnetloom.localisation.model.LocalisedString;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static pl.edu.pwr.wordnetloom.commontests.localisation.LocalistaionForTestsRepository.allStrings;
import static pl.edu.pwr.wordnetloom.commontests.localisation.LocalistaionForTestsRepository.nameString;

public class LocalisedStringRepositoryUTest extends TestBaseRepository {

    private LocalisedStringRepository repository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        repository = new LocalisedStringRepository();
        repository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void saveLocalisedStringAndFindIt() {
        LocalisedKey nameId = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(nameString()).getKey();
        });

        assertThat(nameId, is(notNullValue()));

        LocalisedString string = repository.findByKey(nameId);
        assertThat(string, is(notNullValue()));
        assertThat(string.getKey().getLanguage(), is(equalTo(nameString().getKey().getLanguage())));
        assertThat(string.getValue(), is(equalTo(nameString().getValue())));
    }

    @Test
    public void saveLanguageVariantToExistingString() {
        LocalisedKey nameId1 = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(nameString()).getKey();
        });

        assertThat(nameId1, is(notNullValue()));

        LocalisedString string1 = repository.findByKey(nameId1);

        LocalisedString toSave = new LocalisedString();
        toSave.addKey(string1.getKey().getId(), "en");
        toSave.setValue("name");

        LocalisedKey nameId2 = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(toSave).getKey();
        });
        assertThat(nameId2, is(notNullValue()));
        LocalisedString string2 = repository.findByKey(nameId2);

        assertThat(string1, is(notNullValue()));
        assertThat(string2, is(notNullValue()));
        assertThat(string1.getKey().getLanguage(), is(equalTo(nameString().getKey().getLanguage())));
        assertThat(string1.getValue(), is(equalTo(nameString().getValue())));
        assertThat(string2.getKey().getLanguage(), is(equalTo("en")));
        assertThat(string2.getValue(), is(equalTo("name")));
    }

    @Test
    public void findAllByLanguage() {

        dbCommandExecutor.executeCommand(() -> {
            allStrings().forEach(repository::persist);
            return null;
        });

        List<LocalisedString> pl = repository.findAllByLanguage("pl");
        List<LocalisedString> en = repository.findAllByLanguage("en");

        assertThat(pl.size(), equalTo(3));
        assertThat(en.size(), equalTo(2));
    }

}
