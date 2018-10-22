package pl.edu.pwr.wordnetloom.client.systems.managers;

import org.junit.Test;
import pl.edu.pwr.wordnetloom.client.commontests.RelationTypeForTestsRepository;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationArgument;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static pl.edu.pwr.wordnetloom.client.commontests.RelationTypeForTestsRepository.*;

public class RelationTypeManagerTest {

    @Test
    public void shouldReturnChildrenForRelationId() {
        RelationTypeManager.getInstance().load(RelationTypeForTestsRepository.allRelations());

        List<RelationType> list = RelationTypeManager.getInstance().getChildren(rola().getId());
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), equalTo(2));
        list.forEach(i -> {
            assertThat(i.getParent().getId(), equalTo(rola().getId()));
        });
    }

    @Test
    public void shouldReturnParentSenseRelations() {
        RelationTypeManager.getInstance().load(RelationTypeForTestsRepository.allRelations());

        List<RelationType> list = RelationTypeManager.getInstance().getParents(RelationArgument.SENSE_RELATION);
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), equalTo(3));
        list.forEach(i -> {
            assertThat(i.getParent(), nullValue());
        });
    }


    @Test
    public void shouldReturnParentSynsetRelations() {
        RelationTypeManager.getInstance().load(RelationTypeForTestsRepository.allRelations());

        List<RelationType> list = RelationTypeManager.getInstance().getParents(RelationArgument.SYNSET_RELATION);
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), equalTo(3));
        list.forEach(i -> {
            assertThat(i.getParent(), nullValue());
        });
    }

    /*@Test
    public void shouldReturnRelationsWithoutProxyParentRelations() {

        RelationTypeManager.getInstance().load(RelationTypeForTestsRepository.allRelations());
        List<RelationType> list = RelationTypeManager.getInstance().getRelationsWithoutProxyParent(RelationArgument.SENSE_RELATION);
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), equalTo(5));
        assertThat(list, not(hasItem(aspektowosc())));
        assertThat(list, not(hasItem(rola())));
        assertThat(list, hasItems(aspektowosc_czysta_NDK_DK(), aspektowosc_czysta_DK_NDK(), rola_agens(), rola_pacjens(), antonimia()));
    }*/
}