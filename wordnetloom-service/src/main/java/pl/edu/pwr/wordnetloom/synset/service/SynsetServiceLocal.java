package pl.edu.pwr.wordnetloom.synset.service;

import pl.edu.pwr.wordnetloom.common.filter.SearchFilter;
import pl.edu.pwr.wordnetloom.common.model.PaginatedData;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

public interface SynsetServiceLocal extends SynsetServiceRemote {
    PaginatedData<Synset> findByFilter(SearchFilter synsetFilter);
}
