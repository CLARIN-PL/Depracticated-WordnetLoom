package pl.edu.pwr.wordnetloom.lexicon.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

@Remote
public interface LexiconServiceRemote {

    Lexicon findById(Long id);

    List<Lexicon> findAllByLexicon(List<Long> lexiconIds);

    List<Lexicon> findAll();

    Lexicon add(Lexicon lexicon);
}
