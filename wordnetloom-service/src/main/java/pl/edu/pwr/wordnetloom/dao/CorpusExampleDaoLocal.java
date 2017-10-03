package pl.edu.pwr.wordnetloom.dao;

import java.util.List;

import javax.ejb.Local;

import pl.edu.pwr.wordnetloom.model.CorpusExample;
import pl.edu.pwr.wordnetloom.model.Word;

@Local
public interface CorpusExampleDaoLocal {

	public List<CorpusExample> getCorpusExamplesFor(Word word);

}
