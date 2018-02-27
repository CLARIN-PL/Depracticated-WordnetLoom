package pl.edu.pwr.wordnetloom.client.systems.managers;

import org.junit.Ignore;
import org.junit.Test;
import pl.edu.pwr.wordnetloom.dictionary.model.Dictionary;
import pl.edu.pwr.wordnetloom.dictionary.model.EmotionDictionary;
import pl.edu.pwr.wordnetloom.dictionary.model.RegisterDictionary;

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
        dics.add(new RegisterDictionary());
        dics.add(new RegisterDictionary());
        dics.add(new EmotionDictionary());

        DictionaryManager.getInstance().dictionaries = dics;

        List<RegisterDictionary> test = DictionaryManager.getInstance().getDictionaryByClassName(RegisterDictionary.class);

        assertThat(test, is(notNullValue()));
        assertThat(test.size(), equalTo(2));
    }
}
