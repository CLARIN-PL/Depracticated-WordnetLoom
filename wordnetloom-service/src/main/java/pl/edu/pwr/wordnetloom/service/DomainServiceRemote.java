package pl.edu.pwr.wordnetloom.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.domain.model.Domain;

@Remote
public interface DomainServiceRemote {

    List<Domain> getAllDomains(List<Long> lexicons);

    Domain getDomainByID(Long id);

}
