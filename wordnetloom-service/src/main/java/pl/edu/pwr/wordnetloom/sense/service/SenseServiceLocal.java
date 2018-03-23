package pl.edu.pwr.wordnetloom.sense.service;

import pl.edu.pwr.wordnetloom.common.filter.SearchFilter;
import pl.edu.pwr.wordnetloom.sense.dto.SenseJson;

import java.util.List;

public interface SenseServiceLocal extends SenseServiceRemote {

    List<SenseJson> findByFilter(SearchFilter searchFilter);

    long countByFilter(SearchFilter searchFilter);
}
