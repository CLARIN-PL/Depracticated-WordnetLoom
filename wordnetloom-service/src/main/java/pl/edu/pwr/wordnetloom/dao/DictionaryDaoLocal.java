package pl.edu.pwr.wordnetloom.dao;

import java.util.List;

import javax.ejb.Local;

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

@Local
public interface DictionaryDaoLocal {
	
	List<StyleDictionary> findAllStyleDictionary();	
	List<StatusDictionary> findAllStatusDictionary();
	List<AgeDictionary> findAllAgeDictionary();
	List<InterfixDictionary> findAllInterfixDictionary();
	List<SuffixDictionary> findAllSuffixDictionary();
	List<PrefixDictionary> findAllPrefixDictionary();
	List<TranscriptionDictionary> findAllTranscriptionsDictionary();
	List<DomainDictionary> findAllDomainDictionary();
	List<DomainModifierDictionary> findAllDomainModifierDictionary();
	List<SourceDictionary> findAllSourceDictionary();
	List<GrammaticalGenderDictionary> findAllGrammaticalGenderDictionary();
	List<LexicalCharacteristicDictionary> findAllCharacteristicDictionary();
	List<InflectionDictionary> findAllInflectionDictionary();
	void saveOrUpdate(Dictionary dic);
	void remove(Dictionary dic);
	List<DialectalDictionary> findAllDialecticalDictionary();
	SourceDictionary findById(SourceDictionary s);

}

