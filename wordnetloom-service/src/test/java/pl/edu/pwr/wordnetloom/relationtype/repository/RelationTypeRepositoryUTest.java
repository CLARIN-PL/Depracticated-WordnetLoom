package pl.edu.pwr.wordnetloom.relationtype.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.commontests.utils.TestBaseRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pl.edu.pwr.wordnetloom.commontests.lexicon.LexiconForTestsRepository.allLexicons;
import static pl.edu.pwr.wordnetloom.commontests.partofspeech.PartOfSpeechForTestsRepository.allPartOfSpeechs;
import static pl.edu.pwr.wordnetloom.commontests.relationtype.RelationTypeForTestsRepository.*;

@Ignore
public class RelationTypeRepositoryUTest extends TestBaseRepository {

    private RelationTypeRepository relationTypeRepository;

    @Before
    public void initTestCase() {
        initializeTestDB();

        relationTypeRepository = new RelationTypeRepository();
        relationTypeRepository.em = em;

        loadLexiconsAndPartsOfSpeech();
    }

    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    @Test
    public void shouldSaveRelationtype() {

        RelationType ant = createAntonimia();
        RelationType expect = relationTypeRepository.findById(1l);

        assertThat(ant.getId(), equalTo(expect.getId()));
    }

    @Test
    public void findSenseRelationHigestLeafs() {

        createAntonimia();
        createAspektowosc();
        createRola();
        createHolonimia();

        List<RelationType> top = relationTypeRepository.findHighestLeafs(RelationArgument.SENSE_RELATION);

        assertThat(top.size(), equalTo(3));
    }

    @Test
    public void findSynsetRelationHigestLeafs() {

        createRola();
        createHolonimia();

        List<RelationType> top = relationTypeRepository.findHighestLeafs(RelationArgument.SYNSET_RELATION);

        assertThat(top.size(), equalTo(1));
    }

    @Test
    public void findSenseRelationChildLeafs() {

        createAntonimia();
        createAspektowosc();
        createRola();
        createHolonimia();

        List<RelationType> child = relationTypeRepository.findLeafs(RelationArgument.SENSE_RELATION);

        assertThat(child.size(), equalTo(5));
    }

    @Test
    public void findSynsetRelationChildLeafs() {

        createAntonimia();
        createHolonimia();
        List<RelationType> child = relationTypeRepository.findLeafs(RelationArgument.SYNSET_RELATION);

        assertThat(child.size(), equalTo(2));
    }

    @Test
    public void findReverseRelation() {

        createAspektowosc();

        List<RelationType> child = relationTypeRepository.findLeafs(RelationArgument.SENSE_RELATION);
        assertThat(child.size(), equalTo(2));

        RelationType ndk_dk = child.stream()
                .filter(r -> r.getName().equals(aspektowosc_czysta_NDK_DK().getName()))
                .findFirst().get();
        RelationType reverse = relationTypeRepository.findReverseByRelationType(ndk_dk.getId());

        assertThat(reverse.getName(), is(equalTo(aspektowosc_czysta_DK_NDK().getName())));
    }

    @Test
    public void shouldDeleteRelationTypeWithChildren() {

        createAspektowosc();
        List<RelationType> top = relationTypeRepository.findHighestLeafs(RelationArgument.SENSE_RELATION);
        assertThat(top.size(), equalTo(1));

        List<RelationType> child = relationTypeRepository.findLeafs(RelationArgument.SENSE_RELATION);
        assertThat(child.size(), equalTo(2));

        RelationType toRemove = top.stream().findFirst().get();

        dbCommandExecutor.executeCommand(() -> {
            relationTypeRepository.deleteRelationWithChilds(toRemove);
            return null;
        });

        List<RelationType> result = relationTypeRepository.findAll("id");
        assertThat(result.size(), equalTo(0));
    }

