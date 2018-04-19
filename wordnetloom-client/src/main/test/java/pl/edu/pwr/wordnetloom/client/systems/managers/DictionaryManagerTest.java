package pl.edu.pwr.wordnetloom.client.systems.managers;

import org.junit.Ignore;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.dictionary.model.Emotion;
import pl.edu.pwr.wordnetloom.dictionary.model.Register;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class DictionaryManagerTest {

    @Ignore
    @Test
    public void findDictionaryByClass(){

        List<Dictionary> dics = new ArrayList();
        dics.add(new Register());
        dics.add(new Register());
        dics.add(new Emotion());

        DictionaryManager.getInstance().dictionaries = dics;

        List<Register> test = DictionaryManager.getInstance().getDictionaryByClassName(Register.class);

        assertThat(test, is(notNullValue()));
        assertThat(test.size(), equalTo(2));
    }
}
