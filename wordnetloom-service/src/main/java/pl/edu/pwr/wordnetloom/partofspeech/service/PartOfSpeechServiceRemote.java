package pl.edu.pwr.wordnetloom.partofspeech.service;

import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.util.List;

public interface PartOfSpeechServiceRemote {

    PartOfSpeech findById(Long id);

    List<PartOfSpeech> findByLexicon(Lexicon lexicon);

    List<PartOfSpeech> findAll();

    PartOfSpeech add(PartOfSpeech partOfSpeech);
}
