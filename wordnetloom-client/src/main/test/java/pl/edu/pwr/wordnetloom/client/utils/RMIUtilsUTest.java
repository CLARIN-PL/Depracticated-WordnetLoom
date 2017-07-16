package pl.edu.pwr.wordnetloom.client.utils;

import org.junit.Test;
import pl.edu.pwr.wordnetloom.lexicon.service.LexiconServiceRemote;

public class RMIUtilsUTest {

    @Test
    public void shouldReturnCorrectLooupNameBean() {

       LexiconServiceRemote bean = RMIUtils.lookupForService(LexiconServiceRemote.class);
       System.out.println("Result:" + bean.testUser());

    }
}
