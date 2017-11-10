package pl.edu.pwr.wordnetloom.domain.service;

import pl.edu.pwr.wordnetloom.domain.model.Domain;

import java.util.List;

public interface DomainServiceRemote {

    Domain findById(Long id);

    List<Domain> findAllByLexiconAndPartOfSpeech(Long lexiconId, Long partOfSpeechId);

    List<Domain> findAll();

    Domain add(Domain domain);
}
