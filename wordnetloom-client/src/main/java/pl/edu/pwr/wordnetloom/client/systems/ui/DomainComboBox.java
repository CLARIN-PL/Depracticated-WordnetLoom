package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pl.edu.pwr.wordnetloom.client.systems.managers.DomainManager;
import pl.edu.pwr.wordnetloom.client.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.domain.model.Domain;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

public class DomainComboBox extends ComboBoxPlain<Domain> {

    private static final long serialVersionUID = -5108640841659123235L;
    private List<Domain> all;
    private final String nullRepresentation;

    public DomainComboBox(String nullItemRepresentation) {
        this.nullRepresentation = nullItemRepresentation;
    }

    public void allDomains(boolean withPrefix) {
        all = DomainManager.getInstance().getAllDomainsSorted();
        loadItems(withPrefix);
    }

    public void filterDomainsByLexicon(Lexicon lexicon, boolean withPrefix) {
        removeAllItems();
        addItem(new CustomDescription<>(nullRepresentation, null));
        List<Domain> domains = new ArrayList<>();
        for (Domain domain : all) {
            if (domain.getLexicon().equals(lexicon)) {
                domains.add(domain);
            }
        }
        domains = DomainManager.getInstance().sortDomains(domains);
        for (Domain domain : domains) {
            addItem(new CustomDescription<>(withPrefix == true ? domain.toString() : nameWithoutPrefix(domain.toString()), domain));
        }
    }

    public void filterDomainByUbyPos(PartOfSpeech pos, boolean withPrefix) {
        all = filterDomainByUbyPos(pos.getUbyType());
        loadItems(withPrefix);
    }

    public void filterDomainByUbyPosAndLexcion(PartOfSpeech pos, Lexicon lex, boolean withPrefix) {
        all = filterDomainByUbyPos(pos.getUbyType());
        filterDomainsByLexicon(lex, withPrefix);
    }

    public void filterDomainByPos(PartOfSpeech pos, boolean withPrefix) {
        all = filterByDomainByPos(pos);
        loadItems(withPrefix);
    }

    private void loadItems(boolean withPrefix) {
        removeAllItems();
        all = DomainManager.getInstance().sortDomains(all);
        addItem(new CustomDescription<>(nullRepresentation, null));
        for (Domain domain : all) {
            addItem(new CustomDescription<>(
                    withPrefix == true ? domain.toString() : nameWithoutPrefix(domain.toString()), domain));
        }
    }

    private List<Domain> filterDomainByUbyPos(pl.edu.pwr.wordnetloom.model.uby.enums.PartOfSpeech posUby) {
        Set<Domain> result = new HashSet<>();
        List<PartOfSpeech> poses = new ArrayList<>(PosManager.getInstance().getAllPOSes());
        for (PartOfSpeech pos : poses) {
            if (pos.getUbyType() == posUby) {
                result.addAll(pos.getDomains());
            }
        }
        return new ArrayList<>(result);
    }

    private List<Domain> filterByDomainByPos(PartOfSpeech pos) {
        return new ArrayList<>(pos.getDomains());
    }

    public static String nameWithoutPrefix(String name) {
        String[] splited = name.split("_");
        return splited.length == 1 ? splited[0] : splited[1];
    }
}
