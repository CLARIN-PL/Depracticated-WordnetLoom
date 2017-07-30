package pl.edu.pwr.wordnetloom.relationtype.repository;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static pl.edu.pwr.wordnetloom.commontests.relationtype.SenseRelationTypeForTestsRepository.*;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.SenseRelationType;

public class SenseRelationTypeRepositoryUTest extends TestBaseRepository {

    private SenseRelationTypeRepository repository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        repository = new SenseRelationTypeRepository();
        repository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void findRelationHigestLeafs() {

        createAntonimia();
        createAspektowosc();
        createRola();
        List<SenseRelationType> top = repository.findHighestLeafs(new ArrayList());

        assertThat(top.size(), equalTo(3));
    }

    @Test
    public void findRelationChildLeafs() {

        createAntonimia();
        createAspektowosc();
        createRola();
        List<SenseRelationType> child = repository.findLeafs(new ArrayList());

        assertThat(child.size(), equalTo(5));
    }

    @Test
    public void findReverseRelation() {

        createAspektowosc();
        List<SenseRelationType> child = repository.findLeafs(new ArrayList());
        assertThat(child.size(), equalTo(2));

        SenseRelationType ndk_dk = child.stream()
                .filter(r -> r.getName("pl").equals(aspektowosc_czysta_NDK_DK().getName("pl")))
                .findFirst().get();
        SenseRelationType reverse = repository.findReverseByRelationType(ndk_dk);

        assertThat(reverse.getName("pl"), is(equalTo(aspektowosc_czysta_DK_NDK().getName("pl"))));
    }

    @Test
    public void shouldDeleteRelationTypeWihhChildren() {

        createAspektowosc();
        List<SenseRelationType> top = repository.findHighestLeafs(new ArrayList());
        assertThat(top.size(), equalTo(1));

        List<SenseRelationType> child = repository.findLeafs(new ArrayList());
        assertThat(child.size(), equalTo(2));

        SenseRelationType toRemove = top.stream().findFirst().get();

        dbCommandExecutor.executeCommand(() -> {
            repository.deleteRelationWithChilds(toRemove);
            return null;
        });

        List<SenseRelationType> result = repository.findAll("id");
        assertThat(result.size(), equalTo(0));
    }

    @Test
    public void findFullByRelationType() {
        createAntonimia();

        SenseRelationType ant = repository.findById(1l);
        SenseRelationType expect = repository.findFullByRelationType(ant);

        assertThat(ant.getId(), equalTo(expect.getId()));
        assertThat(ant.getName("pl"), is(equalTo(antonimia().getName("pl"))));
        assertThat(ant.getDescription("pl"), is(equalTo(antonimia().getDescription("pl"))));
        assertThat(ant.getDisplayText("pl"), is(equalTo(antonimia().getDisplayText("pl"))));
        assertThat(ant.getShortDisplayText("pl"), is(equalTo(antonimia().getShortDisplayText("pl"))));
    }

    private void createAntonimia() {
        SenseRelationType anto = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(antonimia());
        });
        anto.setAutoReverse(Boolean.TRUE);
        anto.setReverse(anto);

        dbCommandExecutor.executeCommand(() -> {
            return repository.update(anto);
        });
    }

    private void createRola() {
        SenseRelationType rola = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(rola());
        });

        SenseRelationType agens = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(rola_agens());
        });

        SenseRelationType pacjens = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(rola_pacjens());
        });

        agens.setParent(rola);
        pacjens.setParent(rola);

        dbCommandExecutor.executeCommand(() -> {
            return repository.update(agens);
        });

        dbCommandExecutor.executeCommand(() -> {
            return repository.update(pacjens);
        });
    }

    private void createAspektowosc() {
        SenseRelationType aspektowsc = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(aspektowosc());
        });

        SenseRelationType DK_NDK = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(aspektowosc_czysta_DK_NDK());
        });

        SenseRelationType NDK_DK = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(aspektowosc_czysta_NDK_DK());
        });

        DK_NDK.setAutoReverse(Boolean.TRUE);
        DK_NDK.setParent(aspektowsc);
        DK_NDK.setReverse(NDK_DK);

        NDK_DK.setAutoReverse(Boolean.TRUE);
        NDK_DK.setParent(aspektowsc);
        NDK_DK.setReverse(DK_NDK);

        dbCommandExecutor.executeCommand(() -> {
            return repository.update(DK_NDK);
        });

        dbCommandExecutor.executeCommand(() -> {
            return repository.update(NDK_DK);
        });
    }
}
