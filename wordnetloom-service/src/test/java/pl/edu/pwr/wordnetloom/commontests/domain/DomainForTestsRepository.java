package pl.edu.pwr.wordnetloom.commontests.domain;

import java.util.Arrays;
import java.util.List;
import org.junit.Ignore;
import pl.edu.pwr.wordnetloom.domain.model.Domain;

@Ignore
public class DomainForTestsRepository {

    public static Domain bhp() {
        Domain d = new Domain("PL", "bhp", "najwyższe w hierarchii");
        return d;
    }

    public static Domain rsl() {
        Domain d = new Domain("PL", "rsl", "nazwy roślin");
        return d;
    }

    public static Domain zdarz() {
        Domain d = new Domain("PL", "zdarz", "zdarzenia");
        return d;
    }

    public static Domain cczuj() {
        Domain d = new Domain("PL", "cczuj", "czasowniki wyrażające uczucia");
        return d;
    }

    public static Domain jak() {
        Domain d = new Domain("PL", "jak", "przymiotniki jakościowe");
        return d;
    }

    public static Domain domainWithId(final Domain domain, final Long id) {
        domain.setId(id);
        return domain;
    }

    public static List<Domain> allDomains() {
        return Arrays.asList(bhp(), rsl(), zdarz(), cczuj(), jak());
    }
}
