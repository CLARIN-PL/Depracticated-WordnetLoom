package pl.edu.pwr.wordnetloom.service;

import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;

@Remote
public interface TrackerServiceRemote {

    void insertedLexicalUnit(Sense sense, String comment, String owner);

    void updatedLexicalUnit(Sense sense, String comment, String owner);
}
