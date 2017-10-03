package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import pl.edu.pwr.wordnetloom.dao.DictionaryDaoLocal;
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
public class DictionaryServiceBean implements DictionaryServiceRemote {

	@EJB private DictionaryDaoLocal local;

	@Override
	public List<StyleDictionary> findAllStyleDictionary() {
		return local.findAllStyleDictionary();
	}

	@Override
	public List<StatusDictionary> findAllStatusDictionary() {
		return local.findAllStatusDictionary();
	}

	@Override
	public List<AgeDictionary> findAllAgeDictionary() {
		return local.findAllAgeDictionary();
	}

	@Override
	public List<InterfixDictionary> findAllInterfixDictionary() {
		return local.findAllInterfixDictionary();
	}

	@Override
	public List<SuffixDictionary> findAllSuffixDictionary() {
		return local.findAllSuffixDictionary();
	}

	@Override
	public List<PrefixDictionary> findAllPrefixDictionary() {
		return local.findAllPrefixDictionary();
	}

	@Override
	public List<TranscriptionDictionary> findAllTranscriptionsDictionary() {
		return local.findAllTranscriptionsDictionary();
	}

	@Override
	public List<DomainDictionary> findAllDomainDictionary() {
		return local.findAllDomainDictionary();
	}

	@Override
	public List<GrammaticalGenderDictionary> findAllGrammaticalGenderDictionary() {
		return local.findAllGrammaticalGenderDictionary();
	}

	@Override
	public List<LexicalCharacteristicDictionary> findAllLexicalCharacteristicDictionary() {
		return local.findAllCharacteristicDictionary();
	}

	@Override
	public void saveOrUpdate(Dictionary dic) {
		local.saveOrUpdate(dic);		
	}

	@Override
	public void remove(Dictionary dic) {
		local.remove(dic);
	}

	@Override
	public List<InflectionDictionary> findAllInflectionDictionary() {
		return local.findAllInflectionDictionary();
	}

	@Override
	public List<DialectalDictionary> findAllDialecticalDictionary() {
		return local.findAllDialecticalDictionary();
	}

	@Override
	public List<DomainModifierDictionary> findAllDomainModifiersDictionary() {
		return local.findAllDomainModifierDictionary();
	}

	@Override
	public List<SourceDictionary> findAllSourceDictionary() {
		return local.findAllSourceDictionary();
	}
	
	@Override
	public SourceDictionary findBy(SourceDictionary s){
		return local.findById(s);
	}
	
	
}
