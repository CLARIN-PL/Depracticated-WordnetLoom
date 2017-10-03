package pl.edu.pwr.wordnetloom.service;

import javax.ejb.Remote;

import pl.edu.pwr.wordnetloom.model.Sense;

@Remote
public interface TrackerServiceRemote {

	public void insertedLexicalUnit(Sense sense, String comment, String owner);
	public void updatedLexicalUnit(Sense sense, String comment, String owner);
}
