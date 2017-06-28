package pl.edu.pwr.wordnetloom.word.service;

import java.util.List;
import javax.ejb.Remote;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Remote
public interface WordServiceRemote {

    Word findByWord(String word);
    
    Word findById(Long id);
    
    Integer countByWord(String word);
    
    List<Word> findAll();
    
    Word add(Word word);
}
