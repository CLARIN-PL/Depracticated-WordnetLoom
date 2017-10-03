package pl.edu.pwr.wordnetloom.dao;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import pl.edu.pwr.wordnetloom.model.yiddish.Transcription;
import pl.edu.pwr.wordnetloom.model.yiddish.YiddishDomain;
import pl.edu.pwr.wordnetloom.model.yiddish.YiddishSenseExtension;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.InterfixParticle;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.RootParticle;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.SuffixParticle;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.AgeDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainModifierDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.GrammaticalGenderDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.LexicalCharacteristicDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.PrefixDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SourceDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StatusDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StyleDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.particle.PrefixParticle;

public class SenseYiddishExtensionSpecification {
	
	private static final String PREFIX = "prefix";
	private static final String SUFFIX = "suffix";
	private static final String INTERFIX = "interfix";
	private static final String ROOT = "root";

	public static String getContainsLikePattern(String lemma) {
		if (lemma == null || lemma.isEmpty()) {
			return "%";
		} else {
			return lemma.toLowerCase() + "%";
		}
	}
	
	public static String getAffixConatins(String affix)
	{
		if(affix == null || affix.isEmpty())
		{
			return "%";
		}
		return "%" + affix.toLowerCase() + "%";
	}

	public static Specification<Sense> byYddishSpelling(final String lemma) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			return cb.like(cb.lower(yse.get("yiddishSpelling")), getContainsLikePattern(lemma));
		};
	}

	public static Specification<Sense> byYivoSpelling(final String lemma) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			return cb.like(cb.lower(yse.get("yivoSpelling")), getContainsLikePattern(lemma));
		};
	}

	public static Specification<Sense> byEtymologicalRoot(final String lemma) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			return cb.like(cb.lower(yse.get("etymologicalRoot")), getContainsLikePattern(lemma));
		};
	}
	
	public static Specification<Sense> byEtymology(final String txt) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			return cb.like(cb.lower(yse.get("etymology")), getContainsLikePattern(txt));
		};
	}
	
	public static Specification<Sense> byAge(final Long id) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			Join<YiddishSenseExtension, AgeDictionary> age = yse.join("age");
			return cb.equal(age.get("id"), id);
		};
	}
	
	public static Specification<Sense> byStatus(final Long id) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			Join<YiddishSenseExtension, StatusDictionary> status = yse.join("status");
			return cb.equal(status.get("id"), id);
		};
	}
	
	public static Specification<Sense> byStyle(final Long id) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			Join<YiddishSenseExtension, StyleDictionary> style = yse.join("style");
			return cb.equal(style.get("id"), id);
		};
	}
	
	public static Specification<Sense> byGrammaticalGender(final Long id) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			Join<YiddishSenseExtension, GrammaticalGenderDictionary> grammaticalGender = yse.join("grammaticalGender");
			return cb.equal(grammaticalGender.get("id"), id);
		};
	}
	public static Specification<Sense> byLexicalCharcteristic(final Long id) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			Join<YiddishSenseExtension, LexicalCharacteristicDictionary> lexicalCharcteristic = yse.join("lexicalCharcteristic");
			return cb.equal(lexicalCharcteristic.get("id"), id);
		};
	}

	
	public static Specification<Sense> bySemanticField(final Long semantic, final Long modifier) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			Join<Sense, YiddishDomain> ydom = yse.join("yiddishDomains");
			if(semantic == null && modifier != null){
				Join<YiddishDomain, DomainModifierDictionary> mod = ydom.join("modifier");
				return cb.equal(mod.get("id"), modifier);
			}
			if(semantic != null && modifier != null){
				Join<YiddishDomain, DomainDictionary> yd = ydom.join("domain");
				Join<YiddishDomain, DomainModifierDictionary> mod = ydom.join("modifier");
				return cb.and(cb.equal(yd.get("id"), semantic), cb.equal(mod.get("id"), modifier));
			}
			Join<YiddishDomain, DomainDictionary> yd = ydom.join("domain");
			return cb.equal(yd.get("id"), semantic);
		};
	}
	
	public static Specification<Sense> bySource(final Long id) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			Join<YiddishSenseExtension, SourceDictionary> domain = yse.join("source", JoinType.LEFT);
			return domain.in(id);
		};
	}

	public static Specification<Sense> byTranscritpiton(final String lemma) {
		return (root, query, cb) -> {
			Join<Sense, YiddishSenseExtension> yse = root.join("yiddishSenseExtension");
			Join<Sense, Transcription> trans = yse.join("transcriptions");
			//Join<Sense, TranscriptionDictionary> transDic = trans.join("transcriptionDictionary");
			return cb.like(trans.get("phonography"), getContainsLikePattern(lemma));
		};
	}
	
	public static Specification<Sense> byPrefix(final String prefix)
	{
		return byParticle(prefix, PrefixParticle.class, "prefix");
	}
	
	
	private static<T> Specification<Sense> byParticle(final String particle, Class<T> particleClass, final String particleAttribute)
	{
		return (root, query, cb) -> {
			Subquery<Long> interfixSubquery = query.subquery(Long.class);
			Root<T> interfixRoot = interfixSubquery.from(particleClass);;
			Join<T, PrefixDictionary> dictionaryJoin = interfixRoot.join(particleAttribute);
			interfixSubquery.select(interfixRoot.get("extension")).where(cb.like(dictionaryJoin.get("name"), getAffixConatins(particle)));
			
			Join<Sense, YiddishSenseExtension> extensionJoin = root.join("yiddishSenseExtension");
			return extensionJoin.get("id").in(interfixSubquery);
		};
	}
	
	public static Specification<Sense> byInterfix(final String interfix)
	{
		return byParticle(interfix, InterfixParticle.class, "interfix");
	}
	
	public static Specification<Sense> bySuffix(final String suffix)
	{
		return byParticle(suffix, SuffixParticle.class, "suffix");
	}
	
	public static Specification<Sense> byRoot(final String rootParticle)
	{
		return (root, query, cb) -> {
			Subquery<Long> rootSubquery = query.subquery(Long.class);
			Root<RootParticle> rootRoot = rootSubquery.from(RootParticle.class);
			rootSubquery.select(rootRoot.get("extension")).where(cb.like(rootRoot.get("root"), getContainsLikePattern(rootParticle)));
			Join<Sense, YiddishSenseExtension> extensionJoin = root.join("yiddishSenseExtension");
			return extensionJoin.get("id").in(rootSubquery);
		};
	}
}