package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.model.wordnet.Domain;

@Local
public interface DomainDaoLocal {

    Domain getDomainByID(Long index);

    List<Domain> getAllDomains(List<Long> lexicons);

}
