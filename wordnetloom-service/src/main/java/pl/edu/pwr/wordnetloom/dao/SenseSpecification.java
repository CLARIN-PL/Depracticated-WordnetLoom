package pl.edu.pwr.wordnetloom.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import pl.edu.pwr.wordnetloom.dto.SenseFilter;
import pl.edu.pwr.wordnetloom.model.Text;
import pl.edu.pwr.wordnetloom.dto.CriteriaDTO;
import pl.edu.pwr.wordnetloom.model.AttributeType;
import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseAttribute;
import pl.edu.pwr.wordnetloom.model.SenseRelation;
import pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech;

public class SenseSpecification {

	public static Specification<Sense> search(final CriteriaDTO dto, final PartOfSpeech pos) {

		return (root, query, cb) -> {

			List<Predicate> criteriaList = new ArrayList<>();

			if (dto.getPartOfSpeech() != null) {
				criteriaList.add(byPartOfSpeechId(dto.getPartOfSpeech().getId()).toPredicate(root, query, cb));
			}
			if (dto.getDomain() != null) {
				criteriaList.add(byDomainId(dto.getDomain().getId()).toPredicate(root, query, cb));
			}

			if (pos != null) {
				criteriaList.add(byUbyPartOfSpeech(pos).toPredicate(root, query, cb));
			}

			if (dto.getLexicon() != null) {
				criteriaList.add(byLexiconId(dto.getLexicon().getId()).toPredicate(root, query, cb));
			}

			if (dto.getLemma() != null && !dto.getLemma().isEmpty()) {
				if(dto.getSearchType() == null)
				{
					criteriaList.add(WordSpecification.byWordLemma(dto.getLemma()).toPredicate(root, query, cb));
				} else {
					switch(dto.getSearchType())
					{
						case Yiddish_Spelling:
							criteriaList.add(SenseYiddishExtensionSpecification.byYddishSpelling(dto.getLemma())
									.toPredicate(root, query, cb)); break;
						case YIVO_Spelling:
							criteriaList.add(SenseYiddishExtensionSpecification.byYivoSpelling(dto.getLemma()).toPredicate(root,
									query, cb)); break;
						case Etymological_root:
							criteriaList.add(SenseYiddishExtensionSpecification.byEtymologicalRoot(dto.getLemma())
									.toPredicate(root, query, cb)); break;
						case Latin_Philological_Transcription:
							criteriaList.add(SenseYiddishExtensionSpecification.byTranscritpiton(dto.getLemma())
									.toPredicate(root, query, cb)); break;
						case Latin_YIVO_Transcription:
							criteriaList.add(SenseYiddishExtensionSpecification.byTranscritpiton(dto.getLemma())
									.toPredicate(root, query, cb)); break;
						case Phonetic_Transcription:
							criteriaList.add(SenseYiddishExtensionSpecification.byTranscritpiton(dto.getLemma())
									.toPredicate(root, query, cb)); break;
						case Prefix:
							criteriaList.add(SenseYiddishExtensionSpecification.byPrefix(dto.getLemma())
									.toPredicate(root, query, cb)); break;
						case Root:
							criteriaList.add(SenseYiddishExtensionSpecification.byRoot(dto.getLemma())
									.toPredicate(root, query, cb)); break;
						case Suffix:
							criteriaList.add(SenseYiddishExtensionSpecification.bySuffix(dto.getLemma())
									.toPredicate(root, query, cb)); break;
						case Interfix:
							criteriaList.add(SenseYiddishExtensionSpecification.byInterfix(dto.getLemma())
									.toPredicate(root, query, cb)); break;
						case Sense_number:
							criteriaList.add(WordSpecification.bySenseNumber(dto.getLemma()).toPredicate(root, query, cb)); break;
					default: 
						throw new IllegalArgumentException();
					}
				}
			}
			
			if(dto.getComment() != null && ! dto.getComment().isEmpty()){
				criteriaList.add(byAttribute(dto.getComment(), AttributeType.COLUMN_DEFINITION_SENSE, Sense.COMMENT).toPredicate(root, query, cb));
			}
			if(dto.getDefinition() != null && ! dto.getDefinition().isEmpty()){
				criteriaList.add(byAttribute(dto.getDefinition(), AttributeType.COLUMN_DEFINITION_SENSE, Sense.DEFINITION).toPredicate(root, query, cb));
			}
			if(dto.getRegister() != null){
				criteriaList.add(byAttribute(dto.getRegister().toString(), AttributeType.COLUMN_DEFINITION_SENSE, Sense.REGISTER).toPredicate(root, query, cb));
			}
			if (dto.getAgeDictionary() != null) {
				criteriaList.add(SenseYiddishExtensionSpecification.byAge(dto.getAgeDictionary().getId())
						.toPredicate(root, query, cb));
			}
			if (dto.getStatusDictionary() != null) {
				criteriaList.add(SenseYiddishExtensionSpecification.byStatus(dto.getStatusDictionary().getId())
						.toPredicate(root, query, cb));
			}
			if (dto.getStyleDictionary() != null) {
				criteriaList.add(SenseYiddishExtensionSpecification.byStyle(dto.getStyleDictionary().getId())
						.toPredicate(root, query, cb));
			}
			if (dto.getLexicalCharacteristicDictionary() != null) {
				criteriaList.add(SenseYiddishExtensionSpecification.byLexicalCharcteristic(dto.getLexicalCharacteristicDictionary().getId())
						.toPredicate(root, query, cb));
			}
			if (dto.getGrammaticalGenderDictionary() != null) {
				criteriaList.add(SenseYiddishExtensionSpecification
						.byGrammaticalGender(dto.getGrammaticalGenderDictionary().getId())
						.toPredicate(root, query, cb));
			}
			if (dto.getEthymology() != null && !dto.getEthymology().isEmpty()) {
				criteriaList.add(SenseYiddishExtensionSpecification.byEtymology(dto.getEthymology()).toPredicate(root,
						query, cb));
			}
			
			if(dto.getSourceDictionary() !=null){
				criteriaList.add(SenseYiddishExtensionSpecification.bySource(dto.getSourceDictionary().getId()).toPredicate(root, query, cb));
			}

			if(dto.getRelation() !=null){
				criteriaList.add(byRelationId(dto.getRelation().getId()).toPredicate(root,
						query, cb));
			}
			if(dto.getYiddishDomain() != null || dto.getDomainModifierDictionary() != null){
				Long modifier = dto.getDomainModifierDictionary() != null ? dto.getDomainModifierDictionary().getId() : null;
				Long domain = dto.getYiddishDomain() != null ? dto.getYiddishDomain().getId() : null;
				criteriaList.add(SenseYiddishExtensionSpecification.bySemanticField(domain, modifier).toPredicate(root, query, cb));
			}
			return cb.and(criteriaList.toArray(new Predicate[criteriaList.size()]));
		};
	}

