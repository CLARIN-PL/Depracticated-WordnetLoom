package pl.edu.pwr.wordnetloom.client.plugins.systems.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections15.map.HashedMap;
import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.client.systems.models.GenericListModel;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.word.model.Word;

public class GenericListModels {

//    private List<Sense> senses;
//    private GenericListModel<Sense> genericListModel = new GenericListModel<Sense>();
//
//    @Before
//    public void init() {
//        senses = new ArrayList<Sense>();
//
//        Sense s1 = new Sense();
//        s1.setLemma(new Word("wiadro"));
//        s1.setSenseNumber(1);
//        SenseToSynset sts1 = new SenseToSynset();
//        sts1.setIdSense(24l);
//        sts1.setIdSynset(30l);
//        sts1.setSenseIndex(0);
//        s1.setSenseToSynset(sts1);
//
//        Sense s2 = new Sense();
//        s2.setLemma(new Word("kubeł"));
//        s2.setSenseNumber(1);
//        SenseToSynset sts2 = new SenseToSynset();
//        sts2.setIdSense(26l);
//        sts2.setIdSynset(30l);
//        sts2.setSenseIndex(1);
//        s2.setSenseToSynset(sts2);
//
//        Sense s3 = new Sense();
//        s3.setLemma(new Word("wiadro 2"));
//        s3.setSenseNumber(1);
//        SenseToSynset sts3 = new SenseToSynset();
//        sts3.setIdSense(27l);
//        sts3.setIdSynset(33l);
//        sts3.setSenseIndex(0);
//        s3.setSenseToSynset(sts3);
//
//        senses.add(s1);
//        senses.add(s2);
//        senses.add(s3);
//    }
//
//    @Test
//    public void shouldReturnCollectionWitTwoSynsets() {
//        //given
//        //when
//        List<Sense> after = (List<Sense>) genericListModel.buildSenseCollectionAsSynstes(senses, "");
//        //then
//        assertThat(after.size()).isEqualTo(2);
//    }
//
//    @Test
//    public void shouldReturnEmptyListWhenSenseListEmpty() {
//        //given
//        List<Sense> given = new ArrayList<Sense>();
//        //when
//        List<Sense> after = (List<Sense>) genericListModel.buildSenseCollectionAsSynstes(given, "");
//        //then
//        assertThat(after).isEmpty();
//    }
//
//    @Test
//    public void shouldReturnEmptylMapWithNamesWhennListEmpty() {
//        //given
//        List<Sense> given = new ArrayList<Sense>();
//        //when
//        Map<Long, String> names = new HashedMap<Long, String>();
//        names.putAll(genericListModel.buildSynsetNameWithContainedUnits(given));
//        //then
//        assertThat(names).isEmpty();
//    }
//
//    @Test
//    public void shouldeReturnMapWithSensesWhichAreASynset() {
//        //when
//        Map<Long, String> names = new HashedMap<Long, String>();
//        names.putAll(genericListModel.buildSynsetNameWithContainedUnits(senses));
//        //then
//        assertThat(names.size()).isEqualTo(2);
//    }
}
