package pl.edu.pwr.wordnetloom.corpusexample.service.impl;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;
import pl.edu.pwr.wordnetloom.corpusexample.repository.CorpusExampleRepository;
import pl.edu.pwr.wordnetloom.corpusexample.service.CorpusExampleServiceLocal;
import pl.edu.pwr.wordnetloom.corpusexample.service.CorpusExampleServiceRemote;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Stateless
@Local(CorpusExampleServiceLocal.class)
@Remote(CorpusExampleServiceRemote.class)
public class CorpusExampleServiceBean implements CorpusExampleServiceLocal {

    @Inject
    CorpusExampleRepository corpusExampleRepository;

    @Override
    public List<CorpusExample> findCorpusExamplesByWord(Word word) {
        return findCorpusExamplesByWord(word);
    }

}
