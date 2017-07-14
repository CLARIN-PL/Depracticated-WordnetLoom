package pl.edu.pwr.wordnetloom.synset.service;

import pl.edu.pwr.wordnetloom.synset.model.Synset;

public interface SynsetServiceRemote {

    void clone(Synset synset);

    boolean delete(Synset synset);

}
