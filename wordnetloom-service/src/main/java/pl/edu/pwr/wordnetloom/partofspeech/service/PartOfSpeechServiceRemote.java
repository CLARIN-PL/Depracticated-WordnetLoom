package pl.edu.pwr.wordnetloom.partofspeech.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

@Remote
public interface PartOfSpeechServiceRemote {

    PartOfSpeech findById(Long id);

    List<PartOfSpeech> findByLexicon(Lexicon lexicon);

    List<PartOfSpeech> findAll();

    PartOfSpeech add(PartOfSpeech partOfSpeech);
}
