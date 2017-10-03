package pl.edu.pwr.wordnetloom.service;

import java.util.List;

import javax.ejb.Remote;

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

@Remote
public interface DictionaryServiceRemote {


	List<StyleDictionary> findAllStyleDictionary();
	List<StatusDictionary> findAllStatusDictionary();
	List<AgeDictionary> findAllAgeDictionary();
	List<InterfixDictionary> findAllInterfixDictionary();
	List<SuffixDictionary> findAllSuffixDictionary();
	List<PrefixDictionary> findAllPrefixDictionary();
	List<TranscriptionDictionary> findAllTranscriptionsDictionary();
	List<DomainDictionary> findAllDomainDictionary();
	List<SourceDictionary> findAllSourceDictionary();
	List<DomainModifierDictionary> findAllDomainModifiersDictionary();
	List<GrammaticalGenderDictionary> findAllGrammaticalGenderDictionary();
	List<LexicalCharacteristicDictionary> findAllLexicalCharacteristicDictionary();
	List<InflectionDictionary> findAllInflectionDictionary();
	List<DialectalDictionary> findAllDialecticalDictionary();
	void saveOrUpdate(Dictionary dic);
	void remove(Dictionary dic);
	SourceDictionary findBy(SourceDictionary s);
}
