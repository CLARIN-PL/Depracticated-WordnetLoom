package pl.edu.pwr.wordnetloom.tracker;

import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.tracker.model.Tracker;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

@Local
public interface TrackerDaoLocal {

    void insertedLexicalUnit(Sense sense, String comment, String owner);

    void deletedLexicalUnit(Sense sense, String comment, String owner);

    void updatedLexicalUnit(Sense sense, String comment, String owner);

}
