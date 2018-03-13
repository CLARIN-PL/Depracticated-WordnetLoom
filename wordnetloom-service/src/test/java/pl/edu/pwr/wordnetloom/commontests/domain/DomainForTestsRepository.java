package pl.edu.pwr.wordnetloom.commontests.domain;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.domain.model.Domain;

import java.util.Arrays;
import java.util.List;

@Ignore
public class DomainForTestsRepository {

    public static Domain bhp() {
        Domain d = new Domain();
        d.setName(1L);
        d.setDescription(2L);
        return d;
    }

    public static Domain rsl() {
        Domain d = new Domain();
        d.setName(3L);
        d.setDescription(4L);
        return d;
    }

    public static Domain zdarz() {
        Domain d = new Domain();
        d.setName(5L);
        d.setDescription(6L);
        return d;
    }

    public static Domain cczuj() {
        Domain d = new Domain();
        d.setName(7L);
        d.setDescription(8L);
        return d;
    }

    public static Domain jak() {
        Domain d = new Domain();
        d.setName(9L);
        d.setDescription(10L);
        return d;
    }

    public static Domain domainWithId(Domain domain, Long id) {
        domain.setId(id);
        return domain;
    }

    public static List<Domain> allDomains() {
        return Arrays.asList(bhp(), rsl(), zdarz(), cczuj(), jak());
    }
}
