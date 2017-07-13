package pl.edu.pwr.wordnetloom.synset.service;

import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Remote
public interface SynsetServiceRemote {

    void clone(Synset synset);

    boolean delete(Synset synset);

}
