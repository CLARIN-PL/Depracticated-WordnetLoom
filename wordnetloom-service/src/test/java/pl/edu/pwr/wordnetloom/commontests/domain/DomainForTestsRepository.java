package pl.edu.pwr.wordnetloom.commontests.domain;

import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.domain.model.Domain;

import java.util.Arrays;
import java.util.List;

@Ignore
public class DomainForTestsRepository {

    public static Domain bhp() {
        Domain d = new Domain();
        d.setName(1l);
        d.setDescription(2l);
        return d;
    }

    public static Domain rsl() {
        Domain d = new Domain();
        d.setName(3l);
        d.setDescription(4l);
        return d;
    }

    public static Domain zdarz() {
        Domain d = new Domain();
        d.setName(5l);
        d.setDescription(6l);
        return d;
    }

    public static Domain cczuj() {
        Domain d = new Domain();
        d.setName(7l);
        d.setDescription(8l);
        return d;
    }

    public static Domain jak() {
        Domain d = new Domain();
        d.setName(9l);
        d.setDescription(10l);
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
