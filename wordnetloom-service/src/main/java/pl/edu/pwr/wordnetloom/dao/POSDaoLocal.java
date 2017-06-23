package pl.edu.pwr.wordnetloom.dao;

import java.util.List;
import javax.ejb.Local;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

@Local
public interface POSDaoLocal {

    PartOfSpeech getPOSbyID(Long index);

    List<PartOfSpeech> getAllPOSes(List<Long> usedLexicons);

    List<PartOfSpeech> getAllPOSes();

}
