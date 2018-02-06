package pl.edu.pwr.wordnetloom.sense.repository;

import pl.edu.pwr.wordnetloom.common.repository.Specification;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.sense.dto.SenseCriteriaDTO;
import pl.edu.pwr.wordnetloom.word.model.Word;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class SenseSpecification {

    public static Specification<Sense> filter(SenseCriteriaDTO dto) {

        return (root, query, cb) -> {

            List<Predicate> criteria = new ArrayList<>();

            if (dto.getLemma() != null && !dto.getLemma().isEmpty()) {
                criteria.add(byLemma(dto.getLemma()).toPredicate(root, query, cb));
            }

            if (dto.getVariant() != null) {
                criteria.add(byVarinat(dto.getVariant()).toPredicate(root, query, cb));
            }

            if (dto.getPartOfSpeechId() != null) {
                criteria.add(byPartOfSpeech(dto.getPartOfSpeechId()).toPredicate(root, query, cb));
            }

            if (dto.getDomainId() != null) {
                criteria.add(byDomain(dto.getDomainId()).toPredicate(root, query, cb));
            }

            if (dto.getSynsetId() != null) {
                criteria.add(bySynsetId(dto.getSynsetId()).toPredicate(root, query, cb));
            }

            if (dto.getLexicons() != null && dto.getLexicons().size() > 0) {
                criteria.add(byLexiconIds(dto.getLexicons()).toPredicate(root, query, cb));
            }

            return cb.and(criteria.toArray(new Predicate[criteria.size()]));
        };
    }

    public static Specification<Sense> byPartOfSpeech(Long posId) {
        return (root, query, cb) -> cb.equal(root.get("partOfSpeech").get("id"), posId);
    }

    public static Specification<Sense> byDomain(Long domainId) {
        return (root, query, cb) -> cb.equal(root.get("domain").get("id"), domainId);
    }

    public static Specification<Sense> byWord(Word word) {
        return (root, query, cb) -> cb.equal(root.get("word"), word);
    }

    public static Specification<Sense> byLemma(String lemma) {
        return (root, query, cb) -> cb.equal(root.get("word").get("word"), lemma);
    }

    public static Specification<Sense> byLemmaLike(String lemma) {
        return (root, query, cb) -> cb.like(root.get("word").get("word"), lemma);
    }

    public static Specification<Sense> byLemmaLikeContains(String lemma) {
        if (lemma == null || lemma.isEmpty()) {
            return byLemmaLike("%");
        } else {
            return byLemmaLike("%" + lemma + "%");
        }
    }

    public static Specification<Sense> bySynsetId(Long id) {
        return (root, query, cb) -> cb.equal(root.get("synset").get("id"), id);
    }

    public static Specification<Sense> byVarinat(Integer variant) {
        return (root, query, cb) -> cb.equal(root.get("variant"), variant);
    }

    public static Specification<Sense> byLexicon(Long lexiconId) {
        return (root, query, cb) -> cb.equal(root.get("lexicon").get("id"), lexiconId);
    }

    public static Specification<Sense> byLexiconId(Long id) {
        return (root, query, cb) -> cb.equal(root.get("lexicon").get("id"), id);
    }

    public static Specification<Sense> byLexiconIds(List<Long> ids) {
        return (root, query, cb) -> root.get("lexicon").get("id").in(ids);
    }

}
