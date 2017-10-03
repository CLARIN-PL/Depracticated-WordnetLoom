package pl.edu.pwr.wordnetloom.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.dao.TrackerDaoLocal;
import pl.edu.pwr.wordnetloom.model.Sense;

@Stateless
public class TrackerServiceBean implements TrackerServiceRemote{
	
	@EJB private TrackerDaoLocal local;
	
	@Override
	public void insertedLexicalUnit(Sense sense, String comment, String owner){
		local.insertedLexicalUnit(sense, comment, owner);
	}

	@Override
	public void updatedLexicalUnit(Sense sense, String comment, String owner) {
		local.updatedLexicalUnit(sense, comment, owner);
	}
	
}