    @Test
    public void findFullByRelationType() {

        createAntonimia();

        RelationType ant = relationTypeRepository.findById(1l);
        RelationType expect = relationTypeRepository.findByIdWithDependencies(ant.getId());

        assertThat(ant.getId(), equalTo(expect.getId()));
        assertThat(ant.getName(), is(equalTo(antonimia().getName())));
        assertThat(ant.getDescription(), is(equalTo(antonimia().getDescription())));
        assertThat(ant.getDisplayText(), is(equalTo(antonimia().getDisplayText())));
        assertThat(ant.getShortDisplayText(), is(equalTo(antonimia().getShortDisplayText())));
    }

    private RelationType createAntonimia() {

        RelationType anto = normalizeDependencies(antonimia(), em);

        dbCommandExecutor.executeCommand(() -> {
            relationTypeRepository.persist(anto);
            return null;
        });

        return anto;
    }

    private void createHiperonimiaWithHiponimia() {

        RelationType hipero = normalizeDependencies(hiperonimia(), em);
        hipero.setAutoReverse(true);

        dbCommandExecutor.executeCommand(() -> {
            return relationTypeRepository.persist(hiperonimia());
        });

        RelationType hipo = normalizeDependencies(hiponimia(), em);
        hipo.setAutoReverse(true);

        dbCommandExecutor.executeCommand(() -> {
            return relationTypeRepository.persist(hiponimia());
        });

        dbCommandExecutor.executeCommand(() -> {

            hipero.setReverse(hipo);
            hipo.setReverse(hipero);
            relationTypeRepository.update(hipero);
            relationTypeRepository.update(hipo);
            return null;
        });

    }

    private void createHolonimia() {

        RelationType holo = normalizeDependencies(holonimia(), em);

        dbCommandExecutor.executeCommand(() -> {
            return relationTypeRepository.persist(holo);
        });

        RelationType msc = normalizeDependencies(holonimia_miejsce(), em);

        dbCommandExecutor.executeCommand(() -> {
            return relationTypeRepository.persist(msc);
        });

        RelationType porc = normalizeDependencies(holonimia_porcja(), em);
        dbCommandExecutor.executeCommand(() -> {
            return relationTypeRepository.persist(porc);
        });

        dbCommandExecutor.executeCommand(() -> {
            msc.setParent(holo);
            porc.setParent(holo);
            relationTypeRepository.update(msc);
            relationTypeRepository.update(porc);
            return null;
        });

    }

    private void createRola() {

        RelationType rola = normalizeDependencies(rola(), em);

        RelationType agens = normalizeDependencies(rola_agens(), em);

        RelationType pacjens = normalizeDependencies(rola_pacjens(), em);

        dbCommandExecutor.executeCommand(() -> {
            relationTypeRepository.persist(rola);
            return null;
        });

        agens.setParent(rola);
        pacjens.setParent(rola);

        dbCommandExecutor.executeCommand(() -> {
            relationTypeRepository.persist(agens);
            relationTypeRepository.persist(pacjens);
            return null;
        });


    }

    private void createAspektowosc() {

        RelationType aspektowsc = normalizeDependencies(aspektowosc(), em);
        RelationType DK_NDK = normalizeDependencies(aspektowosc_czysta_DK_NDK(), em);
        RelationType NDK_DK = normalizeDependencies(aspektowosc_czysta_NDK_DK(), em);

        dbCommandExecutor.executeCommand(() -> {
            relationTypeRepository.persist(aspektowsc);
            relationTypeRepository.persist(DK_NDK);
            relationTypeRepository.persist(NDK_DK);
            return null;
        });

        DK_NDK.setAutoReverse(Boolean.TRUE);
        DK_NDK.setParent(aspektowsc);
        DK_NDK.setReverse(NDK_DK);

        NDK_DK.setAutoReverse(Boolean.TRUE);
        NDK_DK.setParent(aspektowsc);
        NDK_DK.setReverse(DK_NDK);

        dbCommandExecutor.executeCommand(() -> {
            relationTypeRepository.update(DK_NDK);
            relationTypeRepository.update(NDK_DK);
            return null;
        });

    }

    private void loadLexiconsAndPartsOfSpeech() {
        dbCommandExecutor.executeCommand(() -> {
            allLexicons().forEach(em::persist);
            allPartOfSpeechs().forEach(em::persist);
            return null;
        });
    }
}
