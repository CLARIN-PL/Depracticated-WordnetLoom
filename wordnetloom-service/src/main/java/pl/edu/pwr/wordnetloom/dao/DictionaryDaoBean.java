package pl.edu.pwr.wordnetloom.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.AgeDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DialectalDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.Dictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainModifierDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.GrammaticalGenderDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.InflectionDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.InterfixDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.LexicalCharacteristicDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.PrefixDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SourceDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StatusDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StyleDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SuffixDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.TranscriptionDictionary;

@Stateless
public class DictionaryDaoBean implements DictionaryDaoLocal{

	@EJB private DAOLocal local;

	 public List<StyleDictionary> findAllStyleDictionary() {
	        Query q = local.getEM().createQuery("FROM StyleDictionary", StyleDictionary.class);
	        return q.getResultList();
	 }
	 
	 public List<StatusDictionary> findAllStatusDictionary() {
	        Query q = local.getEM().createQuery("FROM StatusDictionary", StatusDictionary.class);
	        return q.getResultList();
	 }
	 
	 public List<AgeDictionary> findAllAgeDictionary() {
	        Query q = local.getEM().createQuery("FROM AgeDictionary", AgeDictionary.class);
	        return q.getResultList();
	 }
	 
	 public List<InterfixDictionary> findAllInterfixDictionary() {
	        Query q = local.getEM().createQuery("FROM InterfixDictionary", InterfixDictionary.class);
	        return q.getResultList();
	 }
	 
	 public List<SuffixDictionary> findAllSuffixDictionary() {
	        Query q = local.getEM().createQuery("FROM SuffixDictionary", SuffixDictionary.class);
	        return q.getResultList();
	 }
	 
	 public List<PrefixDictionary> findAllPrefixDictionary() {
	        Query q = local.getEM().createQuery("FROM PrefixDictionary", PrefixDictionary.class);
	        return q.getResultList();
	 }

	 public List<TranscriptionDictionary> findAllTranscriptionsDictionary() {
	        Query q = local.getEM().createQuery("FROM TranscriptionDictionary", TranscriptionDictionary.class);
	        return q.getResultList();
	 }
	 
	 public List<DomainDictionary> findAllDomainDictionary() {
	        Query q = local.getEM().createQuery("FROM DomainDictionary", DomainDictionary.class);
	        return q.getResultList();
	 }
	 
	 public List<GrammaticalGenderDictionary> findAllGrammaticalGenderDictionary() {
	        Query q = local.getEM().createQuery("FROM GrammaticalGenderDictionary", GrammaticalGenderDictionary.class);
	        return q.getResultList();
	 }

	@Override
	public List<LexicalCharacteristicDictionary> findAllCharacteristicDictionary() {
		Query q = local.getEM().createQuery("FROM LexicalCharacteristicDictionary", LexicalCharacteristicDictionary.class);
        return q.getResultList();
	}
	
	@Override
	public List<DialectalDictionary> findAllDialecticalDictionary() {
		Query q = local.getEM().createQuery("FROM DialectalDictionary", DialectalDictionary.class);
        return q.getResultList();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveOrUpdate(Dictionary dic){
		EntityManager em = local.getEM();
				
		if(null != dic.getId())
			em.merge(dic);
		else{
			em.persist(dic);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Dictionary dic){
		EntityManager em = local.getEM();
		Dictionary d = em.getReference(Dictionary.class, dic.getId());
		em.remove(d);
	}

	@Override
	public List<InflectionDictionary> findAllInflectionDictionary() {
		Query q = local.getEM().createQuery("FROM InflectionDictionary", InflectionDictionary.class);
        return q.getResultList();
	}

	@Override
	public List<DomainModifierDictionary> findAllDomainModifierDictionary() {
		Query q = local.getEM().createQuery("FROM DomainModifierDictionary", DomainModifierDictionary.class);
        return q.getResultList();
	}

	@Override
	public List<SourceDictionary> findAllSourceDictionary() {
		Query q = local.getEM().createQuery("FROM SourceDictionary", SourceDictionary.class);
        return q.getResultList();
	}

	@Override
	public SourceDictionary findById(SourceDictionary s) {
		return local.getObject(SourceDictionary.class, s.getId());
	}

}