	public static Specification<Sense> byFilter(final SenseFilter filter) {

		return (root, query, cb) -> {

			List<Predicate> criteriaList = new ArrayList<>();

			if(filter.getLemma() != null && !"".equals(filter.getLemma())){
				criteriaList.add(WordSpecification.byWordLemma(filter.getLemma()).toPredicate(root, query, cb));
			}

			if (filter.getPartOfSpeechId() != null) {
				criteriaList.add(byPartOfSpeechId(filter.getPartOfSpeechId()).toPredicate(root, query, cb));
			}
			if (filter.getDomainId() != null) {
				criteriaList.add(byDomainId(filter.getDomainId()).toPredicate(root, query, cb));
			}

			if (filter.getLexiconId() != null) {
				criteriaList.add(byLexiconId(filter.getLexiconId()).toPredicate(root, query, cb));
			}

			if(filter.getDefinition() != null && ! filter.getDefinition().isEmpty()){
				criteriaList.add(byAttribute(filter.getDefinition(), AttributeType.COLUMN_DEFINITION_SENSE, Sense.DEFINITION).toPredicate(root, query, cb));
			}
			if(filter.getAgeId() != null) {
				criteriaList.add(SenseYiddishExtensionSpecification.byAge(filter.getAgeId())
						.toPredicate(root, query, cb));
			}
			if (filter.getStatusId() != null) {
				criteriaList.add(SenseYiddishExtensionSpecification.byStatus(filter.getStatusId())
						.toPredicate(root, query, cb));
			}
			if (filter.getStyleId() != null) {
				criteriaList.add(SenseYiddishExtensionSpecification.byStyle(filter.getStyleId())
						.toPredicate(root, query, cb));
			}
			if (filter.getLexicalCharacteristicId()!= null) {
				criteriaList.add(SenseYiddishExtensionSpecification.byLexicalCharcteristic(filter.getLexicalCharacteristicId())
						.toPredicate(root, query, cb));
			}
			if (filter.getGrammaticalGenderId() != null) {
				criteriaList.add(SenseYiddishExtensionSpecification
						.byGrammaticalGender(filter.getGrammaticalGenderId())
						.toPredicate(root, query, cb));
			}
			if (filter.getEthymology() != null && !filter.getEthymology().isEmpty()) {
				criteriaList.add(SenseYiddishExtensionSpecification.byEtymology(filter.getEthymology()).toPredicate(root,
						query, cb));
			}
			if(filter.getSourceId() !=null){
				criteriaList.add(SenseYiddishExtensionSpecification.bySource(filter.getSourceId()).toPredicate(root, query, cb));
			}
			if(filter.getYiddishDomainId() != null || filter.getDomainModifierDictionaryId() != null){
				criteriaList.add(SenseYiddishExtensionSpecification.bySemanticField(filter.getYiddishDomainId(), filter.getDomainModifierDictionaryId()).toPredicate(root, query, cb));
			}

			return cb.and(criteriaList.toArray(new Predicate[criteriaList.size()]));
		};
	}

