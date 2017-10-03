package pl.edu.pwr.wordnetloom.systems.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.pwr.wordnetloom.model.Domain;
import pl.edu.pwr.wordnetloom.model.Lexicon;
import pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech;
import pl.edu.pwr.wordnetloom.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.systems.misc.CustomDescription;

public class DomainComboBox extends ComboBoxPlain<Domain>{

	private static final long serialVersionUID = -5108640841659123235L;
	private List<Domain> all;
	private String nullRepresentation;

	public DomainComboBox(String nullItemRepresentation){
		this.nullRepresentation = nullItemRepresentation;
	}

	public void allDomains(boolean withPrefix){
		all = DomainManager.getInstance().getAllDomainsSorted();
		loadItems(withPrefix);
	}

	public void filterDomainsByLexicon(Lexicon lexicon,boolean withPrefix){
		removeAllItems();
		addItem(new CustomDescription<Domain>(nullRepresentation, null));
		List<Domain> domains = new ArrayList<Domain>();
		for(Domain domain: all){
			if(domain.getLexicon().equals(lexicon)){
				domains.add(domain);
			}
		}
		domains = DomainManager.getInstance().sortDomains(domains);
		for(Domain domain : domains){
			addItem(new CustomDescription<Domain>(withPrefix == true ? domain.toString(): nameWithoutPrefix(domain.toString()), domain));
		}
	}

	public void filterDomainByUbyPos(pl.edu.pwr.wordnetloom.model.PartOfSpeech pos, boolean withPrefix){
		all = filterDomainByUbyPos(pos.getUbyType());
		loadItems(withPrefix);
	}

	public void filterDomainByUbyPosAndLexcion(pl.edu.pwr.wordnetloom.model.PartOfSpeech pos, Lexicon lex, boolean withPrefix){
		all = filterDomainByUbyPos(pos.getUbyType());
		filterDomainsByLexicon(lex,withPrefix);
	}

	public void filterDomainByPos(pl.edu.pwr.wordnetloom.model.PartOfSpeech pos, boolean withPrefix){
		all = filterByDomainByPos(pos);
		loadItems(withPrefix);
	}

	private void loadItems(boolean withPrefix) {
		removeAllItems();
		all = DomainManager.getInstance().sortDomains(all);
		addItem(new CustomDescription<Domain>(nullRepresentation, null));
		for(Domain domain: all){
				addItem(new CustomDescription<Domain>(
						withPrefix == true ? domain.toString(): nameWithoutPrefix(domain.toString()), domain));
		}
	}

	private List<Domain> filterDomainByUbyPos(PartOfSpeech posUby){
		Set<Domain> result = new HashSet<Domain>();
		List<pl.edu.pwr.wordnetloom.model.PartOfSpeech> poses = new ArrayList<pl.edu.pwr.wordnetloom.model.PartOfSpeech>(PosManager.getInstance().getAllPOSes());
		for(pl.edu.pwr.wordnetloom.model.PartOfSpeech pos : poses){
			if(pos.getUbyType() == posUby){
				result.addAll(pos.getDomains());
			}
		}
		return new ArrayList<Domain>(result);
	}

	private List<Domain> filterByDomainByPos(pl.edu.pwr.wordnetloom.model.PartOfSpeech pos){
		return new ArrayList<Domain>(pos.getDomains());
	}

	public static String nameWithoutPrefix(String name){
		String[] splited = name.split("_");
		return splited.length == 1 ? splited[0] : splited[1];
	}
}
