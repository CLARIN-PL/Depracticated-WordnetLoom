package pl.edu.pwr.wordnetloom.lexicon.service;

import java.util.List;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

public interface LexiconServiceRemote {

    Lexicon findById(Long id);

    List<Lexicon> findAllByLexicon(List<Long> lexiconIds);

    List<Lexicon> findAll();

    List<Long> findAllLexiconIds();

    Lexicon add(Lexicon lexicon);

    String testUser();
    
}
