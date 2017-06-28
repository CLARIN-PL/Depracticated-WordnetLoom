package pl.edu.pwr.wordnetloom.tracker;

import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

@Remote
public interface TrackerServiceRemote {

    void insertedLexicalUnit(Sense sense, String comment, String owner);

    void updatedLexicalUnit(Sense sense, String comment, String owner);
}
