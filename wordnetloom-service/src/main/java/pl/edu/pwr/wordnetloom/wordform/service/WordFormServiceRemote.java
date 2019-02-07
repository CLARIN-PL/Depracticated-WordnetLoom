package pl.edu.pwr.wordnetloom.wordform.service;

public interface WordFormServiceRemote {

    void deleteAll();

    String findFormByLemmaAndTag(String lemma, String tag);

}