	public static Specification<Sense> byPartOfSpeechId(final Long id) {
		return (root, query, cb) -> {
			Join<Sense, pl.edu.pwr.wordnetloom.model.PartOfSpeech> pos = root.join("partOfSpeech");
			return cb.equal(pos.get("id"), id);
		};
	}

	public static Specification<Sense> byLexiconId(final Long id) {
		return (root, query, cb) -> {
			Join<Sense, Lexicon> lex = root.join("lexicon");
			return cb.equal(lex.get("id"), id);
		};
	}
	
	public static Specification<Sense> byAttribute(final String text, String tableName, String typeName) {
		return (root, query, cb) -> {
			Join<Sense, SenseAttribute> sa = root.join("senseAttributes", JoinType.LEFT);
			Join<SenseAttribute, AttributeType> at = sa.join("type", JoinType.LEFT);
			Join<SenseAttribute, Text> txt = sa.join("value", JoinType.LEFT);
			Join<AttributeType, Text> att = at.join("typeName", JoinType.LEFT);
			
			return cb.and(cb.equal(txt.get("text"), text), cb.equal(at.get("tableName"), tableName), cb.equal(att.get("typeName"), typeName));
		};
	}

	public static Specification<Sense> byLexiconId(final List<Long> ids) {
		return (root, query, cb) -> {
			Join<Sense, Lexicon> lex = root.join("lexicon");
			return lex.get("id").in(ids);
		};
	}

	public static Specification<Sense> byRelationId(final Long id) {
		return (root, query, cb) -> {

			CriteriaQuery<Long> cbs = cb.createQuery(Long.class);
			Subquery<Long> subquery = cbs.subquery(Long.class);
			Root<SenseRelation> relRoot = subquery.from(SenseRelation.class);
			subquery.select(relRoot.get("senseFrom").<Long>get("id"));
			subquery.where(cb.equal(relRoot.get("relation").get("id"), id));

			return cb.in(root.get("id")).value(subquery);
		};

	}

	public static Specification<Sense> byUbyPartOfSpeech(
			final PartOfSpeech partOfSpeech) {
		return (root, query, cb) -> {
			Join<Sense, pl.edu.pwr.wordnetloom.model.PartOfSpeech> pos = root.join("partOfSpeech");
			return cb.equal(pos.get("ubyLmfType"), partOfSpeech);
		};
	}

	public static Specification<Sense> byDomainId(final Long id) {
		return (root, query, cb) -> {
			Join<Sense, Domain> dom = root.join("domain");
			return cb.equal(dom.get("id"), id);
		};
	}
}
