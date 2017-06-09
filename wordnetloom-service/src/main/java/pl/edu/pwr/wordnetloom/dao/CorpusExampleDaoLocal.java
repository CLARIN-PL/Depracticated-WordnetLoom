package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.model.wordnet.CorpusExample;
import pl.edu.pwr.wordnetloom.model.wordnet.Word;

@Local
public interface CorpusExampleDaoLocal {

    List<CorpusExample> getCorpusExamplesFor(Word word);

}
