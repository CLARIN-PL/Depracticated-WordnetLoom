package pl.edu.pwr.wordnetloom.relationtype.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pl.edu.pwr.wordnetloom.commontests.relationtype.RelationTypeForTestsRepository.*;

public class RelationTypeRepositoryUTest extends TestBaseRepository {

    private RelationTypeRepository repository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        repository = new RelationTypeRepository();
        repository.em = em;
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void shouldSaveRelationtype() {

        RelationType ant = createAntonimia();
        RelationType expect = repository.findById(1l);

        assertThat(ant.getId(), equalTo(expect.getId()));
    }

    @Test
    public void findRelationHigestLeafs() {

        createAntonimia();
        createAspektowosc();
        createRola();
        List<RelationType> top = repository.findHighestLeafs(new ArrayList());

        assertThat(top.size(), equalTo(3));
    }

    @Test
    public void findRelationChildLeafs() {

        createAntonimia();
        createAspektowosc();
        createRola();
        List<RelationType> child = repository.findLeafs(new ArrayList());

        assertThat(child.size(), equalTo(5));
    }

    @Test
    public void findReverseRelation() {

        createAspektowosc();

        List<RelationType> child = repository.findLeafs(new ArrayList());
        assertThat(child.size(), equalTo(2));

        RelationType ndk_dk = child.stream()
                .filter(r -> r.getName("pl").equals(aspektowosc_czysta_NDK_DK().getName("pl")))
                .findFirst().get();
        RelationType reverse = repository.findReverseByRelationType(ndk_dk);

        assertThat(reverse.getName("pl"), is(equalTo(aspektowosc_czysta_DK_NDK().getName("pl"))));
    }

    @Test
    public void shouldDeleteRelationTypeWithChildren() {

        createAspektowosc();
        List<RelationType> top = repository.findHighestLeafs(new ArrayList());
        assertThat(top.size(), equalTo(1));

        List<RelationType> child = repository.findLeafs(new ArrayList());
        assertThat(child.size(), equalTo(2));

        RelationType toRemove = top.stream().findFirst().get();

        dbCommandExecutor.executeCommand(() -> {
            repository.deleteRelationWithChilds(toRemove);
            return null;
        });

        List<RelationType> result = repository.findAll("id");
        assertThat(result.size(), equalTo(0));
    }

    @Test
    public void findFullByRelationType() {
        createAntonimia();

        RelationType ant = repository.findById(1l);
        RelationType expect = repository.findFullByRelationType(ant);

        assertThat(ant.getId(), equalTo(expect.getId()));
        assertThat(ant.getName("pl"), is(equalTo(antonimia().getName("pl"))));
        assertThat(ant.getDescription("pl"), is(equalTo(antonimia().getDescription("pl"))));
        assertThat(ant.getDisplayText("pl"), is(equalTo(antonimia().getDisplayText("pl"))));
        assertThat(ant.getShortDisplayText("pl"), is(equalTo(antonimia().getShortDisplayText("pl"))));
    }

    private RelationType createAntonimia() {
        RelationType anto = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(antonimia());
        });
        anto.setAutoReverse(Boolean.TRUE);
        anto.setReverse(anto);

        dbCommandExecutor.executeCommand(() -> {
            return repository.update(anto);
        });
        return anto;
    }

    private void createRola() {
        RelationType rola = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(rola());
        });

        RelationType agens = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(rola_agens());
        });

        RelationType pacjens = dbCommandExecutor.executeCommand(() -> {
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
        RelationType aspektowsc = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(aspektowosc());
        });

        RelationType DK_NDK = dbCommandExecutor.executeCommand(() -> {
            return repository.persist(aspektowosc_czysta_DK_NDK());
        });

        RelationType NDK_DK = dbCommandExecutor.executeCommand(() -> {
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
