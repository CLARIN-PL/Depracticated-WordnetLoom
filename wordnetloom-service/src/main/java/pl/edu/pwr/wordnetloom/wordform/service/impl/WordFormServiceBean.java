package pl.edu.pwr.wordnetloom.wordform.service.impl;

import pl.edu.pwr.wordnetloom.wordform.control.WordFormFinder;
import pl.edu.pwr.wordnetloom.wordform.model.WordForm;
import pl.edu.pwr.wordnetloom.wordform.repository.WordFormRepository;
import pl.edu.pwr.wordnetloom.wordform.service.WordFormServiceLocal;
import pl.edu.pwr.wordnetloom.wordform.service.WordFormServiceRemote;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
@Remote(WordFormServiceRemote.class)
@Local(WordFormServiceLocal.class)
public class WordFormServiceBean implements WordFormServiceLocal {

    @Inject
    WordFormRepository wordFormRepository;

    @Inject
    WordFormFinder wordFormFinder;

    @Override
    public void deleteAll() {
        wordFormRepository.deleteAll();
    }

    @Override
    public String findFormByLemmaAndTag(String lemma, String tag) {
        List<WordForm> forms = wordFormRepository.findFormByLemmaAndTag(lemma);
        if (forms.size() == 0) {
            Set<WordForm> external = wordFormFinder.getWordForms(lemma);
            save(external);
            forms = new ArrayList<>(external);
        }
        return forms.stream()
                .filter(wf -> wf.getTag().contains(tag))
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .map(WordForm::getForm)
                .orElse(null);
    }

    public void save(Set<WordForm> forms) {
        forms.forEach(wordForm -> wordFormRepository.persist(wordForm));
    }
}
