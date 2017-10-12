package pl.edu.pwr.wordnetloom.dao;

import javax.persistence.criteria.Join;

import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Word;

public class WordSpecification {

    public static Specification<Sense> byWordLemma(final String lemma) {
        return (root, query, cb) -> {
        	Join<Sense, Word> word = root.join("lemma");
        	return cb.like(cb.lower(word.get("word")) , getContainsLikePattern(lemma));
        };
    }

    public static Specification<Sense> bySenseNumber(final String senseNumber) {
        return (root, query, cb) -> cb.equal(root.get("senseNumber"), senseNumber);
    }

    public static String getContainsLikePattern(String lemma) {
        if (lemma == null || lemma.isEmpty()) {
            return "%";
        } else {
            return lemma .toLowerCase()+ "%";
        }
    }
}

