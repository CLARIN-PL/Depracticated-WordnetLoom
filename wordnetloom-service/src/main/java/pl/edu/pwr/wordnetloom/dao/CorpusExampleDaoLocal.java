package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Local
public interface CorpusExampleDaoLocal {

    List<CorpusExample> getCorpusExamplesFor(Word word);

}
