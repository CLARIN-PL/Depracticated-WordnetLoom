package pl.edu.pwr.wordnetloom.client.utils;

import org.junit.Test;
import pl.edu.pwr.wordnetloom.domain.service.DomainServiceRemote;

public class RMIUtilsTest {

    @Test
    public void shouldReturnCorrectLooupNameBean() {

        DomainServiceRemote bean = RMIUtils.lookupForService(DomainServiceRemote.class);
        System.out.println(bean.findAll());

    }
}
