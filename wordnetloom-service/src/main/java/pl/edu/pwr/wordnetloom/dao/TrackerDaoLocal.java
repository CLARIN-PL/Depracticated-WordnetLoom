package pl.edu.pwr.wordnetloom.dao;

import javax.ejb.Local;

import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.tracker.Tracker;
import pl.edu.pwr.wordnetloom.model.tracker.Tracker.TABLE;

@Local
public interface TrackerDaoLocal {

	Tracker insert(TABLE table, Long tid, String user);
	Tracker remove(TABLE table, Long tid, String user);
	Tracker update(TABLE table, Long tid, String user);
	void insertedLexicalUnit(Sense sense, String comment, String owner);
	void deletedLexicalUnit(Sense sense, String comment, String owner);
	void updatedLexicalUnit(Sense sense, String comment, String owner);
	
}
