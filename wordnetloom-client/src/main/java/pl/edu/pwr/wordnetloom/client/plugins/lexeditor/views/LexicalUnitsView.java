package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import pl.edu.pwr.wordnetloom.client.plugins.lexeditor.panel.CriteriaDTO;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.model.wordnet.Domain;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;

/**
 * lista jednostek leksykalnych
 *
 * @author Max
 */
public class LexicalUnitsView extends AbstractView {

    /**
     * kontruktor dla klasy
     *
     * @param workbench - wskaznik dla workbencha
     * @param title - etykieta dla okienka
     */
    public LexicalUnitsView(Workbench workbench, String title) {
        super(workbench, title, new LexicalUnitsViewUI());
    }

    /**
     * dodanie obiektu nasługującego zmian w zaznaczeniu jednostki
     *
     * @param newListener - sluchacz
     */
    public void addUnitChangeListener(SimpleListenerInterface newListener) {
        getUI().addActionListener(newListener);
    }

    /**
     * odświeżenie listy jednostek
     */
    public void refreshData() {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI(); // odczytanie UI
        viewUI.refreshData();
    }

    /**
     * ustawienie ostatnio uzywanej jednostki
     *
     * @param unit - ostatnia jednostka
     */
    public void setSelectedUnit(Sense unit) {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI(); // odczytanie UI
        viewUI.setSelectedUnit(unit);
    }

    /**
     * Odczytanie zaznaczonej jednostki
     *
     * @return zaznaczona jednostka lub NULL
     */
    public Sense getSelectedUnit() {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI(); // odczytanie UI
        return viewUI.getSelectedUnit();
    }

    /**
     * Odczytanie aktualnie ustawionego filtra
     *
     * @return filter
     */
    public String getFilter() {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI(); // odczytanie UI
        return viewUI.getFilter();
    }

    /**
     * Ustawienie nowej wartosci dla filtra
     *
     * @param filter - nowa wartosc dla filtra
     */
    public void setFilter(String filter) {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI(); // odczytanie UI
        viewUI.setFilter(filter);
    }

    /**
     * Odczytanie aktualnie ustawionej dziedziny
     *
     * @return dziedzina
     */
    public Domain getDomain() {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI(); // odczytanie UI
        return viewUI.getDomain();
    }

    /**
     * Ustawienie nowej wartosci dla dziedziny
     *
     * @param domain - wartosc dziedziny
     */
    public void setDomain(Domain domain) {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI(); // odczytanie UI
        viewUI.setDomain(domain);
    }

    /**
     * odrysowanie zawartości listy bez ponownego odczytu danych
     */
    public void redrawList() {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI(); // odczytanie UI
        viewUI.redrawList();
    }

    public void refreshLexicons() {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI();
        viewUI.refreshLexiocn();
    }

    public CriteriaDTO getCriteria() {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI();
        return viewUI.getCriteria();
    }

    public void setCriteria(CriteriaDTO crit) {
        LexicalUnitsViewUI viewUI = (LexicalUnitsViewUI) getUI();
        viewUI.setCriteria(crit);
    }
}
