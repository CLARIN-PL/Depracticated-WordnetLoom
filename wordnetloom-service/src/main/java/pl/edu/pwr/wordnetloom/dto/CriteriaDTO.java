package pl.edu.pwr.wordnetloom.dto;

import java.io.Serializable;
import java.util.List;

import pl.edu.pwr.wordnetloom.model.PartOfSpeech;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainModifierDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.GrammaticalGenderDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.SourceDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StatusDictionary;
import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.AgeDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.DomainDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.LexicalCharacteristicDictionary;
import pl.edu.pwr.wordnetloom.model.yiddish.dictionary.StyleDictionary;

public class CriteriaDTO implements Serializable{

	private static final long serialVersionUID = -8346393840508249383L;

	private String lemma;
	private SearchType searchType;
	private Lexicon lexicon;
	private PartOfSpeech partOfSpeech;
	private Domain domain;
	private RelationType relation;
	private RegisterTypes register;
	private GrammaticalGenderDictionary grammaticalGenderDictionary;
	private SourceDictionary sourceDictionary;
	private AgeDictionary ageDictionary;
	private StatusDictionary statusDictionary;
	private StyleDictionary styleDictionary;
	private DomainDictionary yiddishDomain;
	private LexicalCharacteristicDictionary lexicalCharacteristicDictionary;
	private DomainModifierDictionary domainModifierDictionary;
	private String ethymology;
	private String definition;
	private String etymologicalRoot;
	private String comment;
	private String example;
	private String synsetType;
	private List<Sense> sense;

	public CriteriaDTO() {
	}
	
	public CriteriaDTO(String lemma){
		this.lemma = lemma;
	}
	public CriteriaDTO(String lemma, PartOfSpeech pos){
		this.lemma = lemma;
		this.partOfSpeech = pos;
	}

	public boolean hasExtraCriteria(){
		if(lexicon != null) return true;
		if(partOfSpeech != null) return true;
		if(domain != null) return true;
		if(relation != null) return true;
		if(register != null) return true;
		if( hasYiddishCriteria()) return true;
		return false;
	}
	
	public boolean hasYiddishCriteria(){
		if(grammaticalGenderDictionary != null) return true;
		if(sourceDictionary != null) return true;
		if(ageDictionary != null) return true;
		if(statusDictionary != null) return true;
		if(styleDictionary != null) return true;
		if(yiddishDomain != null) return true;
		if(ethymology != null && !"".equals(ethymology)) return true;
		if(definition != null && !"".equals(definition)) return true;
		if(comment != null && !"".equals(comment)) return true;
		if(example != null && !"".equals(example)) return true;
		if(etymologicalRoot != null && !"".equals(etymologicalRoot)) return true;
		if(domainModifierDictionary != null) return true;
		if(lexicalCharacteristicDictionary != null) return true;
		return false;
	}
	
	public Lexicon getLexicon() {
		return lexicon;
	}

	public void setLexicon(Lexicon lexicon) {
		this.lexicon = lexicon;
	}

	public PartOfSpeech getPartOfSpeech() {
		return partOfSpeech;
	}

	public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public RegisterTypes getRegister() {
		return register;
	}

	public void setRegister(RegisterTypes register) {
		this.register = register;
	}

	public SearchType getSearchType() {
		return searchType;
	}

	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}

	public GrammaticalGenderDictionary getGrammaticalGenderDictionary() {
		return grammaticalGenderDictionary;
	}

	public void setGrammaticalGenderDictionary(GrammaticalGenderDictionary grammaticalGenderDictionary) {
		this.grammaticalGenderDictionary = grammaticalGenderDictionary;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	public RelationType getRelation() {
		return relation;
	}

	public void setRelation(RelationType relation) {
		this.relation = relation;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getSynsetType() {
		return synsetType;
	}

	public void setSynsetType(String synsetType) {
		this.synsetType = synsetType;
	}

	public List<Sense> getSense() {
		return sense;
	}

	public void setSense(List<Sense> sense) {
		this.sense = sense;
	}

	public SourceDictionary getSourceDictionary() {
		return sourceDictionary;
	}

	public void setSourceDictionary(SourceDictionary sourceDictionary) {
		this.sourceDictionary = sourceDictionary;
	}

	public AgeDictionary getAgeDictionary() {
		return ageDictionary;
	}

	public void setAgeDictionary(AgeDictionary ageDictionary) {
		this.ageDictionary = ageDictionary;
	}

	public StatusDictionary getStatusDictionary() {
		return statusDictionary;
	}

	public void setStatusDictionary(StatusDictionary statusDictionary) {
		this.statusDictionary = statusDictionary;
	}

	public StyleDictionary getStyleDictionary() {
		return styleDictionary;
	}

	public void setStyleDictionary(StyleDictionary styleDictionary) {
		this.styleDictionary = styleDictionary;
	}

	public DomainDictionary getYiddishDomain() {
		return yiddishDomain;
	}

	public void setYiddishDomain(DomainDictionary yiddishDomain) {
		this.yiddishDomain = yiddishDomain;
	}

	public String getEthymology() {
		return ethymology;
	}

	public void setEthymology(String ethymology) {
		this.ethymology = ethymology;
	}

	public String getEtymologicalRoot() {
		return etymologicalRoot;
	}

	public void setEtymologicalRoot(String etymologicalRoot) {
		this.etymologicalRoot = etymologicalRoot;
	}

	public LexicalCharacteristicDictionary getLexicalCharacteristicDictionary() {
		return lexicalCharacteristicDictionary;
	}

	public void setLexicalCharacteristicDictionary(LexicalCharacteristicDictionary lexicalCharacteristicDictionary) {
		this.lexicalCharacteristicDictionary = lexicalCharacteristicDictionary;
	}

	public DomainModifierDictionary getDomainModifierDictionary() {
		return domainModifierDictionary;
	}

	public void setDomainModifierDictionary(DomainModifierDictionary domainModifierDictionary) {
		this.domainModifierDictionary = domainModifierDictionary;
	}

	@Override
	public String toString() {
		return "CriteriaDTO [lemma=" + lemma + ", searchType=" + searchType + ", lexicon=" + lexicon + ", partOfSpeech="
				+ partOfSpeech + ", domain=" + domain + ", relation=" + relation + ", register=" + register
				+ ", grammaticalGenderDictionary=" + grammaticalGenderDictionary + ", sourceDictionary="
				+ sourceDictionary + ", ageDictionary=" + ageDictionary + ", statusDictionary=" + statusDictionary
				+ ", styleDictionary=" + styleDictionary + ", yiddishDomain=" + yiddishDomain + ", ethymology="
				+ ethymology + ", definition=" + definition + ", comment=" + comment + ", example=" + example
				+ ", synsetType=" + synsetType + ", sense=" + sense + "]";
	}

	
}
