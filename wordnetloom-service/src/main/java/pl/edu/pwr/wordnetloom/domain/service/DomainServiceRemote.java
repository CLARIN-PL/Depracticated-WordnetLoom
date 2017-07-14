package pl.edu.pwr.wordnetloom.domain.service;

import java.util.List;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

public interface DomainServiceRemote {

    Domain findById(Long id);

    List<Domain> findAllByLexiconAndPartOfSpeech(Lexicon lexicon, PartOfSpeech pos);

    List<Domain> findAll();

    Domain add(Domain domain);
}
