package pl.edu.pwr.wordnetloom.corpusexample.service;

import java.util.List;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;
import pl.edu.pwr.wordnetloom.word.model.Word;

public interface CorpusExampleServiceRemote {

    List<CorpusExample> findCorpusExamplesByWord(String word);

}
